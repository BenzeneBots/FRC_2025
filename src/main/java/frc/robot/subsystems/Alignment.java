package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Telemetry;

//here i want to incorporate tx and ta so based on the 
//allignment and postion of the april tag the robot will move to some postion
public class Alignment extends SubsystemBase {
limeLight m_limeLight = new limeLight();
private Telemetry m_telemetry;
double tx =  m_limeLight.Allignment();
double ta =  m_limeLight.distanceAway();

double distanceThreshold = 0.5;

public Alignment(){

}
public void move(){
    if (tx>distanceThreshold){
        //move left
    } else{
        //move right
    }
    if (ta>1){
        //forward
    } else{
        //backward
    }


 //  for (int i = 0; i < 4; ++i) {
 //      m_moduleSpeeds[i].setAngle(state.ModuleStates[i].angle);
 //      m_moduleDirections[i].setAngle(state.ModuleStates[i].angle);
 //      m_moduleSpeeds[i].setLength(state.ModuleStates[i].speedMetersPerSecond / (2 * MaxSpeed));

 //  
 //  }

}

}
