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
        k0(0), k1(1), k2(2);
        
        public int value;

        private GamePiecesPlayed(int value)
        {
            this.value = value;
        }
    }

    //-------------------------------------------------------------------//

    public static enum RowPlayed
    {
        k1(1), k2(2), k3(3);
        
        public int value;

        private RowPlayed(int value)
        {
            this.value = value;
        }
    }

    //-------------------------------------------------------------------//

    public static enum ColumnPlayed
    {
        k1(1), k2(2), k3(3);
        
        public int value;

        private ColumnPlayed(int value)
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

    public StartingLocation startingLocation = StartingLocation.kMiddle;
    public GamePiecesPlayed gamePiecesPlayed = GamePiecesPlayed.k1;
    public MoveOntoChargingStation moveOntoChargingStation = MoveOntoChargingStation.kYes;
    public PickUpGamePieces pickUpGamePieces = PickUpGamePieces.kNo;
    public RowPlayed rowPlayed = RowPlayed.k1;
    public ColumnPlayed columnPlayed = ColumnPlayed.k1;

    public String toString()
    {
        String str = "";

        str += "\n*****  AUTONOMOUS SELECTION  *****\n";
        str += "Starting Location     : "  + startingLocation   + "\n";
        str += "Game Pieces Played   : "  + gamePiecesPlayed  + "\n";
        str += "Move Onto Charging Station           : "  + moveOntoChargingStation   + "\n";
        // str += "Shoot Delay           : "  + shootDelay         + "\n";
        // str += "Move Off Tarmac       : "  + moveOffTarmac      + "\n";
        // str += "Move Delay            : "  + moveDelay          + "\n";
        // str += "Pick Up Cargo         : "  + pickUpCargo        + "\n";
        str += "\n";

        return str;
    }


}
