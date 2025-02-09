// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.pathplanner.lib.auto.AutoBuilder;
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

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.AlgaePivot;
import frc.robot.subsystems.AlgaeSpinner;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.CoralPivot;
import frc.robot.subsystems.CoralSpinner;
import frc.robot.subsystems.Commands.humanPlayerCommand;

public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);
    private final Joystick manip = new Joystick(1);

    private final JoystickButton coral_up = new JoystickButton(manip, 1);
    private final JoystickButton coral_down = new JoystickButton(manip, 2);
    private final JoystickButton coral_in = new JoystickButton(manip, 3);
    private final JoystickButton coral_out = new JoystickButton(manip, 4);

    private final JoystickButton humanPlayer = new JoystickButton(manip, 5);

    private final JoystickButton algae_up = new JoystickButton(manip, 9);
    private final JoystickButton algae_down = new JoystickButton(manip, 10);
    private final JoystickButton algae_in = new JoystickButton(manip, 7);
    private final JoystickButton algae_out = new JoystickButton(manip, 8);

    public final CommandSwerveDrivetrain drivetrain;
    private final CoralPivot s_CoralPivot;
    private final CoralSpinner s_CoralSpinner;
    private final AlgaePivot s_AlgaePivot;
    private final AlgaeSpinner s_AlgaeSpinner;

    private SendableChooser<Command> autoChooser;

    public RobotContainer() {
        s_CoralPivot = new CoralPivot();
        s_CoralSpinner = new CoralSpinner();
        s_AlgaePivot = new AlgaePivot();
        s_AlgaeSpinner = new AlgaeSpinner();

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
        
        humanPlayer.whileTrue(new humanPlayerCommand(s_CoralPivot));

        // Coral Pivot
        coral_up.whileTrue(s_CoralPivot.up());
        coral_down.whileTrue(s_CoralPivot.down());

        // Coral Spinner
        coral_in.whileTrue(s_CoralSpinner.intake());
        coral_out.whileTrue(s_CoralSpinner.outtake());

        // Algae Pivot
        algae_up.whileTrue(s_AlgaePivot.up());
        algae_down.whileTrue(s_AlgaePivot.down());

        // Algae Spinner
        //algae_in.whileTrue(s_AlgaeSpinner.intake());
       // algae_out.whileTrue(s_AlgaeSpinner.outtake());
    }

    public void zeroComponents() {
        s_CoralPivot.zeroMotor();
    }

    public Command getAutonomousCommand() {
        if(autoChooser != null) {
            return autoChooser.getSelected();
        } else {
            return null;
        }
    }
}