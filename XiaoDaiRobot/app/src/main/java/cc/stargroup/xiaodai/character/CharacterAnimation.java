package cc.stargroup.xiaodai.character;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.Timer;

/**
 * Created by Foam on 2017/1/11.
 */

public class CharacterAnimation {
    

    private boolean animating;
    private int breakpointFrame;

    private int frameCount;
    private int accumulatedFrames;

    private Timer timer;
    private Date startTime;
    private Date endTime;

    private boolean reversed = false;
    private String prefix;
    private int index;

    public CharacterAnimation() {

    }

    public void startAnimating() {
        this.animating = true;
    }

    public void stopAnimating() {
        this.animating = false;
    }

    public void nextFrame() {

    }

    /**
     Some expressions don't include a blink-through transition for either the intro, outro, or both
     And instead, they reuse the blink-through transition from an emotion
     e.g. Curious expression should use Curious emotion's blink-through for both intro & outro
     */
    public String prefixWithActionForExpression(AnimatedAction action, CharacterExpression expression) {
        if (action == AnimatedAction.Intro || action == AnimatedAction.Outro) {
            switch (expression) {
                case Angry:
                    return "";

                case Bored:
                    return "";

                case Chuckle:
                    return "";

                case Curious:
                    return "Romo_Emotion_Transition_1";

                case Dizzy:
                    return "";

                case Embarrassed:
                    return "";

                case Excited:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_2";

                case Exhausted:
                    return "";

                case Fart:
                    if (action == AnimatedAction.Outro) return "";
                    return "Romo_Emotion_Transition_3";

                case Happy:
                case Proud:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_3";

                case Hiccup:
                    return "";

                case Bewildered:
                    return "Romo_Emotion_Transition_9";

                case HoldingBreath:
                    return "";

                case Laugh:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_2";

                case LookingAround:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_1";

                case Love:
                    return "";

                case Ponder:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_1";

                case Sad:
                case LetDown:
                    return "Romo_Emotion_Transition_4";

                case Scared:
                    return "Romo_Emotion_Transition_5";

                case Sleepy:
                    return "Romo_Emotion_Transition_6";

                case Sneeze:
                    return "";

                case Talking:
                    return "";

                case Want:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_10";

                case Yawn:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_6";

                case Yippee:
                    return "";

                default:
                    return "";
            }
        }
        return "";
    }

    private Bitmap spriteSheetWithPrefix(int index, String prefix) {
        if (index == 1) {
            frameCount = 0;

            while (true) {

            }
        }
    }

    public void animateWithActionForEmotion(AnimatedAction action, CharacterEmotion emotion) {
        reversed = false;
        switch (action) {
            case Outro:
                reversed = true;
            case Intro:
                if (emotion != CharacterEmotion.Sleeping) {
                    prefix = String.format("Romo_Emotion_Transition_%d", emotion.getValue());
                } else {
                    return;
                }
                break;

            case IdleEmotion:
                break;

            case Blink:

                prefix = String.format("Romo_Emotion_Blink_%d", emotion.getValue());
                break;

            default:
                return;
        }
        this.startAnimating();
    }

    public void animateWithActionForExpression(AnimatedAction action, CharacterExpression expression) {
        switch (action) {
            case Outro:
                reversed = true;
            case Intro:
                this.prefix = this.prefixWithActionForExpression(action, expression);
                if (prefix.length() == 0) {
                    return;
                }
                break;

            case Expression:
                this.prefix = String.format("Romo_Expression_%d", expression.getValue());
                break;

            default:
                return;
        }
        this.startAnimating();
    }

    public void completion() {

    }
}
