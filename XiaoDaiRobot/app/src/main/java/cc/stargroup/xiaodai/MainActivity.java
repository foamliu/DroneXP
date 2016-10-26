package cc.stargroup.xiaodai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.baidu.tts.client.SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85;
import static com.baidu.tts.client.SpeechSynthesizer.AUDIO_ENCODE_AMR;
import static com.baidu.tts.client.SpeechSynthesizer.MIX_MODE_DEFAULT;

public class MainActivity extends AppCompatActivity implements SpeechSynthesizerListener, View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SpeechSynthesizer speechSynthesizer;

    private Button mWeatherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initSynthesizer();

    }

    private void initUI() {
        mWeatherBtn = (Button) findViewById(R.id.btn_weather);
        mWeatherBtn.setOnClickListener(this);
    }

    private void initSynthesizer() {

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

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_weather: {
                doWeather();

                break;
            }
            default:
                break;
        }
    }

    private void doWeather() {
        String url = "http://restapi.amap.com/v3/weather/weatherInfo?city=310000&key=543cf533c1988351e6df98030eca10f2";
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            /*
                            {"status":"1","count":"1","info":"OK","infocode":"10000",
                            "lives":[{"province":"上海","city":"上海市","adcode":"310000","weather":"中雨","temperature":"19",
                            "winddirection":"东北","windpower":"4","humidity":"94","reporttime":"2016-10-26 18:00:00"}]}
                             */
                            JSONArray lives = response.getJSONArray("lives");
                            JSONObject result = lives.getJSONObject(0);
                            String city = result.getString("city");
                            String weather = result.getString("weather");
                            String temperature = result.getString("temperature");
                            String winddirection = result.getString("winddirection");
                            String windpower = result.getString("windpower");
                            String humidity = result.getString("humidity");

                            StringBuilder sb = new StringBuilder();
                            sb.append(city);
                            sb.append("今天" + weather + ",");
                            sb.append("平均" + temperature + "摄氏度" + ",");
                            sb.append(winddirection + "风" + windpower + "级" + ".");

                            speechSynthesizer.speak(sb.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }
}
