package frc.robot.configs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.MotorSafety;

public class MotorController4237 extends MotorSafety
{
    public enum MotorControllerType
    {
        kTalonFX, kCANSparkMax;
    }

    public enum LimitDirection
    {
        kForward, kReverse;
    }
    
    private TalonFX talonFXMotor;

    private CANSparkMax canSparkMaxMotor;
    private SparkMaxLimitSwitch canSparkMaxForwardLimitSwitch;
    private SparkMaxLimitSwitch canSparkMaxReverseLimitSwitch;
    private RelativeEncoder canSparkMaxRelativeEncoder;

    private MotorControllerType motorControllerType;

    public MotorController4237(MotorControllerType motorControllerType, int port)
    {
        this.motorControllerType = motorControllerType;
        switch(motorControllerType)
        {
            case kTalonFX:
                talonFXMotor = new TalonFX(port);
                initTalonFX();
                break;
            case kCANSparkMax:
                canSparkMaxMotor = new CANSparkMax(port, MotorType.kBrushless);
                initCANSparkMax();
                break;
        }
    }

    private void initTalonFX()
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

    private void initCANSparkMax()
    {
        // Factory Defaults
        canSparkMaxMotor.restoreFactoryDefaults();

        // Invert the direction of the motor
        canSparkMaxMotor.setInverted(false);

        // Brake or Coast mode
        canSparkMaxMotor.setIdleMode(IdleMode.kBrake);

        // Set the Feedback Sensor
        canSparkMaxRelativeEncoder = canSparkMaxMotor.getEncoder();

        // Soft Limits
        canSparkMaxMotor.setSoftLimit(SoftLimitDirection.kForward, 0);
        canSparkMaxMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
        canSparkMaxMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        canSparkMaxMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);

        // Hard Limits
        canSparkMaxForwardLimitSwitch = canSparkMaxMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        canSparkMaxForwardLimitSwitch.enableLimitSwitch(false);
        canSparkMaxReverseLimitSwitch = canSparkMaxMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        canSparkMaxReverseLimitSwitch.enableLimitSwitch(false);
    }

    public void set(double speed)
    {
        switch(motorControllerType)
        {
            case kTalonFX:
                talonFXMotor.set(ControlMode.PercentOutput, speed);
                break;
            case kCANSparkMax:
                canSparkMaxMotor.set(speed);
                break;
        }
    }

    public void setInverted(boolean isInverted, boolean changeSensorPhase)
    {
        switch(motorControllerType)
        {
            case kTalonFX:
                talonFXMotor.setInverted(isInverted);
                talonFXMotor.setSensorPhase(changeSensorPhase);
                break;
            case kCANSparkMax:
                canSparkMaxMotor.setInverted(isInverted);
                break;
        }
    }

    public void configSoftLimit(LimitDirection ld, boolean isEnabled, double limit)
    {
        switch(motorControllerType)
        {
            case kTalonFX:
                switch(ld)
                {
                    case kForward:
                        talonFXMotor.configForwardSoftLimitThreshold(limit);
                        talonFXMotor.configForwardSoftLimitEnable(isEnabled);
                        break;
                    case kReverse:
                        talonFXMotor.configReverseSoftLimitThreshold(limit);
                        talonFXMotor.configReverseSoftLimitEnable(isEnabled);
                        break;
                }
                break;

            case kCANSparkMax:
                switch(ld)
                {
                    case kForward:
                        canSparkMaxMotor.setSoftLimit(SoftLimitDirection.kForward, (float) limit);
                        canSparkMaxMotor.enableSoftLimit(SoftLimitDirection.kForward, isEnabled);
                        break;
                    case kReverse:
                        canSparkMaxMotor.setSoftLimit(SoftLimitDirection.kReverse, (float) limit);
                        canSparkMaxMotor.enableSoftLimit(SoftLimitDirection.kReverse, isEnabled);
                        break;
                }
                break;
        }
    }

    public void configPositionConversionFactor(double factor)
    {
        switch(motorControllerType)
        {
            case kTalonFX:
                talonFXMotor.configSelectedFeedbackCoefficient(factor);
                break;
            case kCANSparkMax:
                canSparkMaxRelativeEncoder.setPositionConversionFactor(factor); // 4096.0
                break;
        }
    }


    public void stopMotor()
    {
        System.out.println("Override Me");
    }

    public String getDescription()
    {
        System.out.println("Override Me");
        return "No Component Name";
    }
}
