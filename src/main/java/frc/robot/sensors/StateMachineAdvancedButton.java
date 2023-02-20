package frc.robot.sensors;

public class StateMachineAdvancedButton 
{
    private enum State
    {
        kPressed
        {
            void doAction()
            {}
        }, 
        
        kStillPressed
        {
            void doAction()
            {}
        },
        
        kReleased
        {
            void doAction()
            {}
        },
        
        kStillReleased
        {
            void doAction()
            {}
        };

        abstract void doAction();
    }

    public enum Transition
    {
        
    }
}
