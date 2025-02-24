package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConstants.AlgaePivotConstants;


public class AlgaePivot extends SubsystemBase {
    private final TalonFX pivotMotor;
    private final MotionMagicVoltage controller;

    public AlgaePivot() {
        pivotMotor = new TalonFX(61, "BB_CANIVORE");
        configMotor();
        controller = new MotionMagicVoltage(0);
    }

    public void setZero() {
        pivotMotor.setPosition(0.0);
    }

    public void configMotor() {
        pivotMotor.clearStickyFaults();
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
        config.Slot0.kP = 0.5;
        config.Slot0.kD = 0;
        config.Slot0.kI = 0.5;
        config.Slot0.kG = 0.5;

        var motionMagicConfigs = config.MotionMagic;
        motionMagicConfigs.MotionMagicCruiseVelocity = 80;
        motionMagicConfigs.MotionMagicAcceleration = 160;
        motionMagicConfigs.MotionMagicJerk = 1600;

         config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
         config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
         config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 0.0;
         config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = -27.0;

        pivotMotor.getConfigurator().apply(config);
    }

    public Command up() {
        return new Command() {
            @Override
            public void execute() {
                pivotMotor.set(AlgaePivotConstants.speed);
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
                pivotMotor.set(-AlgaePivotConstants.speed);
            }

            @Override
            public void end(boolean interrupted) {
                pivotMotor.stopMotor();
                pivotMotor.setControl(controller.withSlot(0).withPosition(pivotMotor.getPosition().getValueAsDouble()));
            }
        };
    }
    
    public Command stowPivot() {
        return setPosition(AlgaePivotConstants.stowPos);
    }

    public Command score() {
        return setPosition(AlgaePivotConstants.score);
    }

    public Command deploy() {
        return setPosition(AlgaePivotConstants.deployed);
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
        SmartDashboard.putNumber("Algae Pivot Pos", pivotMotor.getPosition().getValueAsDouble());
    }
}
