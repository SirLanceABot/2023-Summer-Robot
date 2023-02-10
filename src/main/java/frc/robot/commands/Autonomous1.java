package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;


public class Autonomous1 extends SequentialCommandGroup
{
   // private Drivetrain drivetrain;
    

    public Autonomous1(Drivetrain drivetrain)
    {
        //this.drivetrain = drivetrain;

        if (drivetrain != null)
        {
            addCommands(
                new StopDrive(drivetrain),
                new DriveDistanceAuto(drivetrain, 0.5, 0.0, 2.0),
                new DriveDistanceAuto (drivetrain, 0.0, 0.5, 1.0),
                new StopDrive(drivetrain),
                new PrintCommand("Autonomous 1 Done")
            );
        }
    }
}
