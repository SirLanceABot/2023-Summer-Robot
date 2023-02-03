package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import frc.robot.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * Class containing one NEO 550 (according to Owen) and two limit switches
 */
public class Gatherer extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    public static Object turnOff;

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private class PeriodicIO
    {
        // Inputs

        // Outputs
        private double motorSpeed = 0.0;
    }

    int GathererMotorPort = Constants.MotorConstants.GATHERER_MOTOR_PORT;

    private final CANSparkMax gathererMotor = new CANSparkMax(GathererMotorPort, MotorType.kBrushless);
    private SparkMaxLimitSwitch forwardLimitSwitch;
    private SparkMaxLimitSwitch reverseLimitSwitch;
    /**
     *
     */
    private RelativeEncoder gathererEncoder;
    // private static double motorSpeed;

    private final PeriodicIO periodicIO = new PeriodicIO();
    public static Object gatherGamePiece;

    public Gatherer()
    {
        configGathererMotor();
    }

    private void configGathererMotor()
    {   
        gathererMotor.restoreFactoryDefaults();
        gathererMotor.setInverted(false);
        gathererMotor.setIdleMode(IdleMode.kBrake);

        // set feedback device
        // gathererEncoder = gathererEncoder.getEncoder(SparkMaxRelativeEncoder.Type.kQuadrature, 4096);

        gathererMotor.setSoftLimit(SoftLimitDirection.kForward, 0);
        gathererMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
        gathererMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        gathererMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);

        forwardLimitSwitch = gathererMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        forwardLimitSwitch.enableLimitSwitch(false);
        reverseLimitSwitch = gathererMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        reverseLimitSwitch.enableLimitSwitch(false);
    }

    /**
     * Sets the motor speed to positive to pull in the game piece
     */
    public void gatherGamePiece()
    {
        periodicIO.motorSpeed = 0.5;
        // gathererMotor.set(-0.5);
    }

    /**
    * In case gamepiece gets jammed or we need to release it,
    * reverse the direction of motors to push gamepiece out
    */
    public void freeGamePiece()
    {
        // gathererMotor.set(-0.5);
        periodicIO.motorSpeed = -0.5;
    }

    /*
     * Turn off the motors
     */
    public void turnOff()
    {
        periodicIO.motorSpeed = 0.0;
    }


    @Override
    public synchronized void readPeriodicInputs()
    {

    }

    @Override
    public synchronized void writePeriodicOutputs()
    {
        gathererMotor.set(periodicIO.motorSpeed);
    }

    /*
     * This method will be called once per scheduler run
     */
    @Override
    public void periodic()
    {}

    /*
     * This method will be called once per scheduler run during simulation
     */
    @Override
    public void simulationPeriodic()
    {}
}
