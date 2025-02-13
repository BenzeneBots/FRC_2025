package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConstants.IntakeSpinnerConstants;

public class CoralSpinner extends SubsystemBase {
    private final TalonFX spinner = new TalonFX(56, "BB_CANIVORE");

    public CoralSpinner() {}

    public Command intake() {
        return new Command() {
            @Override
            public void execute() {
                spinner.set(IntakeSpinnerConstants.intakeSpeed);
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
                spinner.set(IntakeSpinnerConstants.outtakeSpeed);
            }
            
            @Override
            public void end(boolean interrupted) {
                spinner.stopMotor();
            }
        };
    }

    public Command level1() {
        return new Command() {
            @Override
            public void execute() {
                spinner.set(IntakeSpinnerConstants.level1Speed);
            }

            @Override
            public void end(boolean interrupted) {
                spinner.stopMotor();
            }
        };
    }
}
