package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// package frc.robot.subsystems;
import com.revrobotics.REVLibError;

import java.lang.invoke.MethodHandles;

/**
 * Use this class as a template to create other subsystems.
 */
public class Arm extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    int ArmMotorPort = Constants.MotorConstants.ARM_MOTOR_PORT;
    private final CANSparkMax armMotor = new CANSparkMax (ArmMotorPort, MotorType.kBrushless);
    private final SparkMaxLimitSwitch forwardLimitSwitch = armMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    private final SparkMaxLimitSwitch reverseLimitSwitch = armMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    private RelativeEncoder armEncoder;
    private double armPosition = 0.0;

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public Arm()
    {
        configCANSparkMax();
    }

    private void configCANSparkMax()
    {
        // Factory Defaults
        armMotor.restoreFactoryDefaults();
        // Invert Motor Direction
        armMotor.setInverted(false);
        // Set Coast or Break Mode
        armMotor.setIdleMode(IdleMode.kBrake);
        // Set the Feedback Sensor
        // sparkMaxMotor.ser();

        //Soft Limits
        armMotor.setSoftLimit(SoftLimitDirection.kForward, 0);
        armMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
        armMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        armMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);
        
        //Hard Limits
        forwardLimitSwitch.enableLimitSwitch(false);
        reverseLimitSwitch.enableLimitSwitch(false);
        // Encoder
        armEncoder = armMotor.getEncoder();
    }

   
    public double getArmPosition()
    {
        return armPosition;
    }

    @Override
    public synchronized void readPeriodicInputs()
    {
        armPosition = armEncoder.getPosition();
        SmartDashboard.putNumber("armEncoder", armPosition);
    }

    @Override
    public synchronized void writePeriodicOutputs()
    {}

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }



    private void extendoArm()
    {
    
    }

}
