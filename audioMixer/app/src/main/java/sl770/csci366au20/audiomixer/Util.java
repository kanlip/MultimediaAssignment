package sl770.csci366au20.audiomixer;

import java.nio.ByteBuffer;

class Util {
    static void checkCompatible(WavInfo wav, WavInfo wav2) throws Error{
        if(wav.spec.getRate() != wav2.spec.getRate() || wav.spec.channels != wav2.spec.channels){
            throw new Error("2 audio files are not compatible");
        }
    }
    static boolean isMin(byte[] one,int min){
        return one.length == min;
    }
}
