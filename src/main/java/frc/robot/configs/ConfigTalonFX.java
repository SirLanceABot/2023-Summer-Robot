package frc.robot.configs;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class ConfigTalonFX 
{
    private final TalonFX talonFXMotor = new TalonFX(1);

    public ConfigTalonFX()
    {
        configTalonFX();
    }
    
    private void configTalonFX()
    {
        // Factory Defaults
        talonFXMotor.configFactoryDefault(30);

        // Invert the direction of the motor
        talonFXMotor.setInverted(false);

        // Brake or Coast mode
        talonFXMotor.setNeutralMode(NeutralMode.Brake);

        // Set the Feedback Sensor
        talonFXMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        talonFXMotor.setSensorPhase(false);

        // Soft Limits
        talonFXMotor.configForwardSoftLimitThreshold(0);
        talonFXMotor.configForwardSoftLimitEnable(false);
        talonFXMotor.configReverseSoftLimitThreshold(0);
        talonFXMotor.configReverseSoftLimitEnable(false);

        // Hard Limits
        talonFXMotor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        talonFXMotor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
    }
}