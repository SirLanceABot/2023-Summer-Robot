package frc.robot.tests;

import java.lang.invoke.MethodHandles;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Gatherer;
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
    private static final Joystick JOYSTICK = new Joystick(0);
    private static final Grabber GRABBER = RobotContainer.grabber;


    // *** CLASS CONSTRUCTOR ***
    public OwenTest()
    {}

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
        if(JOYSTICK.getRawButton(3))
        {
            GRABBER.releaseGamePiece();
        }

        if(JOYSTICK.getRawButton(4))
        {
            GRABBER.grabGamePiece();
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
