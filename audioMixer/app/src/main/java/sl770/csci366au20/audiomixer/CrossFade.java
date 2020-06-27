package sl770.csci366au20.audiomixer;

import java.nio.ByteBuffer;

class CrossFade {
    private byte [] data;
    private byte [] data1;
    private byte[] result;
    CrossFade(byte[] data, byte[] data1){
        this.data = data;
        this.data1 = data1;
    }
    private void operation(byte[]result,int sp,byte[]data,byte []data1){
        int j=0;
        int i=0;
        while(i < data.length){
            if(i<=(data.length-sp-1)){
                this.result[i] = data[i];
                this.result[i+1] = data[i+1];
            }else{
                short buf1 = data[i+1];
                short buf2 = data[i];
                buf1 = (short) ((buf1 & 0xff) << 8);
                buf2 = (short) (buf2 & 0xff);
                short res= (short) (buf1 | buf2);
                short bufA1 = data1[j+1];
                short bufA2 = data1[j];
                bufA1 = (short) ((bufA1 & 0xff) << 8);
                bufA2 = (short) (bufA2 & 0xff);
                short res1= (short) (bufA1 | bufA2);
                short val = (short) (res * (1-calculate(i,sp)));
                short val1 = (short) (res1 * calculate(i,sp));
                this.result[i] =(byte) (val + val1);
                this.result[i+1] = (byte) ((val>>8)+(val1>>8));
                j+=2;
            }
            i+=2;
        }
        int t = data.length -j + sp;
        for(int f=j; f<data1.length;f++){
            this.result[t] = data1[f];
            t++;
        }
    }
    private double calculate(int pos, int sp){
        double t0 = data.length - sp;
        return ((pos-t0)/(data.length-t0));
    }
    byte[] crossFade(WavInfo wav, int fadeSec){
        int sp = wav.bytePerSec * fadeSec;
        this.result = new byte[(data.length+data1.length) - sp];
        int min = Math.min(this.data.length,this.data1.length);
        int j=0;
        int i=0;
        if(Util.isMin(data,min)){
            this.operation(result,sp,data,data1);
        }
        else if(Util.isMin(data1,min)){
            this.operation(result,sp,data1,data);
        }
        return this.result;

    }
}
