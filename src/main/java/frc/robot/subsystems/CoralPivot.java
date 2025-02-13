package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConstants.IntakePivotConstants;

public class CoralPivot extends SubsystemBase {
    private final TalonFX pivotMotor;
    private final PositionDutyCycle controller;

    public CoralPivot() {
        pivotMotor = new TalonFX(60, "BB_CANIVORE");
        configMotor();
        // controller = new MotionMagicVoltage(0);
        controller = new PositionDutyCycle(0);
    }

    public void zeroMotor() {
        pivotMotor.setPosition(0);
    }

    public void configMotor() {
        pivotMotor.clearStickyFaults();
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
        config.Slot0.kP = 0;
        config.Slot0.kD = 0;
        config.Slot0.kI = 0;
        config.Slot0.kG = 0.5;

        // var motionMagicConfigs = config.MotionMagic;
        // motionMagicConfigs.MotionMagicCruiseVelocity = 80;
        // motionMagicConfigs.MotionMagicAcceleration = 160;
        // motionMagicConfigs.MotionMagicJerk = 1600;

        config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 50.0;
        config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = 0.0;

        pivotMotor.getConfigurator().apply(config);
    }

    public Command up() {
        return new Command() {
            @Override
            public void execute() {
                pivotMotor.set(IntakePivotConstants.speed);
            }

            @Override
            public void end(boolean interrupted) {
                pivotMotor.stopMotor();
                pivotMotor.setControl(controller.withSlot(0).withPosition(pivotMotor.getPosition().getValueAsDouble()));
            }
        };
    }

    public Command down() {
        return new Command() {
            @Override
            public void execute() {
                pivotMotor.set(-IntakePivotConstants.speed);
            }

            @Override
            public void end(boolean interrupted) {
                pivotMotor.stopMotor();
                pivotMotor.setControl(controller.withSlot(0).withPosition(pivotMotor.getPosition().getValueAsDouble()));
            }
        };
    }

    public Command slowUp() {
        return new Command() {
            @Override
            public void execute() {
                pivotMotor.set(IntakePivotConstants.slowSpeed);
            }

            @Override
            public void end(boolean interrupted) {
                pivotMotor.stopMotor();
                pivotMotor.setControl(controller.withSlot(0).withPosition(pivotMotor.getPosition().getValueAsDouble()));
            }
        };
    }

    public Command slowDown() {
        return new Command() {
            @Override
            public void execute() {
                pivotMotor.set(-IntakePivotConstants.slowSpeed);
            }

            @Override
            public void end(boolean interrupted) {
                pivotMotor.stopMotor();
                pivotMotor.setControl(controller.withSlot(0).withPosition(pivotMotor.getPosition().getValueAsDouble()));
            }
        };
    }

    public Command stowPivot() {
        return new Command() {
            @Override
            public void execute() {
                pivotMotor.setControl(controller.withSlot(0).withPosition(0));
            }
        };
    }

    public Command humanPlayer() {
        return new Command() {
            @Override
            public void execute() {
                pivotMotor.setControl(controller.withSlot(0).withPosition(5));
            }
        };
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("IntakePivot Pos", pivotMotor.getPosition().getValueAsDouble());
    }
}
