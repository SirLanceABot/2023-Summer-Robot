package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

        // Outputs
    }
    
    private PeriodicIO periodicIO = new PeriodicIO();
    private final int ArmMotorPort = Constants.MotorConstants.ARM_MOTOR_PORT;
    private final CANSparkMax armMotor = new CANSparkMax (ArmMotorPort, MotorType.kBrushless);
    private final SparkMaxLimitSwitch forwardLimitSwitch = armMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    private final SparkMaxLimitSwitch reverseLimitSwitch = armMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    private final Timer encoderResetTimer = new Timer();
    private RelativeEncoder armEncoder;
    private double armSpeed = 0.0;
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
        forwardLimitSwitch.enableLimitSwitch(true);
        reverseLimitSwitch.enableLimitSwitch(true);

        // Encoder
        armEncoder = armMotor.getEncoder();
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
    public void retractoArm()
    {
        armSpeed = -0.3;
    }

    /**
     * Set the motor to move forward
     */
    public void extendoArm()
    {
        armSpeed = 0.3;
    }
    
    /**
     * Hold the motor in place by setting it to move very slowly
     */
    public void holdoArm()
    {
        armSpeed = 0.05;
    }

    /**
     * Stops the motor
     */
    public void gestapoArm()
    {
        armSpeed = 0.0;
    }

    /**
     * Moves the arm forward if the desired position is farther extended than the current position,
     * and moves it backwards if the desired position is farther inwards
     */
    public void moveArmToDesired(ArmPosition desiredPosition)
    {
        if (getArmPosition() < desiredPosition.min) 
        {
            extendoArm();
        }
        else if (getArmPosition() > desiredPosition.max)
        {
            retractoArm();
        }
        else 
        {
            gestapoArm();
        }
    }

    @Override
    public synchronized void readPeriodicInputs()
    {
        periodicIO.armPosition = armEncoder.getPosition();
    }

    /**
     * Resets the encoders after a set period of time
     */
    @Override
    public synchronized void writePeriodicOutputs()
    {
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
            armMotor.set(armSpeed);
        }
    }

    @Override
    public String toString()
    {
        SmartDashboard.putNumber("armEncoder", periodicIO.armPosition);
        return "Current Arm Position: " + periodicIO.armPosition;
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
}