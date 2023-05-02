package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;

import frc.robot.RobotContainer;



public class PathPlannerTest implements Test
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
    // Put all class and instance variables here.
    private final RobotContainer robotContainer;
 


    // *** CLASS CONSTRUCTOR ***
    public PathPlannerTest(RobotContainer robotContainer)
    {   
        this.robotContainer = robotContainer;

        PathPlannerTrajectory path1 = PathPlanner.loadPath("TestPath", 4, 1);
        PathPlannerState pathState = (PathPlannerState) path1.sample(1.0);
        System.out.println(pathState.velocityMetersPerSecond);
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {}

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {}
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {}

    // *** METHODS ***
    // Put any additional methods here.

    
}

