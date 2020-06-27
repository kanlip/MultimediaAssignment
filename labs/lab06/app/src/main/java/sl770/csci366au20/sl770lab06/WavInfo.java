package sl770.csci366au20.sl770lab06;

// WavInfo.java
public final class WavInfo {
    AudioSpec spec;
    int size;
    int bitPerSample;
    int duration;

    public WavInfo(AudioSpec sp, int sz, int bitPerSample) {
        spec = sp;
        size = sz;
        this.bitPerSample = bitPerSample;
        duration = (sz * 8) / (bitPerSample * spec.freq * 2);
    }

    int getSize() {
        return size;
    }
}