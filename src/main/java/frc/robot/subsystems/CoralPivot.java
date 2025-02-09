package frc.robot.subsystems;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicDutyCycle;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConstants.CoralPivotConstants;

public class CoralPivot extends SubsystemBase {
    private final TalonFX pivotMotor;
    private final MotionMagicVoltage controller;

   public CoralPivot() {
       pivotMotor = new TalonFX(CoralPivotConstants.CoralPivotDeviceID, "BB_CANIVORE");
       configMotor();
       controller = new MotionMagicVoltage(0);
   }

    public void zeroMotor() {
        pivotMotor.setPosition(0);
    }

    public void configMotor() {
        pivotMotor.clearStickyFaults();
        pivotMotor.getConfigurator().apply(CoralPivotConstants.CoralPivotConfigs);
        TalonFXConfiguration config = new TalonFXConfiguration();

//       config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
//       config.Slot0.kP = 0;
//       config.Slot0.kD = 0;
//       config.Slot0.kI = 0;
//       config.Slot0.kG = 0.5;

//       var motionMagicConfigs = config.MotionMagic;
//       motionMagicConfigs.MotionMagicCruiseVelocity = 80;
//       motionMagicConfigs.MotionMagicAcceleration = 160;
//       motionMagicConfigs.MotionMagicJerk = 1600;

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
                pivotMotor.set(0.1);
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
                pivotMotor.set(-0.1);
            }

            @Override
            public void end(boolean interrupted) {
                pivotMotor.stopMotor();
                pivotMotor.setControl(controller.withSlot(0).withPosition(pivotMotor.getPosition().getValueAsDouble()));
            }
        };
    }

    public void humanPlayer(double degrees){
        pivotMotor.setControl(controller.withSlot(0).withPosition(Units.degreesToRotations(degrees)));
    }

    public double getCoralPosition(){
        return pivotMotor.getPosition().getValueAsDouble();
    }
    @Override
    public void periodic() {
        SmartDashboard.putNumber("IntakePivot Pos", getCoralPosition());
    }
}