package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.shuffleboard.AutonomousTabData;
import frc.robot.shuffleboard.AutonomousTabData.StartingLocation;
// import frc.robot.shuffleboard.AutonomousTabData.TeamColor;
import frc.robot.shuffleboard.AutonomousTabData.PlayPreload;
import frc.robot.shuffleboard.AutonomousTabData.MoveOntoChargingStation;
import frc.robot.shuffleboard.AutonomousTabData.PickUpGamePieces;
import frc.robot.shuffleboard.AutonomousTabData.RowPlayedPiece1;
import frc.robot.shuffleboard.AutonomousTabData.AutonomousCommands;
import frc.robot.shuffleboard.AutonomousTabData.RowPlayedPiece2;
import frc.robot.shuffleboard.AutonomousTabData.ScoreSecondPiece;
import frc.robot.shuffleboard.AutonomousTabData.ContainingPreload;
import frc.robot.shuffleboard.AutonomousTabData.DriveToSecondPiece;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Shoulder;
import frc.robot.subsystems.Shoulder.LevelAngle;

public class AutoCommandList extends SequentialCommandGroup
{
    //This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***
    private static enum CommandState
    {
        kInit, kExecute, kEnd, kAllDone;
    }

    // *** CLASS & INSTANCE VARIABLES ***
    private final AutonomousTabData autonomousTabData;
    // private final Autonomous1 autonomous1;
    private final Drivetrain drivetrain;
    private final Grabber grabber;
    private final Shoulder shoulder;
    // private final Command currentCommand;
    
    

    private static final ArrayList<Command> commandList = new ArrayList<>();
 
    // private  teamColor;
    // private static startingLocation;
    
    // *** CLASS CONSTRUCTOR ***
    public AutoCommandList(RobotContainer robotContainer)
    {
        this.autonomousTabData = robotContainer.autonomousTabData;
        // this.autonomous1 = autonomous1;
        this.drivetrain = robotContainer.drivetrain;
        this.grabber = robotContainer.grabber;
        this.shoulder = robotContainer.shoulder;
        
        commandList.clear();
        
    }

    // *** CLASS & INSTANCE METHODS ***
    public void build()
    {
        commandList.clear();
        DriverStation.Alliance alliance = DriverStation.getAlliance();
        Shoulder.LevelAngle angle = LevelAngle.kGatherer;
        Shoulder.LevelAngle angle2 = LevelAngle.kGatherer;
        double distance = 0.0;
        // RowPlayedPiece1 row1 = autonomousTabData.rowPlayedPiece1.getSelected();

        add(new StopDrive(drivetrain));

        switch(autonomousTabData.rowPlayedPiece1)
        {
            case kNone:
                angle = LevelAngle.kGatherer;
                break;
            case kBottom:
                angle = LevelAngle.kLow;
                break;
            case kMiddle:
                angle = LevelAngle.kMiddle;
                break;
            case kTop:
                angle = LevelAngle.kHigh;
                break;
        }

        switch(autonomousTabData.rowPlayedPiece2)
        {
            case kNone:
                angle2 = LevelAngle.kGatherer;
                break;
            case kBottom:
                angle2 = LevelAngle.kLow;
                break;
            case kMiddle:
                angle2 = LevelAngle.kMiddle;
                break;
            case kTop:
                angle2 = LevelAngle.kHigh;
                break;
        }

        if(alliance == DriverStation.Alliance.Red && autonomousTabData.startingLocation == StartingLocation.kLeft || alliance == DriverStation.Alliance.Blue && autonomousTabData.startingLocation == StartingLocation.kRight)
        {
            distance = 0.5;
        }
        
        else
        {
            distance = -0.5;
        }

        if(autonomousTabData.autonomousCommands != AutonomousCommands.kNeither)
        {
            switch(autonomousTabData.autonomousCommands)
            {
            case kNeither:
                break;
            case kChargingStation:
                moveShoulder(angle);
                releasePiece();
                driveOut(4.26);
                goToChargingStation(distance);
                break;
            case kTwoGamePieces:
                moveShoulder(angle);
                releasePiece();
                driveOut(4.4);
                turnRobot180();
                pickUpPiece2();
                break;
            }
        }
        else
        {
            if(autonomousTabData.containingPreload == ContainingPreload.kYes && autonomousTabData.playPreload == PlayPreload.kYes)
            {
                moveShoulder(angle);
                releasePiece();
            }

            if(autonomousTabData.driveToSecondPiece == DriveToSecondPiece.kYes)
            {
                turnRobot180();
                driveOut(4.4);
            }

            if(autonomousTabData.pickUpGamePieces == PickUpGamePieces.kYes)
            {
                add(new OpenGrabber(grabber));
            }

            if(autonomousTabData.scoreSecondPiece == ScoreSecondPiece.kYes)
            {
                turnRobot180();
                driveOut(4.4);
                moveShoulder(angle2);
            }

            if(autonomousTabData.moveOntoChargingStation == MoveOntoChargingStation.kYes)
            {
                goToChargingStation(0.5);
            }

           
        }
        
    }

    private void driveOut(double distance)
    {
        add(new DriveDistanceAuto(drivetrain, 0.5, 0.0, distance));
    }

    private void strafeDrive(double distance)
    {
        add(new DriveDistanceAuto(drivetrain, 0.0, 0.5, distance)); 
    }

    private void releasePiece()
    {
        add(new OpenGrabber(grabber));
        moveShoulder(LevelAngle.kGatherer);
    }

    private void moveShoulder(LevelAngle level)
    {
        add(new MoveShoulderToAngle(shoulder, level));
    }



    private void turnRobot180()
    {
        add(new DriveDistanceAuto(drivetrain, 0.5, -0.5, 2));
    }

    private void goToSecondGamePiece()
    {
        add(new DriveDistanceAuto(drivetrain, 0.75, 0.0, 4.5));
    }

    private void goToChargingStation(double distance)
    {
        add(new DriveDistanceAuto(drivetrain, 0.75, 0.0, 4.27));
        add(new DriveDistanceAuto(drivetrain, 0.0, distance, 1.0));
        add(new DriveDistanceAuto(drivetrain, -0.75, 0.0, 1.0));
    }

    private void pickUpPiece2()
    {

    }



    // private void addCommand(Command command)
    // {
        // Command commandList[20];
        // Command startingCommand = kDelay;
        // int counter;
        // for(counter = 0; counter <= size(commandList); counter++)
        // {
        //     startingCommand.andThen(commandList[counter]);
        // }
        
    //     commandList.add(command);
    // }

    // ***** Use these methods in AutonomousMode to execute the AutonomousCommandList

    private void add(Command command)
    {
        addCommands(command);
        commandList.add(command);

    }

    public String toString()
    {
        String str = "";

        str += "\n***** AUTONOMOUS COMMAND LIST *****\n";
        for(Command command : commandList)
        {
            str += command + "\n";
        }

        return str;
    }



}
