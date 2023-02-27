package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.Timer;
import com.revrobotics.SparkMaxPIDController;

import java.lang.invoke.MethodHandles;
import frc.robot.Constants;

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
        kGather(Constants.Arm.GATHER),
        kLow(Constants.Arm.LOW),
        kMiddle(Constants.Arm.MIDDLE),
        kHigh(Constants.Arm.HIGH),
        kOverride(-4237);

        public final double value;
        private ArmPosition(double value)
        {
            this.value = value;
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
        

        // Outputs
        private double armSpeed = 0.0;
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
    private double threshold = 2500.0;
    
    //TODO: Tune PID values
    private final double kP = 0.00003;
    private final double kI = 0.0; //0.0001;
    private final double kD = 0.0; //1.0;
    private final double kIz = 0.0;
    private final double kFF = 0.0;
    private final double kMaxOutput = 0.3;
    private final double kMinOutput = -0.3;
    private SparkMaxPIDController pidController;

    private int resetAttemptCounter = 0;
    private LimitSwitchState reverseLSState = LimitSwitchState.kStillReleased;
    private boolean useLSReset = true;
    private ResetState resetState = ResetState.kDone;
    private ArmPosition scoringPosition = ArmPosition.kOverride;
    private final int RESET_ATTEMPT_LIMIT = 5;

    

    
    public Arm()
    {
        System.out.println(fullClassName + " : Constructor Started");

        configCANSparkMax();

        System.out.println(fullClassName + " : Constructor Finished");
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

        pidController = armMotor.getPIDController();

        // Encoder
        armEncoder = armMotor.getEncoder();
        armEncoder.setPositionConversionFactor(4096);

        // Configure PID controller
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
        pidController.setIZone(kIz);
        pidController.setFF(kFF);
        pidController.setOutputRange(kMinOutput, kMaxOutput);

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

    public boolean atSetPoint()
    {
        return Math.abs(scoringPosition.value - periodicIO.armPosition) <= threshold;
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
        scoringPosition = ArmPosition.kOverride;
        periodicIO.armSpeed = -0.15;
    }

    /**
     * Set the motor to move forward
     */
    public void extendArm()
    {
        scoringPosition = ArmPosition.kOverride;
        periodicIO.armSpeed = 0.15;
    }

    /** Moves the arm to high position */
    public void moveToHigh()
    {
        scoringPosition = ArmPosition.kHigh;
    }

    /** Moves the arm to middle position */
    public void moveToMiddle()
    {
        scoringPosition = ArmPosition.kMiddle;
    }
 
    /** Moves the arm to low position */
    public void moveToLow()
    {
        scoringPosition = ArmPosition.kLow;
    }

    /** Moves the arm to gather position */
    public void moveToGather()
    {
        scoringPosition = ArmPosition.kGather;
    }

    /**
     * Stops the motor
     */
    public void off()
    {
        scoringPosition = ArmPosition.kOverride;
        periodicIO.armSpeed = 0.0;
    }

    /** 
     * Turns the motor on 
     * @param speed (double)*/
    public void on(double speed)
    {
        periodicIO.armSpeed = speed;
    }
    
    /**
     * Hold the motor in place by setting it to move very slowly
     */
    public void hold()
    {
        scoringPosition = ArmPosition.kOverride;
        periodicIO.armSpeed = 0.05;
    }

    /**
     * Moves the arm forward if the desired position is farther extended than the current position,
     * and moves it backwards if the desired position is farther inwards
     */
    @Deprecated
    public void moveArmToDesired(ArmPosition desiredPosition)
    {
        if (getArmPosition() < desiredPosition.value - threshold) 
        {
            extendArm();
        }
        else if (getArmPosition() > desiredPosition.value + threshold)
        {
            retractArm();
        }
        else 
        {
            off();
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
                if(scoringPosition == ArmPosition.kOverride)
                {
                    armMotor.set(periodicIO.armSpeed);
                }
                else
                {
                    // SmartDashboard.putNumber("Target Position", scoringPosition.value);
                    pidController.setReference(scoringPosition.value, CANSparkMax.ControlType.kPosition);
                }
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