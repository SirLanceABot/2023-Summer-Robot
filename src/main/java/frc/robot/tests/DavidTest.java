package frc.robot.tests;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Candle4237;

public class DavidTest implements Test
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
    private final Candle4237 candle;
    private Joystick joystick = new Joystick(1);
   
    
    

    // *** CLASS CONSTRUCTOR ***
    public DavidTest(RobotContainer robotContainer)
    {
        this.robotContainer = robotContainer;
        candle = this.robotContainer.candle;
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
        if (joystick.getRawButtonPressed(5))
            candle.signalReadyToDrop();
        else if (joystick.getRawButtonPressed(3))
            candle.signalCube();
        else if (joystick.getRawButtonPressed(4))
            candle.signalCone();
        else if (joystick.getRawButtonPressed(6))
            candle.turnOffLight();
        else if (joystick.getRawButtonPressed(11))
            candle.decrementAnimation();
        else if (joystick.getRawButtonPressed(12))
            candle.incrementAnimation();
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        candle.turnOffLight();
    }

    // *** METHODS ***
    // Put any additional methods here.

    

    
}