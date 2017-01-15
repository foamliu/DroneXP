package cc.stargroup.xiaodai.drive;

/**
 Block that is intended to be used as callback when the robot has completed a
 command that involved moving to a certain heading (orientation).

 @param success Whether the robot successfully executed the turn command
 (NO if the turn was aborted).

 @param heading The heading the robot actually ended up at when the turn
 completed.
 */
public interface CoreTurnCompletion {
    void OnCompletion(boolean success, float heading);
}
