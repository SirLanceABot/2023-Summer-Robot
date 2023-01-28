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
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAnalogSensor;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;

public class Shoulder extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
    
    //TODO: determine real angles
    public enum LevelAngle
    {
        kGatherer(0.0, 10.0), kLow(25.0, 35.0), kMiddle(55.0, 65.0), kHigh(95.0, 105.0);
        public final double min;
        public final double max;

        private LevelAngle(double min, double max)
        {
            this.min = min;
            this.max = max;
        }
    }

    int ShoulderMotorPort = Constants.MotorConstants.SHOULDER_MOTOR_PORT;
    private static final int TIMEOUT_MS = 30;
    
    // private final TalonFX oldShoulderMotor = new TalonFX(ShoulderMotorPort);
    
    // private final CANSparkMax shoulderMotor = new CANSparkMax(ShoulderMotorPort,  MotorType.kBrushless);
    private final CANSparkMax shoulderMotor = new CANSparkMax(3,  MotorType.kBrushless);    //test
    private final Timer encoderResetTimer = new Timer();

    private SparkMaxLimitSwitch forwardLimitSwitch;
    private SparkMaxLimitSwitch reverseLimitSwitch;

    private RelativeEncoder relativeEncoder;
    private SparkMaxAnalogSensor analogSensor;
    private SparkMaxPIDController pidController;

    //TODO: Tune PID values
    private static final double kP = 0.018;
    private static final double kI = 0.0002;
    private static final double kD = 0.000;
    private static final double kIz = 0.0;
    private static final double kFF = 0.04;
    private static final double kMaxOutput = 1;
    private static final double kMinOutput = -1;

    private double motorSpeed;
    private double currentPosition;
    private double currentAngle;
    private double currentVelocity;
    private boolean resetEncoderNow;
    private boolean resetEncoderNowLS;
    private boolean hasEncoderReset;
    private boolean previousLSPressed = false;
    
    //TODO: change calculation for new gear ratio
    // private final double TICKS_PER_DEGREE = 5.69;    // TALON FX
    private final double TICKS_PER_DEGREE = 11.38;

    public Shoulder()
    {   
        configShoulderMotor();
    }

    private void configShoulderMotor()
    {
        // Factory Defaults
        shoulderMotor.restoreFactoryDefaults();

        // Invert the direction of the motor
        shoulderMotor.setInverted(true);

        // Brake or Coast mode
        shoulderMotor.setIdleMode(IdleMode.kBrake);

        // Set the Feedback Sensor and PID Controller
        relativeEncoder = shoulderMotor.getEncoder();
        relativeEncoder.setPositionConversionFactor(4096);  // get position will now return raw encoder ticks
        
        // analogSensor = shoulderMotor.getAnalog(SparkMaxAnalogSensor.Mode.kRelative);
        // pidController = shoulderMotor.getPIDController();   //TODO: what goes inside ()?

        // Configure PID controller
        // pidController.setP(kP);
        // pidController.setI(kI);
        // pidController.setD(kD);
        // pidController.setIZone(kIz);
        // pidController.setFF(kFF);
        // pidController.setOutputRange(kMinOutput, kMaxOutput);

        // Soft Limits
        //TODO: determine soft limit values
        shoulderMotor.setSoftLimit(SoftLimitDirection.kForward, 103000);
        shoulderMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
        shoulderMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
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

    public void resetEndcoder()
    {
        resetEncoderNow = true;
        hasEncoderReset = false;
        encoderResetTimer.reset();
        encoderResetTimer.start();
    }

    public double getPosition() // encoder ticks
    {
        return currentPosition;
    }

    public double getAngle()    // angle
    {
        return currentAngle;
    }

    public double getVelocity() // motor velocity
    {
        return currentVelocity;
    }

    public void moveUp()
    {
        motorSpeed = 0.1;
    }

    public void moveDown()
    {
        motorSpeed = -0.1;
    }

    public void off()
    {
        motorSpeed = 0.0;
    }

    public void hold()
    {
        motorSpeed = 0.01;
    }

    @Override
    public synchronized void readPeriodicInputs()
    {
        currentPosition = relativeEncoder.getPosition();
        currentVelocity = relativeEncoder.getVelocity();
        resetEncoderNowLS = reverseLimitSwitch.isPressed();
        if(resetEncoderNowLS && !previousLSPressed)
        {
            hasEncoderReset = false;
            previousLSPressed = true;
        }
        else if(!resetEncoderNowLS && previousLSPressed)
            previousLSPressed = false;

        // currentAngle = currentPosition / TICKS_PER_DEGREE;

        // currentPosition = oldShoulderMotor.getSelectedSensorPosition();  // TALON FX

    }

    //TODO: when going down and hit reverse limit switch, cannot immediatly go up
    @Override
    public synchronized void writePeriodicOutputs()
    {
        if(resetEncoderNow || resetEncoderNowLS)
        {
            shoulderMotor.set(0.0);
            relativeEncoder.setPosition(0.0);
            resetEncoderNow = false;
            resetEncoderNowLS = false;
        }
        else if(!hasEncoderReset)
        {
            if(Math.abs(currentPosition) < 0.5 )
                hasEncoderReset = true;
            else if(encoderResetTimer.hasElapsed(0.1))
            {
                relativeEncoder.setPosition(0.0);
                encoderResetTimer.reset();
                encoderResetTimer.start();
            }
        }
        else
        {
            shoulderMotor.set(motorSpeed);
        }
        
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
        return "Encoder Position: " + String.format("%.4f", currentPosition) + "   Encoder Velocity: " + String.format("%.4f", currentVelocity) + "\n";
        // return "Forward LSP: " + forwardLimitSwitch.isPressed() + " Reverse LSP: " + reverseLimitSwitch.isPressed() + "\n";
    }
    
}
