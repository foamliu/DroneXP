package cc.stargroup.dronexp.android;

import android.util.Log;

import dji.sdk.FlightController.DJIFlightControllerDataType;
import dji.sdk.RemoteController.DJIRemoteController;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIError;

/**
 * Created by Foam on 2016/9/25.
 */
public class SensorManager {
    private static final String TAG = MainActivity.class.getName();
    private Logger logger = new Logger();
    private MainActivity mActivity;

    public SensorManager(MainActivity activity) {
        this.mActivity = activity;
    }


    public void Sense() {
        float[] values = new float[3];
        float[] R = new float[9];
        R[0] = R[4] = R[8] = 1.0f;
        values = android.hardware.SensorManager.getOrientation(R, values);

        String str = String.format("%f %f %f", values[0], values[1], values[2]);
        logger.appendLog(str);
        Log.i(TAG, str);

        if (mActivity.mFlightController != null) {
            DJIFlightControllerDataType.DJIAttitude attitude = mActivity.mFlightController.getCurrentState().getAttitude();
            String yaw = String.format("%.2f", attitude.yaw);
            String pitch = String.format("%.2f", attitude.pitch);
            String roll = String.format("%.2f", attitude.roll);

            DJIFlightControllerDataType.DJILocationCoordinate3D location = mActivity.mFlightController.getCurrentState().getAircraftLocation();
            String altitude = String.format("%.2f", location.getAltitude());
            String latitude = String.format("%.2f", location.getLatitude());
            String longitude = String.format("%.2f", location.getLongitude());

            str = "Yaw : " + yaw + ", Pitch : " + pitch + ", Roll : " + roll + "\n" + ", Altitude : " + altitude +
                    ", Latitude : " + latitude +
                    ", Longitude : " + longitude;
            logger.appendLog(str);
        }

        if (mActivity.mRemoteController != null) {
            mActivity.mRemoteController.getRCControlGimbalDirection(new DJIBaseComponent.DJICompletionCallbackWith<DJIRemoteController.DJIRCGimbalControlDirection>() {
                @Override
                public void onSuccess(DJIRemoteController.DJIRCGimbalControlDirection djircGimbalControlDirection) {
                    String str = "GimbalDirection: " + djircGimbalControlDirection;
                    logger.appendLog(str);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    logger.appendLog(djiError.getDescription());
                }
            });
        }

    }
}
