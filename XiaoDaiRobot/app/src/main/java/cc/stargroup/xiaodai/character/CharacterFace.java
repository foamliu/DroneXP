package cc.stargroup.xiaodai.character;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by Foam on 2017/1/10.
 */

public class CharacterFace {

    private CharacterAnimation animation;
    private CharacterFaceEmotion faceEmotion;

    private Context appContext;
    private CharacterType characterType;
    private CharacterEmotion emotion;
    private CharacterExpression expression;
    private CharacterExpression queuedExpression;
    private CharacterEmotion queuedEmotion;
    private float pupilDilation;
    private float rotation;
    private boolean emoting;
    private boolean expressing;

    public CharacterFace(Context context, CharacterType characterType) {
        this.appContext = context;
        this.characterType = characterType;

        this.animation = new CharacterAnimation(context);
        this.faceEmotion = new CharacterFaceEmotion(context);
        this.faceEmotion.setEmotion(CharacterEmotion.Scared);
    }

    public void setEmotion(CharacterEmotion emotion) {

        if (!this.emoting && !this.expressing && (emotion != this.emotion)) {
            // If we aren't mid-animation and the character is visible
            this.emoting = true;

            this.animation.animateWithActionForEmotion(
                    AnimatedAction.Outro,
                    emotion,
                    new CompletionCallback() {
                        @Override
                        public void OnCompletion(boolean finished) {
                            animationDidFinish();
                        }
                    });

        } else if (this.emoting || this.expressing) {
            // If we're currently animating, queue this as the final desired emotion
            queuedEmotion = emotion;

        }
    }

    public void setExpressionWithEmotion(final CharacterExpression expression, CharacterEmotion emotion) {

        if (expression == CharacterExpression.None) {
            // If we aren't given an expression, simply change emotions
            this.emotion = emotion;

        } else if (!this.emoting && !this.expressing) {
            this.expressing = true;
            CharacterEmotion startEmotion = this.emotion;
            final CharacterEmotion finalEmotion = emotion;

            this.animation.animateWithActionForExpression(
                    AnimatedAction.Expression,
                    expression,
                    new CompletionCallback() {
                        @Override
                        public void OnCompletion(boolean finished) {
                            // Check if we need to animate out then back in to the emotion, or not
                            boolean needsOutroToEmotion = expressionEndsWithEmotion(expression, finalEmotion);
                            if (needsOutroToEmotion) {
                                animation.animateWithActionForExpression(
                                        AnimatedAction.Outro,
                                        expression,
                                        new CompletionCallback() {
                                            @Override
                                            public void OnCompletion(boolean finished) {
                                                immediateTransitionToEmotion(finalEmotion);
                                            }
                                        });
                             } else {
                                immediateTransitionToEmotion(finalEmotion);
                            }
                        }
                    });

        } else if (this.emoting && this.expressing) {
            queuedEmotion = emotion;

        } else if (this.emoting) {
            queuedExpression = expression;
            queuedEmotion = emotion;

        }
    }

    public void setRotation(float rotation) {
        boolean shouldAnimate = false;
        if (Math.abs(this.rotation - rotation) > 3) {
            shouldAnimate = true;
        }
        this.rotation = rotation;
    }

    public void lookAtPoint(Point3D point, boolean animated) {

    }

    public void lookAtDefault() {

    }

    public void blink() {

    }

    public void doubleBlink() {

    }

    public void drawSelf(Canvas canvas) {
        if (emoting || expressing) {
            animation.drawSelf(canvas);
        } else {
            faceEmotion.drawSelf(canvas);
        }
    }

    // Some expressions began in an emotion and don't need an animated transition
    private boolean expressionStartsWithEmotion(CharacterExpression expression, CharacterEmotion emotion)
    {
        switch (expression) {
            case Curious:
            case LookingAround:
            case Ponder:
                return (emotion == CharacterEmotion.Curious);

            case Sad:
                return (emotion == CharacterEmotion.Sad);

            case Scared:
                return (emotion == CharacterEmotion.Scared);

            case Sleepy:
                return (emotion == CharacterEmotion.Sleepy);

            case Fart:
            case Wee:
            case Struggling:
                return (emotion == CharacterEmotion.Happy);

            case Bewildered:
                return (emotion == CharacterEmotion.Bewildered);

            default:
                break;
        }

        if (expression.getValue() >= 100) {
            return (emotion == CharacterEmotion.Happy);
        }

        return false;
    }

    // Some expressions end in an emotion and don't need an animated transition
    private boolean expressionEndsWithEmotion(CharacterExpression expression, CharacterEmotion emotion)
    {
        switch (expression) {
            case Curious:
            case LookingAround:
            case Ponder:
                return (emotion == CharacterEmotion.Curious);

            case Excited:
            case Laugh:
            case Chuckle:
            case Proud:
                return (emotion == CharacterEmotion.Excited);

            case Happy:
                return (emotion == CharacterEmotion.Happy);

            case Sad:
                return (emotion == CharacterEmotion.Sad);

            case Scared:
                return (emotion == CharacterEmotion.Scared);

            case Sleepy:
            case Yawn:
                return (emotion == CharacterEmotion.Sleepy);

            case Hiccup:
                return (emotion == CharacterEmotion.Indifferent);

            case Bewildered:
                return (emotion == CharacterEmotion.Bewildered);

            case Yippee:
                return (emotion == CharacterEmotion.Delighted);

            default:
                break;
        }

        if (expression.getValue() >= 100) {
            return (emotion == CharacterEmotion.Happy);
        }

        return false;
    }

    // Transitions into the modular emotion using the emotion's intro
    public void transitionToEmotion(final CharacterEmotion emotion)
    {
        this.emotion = emotion;

        // Intro into final emotion
        this.animation.animateWithActionForEmotion(
                AnimatedAction.Intro,
                emotion,
                new CompletionCallback() {
                    @Override
                    public void OnCompletion(boolean finished) {
                        immediateTransitionToEmotion(emotion);
                    }
                });
    }

    // Immediately displays the modular emotion
    public void immediateTransitionToEmotion(CharacterEmotion emotion)
    {
        this.emotion = emotion;
        this.faceEmotion.setEmotion(emotion);

        // This will notify delegate and call any queued emotions
        this.animationDidFinish();
    }

    private void animationDidStart()
    {
    }

    private void animationDidFinish()
    {
        this.emoting = false;
        this.expressing = false;
        this.expression = CharacterExpression.None;

        if (queuedEmotion != null && queuedExpression != null) {
            this.setExpressionWithEmotion(queuedExpression, queuedEmotion);
            queuedExpression = null;
            queuedEmotion = null;
        } else if (queuedExpression != null) {
            this.expression = queuedExpression;
            queuedExpression = null;
        } else if (queuedEmotion != null) {
            this.emotion = queuedEmotion;
            queuedEmotion = null;
        } else {
            //[self.delegate expressionFaceAnimationDidFinish];
        }
    }


}
