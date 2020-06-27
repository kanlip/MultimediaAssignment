package sl770.csci366au20.ciexy;

public class ConvertRGBXYZ {
    private float [][] rgbMatrix = {{3.24f,-1.537f,-0.498f},{-0.969f,1.876f,0.042f},{0.056f,-0.204f,1.057f}};
    private float [][] xyzMatrix = {{0.412f,0.358f,0.180f},{0.213f,0.715f,0.072f},{0.019f,0.119f,0.950f}};
    private float xr,yg,zb;

    public ConvertRGBXYZ(float x, float y , float z) {

        xr =x;
        yg=y;
        zb=z;
    }

    public float[] convertXYZtoRGB(float x,float y, float z){
        float [] rgb = new float[3];
        for(int i=0;i<rgbMatrix.length;i++){
            rgb[i] = (rgbMatrix[i][0] * x) + (rgbMatrix[i][1] * y) + (rgbMatrix[i][2]* z);
        }
        return rgb;
    }
    public float[] convertRGBtoXYZ(float r,float g, float b){
        float [] xyz = new float[3];
        for(int i=0;i<xyzMatrix.length;i++){
            xyz[i] = (xyzMatrix[i][0] * r) + (xyzMatrix[i][1] * g) + (xyzMatrix[i][2]* b);
        }
        return xyz;
    }

}
