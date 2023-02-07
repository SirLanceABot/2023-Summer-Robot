package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

import frc.robot.RobotContainer;

import frc.robot.RobotContainer;

public class MainShuffleboard 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    private final DriverControllerTab driverControllerTab;
    private  final OperatorControllerTab operatorControllerTab;
    private  final AutonomousTab autonomousTab;
    
    
    // *** CLASS CONSTRUCTOR ***
    public MainShuffleboard(RobotContainer robotContainer)
    {
        System.out.println(fullClassName + " : Constructor Started");
        driverControllerTab = new DriverControllerTab(robotContainer.driverController);
        operatorControllerTab = new OperatorControllerTab(robotContainer.operatorController);
        autonomousTab = new AutonomousTab(robotContainer.autonomousTabData);

        System.out.println(fullClassName + ": Constructor Finished");
    }

    
    // *** CLASS & INSTANCE METHODS ***
    //-------------------------------------------------------------------//
    // DRIVER CONTROLLER TAB
    public void setDriverControllerSettings()
    {
        if(driverControllerTab != null)
            driverControllerTab.setDriverControllerAxisSettings();
    }

    //-------------------------------------------------------------------//
    // OPERATOR CONTROLLER TAB
    public void setOperatorControllerSettings()
    {
        if(operatorControllerTab != null)
            operatorControllerTab.setOperatorControllerAxisSettings();
    }

    //-------------------------------------------------------------------//
    // AUTONOMOUS TAB
    public AutonomousTabData getAutonomousTabData()
    {
        if(autonomousTab != null)
        {
            return autonomousTab.getAutonomousTabData();
        }
        else
        {
            return new AutonomousTabData();
        }
    }

    public boolean wasSendDataButtonPressed()
    {
        if(autonomousTab != null)
        {
            return autonomousTab.wasSendDataButtonPressed();
        }
        else
        {
            return false;
        }
    }
}
