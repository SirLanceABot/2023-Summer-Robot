package frc.robot.controls;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.PeriodicIO;

//import frc.constants.Port;

//import edu.wpi.first.wpilibj.DriverStation;

public class DriverController extends Xbox implements PeriodicIO
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
    

    // *** INNER ENUMS and INNER CLASSES ***
    private class PeriodicIO
    {

    }
    private PeriodicIO periodicIO;

    // *** CLASS CONSTRUCTOR ***
    public DriverController(int port)
    {
        super(port);

        System.out.println(fullClassName + " : Constructor Started");

        registerPeriodicIO();
        periodicIO = new PeriodicIO();
        configureAxes();
        createRumbleEvents();
        // checkForTriggerConflict();

        System.out.println(fullClassName + ": Constructor Finished");
    }

    // *** CLASS & INSTANCE METHODS *** 

    public void createRumbleEvents()
    {
        createRumbleEvent(30.0, 2.0, 0.75, 0.75);
        createRumbleEvent(10.0, 1.0, 1.0, 1.0);
        createRumbleEvent(5.0, 0.25, 1.0, 1.0);
        createRumbleEvent(4.0, 0.25, 1.0, 1.0);
        createRumbleEvent(3.0, 0.25, 1.0, 1.0);
        createRumbleEvent(2.0, 0.25, 1.0, 1.0);
        createRumbleEvent(1.0, 0.25, 1.0, 1.0);
    }

    public void configureAxes()
    {
        setAxisSettings(Axis.kLeftX, 0.1, 0.0, 1.0, false, AxisScale.kSquared);
        setAxisSettings(Axis.kLeftY, 0.1, 0.0, 1.0, false, AxisScale.kSquared);
        setAxisSettings(Axis.kLeftTrigger, 0.1, 0.0, 1.0, false, AxisScale.kLinear);
        setAxisSettings(Axis.kRightTrigger, 0.1, 0.0, 1.0, false, AxisScale.kLinear);
        setAxisSettings(Axis.kRightX, 0.1, 0.0, 1.0, true, AxisScale.kSquared);
        setAxisSettings(Axis.kRightY, 0.1, 0.0, 1.0, false, AxisScale.kLinear);
    }

    @Override
    public synchronized void readPeriodicInputs()
    {
        // TODO Put in joystick values
    }

    @Override
    public synchronized void writePeriodicOutputs()
    {
        checkRumbleEvent();
    }

    @Override
    public String toString()
    {
        String str = "";

        // str = str + ""


        return str;
    }
}
