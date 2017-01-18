package cc.stargroup.xiaodai.drive;

import java.util.HashMap;
import java.util.Map;

public enum CoreCommand {
    CMD_SET_LEDS((byte)0x22),
    CMD_SET_MOTORS((byte)0x23),
    CMD_READ_EEPROM((byte)0x31),
    CMD_WRITE_EEPROM((byte)0x32),

    CMD_GET_VITALS((byte)0x34),
    CMD_GET_MOTOR_CURRENT((byte)0x35),
    CMD_GET_BATTERY_STATUS((byte)0x36),
    CMD_GET_CHARGING_STATE((byte)0x37),

    CMD_SET_WATCHDOG((byte)0x38),
    CMD_SET_LEDS_BLINK_LONG((byte)0x39),
    CMD_SET_DEV_CHARGE_ENABLE((byte)0x3A),
    CMD_SET_DEV_CHARGE_CURRENT((byte)0x3B),

    CMD_ACK((byte)0x40),

    CMD_SET_LEDS_OFF((byte)0x44),
    CMD_SET_LEDS_PWM((byte)0x45),
    CMD_SET_LEDS_BLINK((byte)0x46),
    CMD_SET_LEDS_PULSE((byte)0x47),
    CMD_SET_LEDS_HALFPULSEUP((byte)0x48),
    CMD_SET_LEDS_HALFPULSEDOWN((byte)0x49),

    CMD_SET_MODE((byte)0x50),
    CMD_DISABLE_WATCHDOG((byte)0x52),
    CMD_SOFT_RESET((byte)0x53),

    STK_LEAVE_PROGMODE((byte)0x51);


    private final byte value;

    private CoreCommand(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    private static final Map<Byte, CoreCommand> intToTypeMap = new HashMap<Byte, CoreCommand>();

    static {
        for (CoreCommand type : CoreCommand.values()) {
            intToTypeMap.put(type.value, type);
        }
    }

    public static CoreCommand fromByte(byte i) {
        CoreCommand type = intToTypeMap.get(Byte.valueOf(i));
        if (type == null)
            return CoreCommand.CMD_SET_LEDS;
        return type;
    }

}
