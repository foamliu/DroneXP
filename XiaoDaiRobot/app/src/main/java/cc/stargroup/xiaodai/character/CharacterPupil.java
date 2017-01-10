package cc.stargroup.xiaodai.character;

import android.graphics.RectF;

/**
 * Created by Foam on 2017/1/10.
 */

public class CharacterPupil {
    public final static float DEFAULT_PUPIL_SIZE = 29.0f;
    public final static float PUPIL_MULT_MIN = 0.5f;
    public final static float PUPIL_MULT_MAX = 1.25f;

    private float dilation;
    private RectF frame;

    public void setDilation(float dilation) {
        this.dilation = clamp(PUPIL_MULT_MIN, dilation, PUPIL_MULT_MAX);
        this.frame = new RectF(0, 0, DEFAULT_PUPIL_SIZE * dilation, DEFAULT_PUPIL_SIZE * dilation);
    }

    private float clamp(float min, float value, float max) {
        if (value > max) {
            value = max;
        }
        if (value < min) {
            value = min;
        }

        return value;
    }
}
