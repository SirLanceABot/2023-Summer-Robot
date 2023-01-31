package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// package frc.robot.subsystems;
import com.revrobotics.REVLibError;

import java.lang.invoke.MethodHandles;

public class Arm extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum ArmPosition
    {
        // Placeholder values used; real values to be determined when we have the arm length
        kFullyExtended(39.0,44.0), kThreeQuarterExtended(24.0,29.0), kHalfExtended(10.0,15.0), kIn(0.0,5.0);
        public final double min; 
        public final double max;
        private ArmPosition(double min, double max)
        {
            this.min = min;
            this.max = max;
        }
    }
    
    int ArmMotorPort = Constants.MotorConstants.ARM_MOTOR_PORT;
    private final CANSparkMax armMotor = new CANSparkMax (ArmMotorPort, MotorType.kBrushless);
    private final SparkMaxLimitSwitch forwardLimitSwitch = armMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    private final SparkMaxLimitSwitch reverseLimitSwitch = armMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
    private final Timer encoderResetTimer = new Timer();
    private RelativeEncoder armEncoder;
    private double armPosition = 0.0;
    private double armSpeed = 0.0;
    private boolean resetEncoderNow = false;
    private boolean hasEncoderReset = true;    
    
    // Creates a new ExampleSubsystem. 
    public Arm()
    {
        configCANSparkMax();
    }

    private void configCANSparkMax()
    {
        // Factory Defaults
        armMotor.restoreFactoryDefaults();
        // Invert Motor Direction
        armMotor.setInverted(false);
        // Set Coast or Break Mode
        armMotor.setIdleMode(IdleMode.kBrake);
        // Set the Feedback Sensor
        // sparkMaxMotor.ser();

        //Soft Limits
        armMotor.setSoftLimit(SoftLimitDirection.kForward, 0);
        armMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
        armMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        armMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);
        
        //Hard Limits
        forwardLimitSwitch.enableLimitSwitch(false);
        reverseLimitSwitch.enableLimitSwitch(false);

        // Encoder
        armEncoder = armMotor.getEncoder();
    }

    public void resetEncoder()
    {
        resetEncoderNow = true;
        hasEncoderReset = false;
        encoderResetTimer.reset();
        encoderResetTimer.start();
    }
   
    public double getArmPosition()
    {
        return armPosition;
    }

    public void retractoArm()
    {
        // Set the motor to move backward
        armSpeed = -0.3;
    }

    public void extendoArm()
    {
        // Set the motor to move forward
        armSpeed = 0.3;
    }
    
    public void holdoArm()
    {
        // Hold the motor in place
        armSpeed = 0.05;
    }

    public void gestapoArm()
    {
        // Stops the motor
        armSpeed = 0.0;
    }

    public void moveArmToDesired(ArmPosition desiredPosition)
    {
        if (getArmPosition() < desiredPosition.min) 
        {
            extendoArm();
        }
        else if (getArmPosition() > desiredPosition.max)
        {
            retractoArm();
        }
        else 
        {
            gestapoArm();
        }
    }

    public int convertBooleanToInt(boolean bool)
    {
        int integer = (bool) ? 1 : 0;
        return integer;
    }

    @Override
    public synchronized void readPeriodicInputs()
    {
        armPosition = armEncoder.getPosition();
    }

    @Override
    public synchronized void writePeriodicOutputs()
    {
        if (resetEncoderNow)
        {
            armMotor.set(0.0);
            armEncoder.setPosition(0.0);
            resetEncoderNow = false;
            hasEncoderReset = true;
        }
        else if (!hasEncoderReset)
        {
            if(Math.abs(armPosition) < 0.5)
            {
                hasEncoderReset = true;
            }
            else if (encoderResetTimer.hasElapsed(0.1))
            {
                armEncoder.setPosition(0.0);
                encoderResetTimer.reset();
                encoderResetTimer.start();
            }
        }
        else 
        {
            armMotor.set(armSpeed);
        }
    }

    @Override
    public String toString()
    {
        SmartDashboard.putNumber("armEncoder", armPosition);
        return "Current Arm Position: " + armPosition;
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
}