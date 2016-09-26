package cc.stargroup.dronexp.android;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            String t=format.format(new Date());
            //BufferedWriter for performance, true to set append to file flag
            buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(t);
            buf.append(" ");
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
