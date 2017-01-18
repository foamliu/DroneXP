package cc.stargroup.xiaodai.communication;

import cc.stargroup.xiaodai.drive.CoreCommand;
import cc.stargroup.xiaodai.drive.CoreMotorAxis;
import cc.stargroup.xiaodai.robot.functionality.RobotCommunicationProtocol;


public class RobotCommunication implements RobotCommunicationProtocol {
    private static final String TAG = RobotCommunication.class.getSimpleName();

    private RobotDataTransport transport;

    public final static boolean kUseWatchdog = true;
    public final static float kSendRate = 0.05f;              // 20Hz send rate for all commands
    public final static float kHeartbeatRateMin = 0.25f;     // Min time between heartbeat commands
    public final static float kDeadRobotTimeout = 4.0f;      // Timeout for command acks (robot stopped responding)


    private boolean hasNewMotorCommand;
    private byte motorValueLeft;
    private byte motorValueRight;
    private byte motorValueTilt;
    private int ticksSinceLastHeartbeat;
    private int ticksSinceLastAck;

    public RobotCommunication() {
        transport = RobotDataTransport.getInstance();

        transport.connect();
        transport.sendMessage("Hello#");
    }

    public void sendMessage(String message) {
        transport.sendMessage(message + "#");
    }

    public void setMotorNumber(CoreMotorAxis motor, byte value) {
        switch(motor) {
            case Left:
                this.motorValueLeft = value;
                break;

            case Right:
                this.motorValueRight = value;
                break;

            case Tilt:
                this.motorValueTilt = value;
                break;

            default:
                break;
        }
        this.hasNewMotorCommand = true;
    }

    public void communicate() {
        byte[] buffer = new byte[20];

        if (this.hasNewMotorCommand) {
            buffer[0] = CoreCommand.CMD_SET_MOTORS.getValue();
            buffer[1] = 3;
            buffer[2] = this.motorValueLeft;
            buffer[3] = this.motorValueRight;
            buffer[4] = this.motorValueTilt;
            buffer[5] = '#';
            transport.send(buffer);
        }
    }

}
