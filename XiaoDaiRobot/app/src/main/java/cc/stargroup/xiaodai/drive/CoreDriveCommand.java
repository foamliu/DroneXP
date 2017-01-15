package cc.stargroup.xiaodai.drive;

import java.util.HashMap;
import java.util.Map;

/**
 The items in this enumeration are used to keep track of the current command
 drive action
 */
public enum CoreDriveCommand {
    Stop(0),
    Forward(1),
    Backward(2),
    WithPower(3),
    WithHeading(4),
    WithRadius(5),
    Turn(6);

    private final int value;

    private CoreDriveCommand(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static final Map<Integer, CoreDriveCommand> intToTypeMap = new HashMap<Integer, CoreDriveCommand>();
    static {
        for (CoreDriveCommand type : CoreDriveCommand.values()) {
            intToTypeMap.put(type.value, type);
        }
    }

    public static CoreDriveCommand fromInt(int i) {
        CoreDriveCommand type = intToTypeMap.get(Integer.valueOf(i));
        if (type == null)
            return CoreDriveCommand.Stop;
        return type;
    }

    }
