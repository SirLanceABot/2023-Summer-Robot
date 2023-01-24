package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
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
    private final TalonFX shoulderMotor = new TalonFX(ShoulderMotorPort);
    private static final int TIMEOUT_MS = 30;

    //TODO: Tune PID values
    private static final double kP = 0.018;
    private static final double kI = 0.0002;
    private static final double kD = 0.000;
    private static final double kF = 0.04;

    private double motorSpeed;
    private double currentPosition;
    private double currentAngle;
    
    //TODO: change calculation
    private final double TICKS_PER_DEGREE = 5.69;

    public Shoulder()
    {   
        configShoulderMotor();
    }

    private void configShoulderMotor()
    {
        shoulderMotor.configFactoryDefault();
        shoulderMotor.setInverted(false);
        shoulderMotor.setNeutralMode(NeutralMode.Brake);

        shoulderMotor.config_kF(0, kF, TIMEOUT_MS);
        shoulderMotor.config_kP(0, kP, TIMEOUT_MS);
        shoulderMotor.config_kI(0, kI, TIMEOUT_MS);
        shoulderMotor.config_kD(0, kD, TIMEOUT_MS);

        shoulderMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, TIMEOUT_MS);
        shoulderMotor.setSensorPhase(false);

        //TODO: determine soft limit values
        shoulderMotor.configForwardSoftLimitThreshold(0.0);
        shoulderMotor.configForwardSoftLimitEnable(false);
        shoulderMotor.configReverseSoftLimitThreshold(0.0);
        shoulderMotor.configReverseSoftLimitEnable(false);
        
        shoulderMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        shoulderMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);

        //comment out if using a PID
        // flywheelMotor.configOpenloopRamp(0.5);
        // flywheelMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 35, 40, 0.5), 10);

        //increase framerate for sensor velocity checks (currently at 100ms)

        //TODO: determine current limit values,and if we wantto use both Supply and Stator limits
        //Current limits
        shoulderMotor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(false, 20, 25, 1.0));
        shoulderMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(false, 20, 25, 1.0));
    }

    // this will be deleted later because of a configuration
    public void resestEncoder()
    {
        // if reverse limit switch pressed, reset integrated encoder to 0
        if(shoulderMotor.getSensorCollection().isRevLimitSwitchClosed() == 1); 
        {
            shoulderMotor.setSelectedSensorPosition(0.0);
        }
    }

    public double getPosition() // encoder ticks
    {
        return currentPosition;
    }

    public double getAngle()    // angle
    {
        return currentAngle;
    }

    public void moveUp()
    {
        motorSpeed = 0.5;
    }

    public void moveDown()
    {
        motorSpeed = -0.5;
    }

    public void off()
    {
        motorSpeed = 0.0;
    }

    public void hold()
    {
        motorSpeed = 0.1;
    }

    @Override
    public synchronized void readPeriodicInputs()
    {
        currentPosition = shoulderMotor.getSelectedSensorPosition();
        currentAngle = currentPosition / TICKS_PER_DEGREE;
    }

    @Override
    public synchronized void writePeriodicOutputs()
    {
        shoulderMotor.set(ControlMode.PercentOutput, motorSpeed);
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
        return "Encoder Position: " + String.format("%.4f", currentPosition);
    }
    
}
