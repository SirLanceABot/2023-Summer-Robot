package frc.robot.tests;

import java.lang.invoke.MethodHandles;
import java.lang.management.OperatingSystemMXBean;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Shoulder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.commands.GrabGamePiece;
import frc.robot.commands.SuctionControl;
import frc.robot.shuffleboard.AutonomousTab;
import frc.robot.shuffleboard.MainShuffleboard;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import frc.robot.controls.Xbox;
import frc.robot.controls.OperatorController;

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
    private final CANSparkMax grabberMotor = new CANSparkMax(5, MotorType.kBrushless);
    private final Joystick joystick = new Joystick(0);
    // private final double rumbleDistance = 1.0e3;
    // private final double kP = -0.001;
    // private final double kI = 0.0;
    // private final double kD = 0.0;
    // private final Timer tier;
    // final int kUltrasonicPingPort = 2;
    // final int kUltrasonicEchoPort = 1;
    // private final MedianFilter m_filter = new MedianFilter(5);
    // private final PWMSparkMax m_leftMotor = new PWMSparkMax(kUltrasonicEchoPort);
    // private final Ultrasonic m_ultrasonic1 = new Ultrasonic(kUltrasonicPingPort, kUltrasonicEchoPort);
    // private final Ultrasonic m_ultrasonic3 = new Ultrasonic(null, null);
    // private final Ultrasonic m_ultrasonic2 = new Ultrasonic(kUltrasonicPingPort, kUltrasonicPingPort);
    // private final PIDController m_pidController = new PIDController(kP, kI, kD);
    // private final AnalogInput sonarSensor = new AnalogInput(0);
    // private final AnalogPotentiometer pot = new AnalogPotentiometer(sonarSensor, 5000, 0);
    // sonarSensor.setAverageBits(2);
    // private final Grabber grabber;// = RobotContainer.grabber;
    // private final Shoulder shoulder;
    // private final OperatorController operatorController;
    // private final Arm arm;
    // private final MainShuffleboard mainShuffleboard;
    // private final CloseGrabber closeGrabber;
    // private final OpenGrabber openGrabber;


    // *** CLASS CONSTRUCTOR ***
    public OwenTest(RobotContainer robotContainer)
    {
        this.robotContainer = robotContainer;
        // this.operatorController = robotContainer.operatorController;
        // tier = new Timer();
        // this.grabber = robotContainer.grabber;
        // this.mainShuffleboard = robotContainer.mainShuffleboard;
        // this.shoulder = robotContainer.shoulder;
        // this.arm = robotContainer.arm;
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        // m_pidController.setSetpoint(rumbleDistance);]
        // tier.start();
        
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        // double measurement = m_ultrasonic1.getRangeMM();
        // double filteredMeasurement = m_filter.calculate(measurement);
        // double pidOutput = m_pidController.calculate(filteredMeasurement);
        // pot.get();
        // if(tier.hasElapsed(1.0))
        // {
        //     System.out.println("Distance: " + pot.get() / 304.8 + "\n");
        //     tier.restart();
        // }
        // // System.out.println("Distance: " + pot.get() / 304.8 + "\n");
        // if(pot.get() / 304.8 < 4.0 && pot.get() / 304.8 > 3.0)
        // {

        //     operatorController.setRumble(0.1, 0.5, 0.5);
        // }
        // // grabber.compressorEnable();
        // Joystick();
        if(joystick.getRawButton(4))
        {
            System.out.println("Up");
            grabberMotor.set(0.5);
            // shoulder.moveUp();
            // arm.extendArm();
        }
        else if(joystick.getRawButton(3))
        {
            System.out.println("Down");
            grabberMotor.set(-0.5);
            // arm.retractArm();
        }
        else
        {
            grabberMotor.set(0.0);
        }

        // // if(joystick.getRawButton(1))
        // // {
        // //     shoulder.resetEncoder();
        // //     // arm.resetEncoder();
        // // }
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {}

    // *** METHODS ***
    // Put any additional methods here.

    
}
