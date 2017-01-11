package cc.stargroup.xiaodai.character;

/**
 Emotions are persistent emotional states

 When you set the RMCharacterEmotion of an RMCharacter, it will
 stay in that emotional state until it receives another request to change
 its RMCharacterEmotion.
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
}
