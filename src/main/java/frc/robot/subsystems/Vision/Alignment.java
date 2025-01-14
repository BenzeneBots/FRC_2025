package frc.robot.subsystems.Vision;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Telemetry;

//here i want to incorporate tx and ta so based on the 
//allignment and postion of the april tag the robot will move to some postion
public class Alignment extends SubsystemBase {
limeLight m_limeLight = new limeLight();
private Telemetry m_telemetry;
double tx =  m_limeLight.horizontalOfset();
double ta =  m_limeLight.distanceAway();
double ty = m_limeLight.verticalOffset();
double rotation;

double distanceThreshold = 0.5;
private Pose2d pose2d;
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


}
    //figure out how the offsets work i.e rotation calculation and direction
    public void initialize(){
        pose2d = new Pose2d(pose2d.getX() + tx, pose2d.getY() +ta, pose2d.getRotation());
    }
    public void execute(){

        // use setControl coupled with swerve request to actually move the robot to desire position
        // do research on setControl implementation
    }
    //male sure to include @Override
    public void isFinished(){
        // add a threshold tolerance so that when the robot is within a certain distance
        // away from the april tag this method gets called to finish the allignment
    }

}
