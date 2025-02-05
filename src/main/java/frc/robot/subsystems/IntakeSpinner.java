package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSpinner extends SubsystemBase {
    private final TalonFX spinner = new TalonFX(56);

    public IntakeSpinner() {

    }

    public Command intake() {
        return new Command() {
            @Override
            public void execute() {
                spinner.set(0.5);
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
                spinner.set(-0.5);
            }
            
            @Override
            public void end(boolean interrupted) {
                spinner.stopMotor();
            }
        };
    }
    
}
