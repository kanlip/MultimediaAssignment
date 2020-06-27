package sl770.csci366au20.audiomixer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.content.ContentValues.TAG;

class LoadWav {
    private static final String RIFF_HEADER = "RIFF";
    private static final String WAVE_HEADER = "WAVE";
    private static final String FMT_HEADER = "fmt ";
    private static final String DATA_HEADER = "data";
    private static final int HEADER_SIZE = 44;
    private static final String CHARSET = "ASCII";

    private void checkFormat(boolean cond, String message,Context context) {
        if (!cond) {
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            Log.d(TAG, message);
            throw new Error("Not supported");
        }
    }

    WavInfo readHeader(InputStream wavStream,Context context) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        wavStream.read(buffer.array(), buffer.arrayOffset(), buffer.capacity());
        buffer.rewind();
        buffer.position(buffer.position() + 20);
        int format = buffer.getShort();
        short channels = buffer.getShort();
        checkFormat(format == 1, "Unsupported encoding: " + format,context); // 1 means Linear PCM short channels = buffer.getShort();
        checkFormat(channels == 1 || channels == 2, "Unsupported channels: " + channels,context);
        int rate = buffer.getInt();
        checkFormat(rate <= 48000 && rate >= 11025, "Unsupported rate: " + rate,context);
        buffer.position(buffer.position() + 6);
        int bits = buffer.getShort();
        checkFormat(bits == 16, "Unsupported bits: " + bits,context);
        int dataSize = 0;
        while (buffer.getInt() != 0x61746164) { // "data" marker
            Log.d(TAG, "Skipping non-data chunk");
            int size = buffer.getInt();
            wavStream.skip(size);
            buffer.rewind();
            wavStream.read(buffer.array(), buffer.arrayOffset(), 8);
            buffer.rewind();
        }
        dataSize = buffer.getInt();
        checkFormat(dataSize > 0, "wrong datasize: " + dataSize,context);
        return new WavInfo(new AudioSpec(rate, (byte) channels), dataSize, bits);
    }

    static byte[] readWavPcm(WavInfo info, InputStream stream) throws IOException {
        byte[] data = new byte[info.getSize()];
        stream.read(data, 0, data.length);
        return data;
    }
}