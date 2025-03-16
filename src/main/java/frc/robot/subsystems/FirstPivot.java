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

public class FirstPivot extends SubsystemBase {
    private final TalonFX pivotMotor;
    private final MotionMagicVoltage controller;
    private double zeroPos = 0.0;

    public FirstPivot() {
        pivotMotor = new TalonFX(61, "BB_CANIVORE");
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
        config.Slot0.kG = 0;

        var motionMagicConfigs = config.MotionMagic;
        motionMagicConfigs.MotionMagicCruiseVelocity = 40;
        motionMagicConfigs.MotionMagicAcceleration = 80;
        motionMagicConfigs.MotionMagicJerk = 800;

        config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = this.zeroPos + 5.53;
        config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = this.zeroPos - 36.5;

        pivotMotor.getConfigurator().apply(config);
    }

    public Command feedPos() {
        return setPosition(FirstPivotConstants.feedPos);
    }
    public Command level2Pos() {
        return setPosition(FirstPivotConstants.level2Pos);
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
        SmartDashboard.putNumber("First Pivot Pos", pivotMotor.getPosition().getValueAsDouble() - this.zeroPos);
    }
}
