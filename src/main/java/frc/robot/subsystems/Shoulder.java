package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class Shoulder extends Subsystem4237
{
    private final TalonFX shoulderMotor = new TalonFX(1);

    private static final int TIMEOUT_MS = 30;

    //TODO: Tune PID values
    private static final double kP = 0.018;
    private static final double kI = 0.0002;
    private static final double kD = 0.000;
    private static final double kF = 0.04;

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
        shoulderMotor.configReverseSoftLimitEnable(true);
        
        //TODO: determine if switches are normaly opened or normally closed
        shoulderMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        shoulderMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);

        //comment out if using a PID
        // flywheelMotor.configOpenloopRamp(0.5);
        // flywheelMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 35, 40, 0.5), 10);

        //increase framerate for sensor velocity checks (currently at 100ms)
    }

    public void moveUp()
    {
        shoulderMotor.set(ControlMode.PercentOutput, 0.5);
    }

    public void moveDown()
    {
        shoulderMotor.set(ControlMode.PercentOutput, -0.5);
    }




    @Override
    public synchronized void readPeriodicInputs()
    {

    }

    @Override
    public synchronized void writePeriodicOutputs()
    {

    }
    
}
