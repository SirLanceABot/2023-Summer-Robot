package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;


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

    
    // public static enum GamePiece
    // {
    //     kCone, kCube, kNone;

    // }

    // public enum State
    // {
    //     kOpen, kClosed;
    // }

    // public enum WristPosition
    // {
    //     kUp(Value.kForward), kDown(Value.kReverse), kOff(Value.kOff);

    //     public final Value value;

    //     private WristPosition(Value value)
    //     {
    //         this.value = value;
    //     }
    // }

    public enum VacuumState
    {
        kOpen(true), kClosed(false);

        public final boolean value;

        private VacuumState(boolean value)
        {
            this.value = value;
        }
    }

    public class PeriodicIO
    {
        //INPUTS
        private double vacuumEncoder = 0.0;

        //OUTPUTS
        // private WristPosition wristPosition = WristPosition.kOff;
        private double vacuumMotorSpeed = 0.0;
        private VacuumState vacuumState = VacuumState.kClosed;
    }

    // private final PneumaticsModuleType moduleType = PneumaticsModuleType.CTREPCM;
    // private final DoubleSolenoid wristSolenoid = 
    //         new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 
    //         Constants.Grabber.WRIST_UP, Constants.Grabber.WRIST_DOWN);
    private final CANSparkMax vacuumMotor = new CANSparkMax(Constants.Subsystem.GRABBER_MOTOR_PORT, MotorType.kBrushless);
    private final PowerDistribution vacuumSolenoid = new PowerDistribution(Constants.Grabber.VACCUM_CAN_ID, ModuleType.kRev);
    private PeriodicIO periodicIO = new PeriodicIO();

    private RelativeEncoder vacuumMotorEncoder;
    private SparkMaxLimitSwitch forwardLimitSwitch;
    private SparkMaxLimitSwitch reverseLimitSwitch;

    /**
     * Contructor for the grabber mechanism
     */
    public Grabber()
    {
        System.out.println(fullClassName + " : Constructor Started");

        configCANSparkMax();
        vacuumSolenoid.setSwitchableChannel(false);
        // SendableRegistry.addLW(digitalOutput, "Grabber", .toString());

        System.out.println(fullClassName + ": Constructor Finished");
    }
    
    /**
     * Makes the configurations of a Spark Max Motor
     */
    private void configCANSparkMax()
    {   
        // Factory Defaults
        vacuumMotor.restoreFactoryDefaults();
        
        // Invert the direction of the motor
        vacuumMotor.setInverted(false);

        // Brake or Coast mode
        vacuumMotor.setIdleMode(IdleMode.kBrake);

        // Set the Feedback Sensor
        // vacuumMotor.setSensorPhase(false);
        vacuumMotorEncoder = vacuumMotor.getEncoder();
        // grabberMotorEncoder.setPositionConversionFactor(4096);

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
    }

    /**
     * Releases the air on the pneumatics allowing the grabber to close
     */
    public void grabGamePiece()
    {
        periodicIO.vacuumMotorSpeed = 0.5;
        periodicIO.vacuumState = VacuumState.kClosed;
    }

    /**
     * Gives air to the pneumatics allowing the grabber to open
     */
    public void releaseGamePiece()
    {
        periodicIO.vacuumMotorSpeed = 0.0;
        periodicIO.vacuumState = VacuumState.kOpen;
    }

    public void closeSolenoid()
    {
        periodicIO.vacuumState = VacuumState.kClosed;
    }

    // public void wristUp()
    // {
    //     periodicIO.wristPosition = WristPosition.kUp;
    // }

    // public void wristDown()
    // {
    //     periodicIO.wristPosition = WristPosition.kDown;
    // }

    public double getVacuumEncoder()
    {
        return periodicIO.vacuumEncoder;
    }


    /* (non-Javadoc)
     * @see frc.robot.subsystems.Subsystem4237#readPeriodicInputs()
     * Gets motor inputs such as encoders
     */
    @Override
    public synchronized void readPeriodicInputs()
    {
        periodicIO.vacuumEncoder = vacuumMotorEncoder.getPosition();

    }

    /* (non-Javadoc)
     * @see frc.robot.subsystems.Subsystem4237#writePeriodicOutputs()
     * Sets motor speeds and directions
     */
    @Override
    public synchronized void writePeriodicOutputs()
    {
        // wristSolenoid.set(periodicIO.wristPosition.value);
        vacuumMotor.set(periodicIO.vacuumMotorSpeed);
        vacuumSolenoid.setSwitchableChannel(periodicIO.vacuumState.value);
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
        return "Encoder Distance: " + String.format("%.4f", periodicIO.vacuumEncoder);
    }
}


