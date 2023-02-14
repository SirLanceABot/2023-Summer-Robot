package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.shuffleboard.AutonomousTabData;
import frc.robot.shuffleboard.AutonomousTabData.StartingLocation;
import frc.robot.shuffleboard.AutonomousTabData.TeamColor;
import frc.robot.shuffleboard.AutonomousTabData.PlayPreload;
import frc.robot.shuffleboard.AutonomousTabData.MoveOntoChargingStation;
import frc.robot.shuffleboard.AutonomousTabData.PickUpGamePieces;
import frc.robot.shuffleboard.AutonomousTabData.RowPlayedPiece1;
import frc.robot.shuffleboard.AutonomousTabData.AutonomousCommands;
import frc.robot.shuffleboard.AutonomousTabData.ColumnPlayedPiece1;
import frc.robot.shuffleboard.AutonomousTabData.RowPlayedPiece2;
import frc.robot.shuffleboard.AutonomousTabData.ColumnPlayedPiece2;
import frc.robot.shuffleboard.AutonomousTabData.ContainingPreload;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Shoulder;

public class AutoCommandBuilder extends SequentialCommandGroup
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
    private static Command currentCommand;

    private static final ArrayList<Command> commandList = new ArrayList<>();

    // private  teamColor;
    // private static startingLocation;
    
    // *** CLASS CONSTRUCTOR ***
    public AutoCommandBuilder(RobotContainer robotContainer)
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
        addCommands(new StopDrive(drivetrain));
        if(autonomousTabData.autonomousCommands != AutonomousCommands.kNeither)
        {
            switch(autonomousTabData.autonomousCommands)
            {
            case kNeither:
                break;
            case kChargingStation:
                driveOut();
                moveShoulder(0.5);
                releasePiece();
                goToChargingStation();
                break;
            case kTwoGamePieces:
                driveOut();
                releasePiece();
                turnRobot180();
                goToSecondGamePiece();
                pickUpPiece2();
                break;
            }
        }
        else
        {
            if(autonomousTabData.containingPreload == ContainingPreload.kYes && autonomousTabData.playPreload == PlayPreload.kYes && autonomousTabData.rowPlayedPiece1 == RowPlayedPiece1.kBottom)
            {
                moveShoulder(0.25);
                releasePiece();
            }

            if(autonomousTabData.containingPreload == ContainingPreload.kYes && autonomousTabData.playPreload == PlayPreload.kYes && autonomousTabData.rowPlayedPiece1 == RowPlayedPiece1.kMiddle)
            {
                moveShoulder(0.5);
                releasePiece();
            }

            if(autonomousTabData.containingPreload == ContainingPreload.kYes && autonomousTabData.playPreload == PlayPreload.kYes && autonomousTabData.rowPlayedPiece1 == RowPlayedPiece1.kTop)
            {
                moveShoulder(0.75);
                releasePiece();
            }
        }
        
    }

    private void driveOut()
    {
        add(new DriveDistanceAuto(drivetrain, 0.5, 0.0, 2.0));
    }

    private void strafeDrive()
    {
        if((autonomousTabData.teamColor == TeamColor.kRed))
        {
            add(new DriveDistanceAuto(drivetrain, 0.0, -0.5, 2.0)); 
        }
        else
        {
            add(new DriveDistanceAuto(drivetrain, 0.0, 0.5, 2.0));
        }
    }

    private void releasePiece()
    {
        // addCommand(new );
    }

    private void moveShoulder(double angle)
    {
        // MoveShoulderToAngle(shoulder, angle);
    }



    private void turnRobot180()
    {

    }

    private void goToSecondGamePiece()
    {
        add(new DriveDistanceAuto(drivetrain, 0.75, 0.0, 4.5));
    }

    private void goToChargingStation()
    {
        add(new DriveDistanceAuto(drivetrain, 0.75, 0.0, 4.27));
        if(autonomousTabData.teamColor == TeamColor.kRed && autonomousTabData.startingLocation == StartingLocation.kLeft || autonomousTabData.teamColor == TeamColor.kBlue && autonomousTabData.startingLocation == StartingLocation.kRight)
        {
            add(new DriveDistanceAuto(drivetrain, 0.0, -0.5, 1.0));
        } 

        if(autonomousTabData.teamColor == TeamColor.kBlue && autonomousTabData.startingLocation == StartingLocation.kLeft || autonomousTabData.teamColor == TeamColor.kRed && autonomousTabData.startingLocation == StartingLocation.kRight)
        {
            add(new DriveDistanceAuto(drivetrain, 0.0, 0.5, 1.0));
        } 
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
