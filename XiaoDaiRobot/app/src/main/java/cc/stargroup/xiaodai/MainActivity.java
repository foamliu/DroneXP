package cc.stargroup.xiaodai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import static com.baidu.tts.client.SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85;
import static com.baidu.tts.client.SpeechSynthesizer.AUDIO_ENCODE_AMR;
import static com.baidu.tts.client.SpeechSynthesizer.MIX_MODE_DEFAULT;

public class MainActivity extends AppCompatActivity implements SpeechSynthesizerListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SpeechSynthesizer speechSynthesizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {

        speechSynthesizer = SpeechSynthesizer.getInstance();
        speechSynthesizer.setContext(this.getApplicationContext());
        speechSynthesizer.setSpeechSynthesizerListener(this);
        //speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, TEXT_MODEL_FILE_FULL_PATH_NAME);
        //speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, SPEECH_MODEL_FILE_FULL_PATH_NAME);
        //speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, LICENSE_FILE_FULL_PATH_NAME);
        speechSynthesizer.setAppId("8797664");
        speechSynthesizer.setApiKey("G56bCafsmLnBo3RWTgpl7xDG", "6787e3625d5f291e92f19f11500ab396");
        AuthInfo authInfo = speechSynthesizer.auth(TtsMode.MIX);
        speechSynthesizer.initTts(TtsMode.MIX);
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        speechSynthesizer.setParam(SpeechSynthesizer. PARAM_MIX_MODE, MIX_MODE_DEFAULT);
        speechSynthesizer.setParam(SpeechSynthesizer. PARAM_AUDIO_ENCODE, AUDIO_ENCODE_AMR);
        speechSynthesizer.setParam(SpeechSynthesizer. PARAM_AUDIO_RATE, AUDIO_BITRATE_AMR_15K85);
        speechSynthesizer.setParam(SpeechSynthesizer. PARAM_VOCODER_OPTIM_LEVEL, "0");

        speechSynthesizer.speak("你好");
    }

    @Override
    public void onSynthesizeStart(String s) {
        Log.d(TAG, "onSynthesizeStart " + s);
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
        Log.d(TAG, "onSynthesizeDataArrived " + s + i);
    }

    @Override
    public void onSynthesizeFinish(String s) {
        Log.d(TAG, "onSynthesizeFinish " + s);
    }

    @Override
    public void onSpeechStart(String s) {
        Log.d(TAG, "onSpeechStart " + s);
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {
        Log.d(TAG, "onSpeechProgressChanged " + s + i);
    }

    @Override
    public void onSpeechFinish(String s) {
        Log.d(TAG, "onSpeechFinish " + s);
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        Log.d(TAG, "onError " + s + speechError.description);
    }
}
