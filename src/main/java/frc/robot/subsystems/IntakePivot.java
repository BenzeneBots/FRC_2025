package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicDutyCycle;
import com.ctre.phoenix6.controls.MotionMagicExpoDutyCycle;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConstants.IntakePivotConstants;;

public class IntakePivot extends SubsystemBase {
    private final TalonFX pivotMotor;
    private final MotionMagicDutyCycle controller;

    public IntakePivot() {
        pivotMotor = new TalonFX(60);
        configMotor();
        controller = new MotionMagicDutyCycle(0);
    }

    public void configMotor() {
        pivotMotor.setPosition(0);
        pivotMotor.setNeutralMode(NeutralModeValue.Brake);

        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
        config.Slot0.kP = 0;
        config.Slot0.kD = 0;
        config.Slot0.kI = 0;
        config.Slot0.kG = 0;

        var motionMagicConfigs = config.MotionMagic;
        motionMagicConfigs.MotionMagicCruiseVelocity = 80;
        motionMagicConfigs.MotionMagicAcceleration = 160;
        motionMagicConfigs.MotionMagicJerk = 1600;

        pivotMotor.getConfigurator().apply(config);
    }

    public Command up() {
        return new Command() {
            @Override
            public void execute() {
                pivotMotor.set(0.1);
            }

            @Override
            public void end(boolean interrupted) {
                pivotMotor.stopMotor();
            }
        };
    }

    public Command down() {
        return new Command() {
            @Override
            public void execute() {
                pivotMotor.set(-0.1);
            }

            @Override
            public void end(boolean interrupted) {
                pivotMotor.stopMotor();
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
                pivotMotor.setControl(controller.withSlot(0).withPosition(IntakePivotConstants.humanPlayer));
            }
        };
    }

    public Command level2() {
        return new Command() {
            @Override
            public void execute() {
                pivotMotor.setControl(controller.withSlot(0).withPosition(IntakePivotConstants.level2));
            }
        };
    }

    public Command level1() {
        return new Command() {
            @Override
            public void execute() {
                pivotMotor.setControl(controller.withSlot(0).withPosition(IntakePivotConstants.level1));
            }
        };
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("IntakePivot Pos", pivotMotor.getPosition().getValueAsDouble());
    }
}
