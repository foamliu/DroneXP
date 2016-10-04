package cc.stargroup.dronexp.android;

import dji.sdk.FlightController.DJIFlightController;
import dji.sdk.FlightController.DJIFlightControllerDataType;
import dji.sdk.Gimbal.DJIGimbal;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIError;

import static dji.sdk.FlightController.DJIFlightControllerDataType.DJIVirtualStickRollPitchControlMaxVelocity;
import static dji.sdk.FlightController.DJIFlightControllerDataType.DJIVirtualStickVerticalControlMaxVelocity;

/**
 * Created by Foam on 2016/9/25.
 */
public class ActuatorManager {
    Logger logger = new Logger();
    MainActivity mActivity;
    float mSafeLimit = 0.1F;
    boolean isFlightControllerReady = false;
    boolean isGimbalReady = false;

    public ActuatorManager(MainActivity activity) {
        this.mActivity = activity;
    }

    public void takeOff() {
        if (mActivity.isControlled() && mActivity.mFlightController != null) {
            mActivity.mFlightController.takeOff(new DJIBaseComponent.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError != null) {
                        mActivity.showToast("takeOff: " + djiError.getDescription());
                    } else {
                        mActivity.showToast("Take off Success");
                    }
                }
            });
        }
    }

    public void autoLanding() {
        //if (mActivity.isControlled())
        if (mActivity.mFlightController != null) {
            mActivity.mFlightController.autoLanding(new DJIBaseComponent.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError != null) {
                        mActivity.showToast("autoLanding: " + djiError.getDescription());
                    } else {
                        mActivity.showToast("Auto Landing Success");
                    }
                }
            });
        }
    }

    public void goHome() {
        if (mActivity.mFlightController != null) {
            mActivity.mFlightController.goHome(new DJIBaseComponent.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError != null) {
                        mActivity.showToast("goHome: " + djiError.getDescription());
                    } else {
                        mActivity.showToast("Go Home Success");
                    }
                }
            });
        }
    }

    public void sendData(float headPitch, float headRoll, float headYaw, float xAxis, float yAxis, boolean isUpPressed, boolean isDownPressed) {

        DJIFlightController flightController = mActivity.mFlightController;
        DJIGimbal gimbal = mActivity.mGimbal;

        //float safeLimit = 0.1F;
        float throttle = 0.0F;
        if (isDownPressed) {
            throttle = -1.0F;
        }
        else if (isUpPressed) {
            throttle = +1.0F;
        }
        else {
            throttle = 0.0F;
        }

        float pPitch = yAxis * DJIVirtualStickRollPitchControlMaxVelocity * mSafeLimit;
        float pRoll = xAxis * DJIVirtualStickRollPitchControlMaxVelocity * mSafeLimit;
        float pYaw = headYaw;
        float pThrottle = throttle * DJIVirtualStickVerticalControlMaxVelocity * mSafeLimit;

        if (flightController != null && flightController.isConnected()) {
            if (!isFlightControllerReady) {
                flightController.setHorizontalCoordinateSystem(DJIFlightControllerDataType.DJIVirtualStickFlightCoordinateSystem.Body);
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
                                    mActivity.showToast("enableVirtualStickControlMode: " + djiError.getDescription());
                                }
                            });
                        }
                    }
                });

                isFlightControllerReady = true;
            }

            DJIFlightControllerDataType.DJIVirtualStickFlightControlData controlData =
                    new DJIFlightControllerDataType.DJIVirtualStickFlightControlData(pPitch, pRoll, pYaw, pThrottle);
            mActivity.mFlightController.sendVirtualStickFlightControlData(controlData, new DJIBaseComponent.DJICompletionCallback() {
                @Override
                public void onResult(final DJIError djiError) {
                    if (djiError != null) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.showToast("sendVirtualStickFlightControlData: " + djiError.getDescription());
                            }
                        });
                    }
                }
            });
        }

        if (gimbal != null && gimbal.isConnected()) {
            if (-30 <= headPitch && headPitch <= 90) {
                if (!isGimbalReady) {
                    gimbal.setPitchRangeExtensionEnabled(true, new DJIBaseComponent.DJICompletionCallback() {
                        @Override
                        public void onResult(final DJIError djiError) {
                            if (djiError != null) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mActivity.showToast("setPitchRangeExtensionEnabled: " + djiError.getDescription());
                                    }
                                });
                            }
                        }
                    });

                    isGimbalReady = true;
                }

                DJIGimbal.DJIGimbalAngleRotation
                        pitch = new DJIGimbal.DJIGimbalAngleRotation(true, headPitch, DJIGimbal.DJIGimbalRotateDirection.CounterClockwise);
                DJIGimbal.DJIGimbalAngleRotation
                        roll = new DJIGimbal.DJIGimbalAngleRotation(false, 0, DJIGimbal.DJIGimbalRotateDirection.CounterClockwise);
                DJIGimbal.DJIGimbalAngleRotation
                        yaw = new DJIGimbal.DJIGimbalAngleRotation(false, 0, DJIGimbal.DJIGimbalRotateDirection.CounterClockwise);
                gimbal.rotateGimbalByAngle(DJIGimbal.DJIGimbalRotateAngleMode.AbsoluteAngle, pitch, roll, yaw, new DJIBaseComponent.DJICompletionCallback(){
                    @Override
                    public void onResult(final DJIError djiError) {
                        if (djiError != null) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mActivity.showToast("rotateGimbalByAngle: " + djiError.getDescription());
                                }
                            });
                        }
                    }
                });

            }
        }
    }

    public void setSafeLimit(float safeLimit) {
        this.mSafeLimit = safeLimit;
    }

}
