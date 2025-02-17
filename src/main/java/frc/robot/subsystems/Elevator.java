package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.signals.GravityTypeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConstants;
import frc.robot.RobotConstants.IntakeSpinnerConstants;

public class Elevator extends SubsystemBase {
    private TalonFX elevatorMotor;
    private PositionDutyCycle controller;
     public Elevator (){
        elevatorMotor = new TalonFX(63, "BB_CANIVORE");
     }
     public void setMotorZero(){
        elevatorMotor.setPosition(0);
        controller = new PositionDutyCycle(0);
     }
     public void TalonFXConfiguration(){
        elevatorMotor.clearStickyFault_BootDuringEnable();
        TalonFXConfiguration config = new TalonFXConfiguration();


        //PID
        config.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
        config.Slot0.kP = 0.5;
        config.Slot0.kD = 0.0;
        config.Slot0.kI = 0.5;
        config.Slot0.kG = 0.5;


        //MM

        //SoftLimits

        config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 50.0;
        config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = 0.0;



        elevatorMotor.getConfigurator().apply(config);
     }

     public Command elevatorUp(){
        return new Command(){
            @Override
            public void execute(){
                elevatorMotor.set(RobotConstants.ElevatorConstants.speed);
            }
            @Override
            public void end(boolean interrupted){
                elevatorMotor.stopMotor();
                elevatorMotor.setControl(controller.withSlot(0).withPosition(elevatorMotor.getPosition().getValueAsDouble()));
            }
        };
     }
     public Command elevatorDown(){
        return new Command(){
            @Override
            public void execute(){
                elevatorMotor.set(-RobotConstants.ElevatorConstants.speed);
            }
            @Override
            public void end(boolean interrupted){
                elevatorMotor.stopMotor();
                elevatorMotor.setControl(controller.withSlot(0).withPosition(elevatorMotor.getPosition().getValueAsDouble()));
            }
        };
     }
     public Command posFour(){
       return setPosition(RobotConstants.ElevatorConstants.posFour);
     }

     public Command posThree(){
        return setPosition(RobotConstants.ElevatorConstants.posThree);
     }

     public Command posTwo(){
        return setPosition(RobotConstants.ElevatorConstants.posTwo);
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
                elevatorMotor.setControl(controller.withVelocity(0.5).withPosition(position));
            }

            @Override
            public boolean isFinished() {
                return timer.get() > 1.0;
            }
        };
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("elevator Pos", elevatorMotor.getPosition().getValueAsDouble());
    }





}
