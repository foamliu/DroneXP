package cc.stargroup.dronexp.android;

import dji.sdk.FlightController.DJIFlightController;
import dji.sdk.FlightController.DJIFlightControllerDataType;
import dji.sdk.Gimbal.DJIGimbal;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIError;

import static dji.sdk.FlightController.DJIFlightControllerDataType.DJIVirtualStickRollPitchControlMaxAngle;
import static dji.sdk.FlightController.DJIFlightControllerDataType.DJIVirtualStickRollPitchControlMaxVelocity;
import static dji.sdk.FlightController.DJIFlightControllerDataType.DJIVirtualStickVerticalControlMaxVelocity;

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
        if (mActivity.mFlightController != null) {
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
    }

    public void autoLanding() {
        if (mActivity.mFlightController != null) {
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

    public void sendData(float headPitch, float headRoll, float headYaw, float xAxis, float yAxis) {

        DJIFlightController flightController = mActivity.mFlightController;
        DJIGimbal gimbal = mActivity.mGimbal;

        headRoll = headRoll > 30 ? 30 : headRoll;
        headRoll = headRoll < -30 ? -30 : headRoll;

        float pPitch = xAxis * DJIVirtualStickRollPitchControlMaxVelocity;
        float pRoll = headRoll * 1.0F / 30 * DJIVirtualStickRollPitchControlMaxVelocity;
        float pYaw = headYaw;
        float pThrottle = -yAxis * DJIVirtualStickVerticalControlMaxVelocity;

        if (flightController != null) {
            flightController.setVerticalControlMode(DJIFlightControllerDataType.DJIVirtualStickVerticalControlMode.Velocity);
            flightController.setRollPitchControlMode(DJIFlightControllerDataType.DJIVirtualStickRollPitchControlMode.Velocity);
            flightController.setYawControlMode(DJIFlightControllerDataType.DJIVirtualStickYawControlMode.Angle);

            flightController.enableVirtualStickControlMode(new DJIBaseComponent.DJICompletionCallback() {
                @Override
                public void onResult(final DJIError djiError) {
                    if (djiError != null) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.showToast(djiError.getDescription());
                            }
                        });
                    }
                }
            });

            DJIFlightControllerDataType.DJIVirtualStickFlightControlData controlData =
                    new DJIFlightControllerDataType.DJIVirtualStickFlightControlData(pPitch, pRoll, pYaw, pThrottle);
            mActivity.mFlightController.sendVirtualStickFlightControlData(controlData, new DJIBaseComponent.DJICompletionCallback() {
                @Override
                public void onResult(final DJIError djiError) {
                    if (djiError != null) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.showToast(djiError.getDescription());
                            }
                        });
                    }
                }
            });
        }

        if (gimbal != null) {
            if (0 <= headPitch && headPitch <= 90) {
                DJIGimbal.DJIGimbalAngleRotation
                        pitch = new DJIGimbal.DJIGimbalAngleRotation(true, headPitch, DJIGimbal.DJIGimbalRotateDirection.CounterClockwise);
                DJIGimbal.DJIGimbalAngleRotation
                        roll = new DJIGimbal.DJIGimbalAngleRotation(false, 0, DJIGimbal.DJIGimbalRotateDirection.CounterClockwise);
                DJIGimbal.DJIGimbalAngleRotation
                        yaw = new DJIGimbal.DJIGimbalAngleRotation(false, 0, DJIGimbal.DJIGimbalRotateDirection.CounterClockwise);
                mActivity.mGimbal.rotateGimbalByAngle(DJIGimbal.DJIGimbalRotateAngleMode.AbsoluteAngle, pitch, roll, yaw, new DJIBaseComponent.DJICompletionCallback(){
                    @Override
                    public void onResult(final DJIError djiError) {
                        if (djiError != null) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mActivity.showToast(djiError.getDescription());
                                }
                            });
                        }
                    }
                });

            }
        }
    }

}
