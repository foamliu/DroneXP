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
        this.faceEmotion.setEmotion(CharacterEmotion.Curious);
    }

    public void setEmotion(CharacterEmotion emotion) {
        if (!this.emoting && !this.expressing && (emotion != this.emotion)) {
            // If we aren't mid-animation and the character is visible
            this.emoting = true;

            this.animation.animateWithActionForEmotion(AnimatedAction.Outro, emotion);

        } else if (this.emoting || this.expressing) {
            // If we're currently animating, queue this as the final desired emotion
            queuedEmotion = emotion;
        }
    }

    public void setExpressionWithEmotion(CharacterExpression expression, CharacterEmotion emotion) {
        if (expression == CharacterExpression.None) {
            // If we aren't given an expression, simply change emotions
            this.emotion = emotion;
        } else if (!this.emoting && !this.expressing) {
            this.animation.animateWithActionForExpression(AnimatedAction.Expression, expression);
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


}
