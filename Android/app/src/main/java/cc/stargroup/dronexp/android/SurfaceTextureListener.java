package cc.stargroup.dronexp.android;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.view.TextureView;

import dji.sdk.Codec.DJICodecManager;

/**
 * Created by Foam on 2016/9/25.
 */
public class SurfaceTextureListener implements TextureView.SurfaceTextureListener {

    private static final String TAG = SurfaceTextureListener.class.getName();
    private Logger logger = new Logger();
    private Activity mActivity;

    // Codec for video live view
    protected DJICodecManager mCodecManager = null;

    public SurfaceTextureListener(Activity activity) {
        this.mActivity = activity;
    }

    public DJICodecManager getCodecManager() {
        return mCodecManager;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        logger.appendLog("onSurfaceTextureAvailable: width=" + width + ", height=" + height);

        if (mCodecManager == null) {
            mCodecManager = new DJICodecManager(this.mActivity, surface, width, height);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        logger.appendLog("onSurfaceTextureSizeChanged: width=" + width + ", height=" + height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        logger.appendLog("onSurfaceTextureDestroyed");

        if (mCodecManager != null) {
            mCodecManager.cleanSurface();
            mCodecManager = null;
        }

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //logger.appendLog("onSurfaceTextureUpdated");
    }
}
