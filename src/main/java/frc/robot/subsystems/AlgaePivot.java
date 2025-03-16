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
    private double zeroPos = 0.0;

    public AlgaePivot() {
        pivotMotor = new TalonFX(59);
        reset();
        configMotor();
        controller = new MotionMagicVoltage(0);
    }

    public void reset() {
        this.zeroPos = pivotMotor.getPosition().getValueAsDouble();
    }

    public void configMotor() {
        pivotMotor.clearStickyFaults();
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
        config.Slot0.kP = 3.0;
        config.Slot0.kD = 0.15;
        config.Slot0.kI = 15.0;
        config.Slot0.kG = 0.0;

        var motionMagicConfigs = config.MotionMagic;
        motionMagicConfigs.MotionMagicCruiseVelocity = 30;
        motionMagicConfigs.MotionMagicAcceleration = 60;
        motionMagicConfigs.MotionMagicJerk = 600;

        config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = this.zeroPos + 4;
        config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = this.zeroPos;

        pivotMotor.getConfigurator().apply(config);
    }

    public Command stowPivot() {
        return setPosition(AlgaePivotConstants.stowPos + this.zeroPos);
    }

    public Command score() {
        return setPosition(AlgaePivotConstants.score + this.zeroPos);
    }

    public Command deploy() {
        return setPosition(AlgaePivotConstants.deployed + this.zeroPos);
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
        SmartDashboard.putNumber("Algae Pivot Pos", pivotMotor.getPosition().getValueAsDouble() - this.zeroPos);
        SmartDashboard.putNumber("Algae Zero Pos", this.zeroPos);
    }
}
