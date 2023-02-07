package frc.robot.controls;

import java.lang.invoke.MethodHandles;

import frc.robot.PeriodicIO;

//import edu.wpi.first.wpilibj.DriverStation;

public class OperatorController extends Xbox implements PeriodicIO
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
    public OperatorController(int port)
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
        setAxisSettings(Axis.kLeftX, 0.1, 0.0, 1.0, false, AxisScale.kLinear);
        setAxisSettings(Axis.kLeftY, 0.1, 0.0, 1.0, false, AxisScale.kLinear);
        setAxisSettings(Axis.kLeftTrigger, 0.1, 0.0, 1.0, false, AxisScale.kLinear);
        setAxisSettings(Axis.kRightTrigger, 0.1, 0.0, 1.0, false, AxisScale.kLinear);
        setAxisSettings(Axis.kRightX, 0.1, 0.0, 1.0, false, AxisScale.kLinear);
        setAxisSettings(Axis.kRightY, 0.1, 0.0, 1.0, false, AxisScale.kLinear);
    }

    // public void checkForTriggerConflict()
    // {
    //     for(DriverButtonAction dba : DriverButtonAction.values())
    //     {
    //         if(dba.button == Button.kLeftTrigger || dba.button == Button.kRightTrigger)
    //         {
    //             for(DriverAxisAction daa : DriverAxisAction.values())
    //             {
    //                 if(daa.axis == Axis.kLeftTrigger && dba.button == Button.kLeftTrigger)
    //                     DriverStation.reportWarning("ERROR - Left Trigger is button and axis", false);
    //                 if(daa.axis == Axis.kRightTrigger && dba.button == Button.kRightTrigger)
    //                     DriverStation.reportWarning("ERROR - Right Trigger is button and axis", false);
    //             }
    //         }
    //     }
    // }

    @Override
    public synchronized void readPeriodicInputs()
    {
        // TODO find joystick values
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
