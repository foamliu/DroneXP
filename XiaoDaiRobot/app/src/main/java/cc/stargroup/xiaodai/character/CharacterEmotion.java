package cc.stargroup.xiaodai.character;

import java.util.HashMap;
import java.util.Map;

/**
 * Emotions are persistent emotional states
 * <p>
 * When you set the RMCharacterEmotion of an RMCharacter, it will
 * stay in that emotional state until it receives another request to change
 * its RMCharacterEmotion.
 */
public enum CharacterEmotion {
    Curious(1),
    Excited(2),
    Happy(3),
    Sad(4),
    Scared(5),
    Sleepy(6),
    Sleeping(7),
    Indifferent(8),
    Bewildered(9),
    Delighted(10);

    private final int value;

    private CharacterEmotion(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static final Map<Integer, CharacterEmotion> intToTypeMap = new HashMap<Integer, CharacterEmotion>();
    static {
        for (CharacterEmotion type : CharacterEmotion.values()) {
            intToTypeMap.put(type.value, type);
        }
    }

    public static CharacterEmotion fromInt(int i) {
        CharacterEmotion type = intToTypeMap.get(Integer.valueOf(i));
        if (type == null)
            return CharacterEmotion.Happy;
        return type;
    }

    public final static int NUM_EMOTIONS = 10;

    /**
     The total number of available RMCharacterEmotions
     */
    private int numberOfEmotions = NUM_EMOTIONS;

    public int numberOfEmotions() {
        return numberOfEmotions;
    }
}
