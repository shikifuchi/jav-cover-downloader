package benedict.zhang.avcoverdl.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class NIOUtils {

    public void download() throws IOException {
        var url = new URL("http://www.baidu.com");
        var is = url.openStream();
        var channel = Channels.newChannel(is);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(channel, Channels.newChannel(out));
        System.out.println(new String(out.toByteArray()));
    }

    public static void copy(ReadableByteChannel in, WritableByteChannel out) throws IOException {
        // First, we need a buffer to hold blocks of copied bytes.
        ByteBuffer buffer = ByteBuffer.allocateDirect(32 * 1024);

        // Now loop until no more bytes to read and the buffer is empty
        while (in.read(buffer) != -1 || buffer.position() > 0) {
            // The read() call leaves the buffer in "fill mode". To prepare
            // to write bytes from the bufferwe have to put it in "drain mode"
            // by flipping it: setting limit to position and position to zero
            buffer.flip();

            // Now write some or all of the bytes out to the output channel
            out.write(buffer);

            // Compact the buffer by discarding bytes that were written,
            // and shifting any remaining bytes. This method also
            // prepares the buffer for the next call to read() by setting the
            // position to the limit and the limit to the buffer capacity.
            buffer.compact();
        }
    }
}
