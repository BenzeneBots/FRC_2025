package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private final TalonFX climbMotor = new TalonFX(46); 

    public Climber() {}

    public Command up() {
        return new Command() {
            @Override
            public void execute() {
                climbMotor.set(1.0);
            }

            @Override
            public void end(boolean interrupted) {
                climbMotor.stopMotor();
            }
        };
    }

    public Command down() {
        return new Command() {
            @Override
            public void execute() {
                climbMotor.set(-1.0);
            }

            @Override
            public void end(boolean interrupted) {
                climbMotor.stopMotor();
            }
        };
    }
}
