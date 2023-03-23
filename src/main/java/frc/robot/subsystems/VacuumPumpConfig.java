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
    public final double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;

    /**
     * @param pumpName      Name of pump (top or bottom)
     * @param kP            P Gain
     * @param kI            I Gain
     * @param kD            D Gain
     * @param kIz           I Zone
     * @param kFF           Feed Forward
     * @param kMaxOutput    Maximum Output
     * @param kMinOutput    Minimum Output
     * @param maxRPM        Maximum RPM (rounds per minute)
     * @param maxVel        Maximum Velocity
     * @param minVel        Minimum Velocity
     * @param maxAcc        Maximum Acceleration
     * @param allowedErr    Allowed Close Loop Error
     */
    public VacuumPumpConfig(String pumpName,
                            double kP,
                            double kI,
                            double kD,
                            double kIz,
                            double kFF,
                            double kMaxOutput,
                            double kMinOutput,
                            double maxRPM,
                            double maxVel,
                            double minVel,
                            double maxAcc,
                            double allowedErr)
    {
        this.pumpName = pumpName;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kIz = kIz;
        this.kFF = kFF;
        this.kMaxOutput = kMaxOutput;
        this.kMinOutput = kMinOutput;
        this.maxRPM = maxRPM;
        this.maxVel = maxVel;
        this.minVel = minVel;
        this.maxAcc = maxAcc;
        this.allowedErr = allowedErr;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder(400);
        
        sb.append("Name   Channel  PGain  IGain  DGain  IZone  FeedForw.  MaxOut  MinOut  MaxRPM  MaxVel  MinVel  MaxAcc  allowedErr \n");

        sb.append(String.format("%-7s %-9s %-7d %-7d %-7d %-7d %-11d %-8d %-8d %-8d %-8d %-8d %-8d %-12d\n",
            pumpName,
            kP,
            kI,
            kD,
            kIz,
            kFF,
            kMaxOutput,
            kMinOutput,
            maxRPM,
            maxVel,
            minVel,
            maxAcc,
            allowedErr));

        return sb.toString();
    }
}

