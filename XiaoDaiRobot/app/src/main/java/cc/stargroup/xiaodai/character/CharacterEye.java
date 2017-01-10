package cc.stargroup.xiaodai.character;

import android.content.Context;
import android.graphics.RectF;
import android.util.DisplayMetrics;

/**
 * Created by Foam on 2017/1/10.
 */

public class CharacterEye {

    private CharacterPupil pupil;

    private boolean left;
    private float h;
    private float close;

    private RectF eyeFrame;
    private RectF maskFrame;

    private Point pupilCenter;
    private float pupilRadius;

    public CharacterEye(Context context, boolean left) {
        this.left = left;

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        h = (height - 480)/2;
    }

    public void setLeft(boolean left) {
        if (this.left != left) {
            this.left = left;
        }
    }

    public void setEmotion(CharacterEmotion emotion) {
        switch (emotion) {
            case Bewildered:
                if (left) {
                    //eyeFrame = new RectF(32.5, 156.5 + h, this.image.size);
                    maskFrame = new RectF(6, 3, 100, 100);
                    pupilCenter = new Point(57f, 27.5f);
                } else {
                    //eyeFrame = new RectF(158.5, 134.0 + h, self.image.size);
                    maskFrame = new RectF(3, 0, 100, 100);
                    pupilCenter = new Point(42f, 26.5f);
                }
                pupilRadius = 6;
                break;
            default:
                break;
        }
    }
}
