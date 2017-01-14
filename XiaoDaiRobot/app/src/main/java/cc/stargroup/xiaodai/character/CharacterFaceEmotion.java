package cc.stargroup.xiaodai.character;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import cc.stargroup.xiaodai.utilities.Util;
import cc.stargroup.xiaodai.widget.UIImageView;

/**
 * Created by Foam on 2017/1/11.
 */

public class CharacterFaceEmotion {

    private CharacterEye leftEye;
    private CharacterEye rightEye;

    private Context appContext;
    private CharacterEmotion emotion;

    private float h;
    private UIImageView mouthView;
    private float rate_w;
    private float rate_h;

    public CharacterFaceEmotion(Context context) {
        this.appContext = context;
        this.leftEye = new CharacterEye(context);
        this.leftEye.setLeft(true);
        this.rightEye = new CharacterEye(context);
        this.rightEye.setLeft(false);

        RectF frame = Util.getFrame(context);
        this.rate_w = frame.width() / 640.0f;
        this.rate_h = frame.height() / 960.0f;
        this.h = (frame.height() - 480 * rate_h) / 2;
        this.mouthView = new UIImageView();
    }

    public void setEmotion(CharacterEmotion emotion) {

        this.leftEye.setEmotion(emotion);
        this.leftEye.setClose(0.0f);
        this.leftEye.lookAtDefault();
        this.rightEye.setEmotion(emotion);
        this.rightEye.setClose(0.0f);
        this.rightEye.lookAtDefault();

        mouthView.setImage(loadMouthImage(emotion));

        this.emotion = emotion;

        switch (this.emotion) {
            case Bewildered:
                mouthView.setPosition(206.5f * rate_w, 218.5f * rate_h + h);
                break;
            case Curious:
                mouthView.setPosition(156.5f * rate_w, 285.0f * rate_h + h);
                break;
            case Excited:
                mouthView.setPosition(40.0f * rate_w, 266.0f * rate_h + h);
                break;
            case Happy:
            case Delighted:
                mouthView.setPosition(43.5f * rate_w, 252.5f * rate_h + h);
                break;
            case Indifferent:
                mouthView.setPosition(38.5f * rate_w, 261.0f * rate_h + h);
                break;
            case Sad:
                mouthView.setPosition(114f * rate_w, 269.0f * rate_h + h);
                break;
            case Scared:
                mouthView.setPosition(104.5f * rate_w, 264.0f * rate_h + h);
                break;
            case Sleepy:
                mouthView.setPosition(43.5f * rate_w, 231.0f * rate_h + h);
                break;
            case Sleeping:
                mouthView.setPosition(96.0f * rate_w, 268.0f * rate_h + h);
                break;
            default:
                mouthView.setPosition(43.5f * rate_w, 252.5f * rate_h + h);
                break;
        }
    }

    private Bitmap loadMouthImage(CharacterEmotion emotion) {
        String imageNamed = String.format("animations/Static Emotions/%s/Romo_Emotion_Mouth_%d@2x.png", emotion.toString(), emotion.getValue());
        return Util.loadImageFromAssetsFile(appContext, imageNamed);
    }

    public void blink() {
        if ((this.leftEye.close() < 0.89 || this.leftEye.close() < 0.89) && this.emotion != CharacterEmotion.Sleeping) {
            CharacterVoice.sharedInstance(appContext).makeBlinkSound();
        }
    }

    public void drawSelf(Canvas canvas) {
        canvas.drawBitmap(mouthView.image(), mouthView.left(), mouthView.top(), null);

        this.leftEye.drawSelf(canvas);
        this.rightEye.drawSelf(canvas);
    }
}
