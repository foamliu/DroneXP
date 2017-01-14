package cc.stargroup.xiaodai.character;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import java.io.IOException;
import cc.stargroup.xiaodai.utilities.Util;

/**
 * Created by Foam on 2017/1/10.
 */

public class CharacterVoice {

    public final static int kNumFarts = 19;
    public final static int kNumMumbles = 21;
    public final static int kNumBlinks = 5;

    private CharacterType characterType;
    private CharacterEmotion emotion;
    private CharacterExpression expression;

    private Context appContext;
    //private MediaPlayer mediaPlayer;
    private boolean initialized;

    private boolean fading = false;


    public CharacterVoice(Context context) {
        this.appContext = context;
        this.characterType = CharacterType.XiaoDaiRobot;
        this.initialized = true;
    }

    private static CharacterVoice sharedInstance;

    public static CharacterVoice sharedInstance(Context context) {
        if (sharedInstance == null) {
            sharedInstance = new CharacterVoice(context);
        }

        return sharedInstance;
    }

    public void setExpression(CharacterExpression expression) {
        this.expression = expression;
        String path = null;

        if (this.initialized) {
        try {

            if (expression == CharacterExpression.Fart) {
                int randomSound = Util.nextRandomInteger(0, kNumFarts - 1);
                path = String.format("audio/%d-%02d.mp3", expression.getValue(), randomSound);
            } else {
                path = String.format("audio/%d.mp3", expression.getValue());
            }

            AssetFileDescriptor afd = this.appContext.getAssets().openFd(path);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    public void mumbleWithUtterance(String utterance) {
        int randomSound = Util.nextRandomInteger(0, kNumMumbles - 1);
        String path = String.format("audio/mumbles/mumble%d.mp3", randomSound);
        AssetFileDescriptor afd = null;
        try {
            afd = this.appContext.getAssets().openFd(path);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeBlinkSound() {
        int randomSound = Util.nextRandomInteger(0, kNumBlinks - 1);
        String path = String.format("audio/blinks/Creature-Blink-%d.mp3", randomSound);
        AssetFileDescriptor afd = null;
        try {
            afd = this.appContext.getAssets().openFd(path);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFading(boolean fading) {
        if (this.fading != fading) {
            this.fading = fading;
            this.fadeOut();
        }
    }

    public void fadeOut() {

    }
}
