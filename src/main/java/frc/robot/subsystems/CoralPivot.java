package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConstants.IntakePivotConstants;

public class CoralPivot extends SubsystemBase {
    private final TalonFX pivotMotor;
    private final MotionMagicVoltage controller;

    public CoralPivot() {
        pivotMotor = new TalonFX(60, "BB_CANIVORE");
        configMotor();
        controller = new MotionMagicVoltage(0);
    }

    public void zeroMotor() {
        pivotMotor.setPosition(0);
    }

    public void configMotor() {
        pivotMotor.clearStickyFaults();
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
        config.Slot0.kP = 2.15;
        config.Slot0.kD = 0.0;
        config.Slot0.kI = 2.5;
        config.Slot0.kG = 0.25;

         var motionMagicConfigs = config.MotionMagic;
        motionMagicConfigs.MotionMagicCruiseVelocity = 80;
        motionMagicConfigs.MotionMagicAcceleration = 160;
        motionMagicConfigs.MotionMagicJerk = 1600;

        config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 0.0;
        config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = -51;

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
        return setPosition(0);
    }

    public Command level2() {
        return setPosition(IntakePivotConstants.level2);
    }

    public Command level1() {
        return setPosition(IntakePivotConstants.level1);
    }

    public Command humanPlayerStation() {
        return setPosition(IntakePivotConstants.humanPlayer);
    }

    public Command setPosition(double position) {
        return new Command() {
            Timer timer = new Timer();
            @Override
            public void initialize() {
                // TODO Auto-generated method stub
                super.initialize();
                timer.start();
            }

            @Override
            public void execute() {
                pivotMotor.setControl(controller.withSlot(0).withPosition(position));
            }

            @Override
            public boolean isFinished() {
                return timer.get() > 1.0;
            }
        };
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("IntakePivot Pos", pivotMotor.getPosition().getValueAsDouble());
    }

}

