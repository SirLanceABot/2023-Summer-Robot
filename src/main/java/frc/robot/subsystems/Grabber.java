package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder;
import frc.robot.Constants;
// import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DoubleSolenoid;
// import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Compressor;


public class Grabber extends Subsystem4237
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

    
    public static enum GamePiece
    {
        kCone, kCube, kNone;

    }

    public enum State
    {
        kOpen, kClosed;
    }

    public class PeriodicIO
    {
        //INPUTS

        //OUTPUTS
        Value state = Value.kOff;
        Value angle = Value.kOff;
        double grabberMotorControl;
    }

    private final PneumaticsModuleType moduleType = PneumaticsModuleType.CTREPCM;
    private final DoubleSolenoid grabberControlSolenoid = new DoubleSolenoid(0, moduleType, 5, 7);
    private final DoubleSolenoid grabberAngleControlSolenoid = new DoubleSolenoid(0, moduleType, 4, 6);
    private final Compressor compressor = new Compressor(moduleType);

    GamePiece currentGamePiece = GamePiece.kNone;
    // double speed = 0;
    double encoderDistance = 3;
    private PeriodicIO periodicIO;
    
    private RelativeEncoder grabberMotorEncoder;
    private final int GrabberMotorPort = Constants.MotorConstants.GRABBER_MOTOR_PORT;
    private final CANSparkMax grabberMotor = new CANSparkMax(GrabberMotorPort, MotorType.kBrushless);
    private SparkMaxLimitSwitch forwardLimitSwitch;
    private SparkMaxLimitSwitch reverseLimitSwitch;

    /**
     * Contructor for the grabber mechanism
     */
    public Grabber()
    {
        System.out.println(fullClassName + " : Constructor Started");

        // configCANSparkMax();
        // readPeriodicInputs();
        // writePeriodicOutputs();
        // SendableRegistry.addLW(digitalOutput, "Grabber", .toString());
        periodicIO = new PeriodicIO();
        configCANSparkMax();

        System.out.println(fullClassName + ": Constructor Finished");
    }
    
    /**
     * Makes the configurations of a Spark Max Motor
     */
    private void configCANSparkMax()
    {   
        grabberMotorEncoder = grabberMotor.getEncoder();
        // Factory Defaults
        grabberMotor.restoreFactoryDefaults();

        // Invert the direction of the motor
        grabberMotor.setInverted(false);

        // Brake or Coast mode
        grabberMotor.setIdleMode(IdleMode.kBrake);

        // Set the Feedback Sensor
        // grabberMotor.setSensorPhase(false);
        grabberMotorEncoder = grabberMotor.getEncoder(SparkMaxRelativeEncoder.Type.kQuadrature, 4096);
        // analogSensor = grabberMotor.getAnalog(SparkMaxAnalogSensor.Mode.kRelative);

        // Soft Limits
        grabberMotor.setSoftLimit(SoftLimitDirection.kForward, 0);
        grabberMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
        grabberMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        grabberMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);

        // Hard Limits
        forwardLimitSwitch = grabberMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        forwardLimitSwitch.enableLimitSwitch(false);
        reverseLimitSwitch = grabberMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        reverseLimitSwitch.enableLimitSwitch(false);



    }

    
    
    /**
     * Releases the air on the pneumatics allowing the grabber to close
     */
    public void grabGamePiece()
    {
        // periodicIO.state = Value.kForward;
        periodicIO.grabberMotorControl = 0.5;

    }

    /**
     * Gives air to the pneumatics allowing the grabber to open
     */
    public void releaseGamePiece()
    {
        // periodicIO.state = Value.kReverse;
        periodicIO.grabberMotorControl = 0.0;
    }

    // public boolean isGrabberClosed()
    // {
    //     if(encoderDistance == 3 && currentGamePiece == GamePiece.kCone || encoderDistance == 1 && currentGamePiece == GamePiece.kCube)
    //     {
    //         return true;
    //     }
    //     else 
    //     {
    //         return false;
    //     }
    // }

    /**
     * @return true if the grabber is open
     */
    public boolean isGrabberOpen()
    {
        return true;
    }

    /**
     * using pnuematics it controls the angle at which the grabber is pointed
     */
    public void aimGrabberDown()
    {
        periodicIO.angle = Value.kForward;
    }

    // //compressor controls
    // /**
    //  * Disables the compressor automatic control loop
    //  */
    public void compressorDisable()
    {
        compressor.disable();
    }

    // /**
    //  * Enables the compressor automatic control loop
    //  */
    public void compressorEnable()
    {
        compressor.enableDigital();
    }

    public double getGrabberEncoder()
    {
        return encoderDistance;
    }


    /* (non-Javadoc)
     * @see frc.robot.subsystems.Subsystem4237#readPeriodicInputs()
     * Gets motor inputs such as encoders
     */
    @Override
    public synchronized void readPeriodicInputs()
    {
        encoderDistance = grabberMotorEncoder.getPosition();

    }

    /* (non-Javadoc)
     * @see frc.robot.subsystems.Subsystem4237#writePeriodicOutputs()
     * Sets motor speeds and directions
     */
    @Override
    public synchronized void writePeriodicOutputs()
    {
        grabberControlSolenoid.set(periodicIO.state);
        grabberAngleControlSolenoid.set(periodicIO.state);
        grabberMotor.set(periodicIO.grabberMotorControl);
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
        return "Encoder Distance: " + String.format("%.4f", encoderDistance);
    }
}


