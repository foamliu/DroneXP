package cc.stargroup.xiaodai.widget;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Foam on 2017/1/12.
 */

public class UIImageView {

    private RectF frame;
    private Bitmap image;

    public RectF frame() {
        return frame;
    }

    public Bitmap image() {
        return image;
    }

    public void setFrame(RectF frame) {
        this.frame = frame;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }




}
