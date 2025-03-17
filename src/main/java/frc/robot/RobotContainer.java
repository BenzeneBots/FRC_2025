// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.RobotConstants.DriveConstants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.AlgaePivot;
import frc.robot.subsystems.AlgaeSpinner;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.CoralSpinner;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.FirstPivot;
import frc.robot.subsystems.SecondPivot;

public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * DriveConstants.translationDeadband).withRotationalDeadband(MaxAngularRate * DriveConstants.rotationDeadband) // Add a 20% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);
    private final Joystick manip = new Joystick(1);

    private final JoystickButton coral_in = new JoystickButton(manip, 9);
    private final JoystickButton coral_out = new JoystickButton(manip, 10);

    private final JoystickButton algae_in = new JoystickButton(manip, 11);
    private final JoystickButton algae_out = new JoystickButton(manip, 12);

    private final JoystickButton algae_stow = new JoystickButton(manip, 1);
    private final JoystickButton algae_deployed = new JoystickButton(manip, 2);
    private final JoystickButton algae_score = new JoystickButton(manip, 3);

    private final JoystickButton playerFeed = new JoystickButton(manip, 14);
    private final JoystickButton level2Score = new JoystickButton(manip, 5);
    private final JoystickButton level3Score = new JoystickButton(manip, 6);
    private final JoystickButton reset = new JoystickButton(manip, 13);

    private final JoystickButton override = new JoystickButton(manip, 7);
    private final JoystickButton intermediate = new JoystickButton(manip, 8);

    public final CommandSwerveDrivetrain drivetrain;
    private final CoralSpinner s_CoralSpinner;
    private final AlgaePivot s_AlgaePivot;
    private final AlgaeSpinner s_AlgaeSpinner;
    private final Elevator s_Elevator;
    private final FirstPivot s_FirstPivot;
    private final SecondPivot s_SecondPivot;

    private final Command humanPlayerCommand; 
    private final Command intermediateCommand; 
    private final Command zeroAllCommand; 
    private final Command level2Command; 
    private final Command level3Command; 

    private SendableChooser<Command> autoChooser;

    public RobotContainer() {
        s_CoralSpinner = new CoralSpinner();
        s_AlgaePivot = new AlgaePivot();
        s_AlgaeSpinner = new AlgaeSpinner();
        s_Elevator = new Elevator();
        s_FirstPivot = new FirstPivot();
        s_SecondPivot = new SecondPivot();

        // humanPlayerCommand = s_Elevator.feedPos().andThen(s_SecondPivot.feedPos()
        //     .andThen(s_FirstPivot.feedPos()));

        humanPlayerCommand = s_Elevator.feedPos()
            .andThen(s_FirstPivot.feedPos())
            .andThen(s_SecondPivot.feedPos())
            .andThen(s_CoralSpinner.intake());

        zeroAllCommand = s_SecondPivot.resetPose()
            .andThen(s_FirstPivot.resetPose())
            .andThen(s_Elevator.resetPos());
        
        level2Command = s_Elevator.level2Pos()
            .andThen(s_SecondPivot.level2Pos())
            .andThen(s_FirstPivot.level2Pos());

        level3Command = s_Elevator.level3Pos()
            .andThen(s_SecondPivot.level3Pos())
            .andThen(s_FirstPivot.level2Pos());

        intermediateCommand = s_Elevator.intermediatePos();

        regitsterNamedCommands();
        drivetrain = TunerConstants.createDrivetrain();

        try {
            autoChooser = AutoBuilder.buildAutoChooser();
            SmartDashboard.putData("Auto Chooser", autoChooser);
        }
        catch (Exception ex) {
            DriverStation.reportError("AutoBuilder not made", ex.getStackTrace());
            autoChooser = null;
        }

        configureBindings();
    }

    private void regitsterNamedCommands(){
    }
        
    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRawAxis(2) * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
        joystick.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        joystick.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        drivetrain.registerTelemetry(logger::telemeterize);
        
        algae_in.whileTrue(s_AlgaeSpinner.intake());
        algae_out.whileTrue(s_AlgaeSpinner.outtake());

        algae_stow.onTrue(s_AlgaePivot.stowPivot());
        algae_deployed.onTrue(s_AlgaePivot.deploy());
        algae_score.onTrue(s_AlgaePivot.score());

        playerFeed.onTrue(humanPlayerCommand);
        level2Score.onTrue(level2Command);
        level3Score.onTrue(level3Command);

        intermediate.onTrue(intermediateCommand.andThen(new WaitCommand(1.5)).andThen(zeroAllCommand));

        coral_in.whileTrue(s_CoralSpinner.intake());
        coral_out.whileTrue(s_CoralSpinner.outtake());
        
        override.whileTrue(s_Elevator.overrideCommand(() -> manip.getRawAxis(5)));

        reset.onTrue(new InstantCommand() {
            @Override
            public void execute() {
                reset();
            }
        });
    }

    public void reset() {
        s_AlgaePivot.reset();
        s_Elevator.reset();
        s_FirstPivot.reset();
        s_SecondPivot.reset();
    }

    public Command getAutonomousCommand() {
        if(autoChooser != null) {
            return autoChooser.getSelected();
        } else {
            return null;
        }
    }
}
