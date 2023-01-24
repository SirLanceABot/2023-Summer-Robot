package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder;
import frc.robot.Constants;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Encoder;


public class Grabber extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // private final CANSparkMax clawMotor = new CANSparkMax(1, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    public static enum GamePiece
    {
        kCone, kCube, kNone;

    }

    public enum State
    {
        kOpen, kClosed;
    }

    GamePiece currentGamePiece = GamePiece.kNone;
    double speed = 0;
    double encoderDistance;
    private RelativeEncoder grabberMotorEncoder;
    int GrabberMotorPort= Constants.MotorConstants.GRABBER_MOTOR_PORT;
    private final CANSparkMax grabberMotor = new CANSparkMax(GrabberMotorPort, MotorType.kBrushless);
    private SparkMaxLimitSwitch forwardLimitSwitch;
    private SparkMaxLimitSwitch reverseLimitSwitch;
    
    private void configCANSparkMax()
    {   
        grabberMotorEncoder = grabberMotor.getEncoder();
        //Factory Defaults
        grabberMotor.restoreFactoryDefaults();

        //Invert the direction of the motor
        grabberMotor.setInverted(false);

        //Brake or Coast mode
        grabberMotor.setIdleMode(IdleMode.kBrake);

        //Set the Feedback Sensor
        // clawMotor.
        // clawMotor.setSensorPhase(false);

        //Soft Limits
        grabberMotor.setSoftLimit(SoftLimitDirection.kForward, 0);
        grabberMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
        grabberMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        grabberMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);

        //Hard Limits
        forwardLimitSwitch = grabberMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        forwardLimitSwitch.enableLimitSwitch(false);
        reverseLimitSwitch = grabberMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        reverseLimitSwitch.enableLimitSwitch(false);



    }

    public Grabber()
    {
        configCANSparkMax();
        readPeriodicInputs();
        writePeriodicOutputs();
        // SendableRegistry.addLW(digitalOutput, "Grabber", .toString());
    }
    
    public void grabGamePiece()
    {
        speed = 0.33;
        if(encoderDistance == 3 && currentGamePiece == GamePiece.kCone || encoderDistance == 1 && currentGamePiece == GamePiece.kCube)
        {
            speed = 0.05;
        }

    }

    public void releaseGamePiece()
    {
        grabberMotor.set(-0.1);
    }

    public boolean isGrabberClosed()
    {
        if(encoderDistance == 3 && currentGamePiece == GamePiece.kCone || encoderDistance == 1 && currentGamePiece == GamePiece.kCube)
        {
            return true;
        }
        else 
        {
            return false;
        }
    }

    public boolean isGrabberOpen()
    {
        if(encoderDistance == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    @Override
    public synchronized void readPeriodicInputs()
    {
        encoderDistance = grabberMotorEncoder.getPosition();

    }

    @Override
    public synchronized void writePeriodicOutputs()
    {
        grabberMotor.set(speed);
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
        return "Grabber()";
    }
}


