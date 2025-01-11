package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Pivot;

import java.util.function.BooleanSupplier;

public class StateManager {
    //make variables
    private Intake intake;
    private Pivot pivot;
    private Elevator elevator;
    public StateManager() {
        intake = new Intake();
        pivot = new Pivot();
        elevator = new Elevator();
    }

    public Command goToBase() {
        return new Command() {
            @Override
            public void execute() {
                elevator.base();
                pivot.ground();
                intake.intake();
            }



        };
    }

    public Command goToExtend() {
        return new Command() {
            @Override
            public void execute() {
                elevator.extend();
                pivot.straight();
            }
        };
    }

    public Command goToMid() {
        return new Command() {
            @Override
            public void execute() {
                elevator.middle();
                pivot.middle();
                intake.outake(0);
            }
        };
    }


}
