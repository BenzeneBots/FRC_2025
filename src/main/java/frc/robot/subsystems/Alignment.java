package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

//here i want to incorporate tx and ta so based on the 
//allignment and postion of the april tag the robot will move to some postion
public class Alignment extends SubsystemBase {
limeLight m_limeLight = new limeLight();
double tx =  m_limeLight.Allignment();
double ta =  m_limeLight.distanceAway();


}
