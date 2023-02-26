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
        arm.resetEncoder();
        desiredPosition = Arm.ArmPosition.kGather;
    }

    /**
     * This method runs periodically (every 20ms).
     */

    public void periodic()
    {
        if (joystick.getRawButtonPressed(5))
        {   // Leftmost button = all the way in
            desiredPosition = Arm.ArmPosition.kGather;
            System.out.println("In, desiredPosition.min = " + desiredPosition.value + " armPosition = " + arm.getArmPosition());
        }
        else if (joystick.getRawButtonPressed(3))
        {   // Second to leftmost buton = half
            desiredPosition = Arm.ArmPosition.kLow;
            System.out.println("Half, desiredPosition.min = " + desiredPosition.value + " armPosition = " + arm.getArmPosition());
        }
        else if (joystick.getRawButtonPressed(4))
        {   // Second to rightmost button = 3/4
            desiredPosition = Arm.ArmPosition.kMiddle;
            System.out.println("3/4, desiredPosition.min = " + desiredPosition.value + " armPosition = " + arm.getArmPosition());
        }
        else if (joystick.getRawButtonPressed(6))
        {   // Rightmost button = fully extenden
            desiredPosition = Arm.ArmPosition.kHigh;
            System.out.println("All Out, desiredPosition.min = " + desiredPosition.value + " armPosition = " + arm.getArmPosition());
        }

        arm.moveArmToDesired(desiredPosition);
    }


    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {}

    // *** METHODS ***
    // Put any additional methods here.


}
