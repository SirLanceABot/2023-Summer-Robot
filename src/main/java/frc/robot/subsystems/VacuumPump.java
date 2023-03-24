package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAnalogSensor;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAnalogSensor.Mode;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;

public class VacuumPump 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();
    
    // private final CANSparkMax suctionMotor = new CANSparkMax(1, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private final CANSparkMax vacuumMotor;
    private RelativeEncoder vacuumMotorEncoder;
    private SparkMaxLimitSwitch forwardLimitSwitch;
    private SparkMaxLimitSwitch reverseLimitSwitch;
    private SparkMaxAnalogSensor analogSensor;
    public final String pumpName; 
    public final double lowPressure;
    public final double speedLimit;
    public final double asFastAsPossible;
    public final double setPoint;
    public final double maintainSpeedLimit;



    public VacuumPump(VacuumPumpConfig vpc)
    {
        vacuumMotor = new CANSparkMax(vpc.motorChannel, MotorType.kBrushless);
        this.pumpName = vpc.pumpName;
        this.lowPressure = vpc.lowPressure;
        this.speedLimit = vpc.speedLimit;
        this.asFastAsPossible = vpc.asFastAsPossible;
        this.setPoint = vpc.setPoint;
        this.maintainSpeedLimit = vpc.maintainSpeedLimit;
        configCANSparkMax();
    
    }

    /**
     * Makes the configurations of a Spark Max Motor
     */
    private void configCANSparkMax()
    {   
        // // Start Data Log
        // DataLogManager.start();

        // Factory Defaults
        vacuumMotor.restoreFactoryDefaults();
        
        // Invert the direction of the motor
        vacuumMotor.setInverted(false);

        // Brake or Coast mode
        vacuumMotor.setIdleMode(IdleMode.kBrake);

        // Get analog sensors
        analogSensor = vacuumMotor.getAnalog(Mode.kAbsolute);


        // Set the Feedback Sensor
        // vacuumMotor.setSensorPhase(false);
        vacuumMotorEncoder = vacuumMotor.getEncoder();
        // grabberMotorEncoder.setPositionConversionFactor(4096);

        // // PID controllers
        // pidControllerTop = vacuumMotorTop.getPIDController();
        // pidControllerBottom = vacuumMotorBottom.getPIDController();

        // // Set the Feedback Device
        // pidControllerTop.setFeedbackDevice(analogSensorTop);
        // pidControllerBottom.setFeedbackDevice(analogSensorBottom);

        // Soft Limits
        vacuumMotor.setSoftLimit(SoftLimitDirection.kForward, 0);
        vacuumMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
        vacuumMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        vacuumMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);

        // Hard Limits
        forwardLimitSwitch = vacuumMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        forwardLimitSwitch.enableLimitSwitch(false);
        reverseLimitSwitch = vacuumMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        reverseLimitSwitch.enableLimitSwitch(false);

        // Smart Current Limits
        vacuumMotor.setSmartCurrentLimit(40);
    }

    public double getOutputCurrent()
    {
        return vacuumMotor.getOutputCurrent();
    }

    public double getPosition()
    {
        return vacuumMotorEncoder.getPosition();
    }

    public double getVoltage()
    {
        return analogSensor.getVoltage();
    }

    public void set(double speed)
    {
        vacuumMotor.set(speed);
    }

    public double getVelocity()
    {
        return vacuumMotorEncoder.getVelocity();
    }
}

