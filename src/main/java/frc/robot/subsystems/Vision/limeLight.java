package frc.robot.subsystems.Vision;

import org.ejml.equation.IntegerSequence.For;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;
public class limeLight extends SubsystemBase {

    NetworkTable limeLightTable;
    public limeLight() {
        limeLightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }
    // if this method returns 1 then the target exists
    public double targetExistance() {
        return limeLightTable.getEntry("tv").getDouble(0);
    }    
    public void setPipeLine()
    {
       limeLightTable.getEntry("pipeline").setNumber(1);
    }
   
    public double horizontalOfset(){
        return limeLightTable.getEntry("tx").getDouble(0);
    }
    
    public double distanceAway(){
        return limeLightTable.getEntry("ta").getDouble(0);
    }
    public double verticalOffset(){

        return limeLightTable.getEntry("ty").getDouble(0);
    } 
    public void botPosition(){
    double [] botPose = limeLightTable.getEntry("botPose").getDoubleArray(new double[0]);
    if(botPose.length>0){
        for(int i = 0; i < botPose.length -1; i++){
            SmartDashboard.putNumber("botPose"+ i, botPose[i]);
        }

    } 
}
    public void updateDashboard() {
        double Area = distanceAway();
        double verticalOffset = verticalOffset();
        double horizontalOfset = horizontalOfset();

        SmartDashboard.putNumber("Limelight X", horizontalOfset);
        SmartDashboard.putNumber("Limelight Y", Area);
        SmartDashboard.putNumber("Target Area", verticalOffset);
    }


}

