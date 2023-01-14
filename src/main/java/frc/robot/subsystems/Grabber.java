package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
// import com.ctre.phoenix.motorcontrol.can.TalonFX;
// // import Ports;
// import edu.wpi.first.wpilibj.I2C.Port;


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

    // private final TalonFX leftFingerMotor = new TalonFX(Ports.Motor.GRABBER_LEFT_FINGER);
    // private final TalonFX rightFingerMotor = new TalonFX(Ports.Motor.GRABBER_RIGHT_FINGER);
    
    public void pickUpObject()
    {

    }

    public void releaseObject()
    {

    }

    @Override
    public synchronized void readPeriodicInputs()
    {
        
    }

    @Override
    public synchronized void writePeriodicOutputs()
    {

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


