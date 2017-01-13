package cc.stargroup.xiaodai.utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Foam on 2017/1/11.
 */

public class Util {

    public static Bitmap loadImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getAssets();

        try {
            InputStream is = am.open(fileName);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither = true;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            image = BitmapFactory.decodeStream(is, null, options);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static String loadJsonFromAssetsFile(Context context, String fileName) {
        String json = null;
        AssetManager am = context.getAssets();

        try {
            InputStream is = am.open(fileName);
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static RectF getFrame(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        return new RectF(0, 0, width, height);
    }

    public static float clamp(float min, float value, float max) {
        if (value > max) {
            value = max;
        }
        if (value < min) {
            value = min;
        }

        return value;
    }

}
