package cc.stargroup.xiaodai.character;

import android.content.Context;
import android.graphics.Canvas;

import cc.stargroup.xiaodai.widget.UIMainView;

/**
 @brief Character is the public interface for creating characters and
 interfacing with them.

 An Character object represents a socially-embodied creature that will
 respond to events from a programmer while still maintaining an "illusion of
 life". Character is meant to abstract away many problems that arise when
 designing software for social robots, such as animation and gaze. After
 instantiating an Character object with a specific type (and setting its
 delegate), it can be added to a superview where the character will be
 displayed. The Character object is now ready to accept expressions,
 animations, and gaze / eye commands.
 */
public class Character {

    public final static int NUM_MUMBLES = 8;
    public final static int ROTATION_LIMIT = 15;

    /**
     The type of character contained within this instance
     */
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
     An Point3D of where the character's looking.

     Calling lookAtPoint:animated: sets this value, but the character also changes
     gaze automatically.
     */
    private Point3D gaze;

    private CharacterFace face;
    private CharacterVoice voice;

    private Context appContext;

    public Character(Context context) {
        this.appContext = context;
        this.characterType = CharacterType.XiaoDaiRobot;

        this.face = new CharacterFace(context, CharacterType.XiaoDaiRobot);
        this.face.setEmotion(CharacterEmotion.Sleepy);
        //this.face.setExpressionWithEmotion(CharacterExpression.Angry, CharacterEmotion.Sleepy);

        this.leftEyeOpen = this.rightEyeOpen = true;

        this.voice = CharacterVoice.sharedInstance(context);
    }

    /**
     Adds a view displaying the character to a given superview. The view containing
     the character will automatically scale to fit the device's screen.

     @param superview The parent view in which to contain the character's view.
     */
    public void addToSuperview(UIMainView superview) {
        superview.setCharacter(this);
    }

    /**
     Removes the character's view from its superview.
     */
    public void removeFromSuperview(UIMainView superview) {
        superview.setCharacter(null);
    }

    public void setEmotion(CharacterEmotion emotion) {
        this.face.setEmotion(emotion);
    }

    public void setExpressionWithEmotion(CharacterExpression expression, CharacterEmotion emotion) {
        this.face.setExpressionWithEmotion(expression, emotion);
        this.voice.setExpression(expression);
    }

    /**
     Combined setter for changing the state of both eyes.

     Guarantees that the state of both eyes are changed at the same time.

     @param leftEyeOpen YES if the character should set its left eye to be open,
     NO for closed
     @param rightEyeOpen YES if the character should set its right eye to be open,
     NO for closed
     */
    public void setEyesOpen(boolean leftEyeOpen, boolean rightEyeOpen) {
        this.leftEyeOpen = leftEyeOpen;
        this.rightEyeOpen = rightEyeOpen;
    }

    /**
     Tells the character to look at a specified point.

     @param point An RMPoint3D specifying where the character should look.
     When setting the gaze location:
     - x and y values are clamped to the interval [-1, 1]
     - z values are clamped to the interval [0, 1]
     - Negative x values look left, positive x values look right
     - Negative y values look up, positive y values look down
     - Smaller z values converge the eyes to look closer, while larger z values
     diverge eyes toward a parallel gaze

     @param animated YES if the character should animate the gaze change, NO for an
     immediate jump of the pupils.
     */
    public void lookAtPoint(Point3D point, boolean animated) {
        this.face.lookAtPoint(point, animated);
    }

    /**
     Tells the character to look at its default location (straight ahead).
     */
    public void lookAtDefault() {
        this.gaze = new Point3D(0.0f, 0.0f, 0.9f);
        this.face.lookAtDefault();
    }

    /**
     The character says a particular utterance
     */
    public void say(String utterance) {
        this.voice.mumbleWithUtterance(utterance);
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
