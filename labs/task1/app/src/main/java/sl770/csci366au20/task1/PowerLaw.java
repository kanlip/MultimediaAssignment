package sl770.csci366au20.task1;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Arrays;

public class PowerLaw {

    private static Bitmap cloneBitmap(Bitmap bitmap){
        return bitmap.copy(bitmap.getConfig(),true);
    }
    private static int[] lookUpTable(){
        int [] table = new int[256];
        Arrays.fill(table,0);
        return table;
    }
    private static int calculate(int i, double r){
        //double result = 1 * Math.pow(i,r);
        double result = (255 * Math.pow((double)i/255,r));
        return (int) result;
    }
    private static void transform(Bitmap bitmap, double r){
        int [] redTable = lookUpTable();
        int [] greenTable = lookUpTable();
        int [] blueTable = lookUpTable();
        for(int i=0;i<bitmap.getWidth();i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                int pixel = bitmap.getPixel(i, j);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                redTable[red] = calculate(red, r);
                greenTable[green] = calculate(green, r);
                blueTable[blue] = calculate(blue, r);
                bitmap.setPixel(i, j, Color.rgb(Color.red(redTable[red]), Color.green(greenTable[green]), Color.blue(blueTable[blue])));
            }
        }
    }
    public static Bitmap processing(Bitmap bitmap,double r){
        Bitmap cpBitmap = cloneBitmap(bitmap);
        transform(cpBitmap,r);
        return cpBitmap;
    }
    public static Bitmap processing(Bitmap bitmap){
        double r = 2.20;
        Bitmap cpBitmap = cloneBitmap(bitmap);
        transform(cpBitmap,r);
        return cpBitmap;
    }
}
