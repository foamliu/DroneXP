package cc.stargroup.xiaodai;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;

public class ControllerActivity extends Activity implements View.OnClickListener {
    private RobotController robotController;
    private static String address = "AB:0D:AE:56:34:02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        robotController = new RobotController();

        init();
    }

    private void init() {
        findViewById(R.id.left).setOnClickListener(this);
        findViewById(R.id.right).setOnClickListener(this);
        findViewById(R.id.forward).setOnClickListener(this);
        findViewById(R.id.backward).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.spin_left).setOnClickListener(this);
        findViewById(R.id.spin_right).setOnClickListener(this);
        findViewById(R.id.device_list).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            robotController.init(address);
        } catch(Exception ex)
        {
            Log.e("", ex.toString());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.left:
                robotController.left();
                break;
            case R.id.right:
                robotController.left();
                break;
            case R.id.forward:
                robotController.forward();
                break;
            case R.id.backward:
                robotController.backward();
                break;
            case R.id.stop:
                robotController.stop();
                break;
            case R.id.spin_left:
                robotController.spin_left();
                break;
            case R.id.spin_right:
                robotController.spin_right();
                break;
            case R.id.device_list:
                Intent i = new Intent(ControllerActivity.this, DeviceList.class);
                //Change the activity.
                startActivity(i);
                break;
            case R.id.connect:
                robotController.init(address);
                break;
        }
    }
}
