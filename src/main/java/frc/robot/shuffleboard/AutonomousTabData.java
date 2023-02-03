package frc.robot.shuffleboard;

public class AutonomousTabData 
{
    public static enum StartingLocation
    {
        kLeft, kMiddle, kRight;
    }

    //-------------------------------------------------------------------//

    public static enum GamePiecesPlayed
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//


    public static enum RowPlayedPiece1
    {
        k1(1), k2(2), k3(3);
        
        public int value;

        private RowPlayedPiece1(int value)
        {
            this.value = value;
        }
    }

    //-------------------------------------------------------------------//

    public static enum ColumnPlayedPiece1
    {
        k1(1), k2(2), k3(3);
        
        public int value;

        private ColumnPlayedPiece1(int value)
        {
            this.value = value;
        }
    }

    //-------------------------------------------------------------------//

    public static enum RowPlayedPiece2
    {
        k1(1), k2(2), k3(3);
        
        public int value;

        private RowPlayedPiece2(int value)
        {
            this.value = value;
        }
    }

    //-------------------------------------------------------------------//

    public static enum ColumnPlayedPiece2
    {
        k1(1), k2(2), k3(3);
        
        public int value;

        private ColumnPlayedPiece2(int value)
        {
            this.value = value;
        }
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

    public static enum CurrentlyContainingGamePiece
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//

    public StartingLocation startingLocation = StartingLocation.kMiddle;
    public GamePiecesPlayed gamePiecesPlayed = GamePiecesPlayed.kYes;
    public MoveOntoChargingStation moveOntoChargingStation = MoveOntoChargingStation.kYes;
    public PickUpGamePieces pickUpGamePieces = PickUpGamePieces.kNo;
    public RowPlayedPiece1 rowPlayedPiece1 = RowPlayedPiece1.k1;
    public ColumnPlayedPiece1 columnPlayedPiece1 = ColumnPlayedPiece1.k1;
    public RowPlayedPiece2 rowPlayedPiece2 = RowPlayedPiece2.k1;
    public ColumnPlayedPiece2 columnPlayedPiece2 = ColumnPlayedPiece2.k1;
    public CurrentlyContainingGamePiece currentlyContainingGamePiece = CurrentlyContainingGamePiece.kYes;

    public String toString()
    {
        String str = "";

        str += "\n*****  AUTONOMOUS SELECTION  *****\n";
        str += "Starting Location     : "  + startingLocation   + "\n";
        str += "Move Onto Charging Station           : "  + moveOntoChargingStation   + "\n";
        str += " Game Pieces Played             :" + gamePiecesPlayed  + "\n";
        str += " Pick Up Game Pieces             :" + pickUpGamePieces  + "\n";
        str += " Row of First Game Piece             :" + rowPlayedPiece1  + "\n";
        str += " Column of First Game Piece             :" + columnPlayedPiece1  + "\n";
        str += " Row of Second Game Piece             :" + rowPlayedPiece2  + "\n";
        str += " Column of Second Game Piece             :" + columnPlayedPiece2  + "\n";
        str += " Does the Robot Currently Contain a Game Piece             :" + currentlyContainingGamePiece + "\n";

        return str;
    }

    public void updateData(AutonomousTabData atd)
    {
        startingLocation = atd.startingLocation;
        moveOntoChargingStation = atd.moveOntoChargingStation;
        gamePiecesPlayed = atd.gamePiecesPlayed;
        pickUpGamePieces = atd.pickUpGamePieces;
        rowPlayedPiece1 = atd.rowPlayedPiece1;
        columnPlayedPiece1 = atd.columnPlayedPiece1;
        rowPlayedPiece2 = atd.rowPlayedPiece2;
        columnPlayedPiece2 = atd.columnPlayedPiece2;
        currentlyContainingGamePiece = atd.currentlyContainingGamePiece;
    }


}
