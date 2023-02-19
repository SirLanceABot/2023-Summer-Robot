package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.Timer;

import java.lang.invoke.MethodHandles;

/**
 * Class containing one NEO 550 motor and two limit switches
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

    public enum ArmPosition
    {
        // Placeholder values used; real values to be determined when we have the arm length
        kFullyExtended(39.0,44.0), kThreeQuarterExtended(24.0,29.0), kHalfExtended(10.0,15.0), kIn(0.0,5.0);
        public final double min; 
        public final double max;
        private ArmPosition(double min, double max)
        {
            this.min = min;
            this.max = max;
        }
    }

    private class PeriodicIO
    {
        // Inputs
        private double armPosition = 0.0;
        private double armSpeed = 0.0;

        // Outputs
    }
    
    private PeriodicIO periodicIO = new PeriodicIO();
    private final int armMotorPort = Constants.Subsystem.ARM_MOTOR_PORT;
    private final CANSparkMax armMotor = new CANSparkMax (armMotorPort, MotorType.kBrushless);
    private SparkMaxLimitSwitch forwardLimitSwitch;
    private SparkMaxLimitSwitch reverseLimitSwitch;
    private final Timer encoderResetTimer = new Timer();
    private RelativeEncoder armEncoder;
    private boolean resetEncoderNow = false;
    private boolean hasEncoderReset = true;    
    
    public Arm()
    {
        configCANSparkMax();
    }

    private void configCANSparkMax()
    {
        // Factory Defaults
        armMotor.restoreFactoryDefaults();
        // Invert Motor Direction
        armMotor.setInverted(true);
        // Set Coast or Break Mode
        armMotor.setIdleMode(IdleMode.kBrake);
        // Set the Feedback Sensor
        // sparkMaxMotor.ser();

        // Encoder
        armEncoder = armMotor.getEncoder();
        armEncoder.setPositionConversionFactor(4096);

        //Soft Limits
        armMotor.setSoftLimit(SoftLimitDirection.kForward, Constants.Arm.ENCODER_FORWARD_SOFT_LIMIT);
        armMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
        armMotor.setSoftLimit(SoftLimitDirection.kReverse, Constants.Arm.ENCODER_REVERSE_SOFT_LIMIT);
        armMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
        
        //Hard Limits
        forwardLimitSwitch = armMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        forwardLimitSwitch.enableLimitSwitch(true);
        reverseLimitSwitch = armMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        reverseLimitSwitch.enableLimitSwitch(true);


    }

    /**
     * Resets the encoders for the arm encoder
     */
    public void resetEncoder()
    {
        resetEncoderNow = true;
        hasEncoderReset = false;
    }
   
    /**
     * @return Returns the current position of the arm
     */
    public double getArmPosition()
    {
        return periodicIO.armPosition;
    }

    /**
     * Set the motor to move backward
     */
    public void retractArm()
    {
        periodicIO.armSpeed = -0.3;
    }

    /**
     * Set the motor to move forward
     */
    public void extendArm()
    {
        periodicIO.armSpeed = 0.3;
    }
    
    /**
     * Hold the motor in place by setting it to move very slowly
     */
    public void holdArm()
    {
        periodicIO.armSpeed = 0.05;
    }

    /**
     * Stops the motor
     */
    public void stopArm()
    {
        periodicIO.armSpeed = 0.0;
    }

    /**
     * Moves the arm forward if the desired position is farther extended than the current position,
     * and moves it backwards if the desired position is farther inwards
     */
    public void moveArmToDesired(ArmPosition desiredPosition)
    {
        if (getArmPosition() < desiredPosition.min) 
        {
            extendArm();
        }
        else if (getArmPosition() > desiredPosition.max)
        {
            retractArm();
        }
        else 
        {
            stopArm();
        }
    }

    @Override
    public synchronized void readPeriodicInputs()
    {
        // TODO see the Shoulder code for resetting encoder
        periodicIO.armPosition = armEncoder.getPosition();
    }

    /**
     * Resets the encoders after a set period of time
     */
    @Override
    public synchronized void writePeriodicOutputs()
    {
        // TODO see the Shoulder code for resetting encoder
        if (resetEncoderNow)
        {
            armMotor.set(0.0);
            armEncoder.setPosition(0.0);
            resetEncoderNow = false;
            hasEncoderReset = true;
            encoderResetTimer.reset();
            encoderResetTimer.start();
        }
        else if (!hasEncoderReset)
        {
            if(Math.abs(periodicIO.armPosition) < 0.5)
            {
                hasEncoderReset = true;
            }
            else if (encoderResetTimer.hasElapsed(0.1))
            {
                armEncoder.setPosition(0.0);
                encoderResetTimer.reset();
                encoderResetTimer.start();
            }
        }
        else 
        {
            armMotor.set(periodicIO.armSpeed);
        }
    }

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

    @Override
    public String toString()
    {
        return "Current Arm Position: " + periodicIO.armPosition;
    }
}