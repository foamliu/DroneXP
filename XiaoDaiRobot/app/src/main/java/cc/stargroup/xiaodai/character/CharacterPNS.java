package cc.stargroup.xiaodai.character;

/**
 * Created by Foam on 2017/1/19.
 */

public class CharacterPNS {
    public final static float doubleBlinkPercentage = 28.0f;

    public final static float blinkMu       = 6.0f;
    public final static float blinkSigma    = 4.0f;
    public final static boolean blinkEnable   = true;

    public final static float lookMu        = 15.0f;
    public final static float lookSigma     = 4.0f;
    public final static boolean lookEnable    = true;

    public final static float breatheMu     = 10.0f;
    public final static float breatheSigma  = 2.0f;
    public final static boolean breatheEnable = false;

    public CharacterPNS() {

    }

    public void reset() {
        this.stop();

        this.resetBlink();
        this.resetLook();
        this.resetBreathe();
    }

    public void stop() {

    }

    public void triggerBlink() {

    }

    public void triggerLook() {

    }

    public void triggerBreathe() {

    }

    public void resetBlink() {
        if (blinkEnable) {

        }
    }

    public void resetLook() {
        if (lookEnable) {

        }
    }

    public void resetBreathe() {
        if (breatheEnable) {

        }
    }
}
