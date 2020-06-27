package sl770.csci366au20.sl770lab06;

final class AudioSpec {
    int freq;
    // public short format;
    byte channels;

    // public byte silence;
    //  public int samples;
    // public long size;
    public AudioSpec(int f, byte chs) {
        freq = f;
        channels = chs;
        System.out.println(freq);
    }

    public int getRate() {
        return (freq);
    }
}