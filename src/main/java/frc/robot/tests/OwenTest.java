package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Grabber;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.CloseGrabber;
import frc.robot.commands.OpenGrabber;
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
    private final CANSparkMax grabberMotor = new CANSparkMax(3, MotorType.kBrushless);
    private final Joystick joystick = new Joystick(0);
    // private final Grabber grabber;// = RobotContainer.grabber;
    private final MainShuffleboard mainShuffleboard;
    // private final CloseGrabber closeGrabber;
    // private final OpenGrabber openGrabber;


    // *** CLASS CONSTRUCTOR ***
    public OwenTest(RobotContainer robotContainer)
    {
        this.robotContainer = robotContainer;
        // this.grabber = robotContainer.grabber;
        this.mainShuffleboard = robotContainer.mainShuffleboard;
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
        if(joystick.getRawButton(3))
        {

            grabberMotor.set(0.5);
        }

        if(joystick.getRawButton(4))
        {
            grabberMotor.set(0.0);
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
