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

    //private SendableChooser <Integer> pipeLineNumber;
    NetworkTable limeLightTable;
   //
   //pipeLineNumber = SendableChooser<>;
   //for(int i = 0; i<10; i++){
   //SmartDashboard.putData(i, pipeLineNumber);
   // }
    public limeLight() {
        limeLightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }
    // if this method returns 1 then the target exists
    public double targetExistance() {
        return limeLightTable.getEntry("ty").getDouble(0);
    }
//   if(pipelineNumber.getSelected != null){
        
//   limeLightTable.getEntry("pipeline").setNumber(piplineNumber.getSelected);
//   } else {
//       return null;
//   }

    public void setPipeLine()
    {
       limeLightTable.getEntry("pipeline").setNumber(1);
    }
   //public double getPipe(){
   //    return limeLightTable.getEntry("getPipe").getDouble(1);
   //}
    
    public double Allignment(){
        return limeLightTable.getEntry("tx").getDouble(0);
    }
    
    public double distanceAway(){
        return limeLightTable.getEntry("ta").getDouble(0);
    
//   double[] botPose = new double[0];
   // int n = 1;
//for (int i = 0; i < n; i++){
//    if (botPose != null){
//        n++;
//        SmartDashboard.putNumber("botPose", botPose[i]);
//    }

    }

}

