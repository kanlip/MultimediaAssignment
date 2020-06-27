package sl770.csci366au20.ciexy;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ImageView imgView;
    private Bitmap bitmap;
    private EditText edRX, edGY, edZB;
    private Button button;
    private RadioButton rgbToxyz, xyzTorgb;
    private double[] x = {0.1756, 0.1752, 0.1748,
            0.1745, 0.1741, 0.1740,
            0.1738, 0.1736, 0.1733,
            0.1730, 0.1726, 0.1721,
            0.1714, 0.1703, 0.1689,
            0.1669, 0.1644, 0.1611,
            0.1566, 0.1510, 0.1440,
            0.1355, 0.1241, 0.1096,
            0.0913, 0.0687, 0.0454,
            0.0235, 0.0082, 0.0039,
            0.0139, 0.0389, 0.0743,
            0.1142, 0.1547, 0.1929, 0.2296,
            0.2658, 0.3016, 0.3374, 0.3731,
            0.4087, 0.4441, 0.4788, 0.5125, 0.5448,
            0.5752, 0.6029, 0.6270, 0.6482, 0.6658,
            0.6801, 0.6915, 0.7006, 0.7079, 0.7140,
            0.7190, 0.7230, 0.7260, 0.7283,
            0.7300, 0.7311, 0.7320, 0.7327, 0.7334, 0.7340, 0.7344, 0.7346, 0.7347, 0.7347, 0.7347,
            0.7347, 0.7347, 0.7347, 0.7347, 0.7347, 0.7347, 0.7347, 0.7347, 0.7347, 0.7347, 0.7347,
            0.7347, 0.7347, 0.7347, 0.7347, 0.7347, 0.7347, 0.7347, 0.7347, 0.7347
            , 0.7347, 0.7347, 0.7347, 0.7347};
    private double[] y = {0.0053, 0.0053, 0.0052, 0.0052, 0.0050, 0.0050, 0.0049, 0.0049, 0.0048, 0.0048, 0.0048, 0.0048, 0.0051, 0.0058, 0.0069,
            0.0086, 0.0109, 0.0138, 0.0177, 0.0227, 0.0297, 0.0399, 0.0578, 0.0868, 0.1327, 0.2007, 0.2950, 0.4127, 0.5384, 0.6548, 0.7502
            , 0.8120, 0.8338, 0.8262, 0.8059, 0.7816, 0.7543, 0.7243, 0.6923, 0.6588, 0.6245, 0.5896, 0.5547, 0.5202, 0.4866, 0.4544,
            0.4242, 0.3965, 0.3725, 0.3514, 0.3340, 0.3197, 0.3083, 0.2993, 0.2920, 0.2859, 0.2809, 0.2769, 0.2740, 0.2717, 0.2700,
            0.2689, 0.2680, 0.2673, 0.2666, 0.2660, 0.2656, 0.2654, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653,
            0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653, 0.2653,
            0.2653, 0.2653, 0.2653, 0.2653};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = (ImageView) findViewById(R.id.cie);
        bitmap = Bitmap.createBitmap(480, 480, Bitmap.Config.ARGB_8888);
        edRX = (EditText) findViewById(R.id.RXEditText);
        edGY = (EditText) findViewById(R.id.YGEditText);
        edZB = (EditText) findViewById(R.id.ZBEditText);
        button = (Button) findViewById(R.id.ButtonConvert);
        rgbToxyz = (RadioButton) findViewById(R.id.RgbtoXyz);
        xyzTorgb = (RadioButton) findViewById(R.id.XyztoRgb);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                float value1 = Float.valueOf(edRX.getText().toString().isEmpty()? "0.0": edRX.getText().toString());
                float value2 = Float.valueOf(edGY.getText().toString().isEmpty()? "0.0": edGY.getText().toString());
                float value3 = Float.valueOf(edZB.getText().toString().isEmpty()? "0.0": edZB.getText().toString());
                TextView resultRX = (TextView) findViewById(R.id.resultRX);
                TextView resultYG = (TextView) findViewById(R.id.resultGY);
                TextView resultBZ = (TextView) findViewById(R.id.resultBZ);
                float [] result;

                ConvertRGBXYZ conversion = conversion = new ConvertRGBXYZ(value1,value2,value3);
                if(rgbToxyz.isChecked()){
                    result = conversion.convertRGBtoXYZ(value1, value2, value3);
                    resultRX.setText("X =" + result[0]);
                    resultYG.setText("Y =" + result[1]);
                    resultBZ.setText("Z =" + result[2]);

                }else if(xyzTorgb.isChecked()){
                    result = conversion.convertXYZtoRGB(value1, value2, value3);
                    resultRX.setText("R =" + result[0]);
                    resultYG.setText("G =" + result[1]);
                    resultBZ.setText("B =" + result[2]);
                }

            }
        });
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStrokeWidth(3);
        generateRGB(bitmap,canvas,paint);
        DrawActivity drawActivity = new DrawActivity(bitmap,paint,x,y);
        drawActivity.draw(canvas);
        Bitmap rotatedBitmap = rotateBitmap(bitmap);
        imgView.setImageBitmap(rotatedBitmap);
    }
    public void onRadioClick(View v){
        switch (v.getId()){
            case R.id.RgbtoXyz:
                TextView rTextView = (TextView) findViewById(R.id.RX);
                rTextView.setText(getResources().getText(R.string.r));
                TextView gTextView = (TextView) findViewById(R.id.YG);
                gTextView.setText(getResources().getText(R.string.g));
                TextView bTextView = (TextView) findViewById(R.id.ZB);
                bTextView.setText(getResources().getText(R.string.b));
                break;
            case R.id.XyztoRgb:
                TextView xTextView = (TextView) findViewById(R.id.RX);
                xTextView.setText(getResources().getText(R.string.x));
                TextView yTextView = (TextView) findViewById(R.id.YG);
                yTextView.setText(getResources().getText(R.string.y));
                TextView zTextView = (TextView) findViewById(R.id.ZB);
                zTextView.setText(getResources().getText(R.string.z));
                break;
        }
    }
    protected Bitmap rotateBitmap(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postScale(1,-1,bitmap.getWidth()/2f, bitmap.getHeight()/2f);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }
    private void generateRGB(Bitmap bitmap,Canvas canvas, Paint paint) {
        float [] rgbColor;
        for(int i=0;i<=bitmap.getWidth();i++){
            for(int j=0;j<=bitmap.getHeight();j++){
                float x = i / (float)bitmap.getWidth();
                float y = j /  (float)bitmap.getHeight();
                float z = 1.0f - x - y;
                ConvertRGBXYZ conversion = new ConvertRGBXYZ(x,y,z);
                rgbColor = conversion.convertXYZtoRGB(x,y,z);
                if(rgbColor[0] < 0 || rgbColor[1] < 0 || rgbColor[2] <0){
                    //outside of gamut
                    paint.setColor(Color.BLACK);
                }
                else{
                    paint.setColor(Color.rgb(x,y,z));
                }
                canvas.drawPoint(i,j,paint);
            }
        }
    }
}
