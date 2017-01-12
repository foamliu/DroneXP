package cc.stargroup.xiaodai;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by Foam on 2016/10/26.
 */

public class RobotApplication extends Application {
    private Logger logger = new Logger();

    @Override
    public void onCreate() {
        super.onCreate();

        // Setup handler for uncaught exceptions.
//        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
//        {
//            @Override
//            public void uncaughtException (Thread thread, Throwable e)
//            {
//                logger.appendLog(e.getMessage());
//                logger.appendLog(e.toString());
//            }
//        });
    }
}
