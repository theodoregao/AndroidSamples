package gao.shun.sg.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import timber.log.Timber;

/**
 * Created by Theodore on 2017/10/30.
 */

public class Emojifier {

    public enum EmojiType {
        CLOSED_EYE_FROWN(0),
        RIGHT_WINK(1),
        LEFT_WINK(2),
        FROWN(3),
        CLOSED_EYE_SMILE(4),
        RIGHT_WINK_FROWN(5),
        LEFT_WINK_FROWN(6),
        SMILE(7),
        UNKNOWN(-1);

        private int index;

        EmojiType(int index) {
            this.index = index;
        }

        public static EmojiType getEmojiType(int index) {
            for (EmojiType emojiType: EmojiType.values()) {
                if (emojiType.index == index) return emojiType;
            }
            return UNKNOWN;
        }
    }

    public static Bitmap detectFaces(Context context, Bitmap bitmap) {
        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        SparseArray<Face> faces = faceDetector.detect(frame);

        Bitmap resultBitmap = bitmap;

        // Log the number of faces
        Timber.v("detectFaces: number of faces = " + faces.size());

        // If there are no faces detected, show a Toast message
        if (faces.size() == 0){
            Toast.makeText(context, "There is no faces detected in this image", Toast.LENGTH_SHORT).show();
        }
        else for (int i = 0; i < faces.size(); i++) {
            Face face = faces.get(i);

            if (face == null) {
                Timber.w("face is null, continue");
                continue;
            }

            int index = 0;
            Timber.v("left eye open: " + face.getIsLeftEyeOpenProbability());
            index += face.getIsLeftEyeOpenProbability() >= 0.5 ? 1 : 0;
            Timber.v("right eye open: " + face.getIsRightEyeOpenProbability());
            index += face.getIsRightEyeOpenProbability() >= 0.5 ? 2 : 0;
            Timber.v("smile: " + face.getIsSmilingProbability());
            index += face.getIsSmilingProbability() >= 0.2 ? 4 : 0;

            EmojiType emojiType = EmojiType.getEmojiType(index);
            Timber.v("face type: " + emojiType);

            Bitmap emojiBitmap;

            switch (emojiType) {
                case SMILE:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.smile);
                    break;
                case FROWN:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.frown);
                    break;
                case LEFT_WINK:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.leftwink);
                    break;
                case RIGHT_WINK:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.rightwink);
                    break;
                case LEFT_WINK_FROWN:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.leftwinkfrown);
                    break;
                case RIGHT_WINK_FROWN:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.rightwinkfrown);
                    break;
                case CLOSED_EYE_SMILE:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.closed_smile);
                    break;
                case CLOSED_EYE_FROWN:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.closed_frown);
                    break;
                default:
                    emojiBitmap = null;
                    Toast.makeText(context, R.string.no_emoji, Toast.LENGTH_SHORT).show();
            }

            resultBitmap = addBitmapToFace(resultBitmap, emojiBitmap, face);
        }

        faceDetector.release();

        return resultBitmap;
    }

    private static Bitmap addBitmapToFace(Bitmap backgroundBitmap, Bitmap emojiBitmap, Face face) {
        Bitmap resultBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), backgroundBitmap.getConfig());

        float scaleFactor = 0.9f;
        int newEmojiWidth = (int) (face.getWidth() * scaleFactor);
        int newEmojiHeight = (int) (emojiBitmap.getHeight() * newEmojiWidth / emojiBitmap.getWidth() * scaleFactor);

        emojiBitmap = Bitmap.createScaledBitmap(emojiBitmap, newEmojiWidth, newEmojiHeight, false);

        float emojiPositionX = (face.getPosition().x + face.getWidth() / 2) - emojiBitmap.getWidth() / 2;
        float emojiPositionY = (face.getPosition().y + face.getHeight() / 2) - emojiBitmap.getHeight() / 3;

        // Create the canvas and draw the bitmaps to it
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        canvas.drawBitmap(emojiBitmap, emojiPositionX, emojiPositionY, null);

        return resultBitmap;
    }

}
