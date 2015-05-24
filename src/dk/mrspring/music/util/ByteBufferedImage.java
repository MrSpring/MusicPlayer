package dk.mrspring.music.util;

import java.nio.ByteBuffer;

/**
 * Created by Konrad on 24-05-2015.
 */
public class ByteBufferedImage
{
    public ByteBuffer buffer;
    public int width, height;

    public ByteBufferedImage(ByteBuffer buffer, int width, int height)
    {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }
}
