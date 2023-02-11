package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shoulder;

public class JWoodTest implements Test
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
    private final Joystick joystick = new Joystick(0);
    private final Shoulder shoulder;


    // *** CLASS CONSTRUCTOR ***
    public JWoodTest(RobotContainer robotContainer)
    {
        this.robotContainer = robotContainer;
        this.shoulder = this.robotContainer.shoulder;

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
        shoulder.on(0.75);
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        shoulder.off();
    }

    // *** METHODS ***
    // Put any additional methods here.

    
}
