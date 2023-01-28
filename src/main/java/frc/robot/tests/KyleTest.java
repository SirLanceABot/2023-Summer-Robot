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
    private Joystick joystick = new Joystick(2);
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
        // if (joystick.getRawButton(3))
        // {
        //     arm.retractoArm();
        // }
        // else if (joystick.getRawButton(4))
        // {
        //     arm.extendoArm();
        // }
        // else if (joystick.getRawButton(6))
        // {
        //     arm.stopoArm();
        // }
        // else if (joystick.getRawButton(1))
        // {
        //     arm.holdoArm();
        // }
        if (joystick.getRawButtonPressed(5))
        {   // Leftmost button = all the way in
            desiredPosition = Arm.ArmPosition.kIn;
            System.out.println("In");
        }
        else if (joystick.getRawButtonPressed(3))
        {   // Second to leftmost buton = half
            desiredPosition = Arm.ArmPosition.kHalfExtended;
            System.out.println("Half");
        }
        else if (joystick.getRawButtonPressed(4))
        {   // Second to rightmost button = 3/4
            desiredPosition = Arm.ArmPosition.kThreeQuarterExtended;
            System.out.println("3/4");
        }
        else if (joystick.getRawButtonPressed(6))
        {   // Rightmost button = fully extenden
            desiredPosition = Arm.ArmPosition.kFullyExtended;
            System.out.println("All Out");
        }
        else 
        {
            desiredPosition = Arm.ArmPosition.kIn;
            System.out.println("Started");
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
