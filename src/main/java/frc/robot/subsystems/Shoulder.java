package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

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
    

    private final TalonFX shoulderMotor = new TalonFX(1);
    private static final int TIMEOUT_MS = 30;

    //TODO: Tune PID values
    private static final double kP = 0.018;
    private static final double kI = 0.0002;
    private static final double kD = 0.000;
    private static final double kF = 0.04;

    private double shoulderSpeed;
    private double currentShoulderPosition;
    private double currentShoulderAngle;
    
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

    public double getShoulderPosition()
    {
        return currentShoulderPosition;
    }

    public double getShoulderAngle()
    {
        return currentShoulderAngle;
    }

    public void moveUp()
    {
        shoulderSpeed = 0.5;
    }

    public void moveDown()
    {
        shoulderSpeed = -0.5;
    }

    public void off()
    {
        shoulderSpeed = 0.0;
    }

    public void hold()
    {
        shoulderSpeed = 0.1;
    }

    @Override
    public synchronized void readPeriodicInputs()
    {
        currentShoulderPosition = shoulderMotor.getSelectedSensorPosition();
        currentShoulderAngle = currentShoulderPosition / TICKS_PER_DEGREE;
    }

    @Override
    public synchronized void writePeriodicOutputs()
    {
        shoulderMotor.set(ControlMode.PercentOutput, shoulderSpeed);
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
