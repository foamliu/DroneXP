package cc.stargroup.dronexp.android;

import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIError;

/**
 * Created by Foam on 2016/9/25.
 */
public class ActionManager {
    private Logger logger = new Logger();
    private MainActivity mActivity;

    public ActionManager(MainActivity activity) {
        this.mActivity = activity;
    }

    private void takeOffAction() {
        mActivity.mFlightController.takeOff(new DJIBaseComponent.DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if (djiError != null) {
                    logger.appendLog(djiError.getDescription());
                } else {
                    logger.appendLog("Take off Success");
                }
            }
        });
    }

    private void autoLandAction() {
        mActivity.mFlightController.autoLanding(new DJIBaseComponent.DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if (djiError != null) {
                    logger.appendLog(djiError.getDescription());
                } else {
                    logger.appendLog("Take off Success");
                }
            }
        });
    }
}
