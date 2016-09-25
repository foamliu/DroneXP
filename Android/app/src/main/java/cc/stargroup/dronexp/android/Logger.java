package cc.stargroup.dronexp.android;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Foam on 2016/9/25.
 */
public class Logger {

    public void appendLog(String text)
    {
        File logFile = new File(Environment.getExternalStorageDirectory(), "DroneXP.log");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        BufferedWriter buf = null;
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if (buf != null) {
                try {
                    buf.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
