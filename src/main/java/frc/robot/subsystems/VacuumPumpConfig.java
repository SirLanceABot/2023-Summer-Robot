package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

public class VacuumPumpConfig
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public final String pumpName;
    public final Integer motorChannel;
    public final double lowPressure, speedLimit, asFastAsPossible, setPoint, maintainSpeedLimit;

    /**
     * @param pumpName      Name of pump (top or bottom)
     * @param lowPressure           Low Pressure
     * @param speedLimit            Speed Limit
     * @param asFastAsPossible      As Fast As Possible
     * @param setPoint              I Zone
     * @param maintainSpeedLimit    Feed Forward
     */
    public VacuumPumpConfig(String pumpName,
                            Integer motorChannel,
                            double lowPressure,
                            double speedLimit,
                            double asFastAsPossible,
                            double setPoint,
                            double maintainSpeedLimit
                            )
    {
        this.pumpName = pumpName;
        this.motorChannel = motorChannel;
        this.lowPressure = lowPressure;
        this.speedLimit = speedLimit;
        this.asFastAsPossible = asFastAsPossible;
        this.setPoint = setPoint;
        this.maintainSpeedLimit = maintainSpeedLimit;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder(400);
        
        sb.append("Name   MotorChannel  LowPressure  SpeedLimit  AsFastAsPossible  SetPoint  MaintainSpeedLimit");

        sb.append(String.format("%-7s %-10s %-10d %-10d %-10d %-10d %-10d\n",
            pumpName,
            motorChannel,
            lowPressure,
            speedLimit,
            asFastAsPossible,
            setPoint,
            maintainSpeedLimit
            ));

        return sb.toString();
    }
}

