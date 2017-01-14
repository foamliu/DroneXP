package cc.stargroup.xiaodai.character;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import cc.stargroup.xiaodai.utilities.Util;
import cc.stargroup.xiaodai.widget.UIImageView;

/**
 * Created by Foam on 2017/1/10.
 */

public class CharacterEye {

    private CharacterPupil pupil;
    private UIImageView eye;

    private boolean left;
    private float h;
    private float w;
    private Context appContext;
    private CharacterEmotion emotion;

    private float close;

    private Point pupilCenter;
    private float pupilRadius;
    private float rate_w;
    private float rate_h;

    public CharacterEye(Context context) {
        this.appContext = context;

        this.pupil = new CharacterPupil();
        this.eye = new UIImageView();

        RectF frame = Util.getFrame(context);
        this.rate_w = frame.width() / 640.0f;
        this.rate_h = frame.height() / 960.0f;
        this.h = frame.height() / 2 - 480;
        this.w = frame.width() / 2 - 320;
    }

    public void setLeft(boolean left) {
        if (this.left != left) {
            this.left = left;
        }
    }

    public void setEmotion(CharacterEmotion emotion) {

        this.pupil.setDilation(1.0f);
        this.emotion = emotion;
        eye.setImage(getImage(emotion));

        switch (emotion) {
            case Bewildered:
                if (left) {
                    eye.setPosition(32.5f * rate_w + w, 156.5f * rate_h + h);
                    pupilCenter = new Point(57f, 27.5f);
                } else {
                    eye.setPosition(158.5f * rate_w + w, 134.0f * rate_h + h);
                    pupilCenter = new Point(42f, 26.5f);
                }
                pupilRadius = 6;
                break;
            case Curious:
                if (left) {
                    eye.setPosition(41.5f * rate_w + w, 174.5f * rate_h + h);
                    pupilCenter = new Point(78, 56);
                } else {
                    eye.setPosition(170.5f * rate_w + w, 156.5f * rate_h + h);
                    pupilCenter = new Point(22, 58);
                }
                pupilRadius = 8;
                break;
            case Excited:
                if (left) {
                    eye.setPosition(40.5f * rate_w + w, 171.5f * rate_h + h);
                    pupilCenter = new Point(58, 51);
                } else {
                    eye.setPosition(171f * rate_w + w, 162.5f * rate_h + h);
                    pupilCenter = new Point(44, 53);
                }
                pupilRadius = 19;
                break;
            case Happy:
            case Delighted:
                if (left) {
                    eye.setPosition(40.5f * rate_w + w, 153.0f * rate_h + h);
                    pupilCenter = new Point(58f, 48f);
                } else {
                    eye.setPosition(171.0f * rate_w + w, 144.0f * rate_h + h);
                    pupilCenter = new Point(41f, 48f);
                }
                pupilRadius = 26;
                break;
            case Indifferent:
                if (left) {
                    eye.setPosition(46.5f * rate_w + w, 150.5f * rate_h + h);
                    pupilCenter = new Point(56.5f, 51);
                } else {
                    eye.setPosition(172.0f * rate_w + w, 147.5f * rate_h + h);
                    pupilCenter = new Point(35.5f, 54);
                }
                pupilRadius = 26;
                break;
            case Sad:
                if (left) {
                    eye.setPosition(40.5f * rate_w + w, 191.0f * rate_h + h);
                    pupilCenter = new Point(36.6f, 39.5f);
                } else {
                    eye.setPosition(171.0f * rate_w + w, 185.5f * rate_h + h);
                    pupilCenter = new Point(43.7f, 39.8f);
                }
                pupilRadius = 7;
                break;
            case Scared:
                if (left) {
                    eye.setPosition(40.5f * rate_w + w, 124.0f * rate_h + h);
                    pupilCenter = new Point(71, 62);
                } else {
                    eye.setPosition(171.5f * rate_w + w, 115.5f * rate_h + h);
                    pupilCenter = new Point(30, 61);
                }
                pupilRadius = 22;
                this.pupil.setDilation(0.5f);
                break;
            case Sleepy:
                if (left) {
                    eye.setPosition(40.5f * rate_w + w, 164.0f * rate_h + h);
                    pupilCenter = new Point(55.2f, 20.5f);
                } else {
                    eye.setPosition(170.0f * rate_w + w, 164f * rate_h + h);
                    pupilCenter = new Point(42.8f, 20.5f);
                }
                pupilRadius = 14;
                break;
            case Sleeping:
                if (left) {
                    eye.setPosition(40.5f * rate_w + w, 228.0f * rate_h + h);
                } else {
                    eye.setPosition(171.0f * rate_w + w, 228 * rate_h + h);
                }
                break;
            default:
                break;
        }

        lookAtDefault();
    }

    private Bitmap getImage(CharacterEmotion emotion) {
        String imageNamed = String.format("animations/Static Emotions/%s/Romo_Emotion_%seye_%d@2x.png", emotion.toString(), left ? "L" : "R", emotion.getValue());
        return Util.loadImageFromAssetsFile(appContext, imageNamed);
    }

    public void lookAtPoint(Point3D point, boolean animated) {
        point.x = Util.clamp(-1.0f, point.x, 1.0f);
        point.y = Util.clamp(-1.0f, point.y, 1.0f);
        point.z = Util.clamp(0.0f, point.z, 1.0f);

        float x = (point.x * 64) + 12 * (0.9f - point.z) * (left ? 1 : -1);
        float y = (point.y * 44);

        float dist = (float) Math.sqrt(x * x + y * y);
        if (dist > pupilRadius) {
            float theta = -(float) Math.atan2(y, x);
            x = (float) Math.cos(theta) * pupilRadius;
            y = -(float) Math.sin(theta) * pupilRadius;
        }
        if (pupilCenter != null) {
            x += pupilCenter.x;
            y += pupilCenter.y;
        }

        this.pupil.setCenter(transform(new Point(x, y)));
    }

    public void lookAtDefault() {
        lookAtPoint(new Point3D(0.0f, 0.0f, 0.9f), false);
    }

    public void drawSelf(Canvas canvas) {
        canvas.drawBitmap(eye.image(), eye.left(), eye.top(), null);

        if (emotion != CharacterEmotion.Sleeping) {
            this.pupil.drawSelf(canvas);
        }
    }

    private Point transform(Point point) {
        return new Point(point.x * 2 + eye.left(), point.y * 2 + eye.top());
    }

    public float close() {
        return close;
    }

    public void setClose(float c) {
        this.close = c;
    }
}
