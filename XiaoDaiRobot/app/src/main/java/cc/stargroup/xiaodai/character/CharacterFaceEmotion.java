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
        this.emotion = emotion;
        this.leftEye.setEmotion(emotion);
        this.rightEye.setEmotion(emotion);

        mouthView.setImage(getImage(emotion));

        switch (this.emotion) {
            case Bewildered:
                mouthView.setFrame(new RectF(206.5f * rate_w, 218.5f * rate_h + h, 0, 0));
                break;
            case Curious:
                mouthView.setFrame(new RectF(156.5f * rate_w, 285.0f * rate_h + h, 0, 0));
                break;
            case Excited:
                mouthView.setFrame(new RectF(40.0f * rate_w, 266.0f * rate_h + h, 0, 0));
                break;
            case Happy:
            case Delighted:
                mouthView.setFrame(new RectF(43.5f * rate_w, 252.5f * rate_h + h, 0, 0));
                break;
            case Indifferent:
                mouthView.setFrame(new RectF(38.5f * rate_w, 261.0f * rate_h + h, 0, 0));
                break;
            case Sad:
                mouthView.setFrame(new RectF(114f * rate_w, 269.0f * rate_h + h, 0, 0));
                break;
            case Scared:
                mouthView.setFrame(new RectF(104.5f * rate_w, 264.0f * rate_h + h, 0, 0));
                break;
            case Sleepy:
                mouthView.setFrame(new RectF(43.5f * rate_w, 231.0f * rate_h + h, 0, 0));
                break;
            case Sleeping:
                mouthView.setFrame(new RectF(96.0f * rate_w, 268.0f * rate_h + h, 0, 0));
                break;
            default:
                mouthView.setFrame(new RectF(43.5f * rate_w, 252.5f * rate_h + h, 0, 0));
                break;
        }
    }

    private Bitmap getImage(CharacterEmotion emotion) {
        String imageNamed = String.format("animations/Static Emotions/%s/Romo_Emotion_Mouth_%d@2x.png", emotion.toString(), emotion.getValue());
        return Util.getImageFromAssetsFile(appContext, imageNamed);
    }

    public void drawSelf(Canvas canvas) {
        canvas.drawBitmap(mouthView.image(), mouthView.frame().left, mouthView.frame().top, null);

        this.leftEye.drawSelf(canvas);
        this.rightEye.drawSelf(canvas);
    }
}
