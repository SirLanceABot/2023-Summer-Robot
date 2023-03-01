package frc.robot.tests;

import java.lang.invoke.MethodHandles;

// import javax.lang.model.util.ElementScanner14;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// import edu.wpi.first.wpilibj.DoubleSolenoid;
// import javax.lang.model.util.ElementScanner14;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableEntry;
// import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj.PneumaticsModuleType;
// import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Arm;
import frc.robot.commands.MoveShoulderToScoringPosition;
// import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Shoulder;
// import frc.robot.sensors.Vision;
// import frc.robot.subsystems.Drivetrain;
// import frc.robot.commands.AutoAimToPost;
// import frc.robot.subsystems.Shoulder.TargetPosition;

public class SamTest implements Test
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum CommandState
    {
        kWaiting, kRan;
    }

    // *** CLASS & INSTANCE VARIABLES ***
    // Put all class and instance variables here.
    private final RobotContainer robotContainer;
    private final Shoulder shoulder;
    private final Arm arm;
    // private final CANSparkMax canSparkMax = new CANSparkMax(3, MotorType.kBrushless);
    private final Joystick joystick;
    // private final DoubleSolenoid testSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 0, 1);
    // private final Vision vision;
    // private final Drivetrain drivetrain;
    // private CommandState commandState = CommandState.kWaiting;
  

    // *** CLASS CONSTRUCTOR ***
    public SamTest(RobotContainer robotContainer)
    {
        System.out.println(fullClassName + " : Constructor Started");

        this.robotContainer = robotContainer;
        // vision = this.robotContainer.vision;
        // drivetrain = this.robotContainer.drivetrain;
        shoulder = this.robotContainer.shoulder;
        arm = this.robotContainer.arm;
        // canSparkMax.restoreFactoryDefaults();
        // canSparkMax.setIdleMode(IdleMode.kBrake);
        joystick = new Joystick(0);

        System.out.println(fullClassName + " : Constructor Finished");
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        System.out.println("SamTest Init");
        // shoulder.resetEncoder();
        // 
        // AutoAimToPost command = new AutoAimToPost(drivetrain, vision);
        // command.schedule();
        // System.out.println(command.isScheduled());
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
    //     SmartDashboard.putNumber("Encoder Value", shoulder.getPosition());
    //     SmartDashboard.putNumber("Encoder Velocity", shoulder.getVelocity());

        // if(joystick.getRawButton(1))
        // {
        //     System.out.println("Pressed A");
        //     if(commandState == CommandState.kWaiting)
        //     {
        //         // Command command = new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kHigh);
        //         // command.schedule();

        //         shoulder.moveToLow();
        //         commandState = CommandState.kRan;
        //     }
        // }
        if(joystick.getRawButton(1))    //A
        {
            shoulder.moveUp();
        }
        else if(joystick.getRawButton(2))   //B
        {
            shoulder.moveDown();
        }
        else
        {
            shoulder.off();
        }

        if(joystick.getRawButton(3))   //X
        {
            arm.extendArm();
        }
        else if(joystick.getRawButton(4))   //Y
        {
            arm.retractArm();
        }
        else
        {
            arm.off();
            
        }
    
        // System.out.println(shoulder);

        // if(joystick.getRawButton(3))    //X
        // {
        //     testSolenoid.set(Value.kForward);
        // }
        // else if(joystick.getRawButton(4))   //Y
        // {
        //     testSolenoid.set(Value.kReverse);
        // }

        // if(joystick.getRawButton(1))    //A
        // {
        //     canSparkMax.set(0.75);
        // }
        // else
        // {
        //     canSparkMax.set(0.0);
        // }

        // System.out.println(shoulder);
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }

    // *** METHODS ***
    // Put any additional methods here.

    
}
