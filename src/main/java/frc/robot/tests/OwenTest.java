package frc.robot.tests;

import java.lang.invoke.MethodHandles;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Grabber;
import edu.wpi.first.wpilibj.Joystick;

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

    private final Joystick JOYSTICK = new Joystick(1);
    private final Grabber grabber;// = RobotContainer.grabber;


    // *** CLASS CONSTRUCTOR ***
    public OwenTest(RobotContainer robotContainer)
    {
        this.robotContainer = robotContainer;
        this.grabber = robotContainer.grabber;
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {}

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        // Joystick();
        if(JOYSTICK.getRawButton(3))
        {
            grabber.releaseGamePiece();
        }

        if(JOYSTICK.getRawButton(4))
        {
            grabber.grabGamePiece();
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
