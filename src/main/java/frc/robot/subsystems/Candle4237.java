package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.ctre.phoenix.led.CANdle;
import frc.robot.Constants.CANbusConstants;

/**
 * Class containing one CANdle
 */
public class Candle4237 extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum ledStatus
    {
        kPurple, kYellow, kWhite, kOff;
    }

    private class PeriodicIO
    {
        // Inputs

        // Outputs
        private ledStatus status = ledStatus.kOff;
    }

    private PeriodicIO periodicIO = new PeriodicIO();
    private final CANdle candle = new CANdle(CANbusConstants.CANDLE_PORT, "rio");
    private final int ledCount = 8;

    public Candle4237()
    {
        periodicIO.status = ledStatus.kOff;
    }

    /**
     * Sets the LED(s) to cube purple
     */
    public void signalCube()
    {
        periodicIO.status = ledStatus.kPurple;
    }

    /**
     * Sets the LEDs to cone yellow
     */
    public void signalCone()
    {
        periodicIO.status = ledStatus.kYellow;
    }

    /**
     * Sets the LEDs to Go Green
     */
    public void signalReadyToDrop()
    {
        periodicIO.status = ledStatus.kWhite;
    }

    /**
     * Turns off the LEDs
     */
    public void turnOffLight()
    {
        periodicIO.status = ledStatus.kOff;
    }

    @Override
    public synchronized void readPeriodicInputs()
    {}

    /**
     * Sets the color of all LEDs based on the current status
     */
    @Override  
    public synchronized void writePeriodicOutputs() 
    {
        switch (periodicIO.status)
        {
            case kPurple: 
                candle.setLEDs(255, 0, 255, 50, 0, ledCount);
                break;
            case kYellow: 
                candle.setLEDs(255, 185, 0, 50, 0, ledCount);
                break; 
            case kWhite: 
                candle.setLEDs(255, 255, 255, 255, 0, ledCount);
                break;
            case kOff: 
                candle.setLEDs(0, 0, 0, 0, 0, ledCount);
                break;
            default:    // redundant? just to be safe
                candle.setLEDs(0, 0, 0, 0, 0, ledCount);
                break;
        }
    }
}
