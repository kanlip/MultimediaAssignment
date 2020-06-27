package sl770.csci366au20.histeqimage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ImageView userPicture;
    private static final int REQUEST_IMAGE_PICK = 1;
    private Bitmap bitmap;
    private Button normalImage;
    private Button histEq;
    private Bitmap histBitmap;
    private ToggleButton toggleScale;
    private TableLayout tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userPicture = findViewById(R.id.userPicture);
        normalImage = findViewById(R.id.normal);
        histEq = findViewById(R.id.histe);
        toggleScale = findViewById(R.id.toggleButton);
        tb = findViewById(R.id.moveGroup);
        tb.setVisibility(TableLayout.GONE);
        toggleScale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    userPicture.setScaleType(ImageView.ScaleType.CENTER);
                    tb.setVisibility(TableLayout.VISIBLE);
                }else{
                    userPicture.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    tb.setVisibility(TableLayout.GONE);
                }
            }
        });
    }
    public void pickImage (View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            try (InputStream stream = getContentResolver().openInputStream(data.getData());) {
                // recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }

                bitmap = BitmapFactory.decodeStream(stream);
                Histogram hist = new Histogram(bitmap);
                histBitmap = hist.equalization();
                userPicture.setImageBitmap(bitmap);
                normalImage.setTextColor(Color.RED);
                toggleScale.setChecked(false);
                //Scale image to fit and center the image view
                userPicture.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void onClickMove(View view){
        Matrix matrix = new Matrix(userPicture.getImageMatrix());
        switch(view.getId()){
            case R.id.left:
                matrix.postTranslate(5,0);
                break;
            case R.id.right:
                matrix.postTranslate(-5,0);
                break;
            case R.id.up:
                matrix.postTranslate(0,5);
                break;
            case R.id.down:
                matrix.postTranslate(0,-5);
                break;
        }
        userPicture.setScaleType(ImageView.ScaleType.MATRIX);
        userPicture.setImageMatrix(matrix);
    }
    public void onClickButton(View view){
        switch(view.getId()){
            case R.id.normal:
                userPicture.setImageBitmap(bitmap);
                normalImage.setTextColor(Color.RED);
                histEq.setTextColor(Color.BLACK);
                break;
            case R.id.histe:
                userPicture.setImageBitmap(histBitmap);
                normalImage.setTextColor(Color.BLACK);
                histEq.setTextColor(Color.RED);
        }
    }

}
