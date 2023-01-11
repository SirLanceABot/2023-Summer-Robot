package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * This abstract class will be extended for every subsystem on the robot. 
 * Every subsystem will automatically be added to the 
 */
public abstract class Subsystem4237 extends SubsystemBase
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private final static ArrayList<Subsystem4237> allSubsystems = new ArrayList<Subsystem4237>();


    // *** CLASS CONSTRUCTOR ***
    public Subsystem4237()
    {
        super();
        allSubsystems.add(this);
    }

    // Abstract methods to override in subclasses
    public abstract void readPeriodicInputs();
    public abstract void writePeriodicOutputs();

    public static void readInputs()
    {
        for(Subsystem4237 subsystem : allSubsystems)
            subsystem.readPeriodicInputs();
    }

    public static void writeOutputs()
    {
        for(Subsystem4237 subsystem : allSubsystems)
            subsystem.writePeriodicOutputs();
    }
}
