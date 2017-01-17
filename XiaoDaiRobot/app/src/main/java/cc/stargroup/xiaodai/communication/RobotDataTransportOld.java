package cc.stargroup.xiaodai.communication;

/**
 * Created by Foam on 2017/1/15.
 */

public class RobotDataTransportOld {

    private RobotDataTransport robotDataTransport;
    private byte[] txData;
    private byte[] rxData;
    private boolean transmittingBytes;

    private boolean shuttingDown;


    public RobotDataTransportOld() {
        /*make sure to use the same name of the robot.
        And check if it's paired with your device */
        RobotDataTransport robotDataTransport = RobotDataTransport.getInstance("Robot");
        robotDataTransport.connect();
    }

    public void sendMessage(String message) {
        robotDataTransport.sendMessage(message);
    }

    public void clearTxBytes() {
        this.txData = null;
    }

    public void updateFirmware() {

    }

    public boolean isUpdatingFirmware() {
        return false;
    }

    public void stopUpdatingFirmware() {

    }

    public void softReset() {

    }

    public void setWatchdog(RobotWatchdogTimeout timeout) {

    }

    public void disableWatchdog() {

    }

    public void openSession() {
        this.shuttingDown = false;
    }

    public void closeSession() {
        
    }

    private boolean isShuttingDown() {
        return false;
    }

    private void transmitBytes() {

    }
}
