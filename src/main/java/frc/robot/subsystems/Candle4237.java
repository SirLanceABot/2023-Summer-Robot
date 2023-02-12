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

    public enum LedStatus
    {
        kPurple, kYellow, kWhite, kAnimated, kOff, kBlueBlink, kGreenBlink, kWhteBlink;
    }

    // public enum LedAnimation
    // {
    //     kColorFlow, kFire, kLarson, kRainbow, kRgbFade, 
    //     kSingleFade, kStrobe, kTwinkle, kTwinkleOff, kDisabled;
    // }

    // public enum BlinkColor
    // {
        
    // }
    public class BlinkEvent implements Comparable<BlinkEvent>
    {
        public double startBlinkTime;
        public double blinkDuration;
        public LedStatus blinkColor;
        public BlinkEvent(double startBlinkTime, double blinkDuration, LedStatus blinkColor) 
        {
            this.startBlinkTime = startBlinkTime;
            this.blinkDuration = blinkDuration;
            this.blinkColor = blinkColor;
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
            str += blinkColor + " ";
            return str;
        }
    }
    public enum LedAnimation
    {
        kColorFlow, kFire, kLarson, kRainbow, kRgbFade, kSingleFade, kStrobe, kTwinkle, kTwinkleOff, kDisabled;

        private static final LedAnimation[] vals = values();

        public LedAnimation next()
        {
            System.out.println("Next Animation" + vals[(this.ordinal() + 1) % vals.length]);
            return vals[(this.ordinal() + 1) % vals.length];
        }
        public LedAnimation prev()
        {
            return vals[(this.ordinal() - 1 + vals.length) % vals.length];
        }
    }
    private class PeriodicIO
    {
        // Inputs

        // Outputs
        private LedStatus status = LedStatus.kOff;
        private LedAnimation toAnimate = LedAnimation.kDisabled;
    }

    private PeriodicIO periodicIO = new PeriodicIO();
    private final CANdle candle = new CANdle(CANbusConstants.CANDLE_PORT, "rio");
    private final static int ledCount = 68; // CANdle = 8, LED Strip = 60, LED Strip + CANdle = 68
    private Animation animation = null;
    private final ArrayList<BlinkEvent> blinkEvents = new ArrayList<BlinkEvent>();
    private int blinkCounter = 0;

    public Candle4237()
    {
        System.out.println(fullClassName + " : Constructor Started");
        periodicIO.status = LedStatus.kOff;
        createBlinkEvents();
        System.out.println(fullClassName + ": Constructor Finished");
    }

    /**
     * Sets the LED(s) to cube purple
     */
    public void signalCube()
    {
        periodicIO.status = LedStatus.kPurple;
        periodicIO.toAnimate = LedAnimation.kDisabled;
    }

    /**
     * Sets the LEDs to cone yellow
     */
    public void signalCone()
    {
        periodicIO.status = LedStatus.kYellow;
        periodicIO.toAnimate = LedAnimation.kDisabled;
    }

    /**
     * Sets the LEDs to Bright Blinding White
     */
    public void signalReadyToDrop()
    {
        periodicIO.status = LedStatus.kWhite;
        periodicIO.toAnimate = LedAnimation.kDisabled;
    }

    /**
     * Turns off the LEDs
     */
    public void turnOffLight()
    {
        periodicIO.status = LedStatus.kOff;
        periodicIO.toAnimate = LedAnimation.kDisabled;
    }

    /**
     * Set the animation to whatever is passed to it
     */
    private void setAnimation()
    {
        switch (periodicIO.toAnimate)
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
        candle.animate(animation, 0);
    }

    /**
     * Moves the animation to the next one in the order
     */
    // public void incrementAnimation()
    // {
    //     periodicIO.status = LedStatus.kAnimated;
    //     switch (periodicIO.toAnimate)
    //     {
    //         case kColorFlow: 
    //             periodicIO.toAnimate = LedAnimation.kFire;
    //             break;
    //         case kFire: 
    //             periodicIO.toAnimate = LedAnimation.kLarson;
    //             break;
    //         case kLarson: 
    //             periodicIO.toAnimate = LedAnimation.kRainbow;
    //             break;
    //         case kRainbow: 
    //             periodicIO.toAnimate = LedAnimation.kRgbFade;
    //             break;
    //         case kRgbFade: 
    //             periodicIO.toAnimate = LedAnimation.kSingleFade;
    //             break;
    //         case kSingleFade: 
    //             periodicIO.toAnimate = LedAnimation.kStrobe;
    //             break;
    //         case kStrobe: 
    //             periodicIO.toAnimate = LedAnimation.kTwinkle;
    //             break;
    //         case kTwinkle: 
    //             periodicIO.toAnimate = LedAnimation.kTwinkleOff;
    //             break;
    //         case kTwinkleOff: 
    //             periodicIO.toAnimate = LedAnimation.kColorFlow;
    //             break;
    //         case kDisabled:  
    //             periodicIO.toAnimate = LedAnimation.kColorFlow;
    //             break;
    //         default:
    //             break;
    //     }
       
    // }

    // // /**
    // //  * Moves the animation to the previous one in the order
    // //  */
    // public void decrementAnimation()
    // {
    //     periodicIO.status = LedStatus.kAnimated;
    //     switch (periodicIO.toAnimate)
    //     {
    //         case kColorFlow: 
    //             periodicIO.toAnimate = LedAnimation.kTwinkleOff;
    //             break;
    //         case kFire: 
    //             periodicIO.toAnimate = LedAnimation.kColorFlow;
    //             break;
    //         case kLarson: 
    //             periodicIO.toAnimate = LedAnimation.kFire;
    //             break;
    //         case kRainbow: 
    //             periodicIO.toAnimate = LedAnimation.kLarson;
    //             break;
    //         case kRgbFade: 
    //             periodicIO.toAnimate = LedAnimation.kRainbow;
    //             break;
    //         case kSingleFade: 
    //             periodicIO.toAnimate = LedAnimation.kRgbFade;
    //             break;
    //         case kStrobe: 
    //             periodicIO.toAnimate = LedAnimation.kSingleFade;
    //             break;
    //         case kTwinkle: 
    //             periodicIO.toAnimate = LedAnimation.kStrobe;
    //             break;
    //         case kTwinkleOff: 
    //             periodicIO.toAnimate = LedAnimation.kTwinkle;
    //             break;
    //         case kDisabled:  
    //             periodicIO.toAnimate = LedAnimation.kTwinkleOff;
    //             break;
    //         default:
    //             break;
    //     }
        
    // }
    
    public void incrementAnimation()  
    {   
        periodicIO.status = LedStatus.kAnimated;
        periodicIO.toAnimate = periodicIO.toAnimate.next();
        if(periodicIO.toAnimate == LedAnimation.kDisabled)
            periodicIO.toAnimate = periodicIO.toAnimate.next();
    }
    public void decrementAnimation()  
    {
        periodicIO.status = LedStatus.kAnimated;
        periodicIO.toAnimate = periodicIO.toAnimate.prev();
        if(periodicIO.toAnimate == LedAnimation.kDisabled)
            periodicIO.toAnimate = periodicIO.toAnimate.prev();
    }



    private void setColor()
    {
        switch (periodicIO.status)
        {
            case kPurple: 
                candle.setLEDs(255, 0, 255, 50, 0, ledCount);
                break;
            case kYellow: 
                candle.setLEDs(255, 185, 0, 50, 0, ledCount);
                break; 
            case kWhite: 
                candle.setLEDs(255, 255, 200, 255, 0, ledCount);
                break;
            case kOff: 
                candle.setLEDs(0, 0, 0, 0, 0, ledCount);
                break;
            case kAnimated: 
                // candle.animate(animation, 0);
                break;
            case kBlueBlink: 
                candle.setLEDs(0, 0, 255, 255, 0, ledCount);
                break;
            case kGreenBlink: 
                candle.setLEDs(0, 255, 0, 50, 0, ledCount);
                break;
            case kWhteBlink:
                candle.setLEDs(255, 255, 200, 50, 0, ledCount);
                break;
            default:
                candle.setLEDs(0, 0, 0, 0, 0, ledCount);
                stopAnimation();
                break;
        }
    }
    

    /**
     * Completely stops the current animation
     */
    private void stopAnimation()
    {
        periodicIO.toAnimate = LedAnimation.kDisabled;
        candle.animate(null, 0);
    }

    private void stopColor()
    {
        periodicIO.status = LedStatus.kOff;
        candle.setLEDs(0, 0, 0, 0, 0, ledCount);
    }

    @Override
    public synchronized void readPeriodicInputs()
    {}

    /**
     * Sets the color/animation of all LEDs based on the current status
     */
    @Override  
    public synchronized void writePeriodicOutputs() 
    {
        checkBlinkEvent();
        if(periodicIO.status != LedStatus.kOff && periodicIO.status != LedStatus.kAnimated)
        {
            stopAnimation();
            setColor();
        }
        else if(periodicIO.status == LedStatus.kAnimated)
        {
            setAnimation();
        }
        else
        {
            stopAnimation();
            stopColor();
        }
        // else if (periodicIO.status != LedStatus.kAnimated)
        // {    
        //     stopAnimation();
        // }
        // if (periodicIO.toAnimate != LedAnimation.kDisabled)
        //     periodicIO.status = LedStatus.kAnimated;
    }    
    
    
    public void createBlinkEvents()
    {
        createBlinkEvent(15.0, 2.0, LedStatus.kBlueBlink);
    }
    public void createBlinkEvent(double startBlinkTime, double blinkDuration, LedStatus blinkColor)
    {
        boolean isNoOverlap = true;
        double endTime = startBlinkTime - blinkDuration;
        double beEndTime = 0;
        int fail = 0;

        for (BlinkEvent blinkEvent : blinkEvents)
        {
            beEndTime = blinkEvent.startBlinkTime - blinkEvent.blinkDuration;
            if (blinkEvent.startBlinkTime >= startBlinkTime && startBlinkTime > beEndTime)
            {   
                isNoOverlap = false;
                fail = 1;
            }    
            else if (blinkEvent.startBlinkTime > endTime && endTime > beEndTime)
            {   
                isNoOverlap = false;
                fail = 2;
            }  
            else if (startBlinkTime >= blinkEvent.startBlinkTime && blinkEvent.startBlinkTime > endTime)
            {   
                isNoOverlap = false;
                fail = 3;
            }  
            else if (startBlinkTime > beEndTime && beEndTime > endTime)
            {   
                isNoOverlap = false;
                fail = 4;
            }  
        }

        if (isNoOverlap)
        {
            blinkEvents.add(new BlinkEvent(startBlinkTime, blinkDuration, blinkColor));
            blinkCounter++;
            Collections.sort(blinkEvents, Collections.reverseOrder());
        }
        else 
        {
            System.out.println("Blink Event Overlap: " + fail + " " + startBlinkTime + " " + blinkDuration + " " + blinkColor);
        }
    }

    public void checkBlinkEvent()
    {
        if (DriverStation.isEnabled() && blinkEvents.size() > blinkCounter)
        {
            double matchTime = DriverStation.getMatchTime();
            double startTime = blinkEvents.get(blinkCounter).startBlinkTime;
            double duration = blinkEvents.get(blinkCounter).blinkDuration;

            if (startTime >= matchTime && matchTime > startTime - duration)
            {
                if (periodicIO.status == LedStatus.kOff)
                {
                    periodicIO.toAnimate = LedAnimation.kDisabled;
                    periodicIO.status = blinkEvents.get(blinkCounter).blinkColor;
                }
            }
            else if (matchTime <= startTime - duration)
            {
                blinkCounter++;
                periodicIO.status = LedStatus.kOff;
            }
        }
        else if(DriverStation.isDisabled() && blinkCounter > 0)
        {
            resetBlinkCounter();
        }
    }

    public void resetBlinkCounter()
    {
        blinkCounter = 0;
    }
    }

