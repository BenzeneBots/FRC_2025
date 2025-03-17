package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix6.hardware.TalonFX;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.signals.GravityTypeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConstants.ElevatorConstants;

public class Elevator extends SubsystemBase {
    private final TalonFX pivotMotor;
    private final MotionMagicVoltage controller;
    private double zeroPos = 0.0;

    public Elevator() {
        pivotMotor = new TalonFX(60);
        reset();
        configMotor();
        controller = new MotionMagicVoltage(0);
    }

    public Command overrideCommand(DoubleSupplier joystick) {
        return new Command() {
            @Override
            public void execute() {
                if(joystick.getAsDouble() > 0.05) {
                    pivotMotor.set(0.1);
                } else if(joystick.getAsDouble() < -0.05) {
                    pivotMotor.set(-0.1);
                } else {
                    pivotMotor.stopMotor();
                }
            }
        };
    }

    public void reset() {
        this.zeroPos = pivotMotor.getPosition().getValueAsDouble();
    }

    public void configMotor() {
        pivotMotor.clearStickyFaults();
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.GravityType = GravityTypeValue.Elevator_Static;
        config.Slot0.kP = 2.5;
        config.Slot0.kD = 0.15;
        config.Slot0.kI = 20.0;
        config.Slot0.kG = 0;

        var motionMagicConfigs = config.MotionMagic;
        motionMagicConfigs.MotionMagicCruiseVelocity = 100;
        motionMagicConfigs.MotionMagicAcceleration = 200;
        motionMagicConfigs.MotionMagicJerk = 2000;

        config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = this.zeroPos + 298;
        config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = this.zeroPos;

        pivotMotor.getConfigurator().apply(config);
    }

    public Command feedPos() {
        return setPosition(ElevatorConstants.feedPos);
    }

    public Command level2Pos() {
        return setPosition(ElevatorConstants.level2Pos);
    }

    public Command level3Pos() {
        return setPosition(ElevatorConstants.level3);
    }

    public Command resetPos() {
        return setPosition(this.zeroPos);
    }

    public Command intermediatePos() {
        return setPosition(ElevatorConstants.intermediatePos);
    }

    public Command setPosition(double position) {
        return new Command() {
            Timer timer = new Timer();
            @Override
            public void initialize() {
                super.initialize();
                timer.start();
            }

            @Override
            public void execute() {
                pivotMotor.setControl(controller.withSlot(0).withPosition(position));
            }

            @Override
            public boolean isFinished() {
                // Change this to something better
                // return pivotMotor.getMotorOutputStatus().getValue().value != 3;
                return timer.get() > 1;
            }
        };
    }
    
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Elevator Pos", pivotMotor.getPosition().getValueAsDouble() - this.zeroPos);
        SmartDashboard.putString("Elevator Motor Output", pivotMotor.getMotorOutputStatus().toString());

        // if(this.joystick.getAsDouble() > 0.05) {
        //     pivotMotor.set(0.1);
        // } else if(this.joystick.getAsDouble() < -0.05) {
        //     pivotMotor.set(-0.1);
        // } else {
        //     pivotMotor.stopMotor();
        // }
    }
}