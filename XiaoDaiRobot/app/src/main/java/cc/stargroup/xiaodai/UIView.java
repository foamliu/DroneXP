package cc.stargroup.xiaodai;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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

}
