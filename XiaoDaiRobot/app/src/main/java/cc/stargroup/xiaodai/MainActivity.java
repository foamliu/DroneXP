package cc.stargroup.xiaodai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import cc.stargroup.xiaodai.character.CharacterEmotion;
import cc.stargroup.xiaodai.character.CharacterExpression;
import cc.stargroup.xiaodai.widget.UIView;

import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private SpeechManager speechManager;
    private RobotController robotController;
    private boolean ledOn = false;

    private cc.stargroup.xiaodai.character.Character character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        speechManager = new SpeechManager(this.getApplicationContext());
        speechManager.initSynthesizer();
        speechManager.initRecognizer();

        robotController = new RobotController();
        character = new cc.stargroup.xiaodai.character.Character(this);

        UIView superview = new UIView(this);
        //superview.setCharacter(character);
        character.addToSuperview(superview);
        setContentView(superview);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

        String address = getIntent().getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device
        if (robotController != null && address != null) {
            robotController.init(address);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.weather:
                speechManager.doWeather();
                return true;
            case R.id.recognize:
                start();
                return true;
            case R.id.deviceList:
                // Make an intent to start next activity.
                Intent i = new Intent(MainActivity.this, DeviceList.class);
                //Change the activity.
                startActivity(i);
                return true;

            case R.id.toggleLed:
                if (ledOn) {
                    robotController.turnOffLed();
                    ledOn = false;
                } else {
                    robotController.turnOnLed();
                    ledOn = true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static final int REQUEST_UI = 1;

    private void start() {
        Intent recognizerIntent = new Intent();
        recognizerIntent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
        // recognizerIntent.put("...", "...") TODO 为recognizerIntent设置参数，支持的参数见本文档的“识别参数”一节
        recognizerIntent.putExtra("sample", 16000); // 离线仅支持16000采样率
        recognizerIntent.putExtra("language", "cmn-Hans-CN"); // 离线仅支持中文普通话
        recognizerIntent.putExtra("prop", 20000); // 输入
        //    intent.putExtra("prop", 10060); // 地图
        //    intent.putExtra("prop", 10001); // 音乐
        //    intent.putExtra("prop", 10003); // 应用
        //    intent.putExtra("prop", 10008); // 电话
        //    intent.putExtra("prop", 100014); // 联系人
        //    intent.putExtra("prop", 100016); // 手机设置
        //    intent.putExtra("prop", 100018); // 电视指令
        //    intent.putExtra("prop", 100019); // 播放器指令
        //    intent.putExtra("prop", 100020); // 收音机指令
        //    intent.putExtra("prop", 100021); // 命令词
        // 为了支持离线识别能力，请参考“离线语音识别参数设置”一节

        startActivityForResult(recognizerIntent, REQUEST_UI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RESULTS_RECOGNITION);
            // data.get... TODO 识别结果包含的信息见本文档的“结果解析”一节
            showToast("识别成功：" + Arrays.toString(results.toArray(new String[results.size()])));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(TAG,"Action was DOWN");
                this.character.setExpressionWithEmotion(CharacterExpression.Angry, CharacterEmotion.Curious);
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d(TAG,"Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.d(TAG,"Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d(TAG,"Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d(TAG,"Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

}
