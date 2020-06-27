package sl770.csci366au20.task3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Bitmap bitmap;
    Random rand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        rand = new Random();
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.gui);
        imageView.setImageBitmap(bitmap);
    }
    public void onRadioClick(View v){
        switch (v.getId()){
            case R.id.radioGui:
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.gui);
                imageView.setImageBitmap(bitmap);
                break;
            case R.id.radioFlower :
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.flower);
                imageView.setImageBitmap(bitmap);
                break;
            case R.id.radioRandom :
                bitmap = Bitmap.createBitmap(1920,1080, Bitmap.Config.ARGB_8888);
                populatePixels(bitmap);
                Canvas canvas = new Canvas(bitmap);
                Paint paint  = new Paint();
                paint.setStrokeWidth(10);
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                int startX = 0;
                int startY = 0;
                paint.setColor(Color.RED);
                canvas.drawLine(startX,startY,width,height,paint);
                paint.setColor(Color.GREEN);
                canvas.drawLine(startX,height,width,startY,paint);
                imageView.setImageBitmap(bitmap);
                break;
        }

    }
    public void populatePixels(Bitmap bitmap){
        for(int i=0;i<bitmap.getWidth();i++){
            for(int j=0;j<bitmap.getHeight() ; j++){
                bitmap.setPixel(i,j, Color.rgb(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
            }
        }
    }
}
