package cc.stargroup.xiaodai.communication;

import cc.stargroup.xiaodai.robot.functionality.RobotCommunicationProtocol;

/**
 * Created by Foam on 2017/1/16.
 */

public class RobotCommunication implements RobotCommunicationProtocol {

    private RobotDataTransport transport;

    public RobotCommunication() {
        transport = new RobotDataTransport();
    }






}
