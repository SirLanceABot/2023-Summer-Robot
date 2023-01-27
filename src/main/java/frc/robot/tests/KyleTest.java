package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.subsystems.Arm;
import frc.robot.RobotContainer;

public class KyleTest implements Test
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
    private final Arm arm;
    private Joystick joystick = new Joystick(0);
    private Arm.ArmPosition desiredPosition;

    // *** CLASS CONSTRUCTOR ***
    public KyleTest(RobotContainer robotContainer)
    {
        this.robotContainer = robotContainer;
        arm = this.robotContainer.arm;
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
        if (joystick.getRawButtonPressed(5) == true)
        {   // Leftmost button = all the way in
            desiredPosition = Arm.ArmPosition.kIn;
        }
        else if (joystick.getRawButtonPressed(3) == true)
        {   // Second to leftmost buton = half
            desiredPosition = Arm.ArmPosition.kHalfExtended;
        }
        else if (joystick.getRawButtonPressed(4) == true)
        {   // Second to rightmost button = 3/4
            desiredPosition = Arm.ArmPosition.kThreeQuarterExtended;
        }
        else if (joystick.getRawButtonPressed(6) == true)
        {   // Rightmost button = fully extenden
            desiredPosition = Arm.ArmPosition.kFullyExtended;
        }

        if (arm.getArmPosition() < desiredPosition.min)
        {
            arm.extendoArm();
        }
        else if (arm.getArmPosition() > desiredPosition.max)
        {
            arm.retractoArm();
        }
        else 
        {
            arm.holdoArm();
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
