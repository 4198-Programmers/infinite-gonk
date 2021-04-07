package frc.robot;

import com.revrobotics.CANPIDController;
import com.revrobotics.ControlType;

public class launchAngler {
    limelight ranger = new limelight();
    double startAngle = 50;
    boolean launchLast = false;
    boolean centered = false;
    double atRange = -1;
    double delayTimer = 0;
    public double positionTarget;
    public void tip(){
        // System.out.println(centered);
        // if(ranger.xTranslate > ranger.offsetCalculator() + 2){
        //     centered = false;
        // }
        // else if(ranger.xTranslate < ranger.offsetCalculator() - 2){
        //     centered = false;
        // }   
        // else{
        //    centered = true;
        // }
        // if(ranger.rangeFinder() > atRange + 1 || ranger.rangeFinder() < atRange -1){
        atRange = ranger.rangeFinder();
        // }
        // System.out.println(ranger.rangeFinder());
        // System.out.println("atRange");
        if(ranger.rangeFinder() > 70 && ranger.rangeFinder() < 300){
                 if(ranger.rangeFinder()>=120 && ranger.rangeFinder()<145)
                    {
                        
                        Robot.targetAngle = 0.000557516*Math.pow(atRange, 2) +- 0.29449*(atRange) + 66.2420 ;
                    
                    }
             else if(ranger.rangeFinder()>=180 && ranger.rangeFinder()<205)
                    {

                        Robot.targetAngle = 0.000557516*Math.pow(atRange, 2) +- 0.29449*(atRange) + 66.2420 ;
                    
                    }
             else if(ranger.rangeFinder()>=235 && ranger.rangeFinder()<260)
                    {

                        Robot.targetAngle = 0.000557516*Math.pow(atRange, 2) +- 0.29449*(atRange) + 67.2420 ;
                    
                    }
             else 
                    {

                        Robot.targetAngle = 0.000557516*Math.pow(atRange, 2) +- 0.29449*(atRange) + 66.2420 ;
                    
                    }       
            
            /*
            //original
            
            // System.out.println(howFar + " distance");
<<<<<<< HEAD
            Robot.targetAngle = 0.000557516*Math.pow(atRange, 2) +- 0.29449*(atRange) + 66.2420 ; 
=======
            Robot.targetAngle = 0.0006922*Math.pow(atRange, 2)+-0.24+2*(atRange)+56;
>>>>>>> 28eea95560ce7bc7d4f336d161c4b623309204f7
            // System.out.println(Robot.targetAngle + " targetAngle");
            */
             angleSet(Robot.targetAngle);

        }
        else{
            if(Robot.ext.getRawAxis(0) < 0.05 && Robot.ext.getRawAxis(0) > -0.05){
                Robot.pitcherPID.setReference(0, ControlType.kVelocity);
              }
            else{
                Robot.pitcher.set(Robot.ext.getRawAxis(0)*0.25);
            }
        }
        
    }
    public void angleSet(double targetAngle){
        // pitchEnc.getPosition()*(-360/71) + 50
        positionTarget = (targetAngle-50)/(-5.07);
        // System.out.println(positionTarget + " positionTarget");

        if(Robot.pitchEnc.getPosition() > positionTarget + 1){
            Robot.pitcher.set(-0.25);
        }
        else if(Robot.pitchEnc.getPosition() < positionTarget - 1){
            Robot.pitcher.set(0.25);
        }
        else if(Robot.pitchEnc.getPosition() < positionTarget){
            Robot.pitcher.set(.2*Math.pow(Math.abs(Robot.pitchEnc.getPosition()-positionTarget), 0.9));
        }
        else if(Robot.pitchEnc.getPosition() > positionTarget){
            Robot.pitcher.set(-.2*Math.pow(Math.abs(Robot.pitchEnc.getPosition()-positionTarget), 0.9));
        }
        else{
            Robot.pitcherPID.setReference(0, ControlType.kVelocity);
        }
        if(Robot.pitchEnc.getPosition() < positionTarget + 0.2 || Robot.pitchEnc.getPosition() > positionTarget - 0.2){
            Robot.launchStatus = true;
        }
    }
    public void innerSet(){
        double innerHoleAngle = 21;
        angleSet(21);
    }
    public void autoLaunchTime(){
        // if(Robot.launchStatus == true && launchLast == false){
        //     Robot.launchCountdown = System.currentTimeMillis() + Robot.launchWait;
        // }
        // Robot.ballsLeft = 0;
        // System.out.println(launchLast + "launchlast");
        // launchLast = Robot.launchStatus;
        
    }



}
