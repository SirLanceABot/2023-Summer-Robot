package frc.robot.configs;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAnalogSensor;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * This class can help with the configuration settings and using a CAN Spark Max.
 */
public final class ConfigCANSparkMax
{
    private final CANSparkMax motor = new CANSparkMax(2, MotorType.kBrushless);
    private SparkMaxLimitSwitch forwardLimitSwitch;
    private SparkMaxLimitSwitch reverseLimitSwitch;

    private RelativeEncoder relativeEncoder;
    private SparkMaxAnalogSensor analogSensor;


    public ConfigCANSparkMax()
    {
        configCANSparkMax();
    }
    
    private void configCANSparkMax()
    {
        // Factory Defaults
        motor.restoreFactoryDefaults();

        // Invert the direction of the motor
        motor.setInverted(false);

        // Brake or Coast mode
        motor.setIdleMode(IdleMode.kBrake);

        // Set the Feedback Sensor
        relativeEncoder = motor.getEncoder(SparkMaxRelativeEncoder.Type.kQuadrature, 4096);
        analogSensor = motor.getAnalog(SparkMaxAnalogSensor.Mode.kRelative);

        // Soft Limits
        motor.setSoftLimit(SoftLimitDirection.kForward, 0);
        motor.enableSoftLimit(SoftLimitDirection.kForward, false);
        motor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        motor.enableSoftLimit(SoftLimitDirection.kReverse, false);

        // Hard Limits
        forwardLimitSwitch = motor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        forwardLimitSwitch.enableLimitSwitch(false);
        reverseLimitSwitch = motor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        reverseLimitSwitch.enableLimitSwitch(false);


    }
}
