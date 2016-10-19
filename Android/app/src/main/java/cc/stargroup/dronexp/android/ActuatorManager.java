package cc.stargroup.dronexp.android;

import dji.sdk.FlightController.DJIFlightController;
import dji.sdk.FlightController.DJIFlightControllerDataType;
import dji.sdk.Gimbal.DJIGimbal;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIError;

import static dji.sdk.FlightController.DJIFlightControllerDataType.DJIVirtualStickRollPitchControlMaxVelocity;
import static dji.sdk.FlightController.DJIFlightControllerDataType.DJIVirtualStickRollPitchControlMinVelocity;
import static dji.sdk.FlightController.DJIFlightControllerDataType.DJIVirtualStickVerticalControlMaxVelocity;

/**
 * Created by Foam on 2016/9/25.
 */
public class ActuatorManager {
    Logger logger = new Logger();
    MainActivity mActivity;
    float mSafeLimit = 1.0F;
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
                        String message = "takeOff: " + djiError.getDescription();
                        logger.appendLog(message);
                        mActivity.showToast(message);
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
                        String message = "autoLanding: " + djiError.getDescription();
                        logger.appendLog(message);
                        mActivity.showToast(message);
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
                        String message = "goHome: " + djiError.getDescription();
                        logger.appendLog(message);
                        mActivity.showToast(message);
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
        } else if (isUpPressed) {
            throttle = +1.0F;
        } else {
            throttle = 0.0F;
        }

        final float pPitch = bound(xAxis * DJIVirtualStickRollPitchControlMaxVelocity * mSafeLimit,
                                DJIVirtualStickRollPitchControlMinVelocity,
                                DJIVirtualStickRollPitchControlMaxVelocity);
        final float pRoll = bound(-yAxis * DJIVirtualStickRollPitchControlMaxVelocity * mSafeLimit,
                                DJIVirtualStickRollPitchControlMinVelocity,
                                DJIVirtualStickRollPitchControlMaxVelocity);
        final float pYaw = headYaw;
        final float pThrottle = throttle * DJIVirtualStickVerticalControlMaxVelocity * mSafeLimit;

        if (flightController != null && flightController.isConnected()) {
            if (!isFlightControllerReady) {
                flightController.setHorizontalCoordinateSystem(DJIFlightControllerDataType.DJIVirtualStickFlightCoordinateSystem.Body);
                flightController.setVirtualStickAdvancedModeEnabled(true);
                flightController.setVerticalControlMode(DJIFlightControllerDataType.DJIVirtualStickVerticalControlMode.Velocity);
                flightController.setRollPitchControlMode(DJIFlightControllerDataType.DJIVirtualStickRollPitchControlMode.Velocity);
                flightController.setYawControlMode(DJIFlightControllerDataType.DJIVirtualStickYawControlMode.Angle);

                flightController.enableVirtualStickControlMode(new DJIBaseComponent.DJICompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        if (djiError != null) {
                            final String message = "enableVirtualStickControlMode: " + djiError.getDescription();
                            logger.appendLog(message);

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mActivity.showToast(message);
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
                public void onResult(DJIError djiError) {
                    if (djiError != null) {
                        final StringBuilder message = new StringBuilder("sendVirtualStickFlightControlData: " + djiError.getDescription());
                        message.append(" pPitch=" + pPitch + ", pRoll=" + pRoll + ", pYaw=" + pYaw + ", pThrottle=" + pThrottle);
                        logger.appendLog(message.toString());

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.showToast(message.toString());
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
                        public void onResult(DJIError djiError) {
                            if (djiError != null) {
                                final String message = "setPitchRangeExtensionEnabled: " + djiError.getDescription();
                                logger.appendLog(message);

                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mActivity.showToast(message);
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
                gimbal.rotateGimbalByAngle(DJIGimbal.DJIGimbalRotateAngleMode.AbsoluteAngle, pitch, roll, yaw, new DJIBaseComponent.DJICompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        if (djiError != null) {
                            final String message = "rotateGimbalByAngle: " + djiError.getDescription();
                            logger.appendLog(message);

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mActivity.showToast(message);
                                }
                            });

                        }
                    }
                });

            }
        }
    }

//    public void setSafeLimit(float safeLimit) {
//        this.mSafeLimit = safeLimit;
//    }

    private float bound(float value, float min, float max) {
        if (value > max) {
            value = max;
        }
        if (value < min) {
            value = min;
        }
        return value;
    }

}
