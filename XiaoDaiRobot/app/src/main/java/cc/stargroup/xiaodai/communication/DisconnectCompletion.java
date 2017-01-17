package cc.stargroup.xiaodai.communication;

/**
 * Created by Foam on 2017/1/16.
 */

public interface DisconnectCompletion {
    void OnDisconnect(RobotDataTransportOld transport, boolean disconnected);
}
