package sl770.csci366au20.task1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText editText;
    private Bitmap bitmap;
    private static final int REQUEST_IMAGE_PICK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        editText = findViewById(R.id.rValue);
    }
    public void pickImage(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,REQUEST_IMAGE_PICK);
    }
    public void onClickProcessing(View view){
        if(bitmap!= null){
            EditText rValue = findViewById(R.id.rValue);
            Bitmap processedBitmap;
            String rVal = rValue.getText().toString();
            if(rVal.isEmpty()){
                processedBitmap = PowerLaw.processing(this.bitmap);
            }else{
                double r = Double.valueOf(rVal);
                processedBitmap = PowerLaw.processing(this.bitmap,r);
            }
            imageView.setImageBitmap(processedBitmap);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            try (InputStream stream = getContentResolver().openInputStream(data.getData());) {
                // recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                bitmap = BitmapFactory.decodeStream(stream);
                imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
