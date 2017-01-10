package cc.stargroup.xiaodai.character;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by Foam on 2017/1/10.
 */

public class Character {

    private CharacterType characterType;

    /**
     A float in the range [0.5, 1.25] for adjusting the dilation of the character's pupils.

     The value will be internally clamped between 0.5 and 1.25.
     */
    private float pupilDilation;

    /**
     A float in the range [-15, 15] representing rotation of the character's face

     The value will be internally clamped between -15 and 15. Setting this to 15
     from 0 results in the character's face rotating clockwise from the
     character's perspective.
     */
    private float faceRotation;

    /**
     A boolean value representing the state of the character's left eye.

     Read this property to determine whether the character's left eye is open, and
     set it to directly change the state of the character's left eye.
     */
    private boolean leftEyeOpen;

    /**
     A boolean value representing the state of the character's right eye.

     Read this property to determine whether the character's right eye is open, and
     set it to directly change the state of the character's right eye.
     */
    private boolean rightEyeOpen;

    /**
     * where the character's looking.
     */
    private Point3D gaze;

    private CharacterFace face;
    private CharacterVoice voice;

    private Context appContext;

    public Character(Context context) {
        this.appContext = context;

        this.characterType = CharacterType.XiaoDaiRobot;
        this.face = CharacterFace.createByCharacterType(this.appContext, characterType);

        this.leftEyeOpen = this.rightEyeOpen = true;

        this.face.emotion = CharacterEmotion.Happy;
        this.voice = CharacterVoice.sharedInstance;
    }

    public void setEmotion(CharacterEmotion emotion) {
        this.face.emotion = emotion;
    }

    public void setExpression(CharacterExpression expression) {
        this.face.expression = expression;
    }

    public void setEyesOpen(boolean leftEyeOpen, boolean rightEyeOpen) {
        this.leftEyeOpen = leftEyeOpen;
        this.rightEyeOpen = rightEyeOpen;
    }

    public void lookAtPoint(Point3D point) {

    }

    /**
     Tells the character to look at its default location (straight ahead).
     */
    public void lookAtDefault() {
        this.gaze = new Point3D(0.0f, 0.0f, 0.9f);
    }

    /**
     The character says a particular utterance
     */
    public void say(String utterance) {

    }

    /**
     The character makes a short mumbling expression
     */
    public void mumble() {

    }

    public void drawSelf(Canvas canvas) {
        face.drawSelf(canvas);
    }
}
