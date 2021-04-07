package frc.robot;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.trajectory.*;
import java.io.IOException;
import java.nio.file.Path;


public class autoBlocks {
    public double lastRotation;
    public void autoIntake(boolean running){
        if(running){
            if(Robot.pitchEnc.getPosition() > 0.5){
                Robot.pitcher.set(-0.25);
            }
            else if(Robot.pitchEnc.getPosition() < -0.5){
                Robot.pitcher.set(0.25);
            }
            else{
                Robot.pitcher.set((-.5*Robot.pitchEnc.getPosition()));
            }
            Robot.pitcherIn.set(1);
            Robot.intake.set(0.3);
        }
        else{
            Robot.pitcherIn.set(0);
            Robot.intake.set(0);
        }
    }
    limelight autoLime = new limelight();
    int stage = -1;
    launchAngler launcheyBoi = new launchAngler();
    public void straight(double inches){
        boolean direction;
        if(inches < 0){
            direction = false;
        }
        else{
            direction = true;
        }
        if(Robot.stageStart){
            Robot.startPos = Robot.frEnc.getPosition();
            Robot.stageStart = false;
        }
        double encTicks = distanceToEnc(inches);
        if(Robot.flEnc.getPosition() <  Robot.startPos + encTicks && direction == true){
            Robot.scoot.driveCartesian(0, 0.5, 0);
        }
        else if(Robot.flEnc.getPosition() >  Robot.startPos + encTicks && direction == false){
            Robot.scoot.driveCartesian(0, -0.5, 0);
        }
        else{
            Robot.scoot.driveCartesian(0, 0, 0);
            stage++;
            Robot.stageStart = true;
        }
    }
    void rotate(double degree){
        double yawTarget = 0;
        Robot.pigeon.getYawPitchRoll(Robot.gyroRead);
        if(Robot.stageStart){
            yawTarget = Robot.gyroRead[0] + degree;
            Robot.stageStart = false;
        }
  
        if(Math.abs(Robot.gyroRead[0]- yawTarget) >= 0.2){
          Robot.pigeon.getYawPitchRoll(Robot.gyroRead);
          if(Robot.gyroRead[0] > yawTarget + 10){
            Robot.scoot.driveCartesian(0, 0, 0.2);
          }
          else if(Robot.gyroRead[0] < yawTarget - 10){
            Robot.scoot.driveCartesian(0, 0, -0.2);
          }
          else{
            Robot.scoot.driveCartesian(0, 0, (yawTarget - Robot.gyroRead[0]) * 0.01);
          }
          
        }
        else{
          Robot.scoot.driveCartesian(0, 0, 0);
          stage++;
          Robot.stageStart = true;
        }
      }
    // public void arc(double degrees, double radius, String direction){
    //     System.out.println(degrees/360);
    //     double outerDistance = (radius + 9.75) * 2 * Math.PI;
    //     // System.out.println(outerDistance/(8*Math.PI) * 12.75);
    //     double innerDistance = (radius - 9.75) * 2 * Math.PI;
        
    //     double proportion = degrees/360;
    //     double speedRatio = innerDistance/outerDistance;
    //     double leftEnc = 0;
    //     double rightEnc = 0;
    //     boolean sideSpeed = true;
    //     int multiple = 1;
        
    //     System.out.println(distanceToEnc(outerDistance) * proportion + " outer rotations");
    //     System.out.println(Robot.frEnc.getPosition());
    //     if (direction == "right"){
    //         leftEnc = Robot.flEnc.getPosition() + (distanceToEnc(outerDistance) * proportion);
    //         rightEnc = Robot.frEnc.getPosition() + (distanceToEnc(innerDistance) * proportion);
    //         sideSpeed = true;
    //         multiple = -1;
    //     }
    //     else if(direction == "left"){
    //         // System.out.println("lefty");
    //         rightEnc = (Robot.frEnc.getPosition() + (distanceToEnc(outerDistance) * proportion));
    //         leftEnc = Robot.flEnc.getPosition() + (distanceToEnc(innerDistance) * proportion);
    //         // System.out.println(rightEnc + " right, " + leftEnc + " left");
    //         sideSpeed = false;
    //         multiple = 1;
    //     }
    //     // System.out.println(speedRatio);
    //     while(Math.abs(Robot.flEnc.getPosition() - leftEnc) >= 0.5 ||
    //      Math.abs(Robot.frEnc.getPosition() - rightEnc) >= 0.5){
    //         // System.out.println(Robot.frEnc.getPosition() + " right " + Robot.flEnc.getPosition() + " left");
    //         System.out.println(Robot.flEnc.getPosition());
    //         System.out.println(rightEnc + " target");
    //         if(sideSpeed == false){
    //             Robot.flPID.setReference(3000*speedRatio, ControlType.kVelocity);
    //             Robot.blPID.setReference(3000*speedRatio, ControlType.kVelocity);
    //             Robot.frPID.setReference(-3000, ControlType.kVelocity);
    //             Robot.brPID.setReference(-3000, ControlType.kVelocity);
    //         }
    //         else{
    //             Robot.flPID.setReference(3000, ControlType.kVelocity);
    //             Robot.blPID.setReference(3000, ControlType.kVelocity);
    //             Robot.frPID.setReference(-3000*speedRatio, ControlType.kVelocity);
    //             Robot.brPID.setReference(-3000*speedRatio, ControlType.kVelocity);
    //         }
    //     }
    //     Robot.frontL.set(0);
    //     Robot.frontR.set(0);
    //     Robot.backL.set(0);
    //     Robot.backR.set(0);

    // }

    public void fullAuto(int position){
        while(System.currentTimeMillis() < Robot.autoDelay + 250){
            Robot.pitcher.set(-0.2);
        }
        if(position == 1){
            switch (stage){
                case -1:
                    Robot.pitcher.set(0.2);
                    if(Robot.zero.get()){
                        Robot.pitcher.set(0);
                        Robot.pigeon.getYawPitchRoll(Robot.gyroRead);
                        lastRotation = Robot.gyroRead[0];
                        stage++;
                    }
                break;
                case 0:
                    Robot.scoot.driveCartesian(0, 0, -0.1);
                    if(autoLime.isTarget != 0){
                        Robot.scoot.driveCartesian(0, 0, 0);
                        stage++;
                    }
                break;
                case 1:
                    
                    launcheyBoi.tip(); 
                    // launcheyBoi.autoLaunchTime();
                    Robot.launch(2);
                    
                    if(Robot.ballCount == 0){
                        stage++;
                    }
                break;
                case 2:
                    Robot.pigeon.getYawPitchRoll(Robot.gyroRead);
                    rotate(Robot.gyroRead[0]-lastRotation);
                break;
                case 3:
                    autoIntake(true);
                    straight(-194);
                    Robot.pigeon.getYawPitchRoll(Robot.gyroRead);
                    lastRotation = Robot.gyroRead[0];
                break;
                case 4:
                    autoIntake(false);
                    stage++;
                break;
                case 5:
                    
                    autoLime.camControl();
                    launcheyBoi.tip();
                    Robot.ballsLeft = 0;
                    // launcheyBoi.autoLaunchTime();
                    
                    Robot.launch(2);
                    if(Robot.ballCount == 0){
                        stage++;
                    }
                break;
                case 6:
                    rotate(Robot.gyroRead[0]-lastRotation);
                    launcheyBoi.angleSet(0);
                case 7:
                    straight(-200);
                break;
                default:
            }
        }
        else if(position == 2){
            switch (stage){
                case -1:
                    Robot.pitcher.set(0.2);
                    if(Robot.zero.get()){
                        Robot.pitcher.set(0);
                        stage++;
                    }
                break;
                case 0:
                    straight(-35);
                break;
                case 1:
                    if(autoLime.xTranslate > 1 || autoLime.xTranslate < -1){
                        Robot.scoot.driveCartesian(0, 1, 0);
                    }
                    else{
                        Robot.scoot.driveCartesian(0, 0, 0);
                        stage++;
                    }
                break;
                case 2:
                    
                    autoLime.camControl();
                    launcheyBoi.tip();
                    Robot.ballsLeft = 0;
                    // launcheyBoi.autoLaunchTime();
                    Robot.launch(2);
                    
                    if(Robot.ballCount == 0){
                        stage++;
                    }
                break;
                case 3:
                    straight(-200);
                break;
                default:
            }
        }
        else if(position == 3){
            switch(stage){
                case -1:
                Robot.pitcher.set(0.2);
                if(Robot.zero.get()){
                    Robot.pitcher.set(0);
                    stage++;
                }
                break;
                case 0:
                    
                    autoLime.camControl();
                    launcheyBoi.tip();
                    // launcheyBoi.autoLaunchTime();
                    Robot.launch(2);
                    Robot.ballsLeft = 0;
                    
                    if(Robot.ballCount <= 0){
                        stage++;
                        Robot.topLaunch.set(0);
                        Robot.bottomLaunch.set(0);
                    }
                break;
                case 1:
                    straight(-35);
                break;
                default:
            }
        }
    }
    public void autoCam(){
        
        autoLime.camControl();
        launcheyBoi.tip();
        // launcheyBoi.autoLaunchTime();
        Robot.launch(2); 
        
        if(Robot.ballCount >= 0){
            stage++;
            Robot.topLaunch.set(0);
            Robot.bottomLaunch.set(0);
        }
    }
    public double distanceToEnc(double distance){
        double rotations = (distance/(8*Math.PI))*12.75;
        return rotations;
    }

}
