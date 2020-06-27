package sl770.csci366au20.sl770lab06;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ShortBuffer;

public class MainActivity extends Activity {
    private static final String TAG = "PlayWavViaAudioTrack";
    ShortBuffer mSamples; // the samples to play
    int mNumSamples; // number of samples to play
    private View playButton;
    private View stopButton;
    private WavInfo wavInfo;
    private byte[] wavData;
    private AudioTrack audioTrack;
    private boolean continuePlaying = false;
    private int resource = R.raw.stereo16;
    private TextView fileNameText;
    private TextView numChannels;
    private TextView bitsSample;
    private TextView frequencyValue;
    private TextView durationValue;
    private String fileName;

    private void readWavData() {
        InputStream in = getApplicationContext().getResources().openRawResource(resource);
        fileName = getApplicationContext().getResources().getResourceEntryName(resource);
        LoadWav loadWav = new LoadWav();
        try {
            wavInfo = LoadWav.readHeader(in);

            wavData = LoadWav.readWavPcm(wavInfo, in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void playAudio() {
        new Thread(new Runnable() {
            @Override
            public void run() { // mono 16 bits
                int bufferSize = AudioTrack.getMinBufferSize(wavInfo.spec.getRate(), AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
                if (bufferSize == AudioTrack.ERROR || bufferSize == AudioTrack.ERROR_BAD_VALUE) {
                    bufferSize = wavInfo.spec.getRate() * 2;
                }
                // Mono 16 bits
                AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                        wavInfo.spec.getRate(), AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
                audioTrack.play();
                Log.v(TAG, "Audio streaming started");
                short[] buffer = new short[bufferSize];
                mSamples.rewind();
                int limit = mNumSamples;
                int totalWritten = 0;
                while (mSamples.position() < limit && continuePlaying) {
                    int numSamplesLeft = limit - mSamples.position();
                    int samplesToWrite;
                    if (numSamplesLeft >= buffer.length) {
                        mSamples.get(buffer);
                        samplesToWrite = buffer.length;
                    } else {
                        for (int i = numSamplesLeft; i < buffer.length; i++) {
                            buffer[i] = 0;
                        }
                        mSamples.get(buffer, 0, numSamplesLeft);
                        samplesToWrite = numSamplesLeft;
                    }
                    totalWritten += samplesToWrite;
                    audioTrack.write(buffer, 0, samplesToWrite);
                }
                if (!continuePlaying) {
                    audioTrack.release();
                }
                Log.v(TAG, "Audio streaming finished. Samples written: " + totalWritten);
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButton = findViewById(R.id.play);
        stopButton = findViewById(R.id.stop);
        readWavData();
        numChannels = findViewById(R.id.channelValue);
        numChannels.setText(String.valueOf(wavInfo.spec.channels));
        bitsSample = findViewById(R.id.samplevalue);
        bitsSample.setText(String.valueOf(wavInfo.bitPerSample));
        fileNameText = findViewById(R.id.filename);
        fileNameText.setText(fileName);
        frequencyValue = findViewById(R.id.frequencyValue);
        frequencyValue.setText(String.valueOf(wavInfo.spec.getRate()) + " Hz");
        durationValue = findViewById(R.id.durationValue);
        durationValue.setText(String.valueOf(wavInfo.duration) + " seconds");
    }

    public void startPlaying(View view) {
        playButton.setEnabled(false);
        stopButton.setEnabled(true);
        continuePlaying = true;
        short[] audioData = null;
        mNumSamples = wavData.length / 2;
        audioData = new short[mNumSamples];
        for (int i = 0; i < mNumSamples; i++) {
            short MSB = wavData[2 * i + 1];
            short LSB = wavData[2 * i];
            audioData[i] = (short) (MSB << 8 | (255 & LSB));
        }
        mSamples = ShortBuffer.allocate(mNumSamples);
        mSamples.put(audioData);
        playAudio();
    }

    public void stopPlaying(View view) {
        playButton.setEnabled(true);
        stopButton.setEnabled(false);
        continuePlaying = false;
    }
}