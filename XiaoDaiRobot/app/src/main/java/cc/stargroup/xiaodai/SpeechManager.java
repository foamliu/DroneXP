package cc.stargroup.xiaodai;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.speech.VoiceRecognitionService;
import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static android.os.MessageQueue.OnFileDescriptorEventListener.EVENT_ERROR;
import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;
import static com.baidu.tts.client.SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85;
import static com.baidu.tts.client.SpeechSynthesizer.AUDIO_ENCODE_AMR;
import static com.baidu.tts.client.SpeechSynthesizer.MIX_MODE_DEFAULT;

/**
 * Created by Foam on 2017/1/9.
 */

public class SpeechManager implements SpeechSynthesizerListener, RecognitionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context appContext;
    private SpeechSynthesizer speechSynthesizer;
    private SpeechRecognizer speechRecognizer;

    public SpeechManager(Context context) {
        this.appContext = context;
    }

    public void initSynthesizer() {

        speechSynthesizer = SpeechSynthesizer.getInstance();
        speechSynthesizer.setContext(appContext);
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

    public void initRecognizer() {
        // 创建识别器
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(appContext, new ComponentName(appContext, VoiceRecognitionService.class));
        // 注册监听器
        speechRecognizer.setRecognitionListener(this);
        //startASR();
    }



    // 开始识别
    void startASR() {
        Intent intent = new Intent();
        bindParams(intent);
        speechRecognizer.startListening(intent);
    }
    void bindParams(Intent intent) {
        // 设置识别参数

    }

    public void doWeather() {
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

        Volley.newRequestQueue(appContext).add(jsonRequest);

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
    @Override
    public void onReadyForSpeech(Bundle params) {
        // 准备就绪
        Log.d(TAG, "onReadyForSpeech ");
    }
    @Override
    public void onBeginningOfSpeech() {
        // 开始说话处理
        Log.d(TAG, "onBeginningOfSpeech ");
    }
    public void onRmsChanged(float rmsdB) {
        // 音量变化处理
        Log.d(TAG, "onRmsChanged ");
    }
    public void onBufferReceived(byte[] buffer) {
        // 录音数据传出处理
        Log.d(TAG, "onBufferReceived ");
    }
    public void onEndOfSpeech() {
        // 说话结束处理
        Log.d(TAG, "onEndOfSpeech ");
    }
    public void onError(int error) {
        // 出错处理
        Log.d(TAG, "onError ");

        StringBuilder sb = new StringBuilder();
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                sb.append("音频问题");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                sb.append("没有语音输入");
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                sb.append("其它客户端错误");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                sb.append("权限不足");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                sb.append("网络问题");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                sb.append("没有匹配的识别结果");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                sb.append("引擎忙");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                sb.append("服务端错误");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                sb.append("连接超时");
                break;
        }
        sb.append(":" + error);
        Log.e(TAG, "识别失败：" + sb.toString());
    }

    public void onResults(Bundle results) {
        // 最终结果处理
        Log.d(TAG, "onResults ");

        ArrayList<String> nbest = results.getStringArrayList(RESULTS_RECOGNITION);
        Log.d(TAG, "识别成功：" + Arrays.toString(nbest.toArray(new String[nbest.size()])));
    }

    public void onPartialResults(Bundle partialResults) {
        // 临时结果处理
        Log.d(TAG, "onPartialResults ");

        ArrayList<String> nbest = partialResults.getStringArrayList(RESULTS_RECOGNITION);
        if (nbest.size() > 0) {
            Log.e(TAG, "~临时识别结果：" + Arrays.toString(nbest.toArray(new String[0])));
        }
    }

    public void onEvent(int eventType, Bundle params) {
        // 处理事件回调
        Log.d(TAG, "onEvent ");

        switch (eventType) {
            case EVENT_ERROR:
                String reason = params.get("reason") + "";
                Log.e(TAG, "EVENT_ERROR, " + reason);
                break;
        }
    }

}
