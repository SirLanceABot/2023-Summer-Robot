package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Shoulder;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.GrabGamePiece;
import frc.robot.commands.SuctionControl;
import frc.robot.shuffleboard.AutonomousTab;
import frc.robot.shuffleboard.MainShuffleboard;

public class OwenTest implements Test
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    // Put all class and instance variables here.
    private final RobotContainer robotContainer;
    // private final CANSparkMax grabberMotor = new CANSparkMax(3, MotorType.kBrushless);
    private final Joystick joystick = new Joystick(0);
    // private final Grabber grabber;// = RobotContainer.grabber;
    private final Shoulder shoulder;
    // private final Arm arm;
    // private final MainShuffleboard mainShuffleboard;
    // private final CloseGrabber closeGrabber;
    // private final OpenGrabber openGrabber;


    // *** CLASS CONSTRUCTOR ***
    public OwenTest(RobotContainer robotContainer)
    {
        this.robotContainer = robotContainer;
        // this.grabber = robotContainer.grabber;
        // this.mainShuffleboard = robotContainer.mainShuffleboard;
        this.shoulder = robotContainer.shoulder;
        // this.arm = robotContainer.arm;
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        // grabber.compressorEnable();
        // Joystick();
        if(joystick.getRawButton(4))
        {
            System.out.println("Up");
            shoulder.moveUp();
            // arm.extendArm();
        }
        else if(joystick.getRawButton(3))
        {
            System.out.println("Down");
            shoulder.moveDown();
            // arm.retractArm();
        }
        else
        {
            shoulder.off();
            // arm.stopArm();
        }

        if(joystick.getRawButton(1))
        {
            shoulder.resetEncoder();
            // arm.resetEncoder();
        }
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {}

    // *** METHODS ***
    // Put any additional methods here.

    
}
