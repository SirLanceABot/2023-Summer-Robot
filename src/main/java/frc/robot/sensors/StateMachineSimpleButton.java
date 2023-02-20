package frc.robot.sensors;

public class StateMachineSimpleButton extends Sensor4237
{

    // https://micromouseonline.com/2014/05/05/state-machines-introduction/
    private enum ButtonState
    {
        kPressed, kStillPressed, kReleased, kStillReleased;
    }

    private ButtonState buttonState = ButtonState.kStillReleased;
    
    public StateMachineSimpleButton()
    {

    }

    @Override
    public void readPeriodicInputs() 
    {
        boolean isButtonPressedRightNow = true;
        switch(buttonState)
        {
            case kStillReleased:
                if(isButtonPressedRightNow)
                {
                    buttonState = ButtonState.kPressed;
                }
                break;
            case kPressed:
                if(isButtonPressedRightNow)
                    buttonState = ButtonState.kStillPressed;
                else
                    buttonState = ButtonState.kReleased;
                break;
            case kStillPressed:
                if(!isButtonPressedRightNow)
                    buttonState = ButtonState.kReleased;
                break;
            case kReleased:
                if(!isButtonPressedRightNow)
                    buttonState = ButtonState.kStillReleased;
                else
                    buttonState = ButtonState.kPressed;
                break;
        }
    }

    @Override
    public void writePeriodicOutputs() 
    {
        
    }
}
