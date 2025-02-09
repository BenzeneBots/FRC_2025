// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralPivot;

public class humanPlayerCommand extends Command {
  /** Creates a new humanPlayer. */
  private CoralPivot coralPivot;
  public humanPlayerCommand(CoralPivot coralPivot) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.coralPivot = coralPivot;
    addRequirements(this.coralPivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    coralPivot.humanPlayer(10);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    coralPivot.humanPlayer(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
