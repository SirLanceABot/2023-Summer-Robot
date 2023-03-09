package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.FeedbackDevice;
// import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
// import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
// import com.ctre.phoenix.motorcontrol.NeutralMode;
// import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
// import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
// import com.ctre.phoenix.motorcontrol.can.TalonFX;

import com.revrobotics.CANSparkMax;
// import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
// import com.revrobotics.SparkMaxAnalogSensor;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxPIDController;


// import edu.wpi.first.util.datalog.DataLog;
// import edu.wpi.first.util.datalog.StringLogEntry;
// import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Constants.TargetPosition;



/**
 * Shoudler:  contains one NEO Motor (CAN Spark Max), two limit switches
 */
public class Shoulder extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
        // DataLogManager.log("Loading: " + fullClassName);
    }
    
    // public enum TargetPosition
    // {
    //     kGather(Constants.Shoulder.GATHER),
    //     kLow(Constants.Shoulder.LOW),
    //     kMiddle(Constants.Shoulder.MIDDLE),
    //     kHigh(Constants.Shoulder.HIGH),
    //     kOverride(-4237);
        
    //     public final double value;

    //     private TargetPosition(double value)
    //     {
    //         this.value = value;
    //     }

    //     // kGather(0.0, 10.0), kLow(25.0, 35.0), kMiddle(55.0, 65.0), kHigh(95.0, 105.0);
    //     // public final double min;
    //     // public final double max;

    //     // private ShoulderPosition(double min, double max)
    //     // {
    //     //     this.min = min;
    //     //     this.max = max;
    //     // }
    // }

    public enum ResetState
    {
        kStart, kTry, kDone;
    }

    private enum LimitSwitchState
    {
        kPressed, kStillPressed, kReleased, kStillReleased
    }

    public class PeriodicIO
    {
        // INPUTS
        private double currentPosition;
        private double currentAngle;
        private double currentVelocity;
        
        // OUTPUTS
        private double motorSpeed;
    }

    private PeriodicIO periodicIO = new PeriodicIO();
    // private final int TIMEOUT_MS = 30;
    
    // private final TalonFX oldShoulderMotor = new TalonFX(shoulderMotorPort);
    
    private final CANSparkMax shoulderMotor = new CANSparkMax(Constants.Subsystem.SHOULDER_MOTOR_PORT,  MotorType.kBrushless);
    // private final CANSparkMax shoulderMotor = new CANSparkMax(4,  MotorType.kBrushless); //test

    private final Timer encoderResetTimer = new Timer();

    private SparkMaxLimitSwitch forwardLimitSwitch;
    private SparkMaxLimitSwitch reverseLimitSwitch;

    private RelativeEncoder relativeEncoder;
    private SparkMaxPIDController pidController;

    private final double threshold = 2500.0;

    //TODO: Tune PID values
    private final double kP = 0.00003;
    private final double kI = 0.0; //0.0001;
    private final double kD = 0.0; //1.0;
    private final double kIz = 0.0;
    private final double kFF = 0.0;
    private final double kMaxOutput = 0.4;
    private final double kMinOutput = -0.4;
    private final double kGatherMaxOutput = 0.2;
    private final double kGatherMinOutput = -0.2;

    private final int RESET_ATTEMPT_LIMIT = 5;

    private int resetAttemptCounter = 0;
    private LimitSwitchState reverseLSState = LimitSwitchState.kStillReleased;
    // private boolean previousLSPressed = false;
    private boolean useLSReset = true;     // Enable or Disable reverse limit switch reseting encoder
    // private boolean useDataLog = true;      // Enable or Disable data logs
    private ResetState resetState = ResetState.kDone;
    private TargetPosition targetPosition = TargetPosition.kOverride;
    

    


    /** Creates a new Shoulder */
    public Shoulder()
    { 
        System.out.println(fullClassName + " : Constructor Started");

        configShoulderMotor();
        relativeEncoder.setPosition(Constants.Shoulder.STARTING_POSITION);

        System.out.println(fullClassName + " : Constructor Finished");
    }

    /** Configures Neo motor */
    private void configShoulderMotor()
    {
        // Start Data Logging
        // DataLogManager.start();

        // Factory Defaults
        shoulderMotor.restoreFactoryDefaults();

        // Invert the direction of the motor
        shoulderMotor.setInverted(false);

        // Brake or Coast mode
        shoulderMotor.setIdleMode(IdleMode.kBrake);
        
        // analogSensor = shoulderMotor.getAnalog(SparkMaxAnalogSensor.Mode.kRelative);
        pidController = shoulderMotor.getPIDController();

        // Set the Feedback Sensor and PID Controller
        relativeEncoder = shoulderMotor.getEncoder();
        relativeEncoder.setPositionConversionFactor(4096);  // get position will now return raw encoder ticks

        // Configure PID controller
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
        pidController.setIZone(kIz);
        pidController.setFF(kFF);
        pidController.setOutputRange(kMinOutput, kMaxOutput);

        // // display PID coefficients on SmartDashboard
        // SmartDashboard.putNumber("P Gain", kP);
        // SmartDashboard.putNumber("I Gain", kI);
        // SmartDashboard.putNumber("D Gain", kD);
        // SmartDashboard.putNumber("I Zone", kIz);
        // SmartDashboard.putNumber("Feed Forward", kFF);
        // SmartDashboard.putNumber("Max Output", kMaxOutput);
        // SmartDashboard.putNumber("Min Output", kMinOutput);
        // SmartDashboard.putNumber("Set Rotations", 0);

        // Ramp Rate
        shoulderMotor.setClosedLoopRampRate(0.1);

        // Current Limit
        // shoulderMotor.setSmartCurrentLimit(PUT NUMBER HERE);

        // Soft Limits
        shoulderMotor.setSoftLimit(SoftLimitDirection.kForward, Constants.Shoulder.ENCODER_FORWARD_SOFT_LIMIT);
        shoulderMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
        shoulderMotor.setSoftLimit(SoftLimitDirection.kReverse, Constants.Shoulder.ENCODER_REVERSE_SOFT_LIMIT);
        shoulderMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);

        // Hard Limits
        forwardLimitSwitch = shoulderMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        forwardLimitSwitch.enableLimitSwitch(true);
        reverseLimitSwitch = shoulderMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        reverseLimitSwitch.enableLimitSwitch(true);
    }

    // FOLLOWING CODE IS FOR TALON FX
    // NO LONGER USING A FALCON MOTOR

    // private void configShoulderMotor()
    // {
    //     oldShoulderMotor.configFactoryDefault();
    //     oldShoulderMotor.setInverted(false);
    //     oldShoulderMotor.setNeutralMode(NeutralMode.Brake);

    //     oldShoulderMotor.config_kF(0, kF, TIMEOUT_MS);
    //     oldShoulderMotor.config_kP(0, kP, TIMEOUT_MS);
    //     oldShoulderMotor.config_kI(0, kI, TIMEOUT_MS);
    //     oldShoulderMotor.config_kD(0, kD, TIMEOUT_MS);

    //     oldShoulderMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, TIMEOUT_MS);
    //     oldShoulderMotor.setSensorPhase(false);

    //     //todo: determine soft limit values
    //     oldShoulderMotor.configForwardSoftLimitThreshold(0.0);
    //     oldShoulderMotor.configForwardSoftLimitEnable(false);
    //     oldShoulderMotor.configReverseSoftLimitThreshold(0.0);
    //     oldShoulderMotor.configReverseSoftLimitEnable(false);
        
    //     oldShoulderMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
    //     oldShoulderMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);

    //     //comment out if using a PID
    //     // flywheelMotor.configOpenloopRamp(0.5);
    //     // flywheelMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 35, 40, 0.5), 10);

    //     //increase framerate for sensor velocity checks (currently at 100ms)

    //     //Todo: determine current limit values,and if we wantto use both Supply and Stator limits
    //     //Current limits
    //     oldShoulderMotor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(false, 20, 25, 1.0));=p;;;;
    //     oldShoulderMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(false, 20, 25, 1.0));
    // }

    // this will be deleted later because of a configuration
    // public void resestEncoder()
    // {
    //     // if reverse limit switch pressed, reset integrated encoder to 0
    //     if(oldShoulderMotor.getSensorCollection().isRevLimitSwitchClosed() == 1); 
    //     {
    //         oldShoulderMotor.setSelectedSensorPosition(0.0);
    //     }
    // }

    /** Resets shoulder motor encoder */
    public void resetEncoder()
    {
        resetState = ResetState.kStart;
        // DataLogManager.log("Reset Encoder");       
    }

    public boolean atSetPoint()
    {
        return Math.abs(targetPosition.shoulder - periodicIO.currentPosition) <= threshold;
    }

    /** @return encoder ticks (double) */
    public double getPosition() // encoder ticks
    {
        return periodicIO.currentPosition;
    }

    /** @return angle (double) */
    public double getAngle()    // angle
    {
        return periodicIO.currentAngle;
    }

    /** Takes degrees and converts to encoder ticks */
    /** @param degrees degrees you want shoulder to move */
    /** @return encoder ticks neccesary to move given degrees */
    public double getEncoderTicksForDegrees(double degrees)
    {
        double ticks = (degrees / 360.0) * 4096.0 * 100.0 * (68.0/32.0);
        return ticks;
    }

    /** @return  motor velocity (double) */
    public double getVelocity() // motor velocity
    {
        return periodicIO.currentVelocity;
    }

    /** Moves the shoulder up */
    public void moveUp()
    {
        targetPosition = TargetPosition.kOverride;
        periodicIO.motorSpeed = 0.2;//0.5;
    }

    /** Moves the shoulder down */
    public void moveDown()
    {
        targetPosition = TargetPosition.kOverride;
        periodicIO.motorSpeed = -0.2;//0.5;
    }

    /** Moves the shoulder down to get pressure at beginning of match */
    public void initialPinch()
    {
        targetPosition = TargetPosition.kOverride;
        periodicIO.motorSpeed = -0.1;//0.5;
    }

    /** Moves the shoulder to high position */
    public void moveToHighCone()
    {
        targetPosition = TargetPosition.kHighCone;
    }

    /** Moves the shoulder to high position */
    public void moveToHighCube()
    {
        targetPosition = TargetPosition.kHighCube;
    }

    /** Moves the shoulder to middle position */
    public void moveToMiddleCone()
    {
        targetPosition = TargetPosition.kMiddleCone;
    }

    /** Moves the shoulder to high position */
    public void moveToMiddleCube()
    {
        targetPosition = TargetPosition.kMiddleCube;
    }
 
    /** Moves the shoulder to low position */
    public void moveToLow()
    {
        // System.out.println("Shoulder Moving to Low");
        targetPosition = TargetPosition.kLow;
    }

    /** Moves the shoulderto gather position */
    public void moveToGather()
    {
        // System.out.println("Shoulder Moving to Gather");
        targetPosition = TargetPosition.kGather;
    }

    public void moveToReadyToPickUp()
    {
        targetPosition = TargetPosition.kReadyToPickUp;
    }

    public void moveToSuctionCone()
    {
        targetPosition = TargetPosition.kSuctionCone;
    }

    public void moveToShoulderReadyToClamp()
    {
        targetPosition = TargetPosition.kShoulderReadyToClamp;
    }

    public void moveToClampCone()
    {
        targetPosition = TargetPosition.kClamp;
    }

    public void moveToStartingPosition()
    {
        targetPosition = TargetPosition.kStartingPosition;
    }

    public void  moveToSubstation()
    {
        targetPosition = TargetPosition.kSubstation;
    }

    /** Turns the shoulder off */
    public void off()
    {
        targetPosition = TargetPosition.kOverride;
        periodicIO.motorSpeed = 0.0;
    }

    /** 
     * Turns the motor on 
     * @param speed (double)*/
    public void on(double speed)
    {
        periodicIO.motorSpeed = speed;
    }

    /** Holds the motor still */
    public void hold()
    {
        targetPosition = TargetPosition.kOverride;
        periodicIO.motorSpeed = 0.01;
    }

    @Override
    public synchronized void readPeriodicInputs()
    {
        periodicIO.currentPosition = relativeEncoder.getPosition();
        periodicIO.currentVelocity = relativeEncoder.getVelocity();

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

           
            // if(reverseLimitSwitch.isPressed() && !previousLSPressed)
            // {
            //     previousLSPressed = true;
            //     resetState = ResetState.kStart;
            // }
            // else if(!reverseLimitSwitch.isPressed() && previousLSPressed)
            // previousLSPressed = false;
        }
        // currentAngle = currentPosition / TICKS_PER_DEGREE;

        // currentPosition = oldShoulderMotor.getSelectedSensorPosition();  // TALON FX

    }

    @Override
    public synchronized void writePeriodicOutputs()
    {
        switch(resetState)
        {
            case kDone:
                if(targetPosition == TargetPosition.kOverride)
                {
                    shoulderMotor.set(periodicIO.motorSpeed);
                }
                else if(targetPosition == TargetPosition.kGather)
                {
                    pidController.setOutputRange(kGatherMinOutput, kGatherMaxOutput);
                    pidController.setReference(targetPosition.shoulder, CANSparkMax.ControlType.kPosition);
                    pidController.setOutputRange(kMinOutput, kMaxOutput);
                }
                else
                {
                    pidController.setReference(targetPosition.shoulder, CANSparkMax.ControlType.kPosition);
                }
                break;

            case kStart:
                encoderResetTimer.reset();
                encoderResetTimer.start();
                shoulderMotor.set(0.0);
                relativeEncoder.setPosition(0.0);
                resetState = ResetState.kTry;
                resetAttemptCounter++;
                break;

            case kTry:
                if(resetAttemptCounter < RESET_ATTEMPT_LIMIT)
                {
                    if(Math.abs(periodicIO.currentPosition) < 0.5 )
                    {
                        resetState = ResetState.kDone;
                        resetAttemptCounter = 0;
                    }
                    else if(encoderResetTimer.hasElapsed(0.1))
                    {
                        // System.out.println("Attempts: " + resetAttemptCounter);
                        // DataLogManager.log("Attempts: " + resetAttemptCounter);
                        resetAttemptCounter++;
                        relativeEncoder.setPosition(0.0);
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
        // if(resetState == ResetState.kStart)
        // {
        //     encoderResetTimer.reset();
        //     encoderResetTimer.start();
        //     shoulderMotor.set(0.0);
        //     relativeEncoder.setPosition(0.0);
        //     resetState = ResetState.kTry;
        //     resetAttemptCounter++;
        // }
        // else if(resetState == ResetState.kTry && resetAttemptCounter < RESET_ATTEMPT_LIMIT)
        // {
        //     if(Math.abs(periodicIO.currentPosition) < 0.5 )
        //     {
        //         resetState = ResetState.kDone;
        //         resetAttemptCounter = 0;
        //     }
        //     else if(encoderResetTimer.hasElapsed(0.1))
        //     {
        //         System.out.println("Attempts: " + resetAttemptCounter);
        //         // DataLogManager.log("Attempts: " + resetAttemptCounter);
        //         resetAttemptCounter++;
        //         relativeEncoder.setPosition(0.0);
        //         encoderResetTimer.reset();
        //         encoderResetTimer.start();
        //     }
        // }
        // else if(resetAttemptCounter == RESET_ATTEMPT_LIMIT)
        // {
        //     resetState = ResetState.kDone;
        //     resetAttemptCounter = 0;
        //     System.out.println("Reset encoder failed " + RESET_ATTEMPT_LIMIT + " times");
        //     // DataLogManager.log("Reset encoder failed " + RESET_ATTEMPT_LIMIT + " times");
        // }
        // else
        // {
        //     shoulderMotor.set(periodicIO.motorSpeed);
        // }
        // oldShoulderMotor.set(ControlMode.PercentOutput, motorSpeed);     // TALON FX
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
        return String.format("Encoder Position: %.4f\n", periodicIO.currentPosition);
        // return "Encoder Position: " + String.format("%.4f", periodicIO.currentPosition) + "\n";
        // return "Encoder Position: " + String.format("%.4f", periodicIO.currentPosition) + "   Encoder Velocity: " + String.format("%.4f", periodicIO.currentVelocity) + "\n";
        // return "Forward LSP: " + forwardLimitSwitch.isPressed() + " Reverse LSP: " + reverseLimitSwitch.isPressed() + "\n";
    }
    
}
