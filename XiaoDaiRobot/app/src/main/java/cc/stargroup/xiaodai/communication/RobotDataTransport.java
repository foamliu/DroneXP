package cc.stargroup.xiaodai.communication;

/**
 * Created by Foam on 2017/1/15.
 */

public class RobotDataTransport {

    private Session session;
    private List<byte> txData;
    private byte[] rxData;
    private boolean transmittingBytes;

    private boolean shuttingDown;


    public RobotDataTransport() {

    }

    public void queueTxBytes(byte[] bytes) {
        if (bytes.length && this.session.accessory.isConnected && !this.isShuttingDown()) {
            this.txData.appendData(bytes);

            if (this.session.outputStream.hasSpaceAvailable) {
                synchronized(this) {
                    this.transmitBytes();
                }
            }
        }
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
