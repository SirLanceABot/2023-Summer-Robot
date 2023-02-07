package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.shuffleboard.AutonomousTabData;
import frc.robot.shuffleboard.AutonomousTabData.StartingLocation;
import frc.robot.shuffleboard.AutonomousTabData.AreGamePiecesPlayed;
import frc.robot.shuffleboard.AutonomousTabData.MoveOntoChargingStation;
import frc.robot.shuffleboard.AutonomousTabData.PickUpGamePieces;
import frc.robot.shuffleboard.AutonomousTabData.RowPlayedPiece1;
import frc.robot.shuffleboard.AutonomousTabData.ColumnPlayedPiece1;
import frc.robot.shuffleboard.AutonomousTabData.RowPlayedPiece2;
import frc.robot.shuffleboard.AutonomousTabData.ColumnPlayedPiece2;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutonomousCommandList extends CommandBase
{
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

    private static int currentCommandIndex = 0;
    private static CommandState currentCommandState = CommandState.kAllDone;
    private static boolean headerNeedsToBeDisplayed = false;
    private static Command currentCommand;

    private static final ArrayList<Command> commandList = new ArrayList<>();

    private static final double SLOW_DRIVE_SPEED = 1.0;  // meters per second (+/-)
    private static final double FAST_DRIVE_SPEED = 1.8;  // meters per second (+/-)
    private static final double SHORT_DISTANCE = 1.25;   // meters (+)
    
    // *** CLASS CONSTRUCTOR ***
    public AutonomousCommandList(AutonomousTabData autonomousTabData)
    {
        this.autonomousTabData = autonomousTabData;
    }

    // *** CLASS & INSTANCE METHODS ***
    public void build()
    {
        commandList.clear();

        switch(autonomousTabData.autonomousCommands)
        {
        case kNeither:
            break;
        case kChargingStation:
            moveForward();
            goToChargingStation();
            break;
        case kTwoGamePieces:
            moveForward();
            secondGamePiece();
            break;
        }
    }

    private void moveForward()
    {

    }

    private void goToChargingStation()
    {

    }

    private void secondGamePiece()
    {

    }

    private void addCommand(Command command)
    {
        commandList.add(command);
    }

    // ***** Use these methods in AutonomousMode to execute the AutonomousCommandList
    public void init()
    {
        currentCommandIndex = 0;
        headerNeedsToBeDisplayed = true;

        if(currentCommandIndex < commandList.size())
        {
            currentCommand = commandList.get(0);
            currentCommandState = CommandState.kInit;
        }
        else
        {
            currentCommandState = CommandState.kAllDone;
        }
    }

    public void execute()
    {
        switch(currentCommandState)
        {
        case kInit:
            System.out.println("Initializing command number: " + currentCommandIndex);
            
            // currentCommand.init();
            currentCommandState = CommandState.kExecute;
            break;
        case kExecute:
            if(headerNeedsToBeDisplayed)
            {
                System.out.println("Executing command number: " + currentCommandIndex);
                headerNeedsToBeDisplayed = false;
            }
            
            currentCommand.execute();
            if(currentCommand.isFinished())
            {
                currentCommandState = CommandState.kEnd;
                headerNeedsToBeDisplayed = true;
            }
            break;
        case kEnd:
            System.out.println("Ending command number: " + currentCommandIndex);
            
            // currentCommand.end();
            currentCommandIndex++;
            if(currentCommandIndex < commandList.size())
            {
                currentCommandState = CommandState.kInit;
                currentCommand = commandList.get(currentCommandIndex);
            }
            else
            {
                currentCommandState = CommandState.kAllDone;
            }
            break;
        case kAllDone:
            if(headerNeedsToBeDisplayed)
            {
                System.out.println("Done with Autonomous Command List");
                headerNeedsToBeDisplayed = false;
            }
            break;
        }
    }

    public void end()
    {
        currentCommandIndex = 0;
        headerNeedsToBeDisplayed = false;
        currentCommandState = CommandState.kAllDone;
        // currentCommand = null;
        // commandList.clear();
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
