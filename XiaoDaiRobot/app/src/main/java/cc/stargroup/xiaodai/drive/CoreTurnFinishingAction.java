package cc.stargroup.xiaodai.drive;

import java.util.HashMap;
import java.util.Map;

import cc.stargroup.xiaodai.character.CharacterExpression;

/**
 * Created by Foam on 2017/1/16.
 */

public enum CoreTurnFinishingAction {
    StopDriving(0),
    DriveForward(1),
    DriveBackward(2);

    private final int value;

    private CoreTurnFinishingAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static final Map<Integer, CoreTurnFinishingAction> intToTypeMap = new HashMap<Integer, CoreTurnFinishingAction>();
    static {
        for (CoreTurnFinishingAction type : CoreTurnFinishingAction.values()) {
            intToTypeMap.put(type.value, type);
        }
    }

    public static CoreTurnFinishingAction fromInt(int i) {
        CoreTurnFinishingAction type = intToTypeMap.get(Integer.valueOf(i));
        if (type == null)
            return CoreTurnFinishingAction.StopDriving;
        return type;
    }

}
