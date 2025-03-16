package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConstants.IntakeSpinnerConstants;


public class CoralSpinner extends SubsystemBase {
    private final TalonFX spinner = new TalonFX(55, "BB_CANIVORE");

    public CoralSpinner() {
    }

    public Command intake() {
        return new Command() {
            @Override
            public void execute() {
                spinner.setVoltage(4.0);
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
                spinner.setVoltage(-15.0);
            }
            
            @Override
            public void end(boolean interrupted) {
                spinner.stopMotor();
            }
        };
    }
}
