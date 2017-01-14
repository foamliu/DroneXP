package cc.stargroup.xiaodai.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import cc.stargroup.xiaodai.character.Character;

/**
 * Created by Foam on 2017/1/10.
 */

public class UIView extends View {
    private Character character;

    public UIView(Context context) {
        super(context);

        setBackgroundColor(Color.parseColor("#00CCED"));

        runnable.run(); //this is the initial call to draw player at index 0
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (character != null) {
            character.drawSelf(canvas);
        }
    }

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable = new Runnable(){
        public void run(){
            invalidate(); //will trigger the onDraw
            handler.postDelayed(this, 40); //in 40 milli sec
        }
    };

}
