package sl770.csci366au20.audiomixer;

// WavInfo.java
final class WavInfo {
    AudioSpec spec;
    int size;
    int bitPerSample;
    int duration;
    int bytePerSec;
    WavInfo(AudioSpec sp, int sz, int bitPerSample) {
        spec = sp;
        size = sz;
        this.bitPerSample = bitPerSample;
        duration = (sz * 8) / (bitPerSample * spec.freq * 2);
        this.bytePerSec = size/duration;
    }
    int getSize() {
        return size;
    }
}