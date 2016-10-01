package cc.stargroup.dronexp.android;

import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIError;

/**
 * Created by Foam on 2016/9/25.
 */
public class ActuatorManager {
    Logger logger = new Logger();
    MainActivity mActivity;

    public ActuatorManager(MainActivity activity) {
        this.mActivity = activity;
    }

    public void takeOff() {
        mActivity.mFlightController.takeOff(new DJIBaseComponent.DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if (djiError != null) {
                    logger.appendLog(djiError.getDescription());
                } else {
                    mActivity.showToast("Take off Success");
                }
            }
        });
    }

    public void autoLanding() {
        mActivity.mFlightController.autoLanding(new DJIBaseComponent.DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if (djiError != null) {
                    logger.appendLog(djiError.getDescription());
                } else {
                    mActivity.showToast("Take off Success");
                }
            }
        });
    }
}
