package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.ColorFlowAnimation;
import com.ctre.phoenix.led.FireAnimation;
import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.RgbFadeAnimation;
import com.ctre.phoenix.led.SingleFadeAnimation;
import com.ctre.phoenix.led.StrobeAnimation;
import com.ctre.phoenix.led.TwinkleOffAnimation;
import com.ctre.phoenix.led.TwinkleAnimation;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;
import com.ctre.phoenix.led.TwinkleAnimation.TwinklePercent;
import com.ctre.phoenix.led.TwinkleOffAnimation.TwinkleOffPercent;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants.CANbusConstants;

/**
 * Class containing one CANdle
 */
public class Candle4237 extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum ledStatus
    {
        kPurple, kYellow, kWhite, kAnimated, kOff;
    }

    public enum ledAnimation
    {
        kColorFlow, kFire, kLarson, kRainbow, kRgbFade, 
        kSingleFade, kStrobe, kTwinkle, kTwinkleOff, kDisabled;
    }

    public enum BlinkColor
    {
        kBlueBlink, kGreenBlink, kWhteBlink
    }

    private class PeriodicIO
    {
        // Inputs

        // Outputs
        private ledStatus status = ledStatus.kOff;
        private ledAnimation toAnimate = ledAnimation.kDisabled;
    }

    private PeriodicIO periodicIO = new PeriodicIO();
    private final CANdle candle = new CANdle(CANbusConstants.CANDLE_PORT, "rio");
    private final static int ledCount = 68; // CANdle = 8, LED Strip = 60, LED Strip + CANdle = 68
    private Animation animation = null;
    private final ArrayList<BlinkEvent> blinkEvents = new ArrayList<BlinkEvent>();
    private int blinkCounter = 0;

    public Candle4237()
    {
        periodicIO.status = ledStatus.kOff;
    }

    /**
     * Sets the LED(s) to cube purple
     */
    public void signalCube()
    {
        periodicIO.status = ledStatus.kPurple;
        periodicIO.toAnimate = ledAnimation.kDisabled;
    }

    /**
     * Sets the LEDs to cone yellow
     */
    public void signalCone()
    {
        periodicIO.status = ledStatus.kYellow;
        periodicIO.toAnimate = ledAnimation.kDisabled;
    }

    /**
     * Sets the LEDs to Bright Blinding White
     */
    public void signalReadyToDrop()
    {
        periodicIO.status = ledStatus.kWhite;
        periodicIO.toAnimate = ledAnimation.kDisabled;
    }

    /**
     * Turns off the LEDs
     */
    public void turnOffLight()
    {
        periodicIO.status = ledStatus.kOff;
        periodicIO.toAnimate = ledAnimation.kDisabled;
    }

    /**
     * Set the animation to whatever is passed to it
     */
    public void setAnimation(ledAnimation toAnimate)
    {
        periodicIO.toAnimate = toAnimate;
        switch (toAnimate)
        {
            case kColorFlow: 
                animation = new ColorFlowAnimation(0, 255, 46, 0, 0.7, ledCount, Direction.Forward);
                break;
            case kFire: 
                animation = new FireAnimation(0.5, 0.7, ledCount, 0.3, 0.25);
                break;
            case kLarson: 
                animation = new LarsonAnimation(128, 20, 70, 0, 0.4, ledCount, BounceMode.Front, 5);
                break;
            case kRainbow: 
                animation = new RainbowAnimation(1, 0.5, ledCount);
                break;
            case kRgbFade: 
                animation = new RgbFadeAnimation(0.4, 0.7, ledCount);
                break;
            case kSingleFade: 
                animation = new SingleFadeAnimation(50, 2, 200, 0, 0.5, ledCount);
                break;
            case kStrobe: 
                animation = new StrobeAnimation(150, 150, 0, 0, 0.1, ledCount);
                break;
            case kTwinkle: 
                animation = new TwinkleAnimation(0, 0, 255, 0, 0.3, ledCount, TwinklePercent.Percent30);
                break;
            case kTwinkleOff: 
                animation = new TwinkleOffAnimation(255, 0, 255, 0, 0.5, ledCount, TwinkleOffPercent.Percent76);
                break;
            case kDisabled: 
                animation = null;
                break;
        }

        if (toAnimate != ledAnimation.kDisabled)
            periodicIO.status = ledStatus.kAnimated;
    }

    /**
     * Moves the animation to the next one in the order
     */
    public void incrementAnimation()
    {
        switch (periodicIO.toAnimate)
        {
            case kColorFlow: 
                periodicIO.toAnimate = ledAnimation.kFire;
                break;
            case kFire: 
                periodicIO.toAnimate = ledAnimation.kLarson;
                break;
            case kLarson: 
                periodicIO.toAnimate = ledAnimation.kRainbow;
                break;
            case kRainbow: 
                periodicIO.toAnimate = ledAnimation.kRgbFade;
                break;
            case kRgbFade: 
                periodicIO.toAnimate = ledAnimation.kSingleFade;
                break;
            case kSingleFade: 
                periodicIO.toAnimate = ledAnimation.kStrobe;
                break;
            case kStrobe: 
                periodicIO.toAnimate = ledAnimation.kTwinkle;
                break;
            case kTwinkle: 
                periodicIO.toAnimate = ledAnimation.kTwinkleOff;
                break;
            case kTwinkleOff: 
                periodicIO.toAnimate = ledAnimation.kColorFlow;
                break;
            case kDisabled:  
                periodicIO.toAnimate = ledAnimation.kColorFlow;
                break;
            default:
                break;
        }
    }

    /**
     * Moves the animation to the previous one in the order
     */
    public void decrementAnimation()
    {
        switch (periodicIO.toAnimate)
        {
            case kColorFlow: 
                periodicIO.toAnimate = ledAnimation.kTwinkleOff;
                break;
            case kFire: 
                periodicIO.toAnimate = ledAnimation.kColorFlow;
                break;
            case kLarson: 
                periodicIO.toAnimate = ledAnimation.kFire;
                break;
            case kRainbow: 
                periodicIO.toAnimate = ledAnimation.kLarson;
                break;
            case kRgbFade: 
                periodicIO.toAnimate = ledAnimation.kRainbow;
                break;
            case kSingleFade: 
                periodicIO.toAnimate = ledAnimation.kRgbFade;
                break;
            case kStrobe: 
                periodicIO.toAnimate = ledAnimation.kSingleFade;
                break;
            case kTwinkle: 
                periodicIO.toAnimate = ledAnimation.kStrobe;
                break;
            case kTwinkleOff: 
                periodicIO.toAnimate = ledAnimation.kTwinkle;
                break;
            case kDisabled:  
                periodicIO.toAnimate = ledAnimation.kTwinkleOff;
                break;
            default:
                break;
        }
    }

    /**
     * Completely stops the current animation
     */
    public void stopAnimation()
    {
        periodicIO.toAnimate = ledAnimation.kDisabled;
        candle.animate(null, 0);
    }

    @Override
    public synchronized void readPeriodicInputs()
    {}

    int i;
    /**
     * Sets the color/animation of all LEDs based on the current status
     */
    @Override  
    public synchronized void writePeriodicOutputs() 
    {
        setAnimation(periodicIO.toAnimate);

        if (periodicIO.status != ledStatus.kAnimated)
            stopAnimation();

        switch (periodicIO.status)
        {
            case kPurple: 
                candle.setLEDs(255, 0, 255, 50, 0, ledCount);
                break;
            case kYellow: 
                candle.setLEDs(255, 185, 0, 50, 0, ledCount);
                break; 
            case kWhite: 
                candle.setLEDs(255, 255, 220, 255, 0, ledCount);
                break;
            case kOff: 
                candle.setLEDs(0, 0, 0, 0, 0, ledCount);
                break;
            case kAnimated: 
                candle.animate(animation, 0);
                break;
            default:
                candle.setLEDs(0, 0, 0, 0, 0, ledCount);
                stopAnimation();
                break;
        }

        if (periodicIO.toAnimate != ledAnimation.kDisabled)
            periodicIO.status = ledStatus.kAnimated;
    }    
    public class BlinkEvent
    {
        public double startBlinkTime;
        public double blinkDuration;
        public Enum BlinkColor;
        public BlinkEvent(double startBlinkTime, double blinkDuration, Enum BlinkColor) 
        {
            this.startBlinkTime = startBlinkTime;
            this.blinkDuration = blinkDuration;
            this.BlinkColor = BlinkColor;
        }
        public int compareTo(BlinkEvent blinkEvent)
        {
            if (startBlinkTime > blinkEvent.startBlinkTime)
                return 1;
            else if (startBlinkTime < blinkEvent.startBlinkTime)
                return -1;
            else 
            {
                if (blinkDuration > blinkEvent.blinkDuration)
                    return 1;
                else if (blinkDuration < blinkEvent.blinkDuration)
                    return -1;
                else
                    return 0;
            }
        }
        public String toString()
        {
            String str = "";

            str += startBlinkTime + " ";
            str += blinkDuration + " ";
            str += BlinkColor + " ";
            return str;
        }
    }
    

       
public void createBlinkEvent(double startBlinkTime, double blinkDuration, Enum BlinkColor)
    {
        boolean isNoOverlap = true;
        double endTime = startBlinkTime - blinkDuration;
        double reEndTime = 0;
        int fail = 0;

        for (BlinkEvent blinkEvent : blinkEvents)
        {
            reEndTime = blinkEvent.startBlinkTime - blinkEvent.blinkDuration;
            if (blinkEvent.startBlinkTime >= startBlinkTime && startBlinkTime > reEndTime)
            {   
                isNoOverlap = false;
                fail = 1;
            }    
            else if (blinkEvent.startBlinkTime > endTime && endTime > reEndTime)
            {   
                isNoOverlap = false;
                fail = 2;
            }  
            else if (startBlinkTime >= blinkEvent.startBlinkTime && blinkEvent.startBlinkTime > endTime)
            {   
                isNoOverlap = false;
                fail = 3;
            }  
            else if (startBlinkTime > reEndTime && reEndTime > endTime)
            {   
                isNoOverlap = false;
                fail = 4;
            }  
        }

        if (isNoOverlap)
        {
            blinkEvents.add(new BlinkEvent(startBlinkTime, blinkDuration, BlinkColor));
            blinkCounter++;
            Collections.sort(blinkEvents, Collections.reverseOrder());
            // System.out.println("Rumble Event Created: " + fail + " " + startTime + " " + duration + " " + leftPower + " " + rightPower);
        }
        else 
        {
            System.out.println("Rumble Event Overlap: " + fail + " " + startBlinkTime + " " + blinkDuration + " " + BlinkColor);
        }
    }

    public void checkBlinkEvent()
    {
        if (blinkEvents.size() > blinkCounter)
        {
            double matchTime = DriverStation.getMatchTime();
            double startTime = blinkEvents.get(blinkCounter).startBlinkTime;
            double duration = blinkEvents.get(blinkCounter).blinkDuration;

            if (startTime >= matchTime && matchTime > startTime - duration)
            {
                periodicIO.toAnimate = ledAnimation.kDisabled;
                candle.setLEDs(0, 0, 255, 255, 0, ledCount);
            }
            else if (matchTime <= startTime - duration)
            {
                blinkCounter++;
            }
        }
        else if(blinkEvents.size() <= blinkCounter || DriverStation.isDisabled() &&blinkCounter > 0)
        {
            resetBlinkCounter();
        }
    }

    public void resetBlinkCounter()
    {
        blinkCounter = 0;
    }
    }

