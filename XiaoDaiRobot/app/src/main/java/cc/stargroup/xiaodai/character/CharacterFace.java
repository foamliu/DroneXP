package cc.stargroup.xiaodai.character;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import cc.stargroup.xiaodai.utilities.Util;

/**
 * Created by Foam on 2017/1/10.
 */

public class CharacterFace {

    private Context appContext;
    private CharacterType characterType;
    public CharacterEmotion emotion;
    public CharacterExpression expression;
    private float pupilDilation;
    private float rotation;

    private boolean emoting;
    private boolean expressing;

    private CharacterEye leftEye;
    private CharacterEye rightEye;

    private String imageNamedTemp;

    public CharacterFace(Context context, CharacterType characterType) {
        this.appContext = context;
        this.characterType = characterType;
        this.leftEye = new CharacterEye(context, true);
        this.rightEye = new CharacterEye(context, false);

        String imageNamed = String.format("Romo_Emotion_Leye_%d@2x.png", CharacterEmotion.Happy.getValue());
        //this.imageNamedTemp = "bundle/" + imageNamed;
        this.imageNamedTemp = "animations/Static Emotions/Happy/" + "Romo_Emotion_Leye_3@2x.png";
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
        Bitmap bitmap = Util.getImageFromAssetsFile(appContext, imageNamedTemp);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }


}
