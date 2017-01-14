package cc.stargroup.xiaodai.widget;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Foam on 2017/1/12.
 */

public class UIImageView {

    private float left;
    private float top;
    private float width;
    private float height;

    private Bitmap image;

    public Bitmap image() {
        return image;
    }

    public void setPosition(float l, float t) {
        this.left = l;
        this.top = t;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public float left() {
        return left;
    }

    public float top() {
        return top;
    }


}
