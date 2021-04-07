// package frc.robot;

// import com.revrobotics.ColorSensorV3;
// import com.revrobotics.CANEncoder;
// import com.revrobotics.CanSparkMax;
// import com.revrobotics.ControlType;
// Import com.revrobotics.CANSparkMaxLowLevel.MotorType;
// com.revrobotics.CANPIDController;
// import edu.wpi.first.wpilibj.I2C;
// import edu.wpi.first.wpilibj.I2C.Port;

// import edu.wpi.first.wpilibj.DriverStation;

// public class colorParse {
//     I2C.Port i2Color = I2C.Port.kOnboard;

//     ColorSensorV3 scan = new ColorSensorV3(i2Color);

//     String colorSet = DriverStation.getInstance().getGameSpecificMessage();

//     String currentColor;

//     void ColorControl(int button){
//         int counts = 0;
//         String lastColor = "";

//         if(button == 4){
//             int yellow = 0;
//             int blue = 0;
//             int green = 0;
//             int red = 0;
//             String lastTrue = colorOutput(scan.getBlue(), scan.getGreen(), scan.getRed());
//             do{
//                 currentColor = colorOutput(scan.getBlue(), scan.getGreen(), scan.getRed());
//                 if(lastColor != currentColor){
//                     if(currentColor == "B" && lastTrue ==  "G"){
//                         blue++;
//                         counts = blue;
//                         lastTrue = currentColor;
//                     }
//                     else if(currentColor == "G" && lastTrue == "R"){
//                         green++;
//                         counts = green;
//                         lastTrue = currentColor;
//                     }
//                     else if(currentColor == "R" && lastTrue == "Y"){
//                         red++;
//                         counts = red;
//                         lastTrue = currentColor;
//                     }
//                     else if(currentColor == "Y" && lastTrue == "B"){
//                         yellow++;
//                         counts = yellow;
//                         lastTrue = currentColor;
//                     }
//                 }
//                 lastColor = currentColor;
//             }while(counts <= 7);
//         }
//         else if(button == 5){
//             int setter =colorConvert(colorSet);
//             do{
//                 //motor.set()
//                 setter = setter - colorConvert(colorOutput(scan.getBlue(), scan.getGreen(), scan.getRed()));
//             }while(setter != 2 || setter != -2);
//         }
//     }
//     String colorOutput(double cBlue, double cGreen, double cRed){
//         // double redBlue = cRed/cBlue;
//         // double redGreen = cRed/cGreen;
//         // double blueGreen = cBlue/cGreen; 
//         double blueRed = cBlue/cRed;
//         // double greenRed = cGreen/cRed;
//         // double greenBlue = cGreen/cBlue;
        
//         if(cGreen > cRed && blueRed > 2){
//             // System.out.println("BLUE");
//             return "B";
//         }
//         else if(cGreen > cBlue && cGreen > cRed){
//             if(cRed > cBlue){
//                 return "Y";
//             }
//             else{
//                 return "G";
//             }
//         }
//         else if(cGreen > cBlue && cGreen < cRed ) {
//             return "R";
//         }
//         else{
//             return "oops";
//         }
//     }
//     int colorConvert (String input){
//         if(input == "B"){
//             return 1;
//         }
//         else if(input == "G"){
//             return 2;
//         }
//         else if(input == "R"){
//             return 3;
//         }
//         else if(input == "Y"){
//             return 4;
//         }
//         else{
//             return 0;
//         }
//     }
//     static void spinmotor(){
//     if(button == 4)
//            
//      
//  }
// }
