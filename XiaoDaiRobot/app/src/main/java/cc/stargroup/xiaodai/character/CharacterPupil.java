package cc.stargroup.xiaodai.character;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import cc.stargroup.xiaodai.utilities.Util;

/**
 * Created by Foam on 2017/1/10.
 */

public class CharacterPupil {
    public final static float DEFAULT_PUPIL_SIZE = 29.0f;
    public final static float PUPIL_MULT_MIN = 0.5f;
    public final static float PUPIL_MULT_MAX = 1.25f;

    private Point center;
    private float dilation;

    public void CharacterPupil() {

    }

    public void setDilation(float dilation) {
        this.dilation = Util.clamp(PUPIL_MULT_MIN, dilation, PUPIL_MULT_MAX);
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public void drawSelf(Canvas canvas) {
        float radius = DEFAULT_PUPIL_SIZE * dilation;
        float left = center.x - radius;
        float top = center.y - radius;
        float right = center.x + radius;
        float bottom = center.y + radius;
        RectF frame = new RectF(left, top, right, bottom);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawOval(frame, paint);
    }
}
