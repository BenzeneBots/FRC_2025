package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConstants.FirstPivotConstants;
import frc.robot.RobotConstants.SecondPivotConstants;

public class SecondPivot extends SubsystemBase {
    private final TalonFX pivotMotor;
    private final MotionMagicVoltage controller;
    private double zeroPos = 0.0;

    public SecondPivot() {
        pivotMotor = new TalonFX(62, "BB_CANIVORE");
        reset();
        configMotor();
        controller = new MotionMagicVoltage(0);
    }

    public void reset() {
        this.zeroPos = pivotMotor.getPosition().getValueAsDouble();
    }

    public void configMotor() {
        pivotMotor.clearStickyFaults();
        pivotMotor.setNeutralMode(NeutralModeValue.Brake);
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
        config.Slot0.kP = 3.5;
        config.Slot0.kD = 0.05;
        config.Slot0.kI = 15.0;
        config.Slot0.kG = 0.0;

        var motionMagicConfigs = config.MotionMagic;
        motionMagicConfigs.MotionMagicCruiseVelocity = 80;
        motionMagicConfigs.MotionMagicAcceleration = 160;
        motionMagicConfigs.MotionMagicJerk = 1600;

        config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = this.zeroPos + 5.5;
        config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = this.zeroPos - 4.9;

        pivotMotor.getConfigurator().apply(config);
    }

    public Command feedPos() {
        return setPosition(SecondPivotConstants.feedPos);
    }

    public Command level2Pos() {
        return setPosition(SecondPivotConstants.level2Pos + this.zeroPos);
    }

    public Command resetPose() {
        return setPosition(this.zeroPos);
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
        SmartDashboard.putNumber("Second Pivot Pos", pivotMotor.getPosition().getValueAsDouble() - this.zeroPos);
    }
    
}
