package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;
import frc.robot.RobotContainer;
import frc.robot.commands.AutoCommandList;

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

    private boolean useAutonomousTab            = false;
    private boolean useCameraTab                = false;
    private boolean useSensorTab                = true;
    private boolean useDriverControllerTab      = false;
    private boolean useOperatorControllerTab    = false;
    
    public final AutonomousTab autonomousTab;
    public final CameraTab cameraTab;
    public final SensorTab sensorTab;
    public final DriverControllerTab driverControllerTab;
    public final OperatorControllerTab operatorControllerTab;
    
    public AutoCommandList autoCommandList;
    


    
    // *** CLASS CONSTRUCTOR ***
    public MainShuffleboard(RobotContainer robotContainer)
    {
        System.out.println(fullClassName + " : Constructor Started");
        boolean useFullRobot = robotContainer.fullRobot; 
        useDriverControllerTab = useDriverControllerTab && robotContainer.driverController != null;
        useOperatorControllerTab = useOperatorControllerTab && robotContainer.operatorController != null;

        autonomousTab           = (useFullRobot || useAutonomousTab)         ? new AutonomousTab()                                                                                           : null;
        cameraTab               = (useFullRobot || useCameraTab)             ? new CameraTab()                                                                                               : null;
        sensorTab               = (useFullRobot || useSensorTab)             ? new SensorTab(robotContainer.shoulder, robotContainer.grabber, robotContainer.arm, robotContainer.drivetrain) : null;
        driverControllerTab     = (useFullRobot || useDriverControllerTab)   ? new DriverControllerTab(robotContainer.driverController)                                                      : null;
        operatorControllerTab   = (useFullRobot || useOperatorControllerTab) ? new OperatorControllerTab(robotContainer.operatorController)                                                  : null;
        
        // cameraTab = new CameraTab();

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
    // CAMERA TAB
    public void setCameras()
    {
        if(cameraTab != null)
            cameraTab.updateCameraTab();
    }

    //-------------------------------------------------------------------//
    // SENSOR TAB
    public void setSensors()
    {
        if(sensorTab != null)
            sensorTab.updateEncoderData();
    }
}
