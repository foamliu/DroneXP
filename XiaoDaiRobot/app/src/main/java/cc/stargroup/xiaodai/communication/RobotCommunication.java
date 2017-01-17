package cc.stargroup.xiaodai.communication;

import cc.stargroup.xiaodai.robot.functionality.RobotCommunicationProtocol;


public class RobotCommunication implements RobotCommunicationProtocol {
    private static final String TAG = RobotCommunication.class.getSimpleName();

    private RobotDataTransport transport;

    public RobotCommunication() {
        transport = RobotDataTransport.getInstance();

        transport.connect();
        transport.sendMessage("Hello#");
    }

    public void sendMessage(String message) {
        transport.sendMessage(message + "#");
    }

}
