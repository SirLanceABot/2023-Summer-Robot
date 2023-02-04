package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import com.ctre.phoenix.led.CANdle;
import frc.robot.Constants.CANbusConstants;

public class Candle4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum LEDstatus
    {
        kPurple, kYellow, kOff;
    }

    private class PeriodicIO
    {
        // Inputs

        // Outputs
    }

    private final CANdle candle = new CANdle(CANbusConstants.CANDLE_PORT, "rio");
    private final int ledCount = 8;
    private LEDstatus status = LEDstatus.kOff;

    public Candle4237()
    {}

    /**
     * Sets the LED(s) to cube purple
     */
    public void signalCube()
    {
        // candle.setLEDs(redValue, greenValue, blueValue, whiteValue, LED #, count)
        // candle.setLEDs(123, 84, 185, 255, 0, 1);
        
    }

    /**
     * Sets the LED(s) to cone yellow
     */
    public void signalCone()
    {
        // candle.setLEDs(237, 194, 0, 255, 0, 1);
    }

    public void turnOffLight()
    {
        // candle.setLEDs(0, 0, 0, 0, 0, 1);
    }
}
