package cc.stargroup.xiaodai;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import cc.stargroup.xiaodai.character.Character;

/**
 * Created by Foam on 2017/1/10.
 */

public class DrawView extends View {
    private Character character;

    public DrawView(Context context, Character character) {
        super(context);

        this.character = character;

        setBackgroundColor(Color.parseColor("#00CCED"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        character.drawSelf(canvas);
    }

}
