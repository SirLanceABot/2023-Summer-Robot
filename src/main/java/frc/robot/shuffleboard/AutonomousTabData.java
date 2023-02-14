package frc.robot.shuffleboard;

public class AutonomousTabData 
{
    public static enum StartingLocation
    {
        kLeft, kMiddle, kRight;
    }

    //-------------------------------------------------------------------//

    public static enum PlayPreload
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//


    public static enum RowPlayedPiece1
    {
        kNone, kBottom, kMiddle, kTop;
    }

    //-------------------------------------------------------------------//

    public static enum ColumnPlayedPiece1
    {
        kNone, kLeft, kMiddle, kRight;
    }

    //-------------------------------------------------------------------//

    public static enum RowPlayedPiece2
    {
        kNone, kBottom, kMiddle, kTop;
    }

    //-------------------------------------------------------------------//

    public static enum ColumnPlayedPiece2
    {
        kNone, kLeft, kMiddle, kRight;
    }

    //-------------------------------------------------------------------//

    public static enum MoveOntoChargingStation
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//

    public static enum PickUpGamePieces
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//

    public static enum ContainingPreload
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//

    public static enum AutonomousCommands
    {
        kNeither, kChargingStation, kTwoGamePieces
    }

    //-------------------------------------------------------------------//

    public static enum DriveToSecondPiece
    {
        kYes, kNo
    }

    //-------------------------------------------------------------------//

    public StartingLocation startingLocation = StartingLocation.kMiddle;
    public PlayPreload playPreload = PlayPreload.kYes;
    public MoveOntoChargingStation moveOntoChargingStation = MoveOntoChargingStation.kYes;
    public PickUpGamePieces pickUpGamePieces = PickUpGamePieces.kNo;
    public RowPlayedPiece1 rowPlayedPiece1 = RowPlayedPiece1.kBottom;
    public ColumnPlayedPiece1 columnPlayedPiece1 = ColumnPlayedPiece1.kLeft;
    public RowPlayedPiece2 rowPlayedPiece2 = RowPlayedPiece2.kNone;
    public ColumnPlayedPiece2 columnPlayedPiece2 = ColumnPlayedPiece2.kNone;
    public ContainingPreload containingPreload = ContainingPreload.kYes;
    public AutonomousCommands autonomousCommands = AutonomousCommands.kNeither;
    public DriveToSecondPiece driveToSecondPiece = DriveToSecondPiece.kNo;
    

    public String toString()
    {
        String str = "";

        str += "\n*****  AUTONOMOUS SELECTION  *****\n";
        str += "Starting Location           : "  + startingLocation   + "\n";
        str += "Move Onto Charging Station  : "  + moveOntoChargingStation   + "\n";
        str += " Are Game Pieces Played     :" + playPreload  + "\n";
        str += " Pick Up Game Pieces        :" + pickUpGamePieces  + "\n";
        str += " Row of First Game Piece    :" + rowPlayedPiece1  + "\n";
        str += " Column of First Game Piece :" + columnPlayedPiece1  + "\n";
        str += " Row of Second Game Piece   :" + rowPlayedPiece2  + "\n";
        str += " Column of Second Game Piece:" + columnPlayedPiece2  + "\n";
        str += " Containing Preload         :" + containingPreload + "\n";
        str += " Autonomous Commands        :" + autonomousCommands + "\n";
        str += " Drive to Second Piece      :" + autonomousCommands + "\n";


        return str;
    }

    public void updateData(AutonomousTabData atd)
    {
        startingLocation = atd.startingLocation;
        playPreload = atd.playPreload;
        moveOntoChargingStation = atd.moveOntoChargingStation;
        pickUpGamePieces = atd.pickUpGamePieces;
        rowPlayedPiece1 = atd.rowPlayedPiece1;
        columnPlayedPiece1 = atd.columnPlayedPiece1;
        rowPlayedPiece2 = atd.rowPlayedPiece2;
        columnPlayedPiece2 = atd.columnPlayedPiece2;
        containingPreload = atd.containingPreload;
        autonomousCommands = atd.autonomousCommands;
        driveToSecondPiece = atd.driveToSecondPiece;
        if(autonomousCommands == AutonomousCommands.kChargingStation)
        {
            moveOntoChargingStation = MoveOntoChargingStation.kYes;
            rowPlayedPiece2 = RowPlayedPiece2.kNone;
            columnPlayedPiece2 = ColumnPlayedPiece2.kNone;
            pickUpGamePieces = PickUpGamePieces.kNo;
        }
        if(autonomousCommands == AutonomousCommands.kTwoGamePieces)
        {
            moveOntoChargingStation = MoveOntoChargingStation.kNo;
            pickUpGamePieces = PickUpGamePieces.kYes;

        }
    }


}
