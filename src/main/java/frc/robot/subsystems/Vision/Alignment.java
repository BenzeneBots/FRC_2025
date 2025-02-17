package frc.robot.subsystems.Vision;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Telemetry;

//here i want to incorporate tx and ta so based on the 
//allignment and postion of the april tag the robot will move to some postion
public class Alignment extends SubsystemBase {
    private final limeLight m_limeLight = new limeLight();
    private Telemetry m_telemetry;
    private Pose2d pose2d;

    double tx;
    double ta;
    double ty;
    double tv;
    double rotation;

    double velocityXOffset;
    double velocityZOffset;
    double  zero;

    double finishedDistanceThreshold;
    double finishedXThreshold;


//private final SwerveRequest.RobotCentric m_limeRequest;

    public Alignment(){
    }
    public void updatePosition(){
    tx = m_limeLight.horizontalOfset();
    ta = m_limeLight.distanceAway();
    tv = m_limeLight.targetExistance();
    }
    public  void move(){
        if (tv == 1){
            if (tx == 0){
                velocityXOffset = 0;
            } else if(tx>0.2){
                velocityXOffset =1;
            } else {
                velocityXOffset = -1;
            }

            if (ta==80){
                velocityZOffset = 0;
            } else if (ta<20){
            velocityZOffset= 1;
            } else{
                velocityZOffset = -1;
            }

            zero = 0;

            } else {
            velocityXOffset =0;
            velocityZOffset = 0;
         } 

    }
}