package cc.stargroup.xiaodai.character;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by Foam on 2017/1/10.
 */

public class CharacterFace {

    CharacterAnimation animation;
    CharacterFaceEmotion faceEmotion;

    private Context appContext;
    private CharacterType characterType;
    public CharacterEmotion emotion;
    public CharacterExpression expression;
    private float pupilDilation;
    private float rotation;
    private boolean emoting;
    private boolean expressing;

    public CharacterFace(Context context, CharacterType characterType) {
        this.appContext = context;
        this.characterType = characterType;

        this.animation = new CharacterAnimation();
        this.faceEmotion = new CharacterFaceEmotion(context);
        this.faceEmotion.setEmotion(CharacterEmotion.Curious);
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
        faceEmotion.drawSelf(canvas);
    }


}
