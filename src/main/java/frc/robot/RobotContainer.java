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
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.RobotConstants.DriveConstants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.AlgaePivot;
import frc.robot.subsystems.AlgaeSpinner;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.CoralPivot;
import frc.robot.subsystems.CoralSpinner;

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
    // private final Joystick test = new Joystick(2);

    private final JoystickButton coral_in = new JoystickButton(manip, 11);
    private final JoystickButton coral_out = new JoystickButton(manip, 12);

    private final JoystickButton algae_in = new JoystickButton(manip, 9);
    private final JoystickButton algae_out = new JoystickButton(manip, 10);

    private final JoystickButton stow = new JoystickButton(manip, 5);
    private final JoystickButton humanPlayer = new JoystickButton(manip, 6);
    private final JoystickButton level2 = new JoystickButton(manip, 7);
    private final JoystickButton level1 = new JoystickButton(manip, 8);

    private final JoystickButton algaeStow = new JoystickButton(manip, 4);
    private final JoystickButton algaeScore = new JoystickButton(manip, 2);
    private final JoystickButton algaeDeploy = new JoystickButton(manip, 1);

    private final JoystickButton algaedown = new JoystickButton(manip, 14);
    private final JoystickButton algaeup= new JoystickButton(manip, 13);
    public final CommandSwerveDrivetrain drivetrain;
    private final CoralPivot s_CoralPivot;
    private final CoralSpinner s_CoralSpinner;
    private final AlgaePivot s_AlgaePivot;
    private final AlgaeSpinner s_AlgaeSpinner;
    
    private final Climber s_Climber;

    private SendableChooser<Command> autoChooser;

    public RobotContainer() {
        s_CoralPivot = new CoralPivot();
        s_CoralSpinner = new CoralSpinner();
        s_AlgaePivot = new AlgaePivot();
        s_AlgaeSpinner = new AlgaeSpinner();

        s_Climber = new Climber();

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
            //AUTONOMOUS NAMED COMMANDS
        NamedCommands.registerCommand("coralOut", Instant(s_CoralSpinner.outtake())); 
                NamedCommands.registerCommand("coralIn", Instant(s_CoralSpinner.intake()));
                NamedCommands.registerCommand("pivotHPS", s_CoralPivot.humanPlayerStation());
                NamedCommands.registerCommand("LevelTwo", s_CoralPivot.level2()); 
                NamedCommands.registerCommand("LevelOne", s_CoralPivot.level1()); 
                NamedCommands.registerCommand("Stow", s_CoralPivot.stowPivot()); 
            }
        
        private Command Instant(Command command) {
            return command.withTimeout(1);
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

        // Coral Spinner
        coral_in.whileTrue(s_CoralSpinner.intake());
        coral_out.whileTrue(s_CoralSpinner.outtake());

        // Algae Spinner
        algae_in.whileTrue(s_AlgaeSpinner.intake());
        algae_out.whileTrue(s_AlgaeSpinner.outtake());

        // Adjust these buttons later
        joystick.button(1).whileTrue(s_Climber.up());
        joystick.button(2).whileTrue(s_Climber.down());

        // CORAL
        humanPlayer.onTrue(s_CoralPivot.humanPlayerStation());
        level1.onTrue(s_CoralPivot.level1());
        level2.onTrue(s_CoralPivot.level2());
        stow.onTrue(s_CoralPivot.stowPivot());

        // ALGAE
        algaeStow.onTrue(s_AlgaePivot.stowPivot());
        algaeDeploy.onTrue(s_AlgaePivot.deploy());
        algaeScore.onTrue(s_AlgaePivot.score());
        algaedown.whileTrue(s_AlgaePivot.down());
        algaeup.whileTrue(s_AlgaePivot.up());
    }

    public void zeroComponents() {
        s_CoralPivot.zeroMotor();
        s_AlgaePivot.setZero();
    }

    public void stowSubsystems() {
        s_CoralPivot.stowPivot().schedule();
        s_AlgaePivot.stowPivot().schedule();
    }

    public Command getAutonomousCommand() {
        if(autoChooser != null) {
            return autoChooser.getSelected();
        } else {
            return null;
        }
    }
}
