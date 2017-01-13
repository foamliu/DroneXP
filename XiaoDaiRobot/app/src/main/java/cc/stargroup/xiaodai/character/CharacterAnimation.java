package cc.stargroup.xiaodai.character;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cc.stargroup.xiaodai.utilities.Util;
import cc.stargroup.xiaodai.widget.UIImageView;

/**
 * Created by Foam on 2017/1/11.
 */

public class CharacterAnimation {
    private Context appContext;
    private UIImageView sprite;
    private List<JsonFrame> crop;

    private boolean animating;
    private int breakpointFrame;

    private int frameCount;
    private int accumulatedFrames;

    private long startTime;
    private long endTime;

    private boolean reversed = false;
    private String prefix;
    private int index;

    private Bitmap staticImage;
    private Bitmap image;

    private CompletionCallback completion;

    public CharacterAnimation(Context context) {
        this.appContext = context;
        this.sprite = new UIImageView();

    }

    public void startAnimating() {
        startTime = System.currentTimeMillis();
        endTime = startTime + (long) ((1.0 / 24.0) * frameCount * 1000);

        this.animating = true;
    }

    public void stopAnimating() {
        this.animating = false;
        this.sprite.setImage(null);
        this.crop = null;
        this.reversed = false;

        if (this.completion != null) {
            this.completion.OnCompletion(true);
        }
    }

    public void nextFrame() {

    }

    /**
     * Some expressions don't include a blink-through transition for either the intro, outro, or both
     * And instead, they reuse the blink-through transition from an emotion
     * e.g. Curious expression should use Curious emotion's blink-through for both intro & outro
     */
    public String prefixWithActionForExpression(AnimatedAction action, CharacterExpression expression) {
        if (action == AnimatedAction.Intro || action == AnimatedAction.Outro) {
            switch (expression) {
                case Angry:
                    return "";

                case Bored:
                    return "";

                case Chuckle:
                    return "";

                case Curious:
                    return "Romo_Emotion_Transition_1";

                case Dizzy:
                    return "";

                case Embarrassed:
                    return "";

                case Excited:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_2";

                case Exhausted:
                    return "";

                case Fart:
                    if (action == AnimatedAction.Outro) return "";
                    return "Romo_Emotion_Transition_3";

                case Happy:
                case Proud:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_3";

                case Hiccup:
                    return "";

                case Bewildered:
                    return "Romo_Emotion_Transition_9";

                case HoldingBreath:
                    return "";

                case Laugh:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_2";

                case LookingAround:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_1";

                case Love:
                    return "";

                case Ponder:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_1";

                case Sad:
                case LetDown:
                    return "Romo_Emotion_Transition_4";

                case Scared:
                    return "Romo_Emotion_Transition_5";

                case Sleepy:
                    return "Romo_Emotion_Transition_6";

                case Sneeze:
                    return "";

                case Talking:
                    return "";

                case Want:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_10";

                case Yawn:
                    if (action == AnimatedAction.Intro) return "";
                    return "Romo_Emotion_Transition_6";

                case Yippee:
                    return "";

                default:
                    return "";
            }
        }
        return "";
    }

    private Bitmap spriteSheetWithPrefix(int index, String prefix) {
        if (index == 1) {
            frameCount = 0;

            while (true) {

            }
        }
        return null;
    }

    private List<JsonFrame> loadEmotionMetaData(CharacterEmotion emotion) {
        String fileName = String.format("animations/Emotions/%s/Romo_Emotion_Transition_%d_1.json", emotion.toString(), emotion.getValue());
        String strJson = Util.loadJsonFromAssetsFile(appContext, fileName);
        List<JsonFrame> frames = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(strJson);
            JSONArray jsonarray = json.getJSONArray("frames");
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                JSONObject frame = jsonobject.getJSONObject("frame");
                boolean rotated = jsonobject.getBoolean("rotated");
                JSONObject spriteSourceSize = jsonobject.getJSONObject("spriteSourceSize");

                int frameH = frame.getInt("h");
                int frameW = frame.getInt("w");
                int frameX = frame.getInt("x");
                int frameY = frame.getInt("y");
                int spriteSourceSizeH = spriteSourceSize.getInt("h");
                int spriteSourceSizeW = spriteSourceSize.getInt("w");
                int spriteSourceSizeX = spriteSourceSize.getInt("x");
                int spriteSourceSizeY = spriteSourceSize.getInt("y");

                frames.add(new JsonFrame(new JsonRect(frameH, frameW, frameX, frameY), rotated, new JsonRect(spriteSourceSizeH, spriteSourceSizeW, spriteSourceSizeX, spriteSourceSizeY)));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return frames;
    }

    private List<JsonFrame> loadExpressionMetaData(CharacterExpression expression) {
        String fileName = String.format("animations/Expressions/%s/Romo_Expression_%d_1.json", expression.toString(), expression.getValue());
        String strJson = Util.loadJsonFromAssetsFile(appContext, fileName);
        List<JsonFrame> frames = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(strJson);
            JSONArray jsonarray = json.getJSONArray("frames");
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                JSONObject frame = jsonobject.getJSONObject("frame");
                boolean rotated = jsonobject.getBoolean("rotated");
                //boolean trimmed = jsonobject.getBoolean("trimmed");
                JSONObject spriteSourceSize = jsonobject.getJSONObject("spriteSourceSize");
                //JSONObject sourceSize = jsonobject.getJSONObject("sourceSize");

                int frameH = frame.getInt("h");
                int frameW = frame.getInt("w");
                int frameX = frame.getInt("x");
                int frameY = frame.getInt("y");
                int spriteSourceSizeH = spriteSourceSize.getInt("h");
                int spriteSourceSizeW = spriteSourceSize.getInt("w");
                int spriteSourceSizeX = spriteSourceSize.getInt("x");
                int spriteSourceSizeY = spriteSourceSize.getInt("y");

                frames.add(new JsonFrame(new JsonRect(frameH, frameW, frameX, frameY), rotated, new JsonRect(spriteSourceSizeH, spriteSourceSizeW, spriteSourceSizeX, spriteSourceSizeY)));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return frames;
    }

    public void animateWithActionForEmotion(AnimatedAction action, CharacterEmotion emotion, CompletionCallback completion) {
        this.completion = completion;
        this.reversed = false;

        switch (action) {
            case Outro:
                reversed = true;
            case Intro:
                if (emotion != CharacterEmotion.Sleeping) {
                    prefix = String.format("Romo_Emotion_Transition_%d", emotion.getValue());
                } else {
                    if (this.completion != null) {
                        this.completion.OnCompletion(true);
                    }
                    return;
                }
                break;

            case IdleEmotion:
                break;

            case Blink:

                prefix = String.format("Romo_Emotion_Blink_%d", emotion.getValue());
                break;

            default:
                return;
        }

        this.crop = loadEmotionMetaData(emotion);
        this.frameCount = this.crop.size();
        String fileName = String.format("animations/Emotions/%s/Romo_Emotion_Transition_%d_1@2x.png", emotion.toString(), emotion.getValue());
        this.sprite.setImage(Util.loadImageFromAssetsFile(appContext, fileName));
        this.startAnimating();
    }

    public void animateWithActionForExpression(AnimatedAction action, CharacterExpression expression, CompletionCallback completion) {

        this.completion = completion;

        switch (action) {
            case Outro:
                reversed = true;
            case Intro:
                this.prefix = this.prefixWithActionForExpression(action, expression);
                if (prefix.length() == 0) {
                    this.completion.OnCompletion(true);
                    return;
                }
                break;

            case Expression:
                this.prefix = String.format("Romo_Expression_%d", expression.getValue());
                break;

            default:
                return;
        }

        this.crop = loadExpressionMetaData(expression);
        this.frameCount = this.crop.size();
        String fileName = String.format("animations/Expressions/%s/Romo_Expression_%d_1@2x.png", expression.toString(), expression.getValue());
        this.sprite.setImage(Util.loadImageFromAssetsFile(appContext, fileName));
        this.startAnimating();
    }

    void setImage(Bitmap image) {
        if (animating) {
            this.staticImage = image;
        } else {
            this.image = image;
        }
    }

    public void drawSelf(Canvas canvas) {
        if (animating) {
            long curTime = System.currentTimeMillis();
            int currentFrame = (int) (24.0 * (curTime - startTime) / 1000) - accumulatedFrames;
            if (curTime > endTime || currentFrame >= frameCount + accumulatedFrames) {
                this.stopAnimating();
            } else if (currentFrame >= crop.size()) {
                index++;
                accumulatedFrames += crop.size();
                breakpointFrame -= crop.size();
                sprite.setImage(spriteSheetWithPrefix(index, prefix));
                if (sprite.image() != null) {
                    this.nextFrame();
                } else {
                    this.stopAnimating();
                }
            } else {
                if (currentFrame >= this.breakpointFrame && this.breakpointFrame >= 0) {
                    //[self.delegate animationReachedBreakpointAtFrame:currentFrame];
                    breakpointFrame = -1;
                }

                int frameIndex = reversed ? (crop.size() - currentFrame - 1) : currentFrame;

                JsonFrame jsonFrame = crop.get(frameIndex);
                JsonRect frame = jsonFrame.frame();
                JsonRect sourceFrame = jsonFrame.spriteSourceSize();
                float w = frame.w();
                float h = frame.h();
                float x = frame.x();
                float y = frame.y();
                float drawX = sourceFrame.x();
                float drawY = sourceFrame.y();

                canvas.drawBitmap(sprite.image(), new Rect((int) x, (int) y, (int) (x + w), (int) (y + h)), new Rect((int) drawX, (int) drawY, (int) (drawX + w), (int) (drawY + h)), null);
            }

        } else {

        }
    }

    class JsonRect {

        private float h, w, x, y;

        public float h() {
            return h;
        }

        public float w() {
            return w;
        }

        public float x() {
            return x;
        }

        public float y() {
            return y;
        }

        public void setH(float h) {
            this.h = h;
        }

        public void setW(float w) {
            this.w = w;
        }

        public void setX(float x) {
            this.x = x;
        }

        public void setY(float y) {
            this.y = y;
        }

        public JsonRect(float h, float w, float x, float y) {
            this.h = h;
            this.w = w;
            this.x = x;
            this.y = y;
        }
    }

    class JsonFrame {

        private JsonRect frame;
        private boolean rotated;
        private JsonRect spriteSourceSize;

        public JsonRect frame() {
            return frame;
        }

        public boolean rotated() {
            return rotated;
        }

        public JsonRect spriteSourceSize() {
            return spriteSourceSize;
        }

        public void setFrame(JsonRect frame) {
            this.frame = frame;
        }

        public void setRotated(boolean rotated) {
            this.rotated = rotated;
        }

        public void setSpriteSourceSize(JsonRect spriteSourceSize) {
            this.spriteSourceSize = spriteSourceSize;
        }

        public JsonFrame(JsonRect frame, boolean rotated, JsonRect spriteSourceSize) {
            this.frame = frame;
            this.rotated = rotated;
            this.spriteSourceSize = spriteSourceSize;
        }
    }
}
