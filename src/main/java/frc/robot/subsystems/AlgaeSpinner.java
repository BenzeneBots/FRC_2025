package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConstants.AlgaeSpinnerConstants;

public class AlgaeSpinner extends SubsystemBase {
    private final SparkFlex spinner = new SparkFlex(23, MotorType.kBrushless);
    
    public AlgaeSpinner() {} 

    public Command intake() {
        return new Command() {
            @Override
            public void execute() {
                spinner.set(-AlgaeSpinnerConstants.speed);
            }

            @Override
            public void end(boolean interrupted) {
                spinner.stopMotor();
            }
        };
    }

    public Command outtake() {
        return new Command() {
            @Override
            public void execute() {
                spinner.set(AlgaeSpinnerConstants.speed);
            }

            @Override
            public void end(boolean interrupted) {
                spinner.stopMotor();
            }
        };
    }
}
