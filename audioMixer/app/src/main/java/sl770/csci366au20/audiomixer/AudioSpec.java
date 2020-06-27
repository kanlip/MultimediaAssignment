package sl770.csci366au20.audiomixer;

final class AudioSpec {
    int freq;
    byte channels;

    AudioSpec(int f, byte chs) {
        freq = f;
        channels = chs;

    }
    int getRate() {
        return (freq);
    }
}