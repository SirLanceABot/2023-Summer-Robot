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


public class Wrist extends Subsystem4237
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

    public enum WristPosition
    {
        kUp(Value.kForward), kDown(Value.kReverse), kOff(Value.kOff);

        public final Value value;

        private WristPosition(Value value)
        {
            this.value = value;
        }
    }
 
    public class PeriodicIO
    {
        //INPUTS

        //OUTPUTS
        private WristPosition wristPosition = WristPosition.kOff;
    }

    private final DoubleSolenoid wristSolenoid = 
            new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 
            Constants.Grabber.WRIST_DOWN, Constants.Grabber.WRIST_UP);
    private PeriodicIO periodicIO = new PeriodicIO();

    /**
     * Contructor for the grabber mechanism
     */
    public Wrist()
    {
        System.out.println(fullClassName + " : Constructor Started");

        System.out.println(fullClassName + ": Constructor Finished");
    }

    public void wristUp()
    {
        periodicIO.wristPosition = WristPosition.kUp;
    }

    public void wristDown()
    {
        periodicIO.wristPosition = WristPosition.kDown;
    }


    /**
     * Gets motor inputs such as encoders
     */
    @Override
    public void readPeriodicInputs()
    {

    }

    /**
     * Sets motor speeds and directions
     */
    @Override
    public void writePeriodicOutputs()
    {
        wristSolenoid.set(periodicIO.wristPosition.value);
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
        return "Wrist Position: " + periodicIO.wristPosition;
    }
}