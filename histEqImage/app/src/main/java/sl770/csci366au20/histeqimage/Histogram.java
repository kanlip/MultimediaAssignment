package sl770.csci366au20.histeqimage;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class Histogram {
    private Bitmap histBitmap;
    private Bitmap bitmap;
    public Histogram(Bitmap bitmap){
        this.bitmap = bitmap;
        this.histBitmap = bitmap.copy(bitmap.getConfig(),true);
    }
    public Bitmap equalization(){
        int[] redHist = new int[256];
        int[] blueHist = new int[256];
        int[] greenHist = new int[256];

        Arrays.fill(redHist,0);
        Arrays.fill(blueHist,0);
        Arrays.fill(greenHist,0);
        //CALCULATE histogram of the image
        for(int i=0;i<bitmap.getWidth();i++){
            for(int j=0;j<bitmap.getHeight();j++){
                redHist[Color.red(bitmap.getPixel(i,j))]++;
                blueHist[Color.blue(bitmap.getPixel(i,j))]++;
                greenHist[Color.green(bitmap.getPixel(i,j))]++;
            }
        }
        ArrayList<int[]> histogram = new ArrayList<>();
        histogram.add(redHist);
        histogram.add(greenHist);
        histogram.add(blueHist);
        double val_r = 0;
        double val_g = 0;
        double val_b = 0;
        int k = 256;
        int mass = bitmap.getWidth() * bitmap.getHeight();
        double scale = (double)(k-1)/ mass;
        for(int i=0;i<redHist.length;i++){
            val_r += histogram.get(0)[i];
            int redValue = (int) (val_r * scale);
            if(redValue > 255){
                redHist[i] = 255;
            }else{
                redHist[i] = redValue;
            }
            val_g += histogram.get(1)[i];
            int greenValue = (int) (val_g * scale);
            if(greenValue > 255){
                greenHist[i] = 255;
            }else{
                greenHist[i] = greenValue;
            }

            val_b += histogram.get(2)[i];
            int blueValue = (int) (val_b * scale);
            if(blueValue > 255){
                blueHist[i] = 255;
            }
            else{
                blueHist[i] = blueValue;
            }
        }
        for(int i=0;i<bitmap.getWidth();i++){
            for(int j=0;j<bitmap.getHeight();j++){
                int pixel = bitmap.getPixel(i,j);
                int red = redHist[Color.red(pixel)];
                int green = greenHist[Color.green(pixel)];
                int blue = blueHist[Color.blue(pixel)];
                int rgb = Color.rgb(red,green,blue);
                histBitmap.setPixel(i,j,rgb);
            }
        }
        return histBitmap;
    }
}
