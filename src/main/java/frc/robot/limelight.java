package frc.robot;

//import com.revrobotics.*;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.*;

public class limelight{
      final Integer forceOff = 1;
      final Integer forceBlink = 2;
      final Integer forceOn = 3;
  
      final Integer VisionProccesing = 0;
      final Integer DrivingCamera = 1;
  
      final Integer PIPMain = 1;
      final Integer sideBySide = 0;
      final Integer PIPSec = 2;

      final Integer mainPipe = 0;
      final Integer rightPipe = 2;
      final Integer leftPipe = 1;
    
      NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
      NetworkTableEntry tx = table.getEntry("tx");
      NetworkTableEntry ty = table.getEntry("ty");
      NetworkTableEntry tv = table.getEntry("tv");
      NetworkTableEntry ta = table.getEntry("ta");
      NetworkTableEntry ts = table.getEntry("ts");
      NetworkTableEntry tvert = table.getEntry("tvert");
      NetworkTableEntry tlong = table.getEntry("tlong");
      NetworkTableEntry tshort = table.getEntry("tshort");
  
      NetworkTableEntry ledMode = table.getEntry("ledMode");
      NetworkTableEntry camMode = table.getEntry("camMode");
      NetworkTableEntry stream = table.getEntry("stream");
      NetworkTableEntry pipeline = table.getEntry("pipeline");

      // NetworkTableEntry dashxOff = 
      //   Limit.add("xOffset",0.0)
      //   .getEntry();
      // NetworkTableEntry dashTarget =
      //   Limit.add("Target",false)
      //   .getEntry();
      final double visionSpeed = .666;
      final double turnval = .4;//originally 0.4
      public double xTranslate;
      public double yTranslate;
      public double yOffLimit = -509;

      double distance;
      public boolean trueTarget;

      public int isTarget;
    public void camControl(){
      pipeline.setDouble(0);
      isTarget = (int)tv.getDouble(0);
        xTranslate = tx.getDouble(0.0);
        yTranslate = ty.getDouble(0);
        // if(yTranslate <= yOffLimit && isTarget != 0){
        //   trueTarget = false;
        // }
        // else{
          trueTarget = true;
        // }
        if(isTarget != 0){
          // System.out.println(offsetCalculator() + " calculated " + xTranslate + " actual");
            // System.out.println(-0.3*Math.pow(-(xTranslate - offsetCalculator())/57, 0.6));
            
            if(xTranslate > offsetCalculator() + 1){ //was + 0.1
                // System.out.println(-0.4*Math.pow(-xTranslate/54, 0.5));
                Robot.scoot.driveCartesian(Robot.logi1.getRawAxis(0), -Robot.logi1.getRawAxis(1), 0.25*Math.pow((xTranslate + offsetCalculator())/57, 0.9));
                // Robot.scoot.driveCartesian(Robot.ps4.getRawAxis(0), -Robot.ps4.getRawAxis(1), -0.2);
            }
            
            else if(xTranslate <   offsetCalculator() - 1){ //was - 0.1
              // System.out.println(0.4*Math.pow(xTranslate/54, 0.5));
                Robot.scoot.driveCartesian(Robot.logi1.getRawAxis(0), -Robot.logi1.getRawAxis(1), -0.25*Math.pow(-(xTranslate - offsetCalculator())/57, 0.9));
                // Robot.scoot.driveCartesian(Robot.ps4.getRawAxis(0), -Robot.ps4.getRawAxis(1), 0.2);
            }
            else{
              Robot.scoot.driveCartesian(Robot.logi1.getRawAxis(0), -Robot.logi1.getRawAxis(1), Robot.logi.getRawAxis(0)); 
            }
        }
        else{
          Robot.scoot.driveCartesian(Robot.logi1.getRawAxis(0), -Robot.logi1.getRawAxis(1), Robot.logi.getRawAxis(0));
        }
    }
    public double offsetCalculator(){
      double inchOffRobot = 8.5;
      double offset = Math.atan(inchOffRobot/(rangeFinder()));
      
      offset = (offset * 180)/Math.PI;
      return offset;
    }
    
    public double rangeFinder(){
      isTarget = (int)tv.getDouble(0);
      double heightFloor = 23;//originally 23
      double llOffset = 8.25;//originally 8.25    note-not used
      double llAngle = 27;//originally 27
      double targetHeight = 91.125; //originally 91.125

      double yOff = ty.getDouble(0.0);
      double xOff = tx.getDouble(0.0);
      // System.out.println(xOff);
      // System.out.println(yOff + llAngle);
      // System.out.println(isTarget);
      if (isTarget != 0){
        // System.out.println("TARGET ACQUIRED");
        distance = (int)((targetHeight-heightFloor)/Math.tan((yOff + llAngle) * ((2 * Math.PI)/360)));
        // distance = distance - (llOffset * Math.sin(Math.abs(xOff) * ((2 * Math.PI)/360)));
        
      }
      else{
        distance = -1;
      }
      return distance+29;
    }
    
}
