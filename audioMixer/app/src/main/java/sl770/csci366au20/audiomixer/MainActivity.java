package sl770.csci366au20.audiomixer;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ShortBuffer;


public class MainActivity extends AppCompatActivity {
    private Button playButton;
    private AudioTrack audioTrack;
    private Button stopButton;
    private Button pauseButton;
    private static final String TAG = "PlayWavViaAudioTrack";
    private boolean continuePlaying = false;
    public static final int LOADWAV = 1;
    public static final int MIXER = 2;
    public static final int LINKER = 3;
    private WavInfo wavInfo;
    private WavInfo wavInfo1;
    ShortBuffer mSamples;       // the samples to play
    int mNumSamples;           // number of samples to play
    private byte[] wavData;
    private byte[] wavData1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loadButton = findViewById(R.id.load);
        playButton = findViewById(R.id.play);
        stopButton = findViewById(R.id.stop);
        pauseButton = findViewById(R.id.pause);
        Button linkerButton = findViewById(R.id.linker);
    }

    private void readWavData(Uri data){
        try(InputStream stream = getContentResolver().openInputStream(data)) {
            LoadWav load = new LoadWav();
            assert stream != null;
            wavInfo = load.readHeader(stream,getApplicationContext());
            wavData = LoadWav.readWavPcm(wavInfo,stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadWav(View view){
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,LOADWAV);
    }
    public void loadMixer(View view){
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,MIXER);
    }
    public void loadLinker(View view){
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,LINKER);
    }
    void stream2Files(ClipData clipData) throws Error,IOException{
        try{
            InputStream stream = getContentResolver().openInputStream(clipData.getItemAt(0).getUri());
            LoadWav load = new LoadWav();
            assert stream != null;
            wavInfo = load.readHeader(stream,getApplicationContext());
            wavData = LoadWav.readWavPcm(wavInfo, stream);
            InputStream stream1 = getContentResolver().openInputStream(clipData.getItemAt(1).getUri());
            assert stream1 != null;
            wavInfo1 = load.readHeader(stream1,getApplicationContext());
            wavData1 = LoadWav.readWavPcm(wavInfo1,stream1);
            Util.checkCompatible(wavInfo,wavInfo1);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }catch(Error e){
            throw new Error(e.getMessage());
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if( requestCode == LOADWAV && resultCode == Activity.RESULT_OK){
            readWavData(data.getData());
        }
        else if(requestCode == MIXER ){
            ClipData mClipData = data.getClipData();
            if(mClipData!=null){
                try {
                    stream2Files(mClipData);
                    Mixer mixer = new Mixer(wavData,wavData1);
                    wavData = mixer.mix();
                } catch(IOException e){
                    e.printStackTrace();
                }catch(Error e){
                    wavData = null;
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }else if(requestCode == LINKER){
            ClipData mClipData = data.getClipData();
            if(mClipData != null){
                try {
                    stream2Files(mClipData);
                    CrossFade crossFade = new CrossFade(wavData,wavData1);
                    wavData = crossFade.crossFade(wavInfo,2);
                } catch(IOException e){
                    e.printStackTrace();
                }catch(Error e){
                    wavData = null;
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void startPlaying(View view) {
        if (wavData!= null){
            playButton.setEnabled(false);
            stopButton.setEnabled(true);
            pauseButton.setEnabled(true);
            continuePlaying = true;
            short[] audioData;
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
    }

    public void stopPlaying(View view) {
        if(wavData!=null){
            playButton.setEnabled(true);
            stopButton.setEnabled(false);
            continuePlaying = false;
            audioTrack.stop();
        }
    }

    public void pause(View view){
        if(audioTrack!=null){
            pauseButton.setEnabled(false);
            playButton.setEnabled(true);
            audioTrack.pause();
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
                audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                        wavInfo.spec.getRate(), AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
                audioTrack.play();
                Log.v(TAG, "Audio streaming started");
                short[] buffer = new short[bufferSize];
                mSamples.rewind();

                int limit = mNumSamples;
                int totalWritten = 0;
                while (mSamples.position() < limit && continuePlaying) {
                    int numSamplesLeft = limit - mSamples.position();
                    Log.v(TAG,String.valueOf(numSamplesLeft));
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


}
