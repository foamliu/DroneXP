package cc.stargroup.xiaodai.character;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by Foam on 2017/1/10.
 */

public class CharacterFace {

    private CharacterType characterType;
    public CharacterEmotion emotion;
    public CharacterExpression expression;
    private float pupilDilation;
    private float rotation;

    private boolean emoting;
    private boolean expressing;

    private CharacterEye leftEye;
    private CharacterEye rightEye;

    public static CharacterFace createByCharacterType(Context context, CharacterType characterType) {
        return new CharacterFace(context, characterType);
    }

    public CharacterFace(Context context, CharacterType characterType) {
        this.characterType = characterType;
        this.leftEye = new CharacterEye(context, true);
        this.rightEye = new CharacterEye(context, false);


    }

    public void setRotation(float rotation) {
        boolean shouldAnimate = false;
        if (Math.abs(this.rotation - rotation) > 3) {
            shouldAnimate = true;
        }
        this.rotation = rotation;
    }

    public void drawSelf(Canvas canvas) {


    }
}
