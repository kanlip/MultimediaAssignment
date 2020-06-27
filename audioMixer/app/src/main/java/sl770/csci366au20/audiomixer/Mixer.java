package sl770.csci366au20.audiomixer;

class Mixer {
    private byte[] data;
    private byte[] data1;
    Mixer(byte[] data, byte[] data1){
        this.data = data;
        this.data1 = data1;
    }
    private void mixer(byte[] minArray,byte[] maxArray,byte[] temp){
        int j = 0;
        for(int i=0;i<maxArray.length;i++){
            double minValue = minArray[j] ;
            double maxValue = maxArray[i] ;
            double mixValue = ((0.5) * minValue) + ((1-0.5) * maxValue);
            temp[i] = (byte) mixValue;
            if(minArray.length -1 == j){
                j = 0;
            }else j++;
        }

    }
    byte[] mix(){
        int min = Math.min(this.data.length,this.data1.length);
        int max = Math.max(this.data.length,this.data1.length);
        byte [] temp = new byte[max];
        if(Util.isMin(this.data,min)){
            mixer(this.data,this.data1,temp);

        }else if(Util.isMin(this.data1,min)){
            mixer(this.data1,this.data,temp);

        }
        return temp;
    }
}
