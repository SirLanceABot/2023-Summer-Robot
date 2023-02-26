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
        kHigh(39.0,44.0), kMiddle(24.0,29.0), kLow(10.0,15.0), kGather(0.0,5.0);
        public final double min; 
        public final double max;
        private ArmPosition(double min, double max)
        {
            this.min = min;
            this.max = max;
        }
    }

    public enum ResetState
    {
        kStart, kTry, kDone;
    }

    private enum LimitSwitchState
    {
        kPressed, kStillPressed, kReleased, kStillReleased
    }

    private class PeriodicIO
    {
        // Inputs
        private double armPosition = 0.0;
        private double armSpeed = 0.0;

        // Outputs
    }
    
    private PeriodicIO periodicIO = new PeriodicIO();
    private final CANSparkMax armMotor = new CANSparkMax (Constants.Subsystem.ARM_MOTOR_PORT, MotorType.kBrushless);
    // private final CANSparkMax armMotor = new CANSparkMax (3, MotorType.kBrushless);  //test
    private SparkMaxLimitSwitch forwardLimitSwitch;
    private SparkMaxLimitSwitch reverseLimitSwitch;
    private final Timer encoderResetTimer = new Timer();
    private RelativeEncoder armEncoder;
    private boolean resetEncoderNow = false;
    private boolean hasEncoderReset = true;
    
    private boolean useLSReset = true;
    private LimitSwitchState reverseLSState = LimitSwitchState.kStillReleased;
    private ResetState resetState = ResetState.kDone;
    private int resetAttemptCounter = 0;
    private final int RESET_ATTEMPT_LIMIT = 5;

    
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
        resetState = resetState.kStart;

        // resetEncoderNow = true;
        // hasEncoderReset = false;
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
        periodicIO.armSpeed = -0.50;
    }

    /**
     * Set the motor to move forward
     */
    public void extendArm()
    {
        periodicIO.armSpeed = 0.50;
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
        periodicIO.armPosition = armEncoder.getPosition();

        if(useLSReset)
        {
            boolean isReverseLimitSwitchPressed = reverseLimitSwitch.isPressed();

            switch(reverseLSState)
            {
                case kStillReleased:
                    if(isReverseLimitSwitchPressed)
                    {
                        resetState = ResetState.kStart;
                        reverseLSState = LimitSwitchState.kPressed;
                    }
                    break;
                case kPressed:
                    if(isReverseLimitSwitchPressed)
                        reverseLSState = LimitSwitchState.kStillPressed;
                    else
                        reverseLSState = LimitSwitchState.kReleased;
                    break;
                case kStillPressed:
                    if(!isReverseLimitSwitchPressed)
                        reverseLSState = LimitSwitchState.kReleased;
                    break;
                case kReleased:
                    if(!isReverseLimitSwitchPressed)
                        reverseLSState = LimitSwitchState.kStillReleased;
                    else
                        reverseLSState = LimitSwitchState.kPressed;
                    break;
            }
        }

        // // TODO see the Shoulder code for resetting encoder
        // periodicIO.armPosition = armEncoder.getPosition();
    }

    /**
     * Resets the encoders after a set period of time
     */
    @Override
    public synchronized void writePeriodicOutputs()
    {
        switch(resetState)
        {
            case kDone:
                armMotor.set(periodicIO.armSpeed);
                break;

            case kStart:
                encoderResetTimer.reset();
                encoderResetTimer.start();
                armMotor.set(0.0);
                armEncoder.setPosition(0.0);
                resetState = ResetState.kTry;
                resetAttemptCounter++;
                break;

            case kTry:
                if(resetAttemptCounter < RESET_ATTEMPT_LIMIT)
                {
                    if(Math.abs(periodicIO.armPosition) < 0.5 )
                    {
                        resetState = ResetState.kDone;
                        resetAttemptCounter = 0;
                    }
                    else if(encoderResetTimer.hasElapsed(0.1))
                    {
                        System.out.println("Attempts: " + resetAttemptCounter);
                        // DataLogManager.log("Attempts: " + resetAttemptCounter);
                        resetAttemptCounter++;
                        armEncoder.setPosition(0.0);
                        encoderResetTimer.reset();
                        encoderResetTimer.start();
                    }
                }
                else if(resetAttemptCounter >= RESET_ATTEMPT_LIMIT)
                {
                    resetState = ResetState.kDone;
                    resetAttemptCounter = 0;
                    System.out.println("Reset encoder failed " + RESET_ATTEMPT_LIMIT + " times");
                    // DataLogManager.log("Reset encoder failed " + RESET_ATTEMPT_LIMIT + " times");
                }
                break;
        }
        // // TODO see the Shoulder code for resetting encoder
        // if (resetEncoderNow)
        // {
        //     armMotor.set(0.0);
        //     armEncoder.setPosition(0.0);
        //     resetEncoderNow = false;
        //     hasEncoderReset = true;
        //     encoderResetTimer.reset();
        //     encoderResetTimer.start();
        // }
        // else if (!hasEncoderReset)
        // {
        //     if(Math.abs(periodicIO.armPosition) < 0.5)
        //     {
        //         hasEncoderReset = true;
        //     }
        //     else if (encoderResetTimer.hasElapsed(0.1))
        //     {
        //         armEncoder.setPosition(0.0);
        //         encoderResetTimer.reset();
        //         encoderResetTimer.start();
        //     }
        // }
        // else 
        // {
        //     armMotor.set(periodicIO.armSpeed);
        // }
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