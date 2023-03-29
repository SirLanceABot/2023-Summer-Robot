package frc.robot.commands;

import frc.robot.commands.SuctionControl.vacuumState;
import frc.robot.subsystems.VacuumPump;
import frc.robot.subsystems.VacuumPumpConfig;


import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** 
 * An example command that uses an example subsystem. 
 */
class VacuumPumpControl extends CommandBase 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum VacuumState
    {
        kMakeVacuum, kHoldVacuum;
    }

    public enum MotorState
    {
        kOff, kRun, kFast;
    }

    private final VacuumPump vacuumPump;
    private VacuumState vacuumState;
    private MotorState motorState;
    private double motorSpeed;
    /**
     * Creates a new Vacuum
     *
     * @param grabber The grabber subsystem.
     */
    VacuumPumpControl(VacuumPump vacuumPump) 
    {

        this.vacuumPump = vacuumPump;

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
        vacuumState = VacuumState.kMakeVacuum; // we start off very fast.
        motorState = MotorState.kFast;
        motorSpeed = vacuumPump.asFastAsPossible;
        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        switch (vacuumState)
        {

        case kMakeVacuum:
        
            switch (motorState)
            {
            case kFast:
                if(vacuumPump.getVelocity() >= vacuumPump.speedLimit) //then check if ur speed is bigger than the # u wanna be going
                {
                    motorState = MotorState.kRun; //then slow down
                    motorSpeed = vacuumPump.maintainSpeedLimit; //to this speed
                }
                break;
            case kRun:
                if(vacuumPump.getVoltage() <= vacuumPump.setPoint) //check if we're at setpoint
                {
                    motorState = MotorState.kOff; //if we are, turn motor off
                    motorSpeed = 0;
                }
                break;
            case kOff:
                if(vacuumPump.getVoltage() <= vacuumPump.setPoint) //going to check again if still off, as long as setpoints good still
                {
                    vacuumState = VacuumState.kHoldVacuum;//we go to hold vacuum
                }
                else
                {
                    motorState = MotorState.kFast; //else if we don't have setpoint anymore
                    motorSpeed = vacuumPump.asFastAsPossible; // we go fast again
                }
                break;
            }
            
            vacuumPump.set(motorSpeed);
            break;

        case kHoldVacuum:
            if (vacuumPump.getVoltage() > vacuumPump.lowPressure)
            {
                vacuumState = VacuumState.kMakeVacuum;
                motorState = MotorState.kFast;
                motorSpeed = vacuumPump.asFastAsPossible;
            }
            vacuumPump.set(0.0);
            break;
        }

        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        motorSpeed = 0;
        motorState = MotorState.kOff;
        vacuumPump.set(motorSpeed);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        return false;
    }
    
    @Override
    public boolean runsWhenDisabled()
    {
        return false;
    }

    @Override
    public String toString()
    {
        String str = this.getClass().getSimpleName();
        return String.format("Command: %s( )", str);
    }
}

