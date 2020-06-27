package sl770.csci366au20.ciexy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DrawActivity extends Drawable {

    private Bitmap bitmap;
    private double [] x;
    private double [] y;
    private Paint paint;
    public DrawActivity(Bitmap bitmap,Paint paint,double [] x, double [] y){
        this.bitmap = bitmap;
        this.x  = x;
        this.y = y;
        this.paint = paint;
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        int height = this.bitmap.getHeight();
        int width = this.bitmap.getWidth();
        paint.setStrokeWidth(3);

        paint.setColor(Color.WHITE);
        for (int i = 0; i < x.length; i++) {
            float startX = (float) x[i] * width;
            float startY = (float) y[i] * height;
            float endX, endY;
            if (i + 1 == x.length) {
                endX = (float) x[0] * width;
                endY = (float) y[0] * height;
            } else {
                endX = (float) x[i + 1] * width;
                endY = (float) y[i + 1] * height;
            }
            canvas.drawLine(startX, startY, endX, endY, paint);
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}
