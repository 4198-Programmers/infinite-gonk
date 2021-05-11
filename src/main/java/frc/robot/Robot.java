package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.TimedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Solenoid;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.vision.VisionPipeline;
import edu.wpi.first.wpilibj.shuffleboard.*;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;


public class Robot extends TimedRobot {
  
  public static Joystick logi1 = new Joystick(0); //0 drives forward, backward, and crab

  public static Joystick logi = new Joystick(1);  //1 does twisting

  public static Joystick ext = new Joystick(2);   //2 Controls all button for shooting and axis for angling the pitcher

  public static CANSparkMax frontL = new CANSparkMax(1, MotorType.kBrushless);
  public static CANSparkMax frontR = new CANSparkMax(2, MotorType.kBrushless);
  public static CANSparkMax backL = new CANSparkMax(12, MotorType.kBrushless);
  public static CANSparkMax backR = new CANSparkMax(11, MotorType.kBrushless);
  public static CANSparkMax pitcher = new CANSparkMax(10, MotorType.kBrushless);
  public static CANSparkMax belt = new CANSparkMax(5, MotorType.kBrushless);
  public static CANSparkMax topLaunch = new CANSparkMax(6, MotorType.kBrushless);
  public static CANSparkMax bottomLaunch = new CANSparkMax(7, MotorType.kBrushless);
  public static CANSparkMax intake = new CANSparkMax(9, MotorType.kBrushless);
  public static CANSparkMax pitcherIn = new CANSparkMax(4, MotorType.kBrushed);
  public static CANSparkMax climbWinch = new CANSparkMax(3, MotorType.kBrushless);
  public static CANSparkMax fricWheel = new CANSparkMax(8, MotorType.kBrushed);

  public static CANEncoder flEnc = new CANEncoder(frontL);
  public static CANEncoder frEnc = new CANEncoder(frontR);
  public static CANEncoder blEnc = new CANEncoder(backL);
  public static CANEncoder brEnc = new CANEncoder(backR);
  public static CANEncoder pitchEnc = new CANEncoder(pitcher);
  public static CANEncoder beltEnc = new CANEncoder(belt);
  public static CANEncoder topLaunchEnc = new CANEncoder(topLaunch);
  public static CANEncoder botLaunchEnc = new CANEncoder(bottomLaunch);
  public static CANEncoder climbEnc = new CANEncoder(climbWinch);


  public static CANPIDController frPID = new CANPIDController(frontR);
  public static CANPIDController flPID = new CANPIDController(frontL);
  public static CANPIDController brPID = new CANPIDController(backR);
  public static CANPIDController blPID = new CANPIDController(backL);
  public static CANPIDController pitcherPID = new CANPIDController(pitcher);
  public static CANPIDController uSpeedControl = new CANPIDController(bottomLaunch);
  public static CANPIDController lSpeedControl = new CANPIDController(topLaunch);
  public static CANPIDController beltPID = new CANPIDController(belt);

  public static DigitalInput inSensor = new DigitalInput(0);
  public static DigitalInput outSensor = new DigitalInput(1);
  
  public static MecanumDrive scoot = new MecanumDrive(frontL, backL, frontR, backR);

  public static DigitalInput zero = new DigitalInput(2);

  public static double kP = 1e-4; 
  public static double kI = 3e-7;
  public static double kD = 0; 
  public static double kIz = 0; 
  public static double kFF = 0; 
  public static double kMaxOutput = 1; 
  public static double kMinOutput = -1;
  public static double maxRPM = 5700;

  public static double driveP = 1e-3; 
  public static double driveI = 3e-4;
  public static double driveD = 0; 
  public static double driveIz = 0; 
  public static double driveFF = 0; 
  public static double driveMaxOutput = 1; 
  public static double driveMinOutput = -1;
  public static double diveRPM = 5700;

  public static double pAdjust = -4;

  public static double launchCountdown;
  public static double launchWait = 1000;
  public static boolean launchStatus = true;
  public static boolean beltCheck = true;

  public static boolean ballWasFront = false;
  public static int ballCount = 0;
  public static boolean ballWasBack = false;

  public static SendableChooser autoChoice = new SendableChooser<>();
  public static int primeAuto = 1;
  public static int universalAuto = 2;
  public static int straightBack = 3;

  public static double actualEnc;
  public static double encOffset;
  
  public static double currentPos;

  public static int ballsLeft;
  public static int lastCount = 0;
  public static double countDelay = 0;

  public static PigeonIMU pigeon = new PigeonIMU(42);
  public static double [] gyroRead = new double[3];
  public static double yawStart;
  public static double startPos;
  public static boolean stageStart = true;

  public static boolean demoRunner = false;

  public static double targetAngle;

  public static double autoDelay;

  public static int climbState = 1;

  public static ballCounter ballsOut = new ballCounter();

  autoBlocks basicallyAI = new autoBlocks();

  limelight vision = new limelight();

  climb climber = new climb();

  //public static int toggleLime = 1;

  // colorParse colorWheel = new colorParse();

  launchAngler sniper = new launchAngler();


  // public ComplexWidget auto =
  //   tab.add("Auto", autoChoice);
 
 double controlMultiply = 1;
  
      


  public static void launch(int mode)  {
    uSpeedControl.setP(kP);
    uSpeedControl.setI(kI);
    uSpeedControl.setD(kD);
    uSpeedControl.setIZone(kIz);
    uSpeedControl.setFF(kFF);
    uSpeedControl.setOutputRange(kMinOutput, kMaxOutput);

    lSpeedControl.setP(kP);
    lSpeedControl.setI(kI);
    lSpeedControl.setD(kD);
    lSpeedControl.setIZone(kIz);
    lSpeedControl.setFF(kFF);
    lSpeedControl.setOutputRange(kMinOutput, kMaxOutput);
 
      

    // System.out.println(ballCount>ballsLeft);
    if(ballCount > ballsLeft && beltCheck == false){
      uSpeedControl.setReference(2250, ControlType.kVelocity); //was 3000rpm controls top launch overall speed
      lSpeedControl.setReference(2500, ControlType.kVelocity); //controls bottom launch overall speed
      if(System.currentTimeMillis() >= launchCountdown &&      //does not wait before beginning launch
        launchStatus == true && mode == 2){
          //beltindexer.set(min, kVelocity)
          //belt.set(0.5); //internal belt speed
          if(outSensor.get() )            //when running launch, if the sensor senses a ball going out, it reverses the internal belt
        {
          belt.set(-0.3);
        }
        else if (outSensor.get() == false)
        {
          belt.set(0.35);
        }

        }
      else if(System.currentTimeMillis() >= launchCountdown && //current mode, waits for a moment before launch
      launchStatus == true && ballCount > ballsLeft && mode == 1){ 
       //belt.set() was 0.5
        
        if(outSensor.get() )              //when running launch, if the sensor senses a ball going out, it reverses the internal belt
        {
          belt.set(-0.3);
        }
        else if (outSensor.get() == false)
        {
          belt.set(0.35);
        }
      }
    }
    else if(outSensor.get()){             //after launching a ball belt runs backwards
      belt.set(-0.4);
    }
    else if(outSensor.get() == false){    //checks that nothing is going out and stops the belt if there is nothing
      belt.set(0);
      beltCheck = false;
    }
    else{
      topLaunch.set(0);
      bottomLaunch.set(0);
      belt.set(0);
    }
  }



  public static void beltIndexer() {
    if(inSensor.get()){                   //when loading balls it moves the belt forward slightly to prepare for the next ball
      // beltDrive.setReference(400, kVelocity)
      if(ballCount < 4){
        currentPos = beltEnc.getPosition() + 0.1;
      }
      else{
        currentPos = beltEnc.getPosition() + 0.0001;
      }
    }
  }
 
  @Override
  public void robotInit() {
    autoChoice.addOption("6 Ball", primeAuto);
    autoChoice.addOption("Universal Auto", universalAuto);
    autoChoice.addOption("Straight Back", straightBack);
    Shuffleboard.getTab("Preround")
      .add("Auto Choice", autoChoice)
      .withWidget(BuiltInWidgets.kComboBoxChooser);
    pitcher.setInverted(true);
    topLaunch.setInverted(false);
    bottomLaunch.setInverted(true);
    belt.setInverted(false);
    pitcherIn.setInverted(false);
    currentPos = beltEnc.getPosition();
    frEnc.setPosition(0);
    flEnc.setPosition(0);
    blEnc.setPosition(0);
    brEnc.setPosition(0);
    ballCount = 3;
    
    frPID.setP(kP);
    frPID.setI(kI);
    frPID.setD(kD);
    frPID.setIZone(kIz);
    frPID.setFF(kFF);
    frPID.setOutputRange(kMinOutput, kMaxOutput);

    brPID.setP(kP);
    brPID.setI(kI);
    brPID.setD(kD);
    brPID.setIZone(kIz);
    brPID.setFF(kFF);
    brPID.setOutputRange(kMinOutput, kMaxOutput);

    flPID.setP(kP);
    flPID.setI(kI);
    flPID.setD(kD);
    flPID.setIZone(kIz);
    flPID.setFF(kFF);
    flPID.setOutputRange(kMinOutput, kMaxOutput);

    blPID.setP(kP);
    blPID.setI(kI);
    blPID.setD(kD);
    blPID.setIZone(kIz);
    blPID.setFF(kFF);
    blPID.setOutputRange(kMinOutput, kMaxOutput);

    pitcher.setOpenLoopRampRate(0.5);
    pitcher.setClosedLoopRampRate(0.5);
  }

  
  @Override
  public void robotPeriodic() {
    
  }

  @Override
  public void autonomousInit() {
    autoDelay = System.currentTimeMillis();
  }

  
  @Override
  public void autonomousPeriodic() {
    vision.pipeline.setDouble(0);
    ballsOut.ballsIn();
    beltIndexer();
    int finalChoice = (int)autoChoice.getSelected();
    basicallyAI.fullAuto(finalChoice);
  }


  
  @Override
  public void teleopPeriodic() {
    SmartDashboard.putNumber("Angle", pitchEnc.getPosition()*(-360/71) + 50);
    SmartDashboard.putNumber("Target Angle", targetAngle);
    SmartDashboard.putNumber("Range", vision.rangeFinder());
    // frontR.set(0.2);
    // System.out.println(logi.getRawAxis(0));
    // System.out.println(pitchEnc.getPosition() + " pitchEnc");
    // System.out.println(inSensor.get());;
    if(zero.get())
    {
      pitchEnc.setPosition(0);
    }
    // System.out.println(pitchEnc.getPosition());
    // System.out.println(vision.rangeFinder() + " range");
    // System.out.println(vision.offsetCalculator() + " offset");
    // System.out.println(inSensor.get() + " in " + outSensor.get() + " out");
    ballsOut.ballsIn();
    beltIndexer();
    // System.out.println(outSensor.get() + " out, was " + ballWasFront);
    // System.out.println(ballCount);
    pitcherPID.setP(1e-5);
    pitcherPID.setI(1e-7);
    
    if (ext.getRawButtonPressed(11)) 
    {
      basicallyAI.fullAuto(3);
    }
    
    if(ext.getRawButton(2)) 
    {
      if(ext.getRawButton(7))
      {
        belt.set(0.3);
      }
      else if(ext.getRawButton(8))
      {
        belt.set(-0.3);
      }
      else
      {
        belt.set(0);
      }
      if(ext.getRawButton(1))
      {
        topLaunch.set(0.4);             //og 0.4,revision 1:0.3950
        bottomLaunch.set(0.6);         //og 0.7,revision 1:0.4386
      }
      else
      {
        topLaunch.set(0);
        bottomLaunch.set(0);
      }
      
    }
    else if(ext.getRawButtonPressed(5))
    {
      launchCountdown = System.currentTimeMillis() + launchWait;
      ballsLeft = ballCount - 1;
    }
    else if(ext.getRawButtonPressed(1))
    {
      launchCountdown = System.currentTimeMillis() + launchWait;
      ballsLeft = 0;
    }
    else if(ext.getRawButton(1))
    {
        launch(1);
    }
    //else if(logi.getRawButton(8)){      //launch descriptions further up in code- not used for challenges
    //launch(2);
    //}
    else
    {
      if(beltEnc.getPosition() < currentPos)
      {
        belt.set(0.5);
      }
      else if(beltEnc.getPosition() > currentPos && inSensor.get() == false)
      {
        belt.set((currentPos-beltEnc.getPosition())/10);
      }
      else
      {
        belt.set(0);
      }
      topLaunch.set(0);
      bottomLaunch.set(0);
    }


    if(ext.getRawButton(8)){
      pitcherIn.set(1);
      intake.set(0.3);
    }
    else if(ext.getRawButton(9)){
      pitcherIn.set(-1);
      intake.set(-0.3);
    }
    else{
      pitcherIn.set(0);
      intake.set(0);
    }

    if(logi.getRawButtonReleased(3)){
      // System.out.println("SHOULD BE FLIPPING");
      controlMultiply = controlMultiply * (-1);
    }    
    
    // System.out.println(controlMultiply);
      
    if(ext.getRawButtonReleased(1) || ext.getRawButtonReleased(2)){
      beltCheck = true;
    }



      
    
    /*
    if (ext.getRawButtonPressed(4)){
      toggleLime *= -1;
    } 

    if(toggleLime == 1)
    {              //while ext 4 pressed the limelight will be off - you can still drive and pickup balls 
      vision.pipeline.setDouble(1);       //when (1) limelight is off
      scoot.driveCartesian(logi1.getRawAxis(0) * controlMultiply, -logi1.getRawAxis(1) * controlMultiply, logi.getRawAxis (0)); //allows for drive while stopping limelight
      if(ext.getRawButton(8))
      {            //while holding ext 8 the pitcher will rotate to its specific position to load balls
        if(pitchEnc.getPosition() > 1)
        { 
          pitcher.set(-0.25); 
        }
        else if(pitchEnc.getPosition() < -1)
        {
          pitcher.set(0.25);
        }
        else
        {
          pitcher.set((-.24*pitchEnc.getPosition()));
        }
      }
    else if(toggleLime == -1)
      {
      //vision.pipeline.setDouble(0);       //when (1) limelight is off
      scoot.driveCartesian(logi1.getRawAxis(0) * controlMultiply, -logi1.getRawAxis(1) * controlMultiply, logi.getRawAxis (0)); //allows for drive while stopping limelight
      if(ext.getRawButton(8))
      {                                     //while holding ext 8 the pitcher will rotate to its specific position to load balls
        if(pitchEnc.getPosition() > 1)
        { 
          pitcher.set(-0.25); 
        }
        else if(pitchEnc.getPosition() < -1)
        {
          pitcher.set(0.25);
        }
        else
        {
          pitcher.set((-.24*pitchEnc.getPosition()));
        }
      }
   }
  }
*/
    if(logi.getRawButton(1)){      //while holding logi 1 - limelight on, limelight angling, limelight positioning
      vision.pipeline.setDouble(0);   //when (1) limelight is off
      vision.camControl();              //Made the robot spin like murder - no longer does so, no idea what we did to fix...
      sniper.tip();                     //angles pitcher based on limelight data
    }
    else{
      vision.pipeline.setDouble(1);
      if(ext.getRawButton(8)){          //while holding ext 8 the pitcher will rotate to its specific position to load balls
        if(pitchEnc.getPosition() > 1){
          pitcher.set(-0.25);
      }
        else if(pitchEnc.getPosition() < -1){
          pitcher.set(0.25);
        }
        else{
          pitcher.set((-.24*pitchEnc.getPosition()));
        }
        // sniper.angleSet(50);
      }
      else if(ext.getRawButton(10)){
        sniper.angleSet(0);
      }
      //else if(climbState == -1){
       // if(pitchEnc.getPosition() > -3){
       //   pitcher.set(-0.24);
        //}
        //else if(pitchEnc.getPosition() > -3.353 && pitchEnc.getPosition() <= -3){
       //   pitcher.set((pitchEnc.getPosition() + 3.35)/-3);
       // }
      //}

      else if(ext.getRawAxis(0) < 0.05 && ext.getRawAxis(0) > -0.05){
        pitcherPID.setReference(0, ControlType.kVelocity);
      }
      else{
        if(ext.getRawButton(2)){
          pitcher.set(ext.getRawAxis(0)*0.25);
        }
      }

    
        scoot.driveCartesian(logi1.getRawAxis(0) * controlMultiply, -logi1.getRawAxis(1) * controlMultiply, logi.getRawAxis (0)); //normal controls for drive base
      
       
    // scoot.driveCartesian(roll, crab, rotate);
      }
     // if(ext.getRawButtonPressed(14)){
       // climbState = climbState * -1;
      //}
      //if(ext.getRawButton(6) || ext.getRawButton(5)){
        //climber.winch();
      //}
      //else{
        //climbWinch.set(0);
    //}
    // if(logi.getRawButton(4)){
    //   colorWheel.ColorControl(4);
    // }
    // if(logi.getRawButton(5)){
    //   colorWheel.ColorControl(5);
    // }
    
  }

 
  @Override
  public void testPeriodic() {
  
  }
}
