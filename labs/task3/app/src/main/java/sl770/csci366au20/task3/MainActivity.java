package sl770.csci366au20.task3;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import java.io.IOException;
import java.io.InputStream;
public class MainActivity extends AppCompatActivity {
    boolean male = true;
    private static final int REQUEST_IMAGE_PICK = 1;
    private Bitmap bitmap;
    private Bitmap cpyBitmap;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profilePicture = findViewById(R.id.userpicture);
        final RadioGroup group1 = (RadioGroup) findViewById(R.id.radiogroup);
        group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.colorimage:
                        profilePicture.setImageBitmap(bitmap);
                        break;
                    case R.id.bayer:
                            bayerRawData(cpyBitmap);
                            profilePicture.setImageBitmap(cpyBitmap);

                        break;
                }
            }
        });
    }

    private void bayerRawData(Bitmap bitmap){
        if(bitmap != null){
            for(int i=0;i<bitmap.getWidth();i++){
                for(int j=0; j<bitmap.getHeight();j++){
                    Color color = bitmap.getColor(i,j);
                    //green
                    if(isGreen(i,j)) {
                        bitmap.setPixel(i,j,Color.rgb(0.0f,color.green(),0.0f));
                    }
                    else if(isBlue(i,j)){
                        bitmap.setPixel(i,j,Color.rgb(0.0f,0.0f,color.blue()));
                    }
                    else if(isRed(i,j)){
                        bitmap.setPixel(i,j,Color.rgb(color.red(),0.0f,0.0f));

                    }

                }
            }
        }
    }
    public boolean isGreen(int x, int y){
        return (x%2 == 0 && y%2==0)|| (x%2==1 && y%2==1);
    }
    public boolean isRed(int x, int y){
        return (x%2==1 && y % 2 == 0);
    }
    public boolean isBlue(int x,int y){
        return (x%2==0 && y % 2 == 1);
    }
    public void pickImage(View View) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK)
            try (InputStream stream = getContentResolver().openInputStream(data.getData());) {
                // recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }

                bitmap = BitmapFactory.decodeStream(stream);
                cpyBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                profilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}