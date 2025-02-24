package frc.robot.subsystems.Vision;


import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.CoralSpinner;   
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class limeLight extends SubsystemBase {
    private final CoralSpinner s_CoralSpinner;
    NetworkTable limeLightTable;
    public limeLight() {
        limeLightTable = NetworkTableInstance.getDefault().getTable("limelight");
        s_CoralSpinner = new CoralSpinner();
    }
        public double[] getBotPose() {
        return limeLightTable.getEntry("botpose").getDoubleArray(new double[6]);
    }
    public double[] getTargetPose() {
        return limeLightTable.getEntry("targetpose").getDoubleArray(new double[6]);
    }

    // if this method returns 1 then the target exists
    public boolean targetExistance() {
        return limeLightTable.getEntry("tv").getDouble(0) == 1;
    }    
    public void setPipeLine(int pipeline)
    {
       limeLightTable.getEntry("pipeline").setNumber(pipeline);
    }
   
    public double horizontalOfset(){
        return limeLightTable.getEntry("tx").getDouble(0.0);
    }
    
    public double distanceAway(){
        return limeLightTable.getEntry("ta").getDouble(0.0);
    }
    public double verticalOffset(){

        return limeLightTable.getEntry("ty").getDouble(0.0);
    } 
    public Pose2d robotPose(){
        double[] botPose = getBotPose();
        double x = botPose[0];
        double y = botPose[1];
        double r = botPose[5];
        Pose2d robotPose = new Pose2d(x,y,Rotation2d.fromDegrees(r));
        return robotPose;
    }
    public void botPosition(){
        double[] botPose = getBotPose();
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
    public Command moveCoralRight(){
        return new InstantCommand(() -> {
            double[] botPose = getBotPose();
            if (botPose.length < 6){
                return;
            }
            double robotX = botPose[0];
            double robotY = botPose[1];
            double robotRotation = botPose[5];

            Pose2d targetPose = new Pose2d(robotX - 0.3, robotY, Rotation2d.fromDegrees(robotRotation));

            PathConstraints constraints = new PathConstraints(
            3.0, 4.0,
            Units.degreesToRadians(540), Units.degreesToRadians(720));

            Command pathfindingCommand = AutoBuilder.pathfindToPose(
            targetPose,
            constraints, 
            0.0);           
            pathfindingCommand.schedule();  
        });
    
    }
    public Command limeExists(){
             return new Command() {
            @Override
            public void execute() {
                if(targetExistance()){
                    s_CoralSpinner.level1();
                }
            }
        };
    }

    public Pose2d target(){
        double[] targetPose = getTargetPose();
        double targetX = targetPose[0];
        double targetY = targetPose[1];
        double targetR = targetPose[5];
        double newX = targetX - 0.3;
        Pose2d poseTarget = new Pose2d(newX, targetY, Rotation2d.fromDegrees(targetR));
        return poseTarget;
    }
    public Command targetCommand(){
        return new InstantCommand(() -> {
            if (!targetExistance()) {
                System.out.println("No AprilTag");
                return;
            }

            double[] target = getTargetPose();
            double targetX = target[0];
            double targetY = target[1];
            double targetRotation = target[5];

            Pose2d tp = new Pose2d(targetX - 0.3, targetY, Rotation2d.fromDegrees(targetRotation));


            PathConstraints constraints = new PathConstraints(
            3.0, 4.0,
            Units.degreesToRadians(540), Units.degreesToRadians(720));

            Command pathfindingCommand = AutoBuilder.pathfindToPose(
            tp,
            constraints, 
            0.0);           
            pathfindingCommand.schedule();             
        });
    }
    public Command tar() {
        return new Command() {
            public void execute() {
           double[] target = getTargetPose();
            double targetX = target[0];
            double targetY = target[1];
            double targetRotation = target[5];

            Pose2d tp = new Pose2d(targetX - 0.3, targetY, Rotation2d.fromDegrees(targetRotation));


            PathConstraints constraints = new PathConstraints(
            3.0, 4.0,
            Units.degreesToRadians(540), Units.degreesToRadians(720));

            Command pathCommand = AutoBuilder.pathfindToPose(
            tp,
            constraints, 
            0.0);                        
        
            pathCommand.schedule();             
            }
        };
    }


    @Override
    public void periodic(){
        updateDashboard();
        botPosition();
        SmartDashboard.putBoolean("limelightExists?", targetExistance());
        SmartDashboard.putNumberArray("targetPose", getTargetPose());
    }


}