package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAnalogSensor;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAnalogSensor.Mode;

import frc.robot.Constants;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.util.datalog.BooleanLogEntry;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.Timer;


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

    public enum RumbleState
    {
        kWaiting, kNow, kRumbled;
    }

    public class PeriodicIO
    {
        //INPUTS
        private double vacuumEncoderBottom = 0.0;
        private double vacuumEncoderTop = 0.0;
        private double vacuumMotorBottomCurrent = 0.0;
        private double vacuumMotorTopCurrent = 0.0;
        private double pdhSolenoidCurrent;
        private boolean pdhSolenoidGet;
        private boolean pdhSolenoidSet;
        private double analogSensorVoltageTop;
        private double analogSensorVoltageBottom;


        //OUTPUTS
        // private WristPosition wristPosition = WristPosition.kOff;
        
        private VacuumState vacuumState = VacuumState.kClosed;

        private double vacuumVelocityTop;
        private double vacuumVelocityBottom;

        DoubleLogEntry vacuumCurrentEntryTop;
        DoubleLogEntry vacuumCurrentEntryBottom;
        DoubleLogEntry pdhSolenoidCurrentEntry;
        DoubleLogEntry vacuumVelocityEntryTop;
        DoubleLogEntry vacuumVelocityEntryBottom;
        DoubleLogEntry analogSensorPressureEntryTop;
        DoubleLogEntry analogSensorPressureEntryBottom;
        DoubleLogEntry analogSensorVoltageEntryTop;
        DoubleLogEntry analogSensorVoltageEntryBottom;
        BooleanLogEntry pdhSolenoidSetEntry;
        BooleanLogEntry pdhSolenoidGetEntry;
        BooleanLogEntry vacuumStateEntry;
        BooleanLogEntry isEnabledEntry;

    }

    // private final PneumaticsModuleType moduleType = PneumaticsModuleType.CTREPCM;
    // private final DoubleSolenoid wristSolenoid = 
    //         new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 
    //         Constants.Grabber.WRIST_UP, Constants.Grabber.WRIST_DOWN);
    private final CANSparkMax vacuumMotorBottom =   new CANSparkMax(Constants.Grabber.GRABBER_MOTOR_BOTTOM_PORT, MotorType.kBrushless);
    private final CANSparkMax vacuumMotorTop =      new CANSparkMax(Constants.Grabber.GRABBER_MOTOR_TOP_PORT, MotorType.kBrushless);
    // private final CANSparkMax vacuumMotor = new CANSparkMax(7, MotorType.kBrushless);   //testing

    private final PowerDistribution vacuumSolenoid = new PowerDistribution(Constants.Grabber.VACUUM_CAN_ID, ModuleType.kRev);
    private PeriodicIO periodicIO = new PeriodicIO();

    private RelativeEncoder vacuumMotorEncoderBottom;
    private RelativeEncoder vacuumMotorEncoderTop;
    private SparkMaxLimitSwitch forwardLimitSwitchBottom;
    private SparkMaxLimitSwitch forwardLimitSwitchTop;
    private SparkMaxLimitSwitch reverseLimitSwitchBottom;
    private SparkMaxLimitSwitch reverseLimitSwitchTop;
    private SparkMaxAnalogSensor analogSensorTop;
    private SparkMaxAnalogSensor analogSensorBottom;
    private Timer solenoidTimer = new Timer();

    private boolean useDataLog = true;
    private DataLog log;
    private boolean isEnabled = false;
    private boolean isDisabling = false;
    private boolean isMaximumPressureReachedTop = false;
    private boolean isMaximumPressureReachedBottom = false;
    private boolean isTargetPressureReachedTop = false;
    private boolean isTargetPressureReachedBottom = false;

    private RumbleState rumbleState = RumbleState.kWaiting;
    

    /**
     * Contructor for the grabber mechanism
     */
    public Grabber(DataLog log)
    {
        System.out.println(fullClassName + " : Constructor Started");

        this.log = log;
        if(log == null)
        {
            useDataLog = false;
        }
        if(useDataLog)
        {
            logVacuumInit();
        }

        configCANSparkMax();
        vacuumSolenoid.setSwitchableChannel(VacuumState.kClosed.value);
        // SendableRegistry.addLW(digitalOutput, "Grabber", .toString());

        System.out.println(fullClassName + ": Constructor Finished");

        analogSensorTop = vacuumMotorTop.getAnalog(Mode.kAbsolute);
        analogSensorBottom = vacuumMotorBottom.getAnalog(Mode.kAbsolute);
    }

    /**
     * Makes the configurations of a Spark Max Motor
     */
    private void configCANSparkMax()
    {   
        // // Start Data Log
        // DataLogManager.start();

        // Factory Defaults
        vacuumMotorBottom.restoreFactoryDefaults();
        vacuumMotorTop.restoreFactoryDefaults();
        
        // Invert the direction of the motor
        vacuumMotorBottom.setInverted(false);
        vacuumMotorTop.setInverted(false);

        // Brake or Coast mode
        vacuumMotorBottom.setIdleMode(IdleMode.kBrake);
        vacuumMotorTop.setIdleMode(IdleMode.kBrake);

        // Set the Feedback Sensor
        // vacuumMotor.setSensorPhase(false);
        vacuumMotorEncoderBottom = vacuumMotorBottom.getEncoder();
        vacuumMotorEncoderTop = vacuumMotorTop.getEncoder();
        // grabberMotorEncoder.setPositionConversionFactor(4096);

        // Soft Limits
        vacuumMotorBottom.setSoftLimit(SoftLimitDirection.kForward, 0);
        vacuumMotorBottom.enableSoftLimit(SoftLimitDirection.kForward, false);
        vacuumMotorBottom.setSoftLimit(SoftLimitDirection.kReverse, 0);
        vacuumMotorBottom.enableSoftLimit(SoftLimitDirection.kReverse, false);

        vacuumMotorTop.setSoftLimit(SoftLimitDirection.kForward, 0);
        vacuumMotorTop.enableSoftLimit(SoftLimitDirection.kForward, false);
        vacuumMotorTop.setSoftLimit(SoftLimitDirection.kReverse, 0);
        vacuumMotorTop.enableSoftLimit(SoftLimitDirection.kReverse, false);

        // Hard Limits
        forwardLimitSwitchBottom = vacuumMotorBottom.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        forwardLimitSwitchBottom.enableLimitSwitch(false);
        reverseLimitSwitchBottom = vacuumMotorBottom.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        reverseLimitSwitchBottom.enableLimitSwitch(false);

        forwardLimitSwitchTop = vacuumMotorTop.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        forwardLimitSwitchTop.enableLimitSwitch(false);
        reverseLimitSwitchTop = vacuumMotorTop.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        reverseLimitSwitchTop.enableLimitSwitch(false);
    }

    /**
     * Releases the air on the pneumatics allowing the grabber to close
     */
    public void grabGamePiece()
    {
        isEnabled = true;
        isDisabling = false;
        rumbleState = RumbleState.kWaiting;
        // periodicIO.vacuumMotorSpeedTop = -0.5;
        // periodicIO.vacuumMotorSpeedBottom = -0.5;
        // periodicIO.vacuumState = VacuumState.kClosed;
    }

    /**
     * Gives air to the pneumatics allowing the grabber to open
     */
    public void releaseGamePiece()
    {
        isEnabled = false;
        isDisabling = true;
        rumbleState = RumbleState.kRumbled;
        // periodicIO.vacuumMotorSpeedTop = 0.0;
        // periodicIO.vacuumMotorSpeedBottom = 0.0;
        // periodicIO.vacuumState = VacuumState.kOpen;
    }

    public void closeSolenoid()
    {
        isEnabled = false;
        isDisabling = false;
        //periodicIO.vacuumState = VacuumState.kClosed;
    }

    // public void wristUp()
    // {
    //     periodicIO.wristPosition = WristPosition.kUp;
    // }

    // public void wristDown()
    // {
    //     periodicIO.wristPosition = WristPosition.kDown;
    // }

    public double getVacuumEncoderBottom()
    {
        return periodicIO.vacuumEncoderBottom;
    }

    public double getVacuumEncoderTop()
    {
        return periodicIO.vacuumEncoderTop;
    }

    public double getVacuumBottomCurrent()
    {
        return periodicIO.vacuumMotorBottomCurrent;
    }

    public double getVacuumTopCurrent()
    {
        return periodicIO.vacuumMotorTopCurrent;
    }

    public double getAnalogSensorVoltageTop()
    {
        return periodicIO.analogSensorVoltageTop;
    }

    public double getAnalogSensorVoltageBottom()
    {
        return periodicIO.analogSensorVoltageBottom;
    }

    public double convertVoltageToPsi(double voltage)
    {
        return (10.875 * voltage) - 19.9375;
    }

    public BooleanSupplier vacuumSuctionSupplier()
    {
        return () -> (isTargetPressureReachedTop && isTargetPressureReachedBottom && rumbleState == RumbleState.kNow);
    }

    private void logVacuumInit()
    {
        periodicIO.vacuumCurrentEntryTop = new DoubleLogEntry(log, "Top Current", "Amps");
        periodicIO.vacuumCurrentEntryBottom = new DoubleLogEntry(log, "Bottom Current", "Amps");
        periodicIO.pdhSolenoidCurrentEntry = new DoubleLogEntry(log, "PDH Solenoid Current", "Amps");
        periodicIO.vacuumVelocityEntryTop = new DoubleLogEntry(log, "Top Motor Speed", "RPM");
        periodicIO.vacuumVelocityEntryBottom = new DoubleLogEntry(log, "Bottom Motor Speed", "RPM");
        periodicIO.analogSensorPressureEntryTop = new DoubleLogEntry(log, "Top Analog Sensor Pressure", "psi");
        periodicIO.analogSensorPressureEntryBottom = new DoubleLogEntry(log, "Bottom Analog Sensor Pressure", "psi");
        periodicIO.analogSensorVoltageEntryTop = new DoubleLogEntry(log, "Top Analog Sensor Voltage", "Volts");
        periodicIO.analogSensorVoltageEntryBottom = new DoubleLogEntry(log, "Bottom Analog Sensor Voltage", "Volts");
        periodicIO.pdhSolenoidGetEntry = new BooleanLogEntry(log, "PDH Solenoid Get", "raw");
        periodicIO.pdhSolenoidSetEntry = new BooleanLogEntry(log, "PDH Solenoid Set", "raw");
        periodicIO.vacuumStateEntry = new BooleanLogEntry(log, "PDH Solenoid Relay State", "raw");
        periodicIO.isEnabledEntry = new BooleanLogEntry(log, "Is Vacuum Enabled?", "raw");
    }

    private void logVacuum()
    {
        periodicIO.vacuumCurrentEntryBottom.append(periodicIO.vacuumMotorBottomCurrent);
        periodicIO.vacuumCurrentEntryTop.append(periodicIO.vacuumMotorTopCurrent);
        periodicIO.pdhSolenoidCurrentEntry.append(periodicIO.pdhSolenoidCurrent);
        periodicIO.vacuumVelocityEntryTop.append(periodicIO.vacuumVelocityTop);
        periodicIO.vacuumVelocityEntryBottom.append(periodicIO.vacuumVelocityBottom);
        periodicIO.analogSensorPressureEntryTop.append(convertVoltageToPsi(periodicIO.analogSensorVoltageTop * Constants.Grabber.VOLTAGE_SCALE_FACTOR));
        periodicIO.analogSensorPressureEntryBottom.append(convertVoltageToPsi(periodicIO.analogSensorVoltageBottom * Constants.Grabber.VOLTAGE_SCALE_FACTOR));
        periodicIO.analogSensorVoltageEntryTop.append(periodicIO.analogSensorVoltageTop);
        periodicIO.analogSensorVoltageEntryBottom.append(periodicIO.analogSensorVoltageBottom);
        periodicIO.pdhSolenoidGetEntry.append(periodicIO.pdhSolenoidGet);
        periodicIO.pdhSolenoidSetEntry.append(periodicIO.pdhSolenoidSet);
        periodicIO.vacuumStateEntry.append(periodicIO.vacuumState.value);
        periodicIO.isEnabledEntry.append(isEnabled);
    }

    public void reset()
    {
        isEnabled = false;
        isDisabling = false;
        isTargetPressureReachedTop = false;
        isTargetPressureReachedBottom = false;
        vacuumMotorTop.set(0);
        vacuumMotorBottom.set(0);
    }

    /* (non-Javadoc)
     * @see frc.robot.subsystems.Subsystem4237#readPeriodicInputs()
     * Gets motor inputs such as encoders
     */
    @Override
    public void readPeriodicInputs()
    {
        periodicIO.vacuumMotorTopCurrent = vacuumMotorTop.getOutputCurrent();
        periodicIO.vacuumMotorBottomCurrent = vacuumMotorBottom.getOutputCurrent();
        periodicIO.vacuumEncoderTop = vacuumMotorEncoderTop.getPosition();
        periodicIO.vacuumEncoderBottom = vacuumMotorEncoderBottom.getPosition();
        periodicIO.analogSensorVoltageTop = analogSensorTop.getVoltage();
        periodicIO.analogSensorVoltageBottom = analogSensorBottom.getVoltage();
        periodicIO.pdhSolenoidCurrent = vacuumSolenoid.getCurrent(23);
        periodicIO.pdhSolenoidGet = vacuumSolenoid.getSwitchableChannel();
        periodicIO.vacuumVelocityTop = vacuumMotorEncoderTop.getVelocity();
        periodicIO.vacuumVelocityBottom = vacuumMotorEncoderBottom.getVelocity();
        
        // if(useDataLog)
        // {
        //     DataLogManager.log("Vacuum Bottom Current:  " + periodicIO.vacuumMotorBottomCurrent);
        //     DataLogManager.log("Vacuum Top Current:  " + periodicIO.vacuumMotorTopCurrent);
        //     DataLogManager.log("Bottom Digital Input:  " + periodicIO.bottomDigitalInputBool);
        //     DataLogManager.log("Top Digital Input:  " + periodicIO.topDigitalInputBool);
        // }
    }

    /* (non-Javadoc)
     * @see frc.robot.subsystems.Subsystem4237#writePeriodicOutputs()
     * Sets motor speeds and directions
     */
    @Override
    public void writePeriodicOutputs()
    {
        if (isEnabled) 
        {
            vacuumSolenoid.setSwitchableChannel(VacuumState.kClosed.value);
            
            if (!isTargetPressureReachedTop) 
            {
                if (periodicIO.analogSensorVoltageTop > Constants.Grabber.TARGET_PRESSURE_TOP) 
                {
                    vacuumMotorTop.set(Constants.Grabber.MAX_SPEED_TOP);
                } 
                else 
                {
                    vacuumMotorTop.set(0);
                    isTargetPressureReachedTop = true;
                }
            } 
            else 
            {
                if (periodicIO.analogSensorVoltageTop > Constants.Grabber.MAX_PRESSURE_TOP) 
                {
                    isTargetPressureReachedTop = false;
                }
            } 

            //We seperated the sensor statements so the motors only run when necessary and not when the individual one has enough pressure
            if (!isTargetPressureReachedBottom) 
            {
                if (periodicIO.analogSensorVoltageBottom > Constants.Grabber.TARGET_PRESSURE_BOTTOM) 
                {
                    vacuumMotorBottom.set(Constants.Grabber.MAX_SPEED_BOTTOM);
                } 
                else 
                {
                    vacuumMotorBottom.set(0);
                    isTargetPressureReachedBottom = true;
                }
            } 
            else 
            {
                if (periodicIO.analogSensorVoltageBottom > Constants.Grabber.MAX_PRESSURE_BOTTOM) 
                {
                    isTargetPressureReachedBottom = false;
                }
            } 

            if(isTargetPressureReachedTop && isTargetPressureReachedBottom)
            {
                if(rumbleState == RumbleState.kWaiting)
                {
                    rumbleState = RumbleState.kNow;
                }
                else if(rumbleState == RumbleState.kNow)
                {
                    rumbleState = RumbleState.kRumbled;
                }
            }
      
            

        } 
        else 
        {
            if (isDisabling)
            {
                vacuumMotorTop.set(0);
                vacuumMotorBottom.set(0);
                vacuumSolenoid.setSwitchableChannel(VacuumState.kOpen.value);
                isTargetPressureReachedTop = false;
                isTargetPressureReachedBottom = false;
                isDisabling = false;
                solenoidTimer.reset();
                solenoidTimer.start();
            } 
            else if (solenoidTimer.hasElapsed(0.5))
            {
                vacuumSolenoid.setSwitchableChannel(VacuumState.kClosed.value);
                //reset();
            }
        } 

       
        // wristSolenoid.set(periodicIO.wristPosition.value);
        // vacuumMotorTop.set(periodicIO.vacuumMotorSpeedTop);
        // vacuumMotorBottom.set(periodicIO.vacuumMotorSpeedBottom);
        // vacuumSolenoid.setSwitchableChannel(periodicIO.vacuumState.value);
        //periodicIO.pdhSolenoidSet = periodicIO.vacuumState.value;

        if(useDataLog && DriverStation.isEnabled())
        {
            logVacuum();
        }
        
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
        return "Encoder Distance: " + String.format("%.4f", periodicIO.vacuumEncoderTop) + String.format("%.4f", periodicIO.vacuumEncoderBottom);
    }
}
