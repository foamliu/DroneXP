package cc.stargroup.xiaodai.character;

/**
 * Expressions are briefly animated actions
 * <p>
 * Each RMCharacterExpression represents a type of animation that the
 * robot will briefly express
 */
public enum CharacterExpression {
    // is not expressing anything
    None(0),
    // becomes angry
    Angry(1),
    // gets bored
    Bored(2),
    // expresses curiosity
    Curious(3),
    // gets dizzy
    Dizzy(4),
    // becomes embarassed
    Embarrassed(5),
    // gets really excited
    Excited(6),
    // becomes exhausted
    Exhausted(7),
    // gets happy
    Happy(8),
    // holds his breath
    HoldingBreath(9),
    // laughs
    Laugh(10),
    // looks around
    LookingAround(11),
    // falls in love
    Love(12),
    // thinks about something
    Ponder(13),
    // gets sad
    Sad(14),
    // gets scared
    Scared(15),
    // becomes sleepy
    Sleepy(16),
    // sneezes
    Sneeze(17),
    // starts babbling
    Talking(18),
    // yawns
    Yawn(19),
    // gets scared
    Startled(20),
    // lets out a big laugh
    Chuckle(21),
    // lets out a warm smile
    Proud(22),
    // gets disappointed & sad
    LetDown(23),
    // makes an "oooh" face
    Want(24),
    // hiccups
    Hiccup(25),
    // becomes exhausted
    Fart(26),
    // gets bewildered
    Bewildered(27),
    // makes a "Yippee!"
    Yippee(28),
    // sniffs something
    Sniff(29),
    // smacks into the screen
    Smack(30),
    // makes an "Wee" face
    Wee(31),
    // struggles to move
    Struggling(32);

    private final int value;

    private CharacterExpression(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
