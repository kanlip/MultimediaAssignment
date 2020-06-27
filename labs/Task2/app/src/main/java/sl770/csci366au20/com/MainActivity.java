package sl770.csci366au20.com;

import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView button = (ImageView) findViewById(R.id.image);
        InputStream resource = getResources().openRawResource(R.raw.dog);
        Bitmap bitmap = BitmapFactory.decodeStream(resource);
        button.setBackground(new MyRoundCornerDrawable(bitmap));
    }
}
