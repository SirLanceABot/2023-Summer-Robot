package frc.robot.configs;

import java.lang.invoke.MethodHandles;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class ConfigTalonFX 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private final TalonFX talonFXMotor = new TalonFX(1);

    public ConfigTalonFX()
    {
        System.out.println(fullClassName + ": Constructor Started");

        configTalonFX();
        
        System.out.println(fullClassName + ": Constructor Finished");
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
