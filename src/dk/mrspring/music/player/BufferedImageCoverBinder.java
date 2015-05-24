package dk.mrspring.music.player;

import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Konrad on 24-05-2015.
 */
public class BufferedImageCoverBinder implements ICoverBinder
{
    private static final int BYTES_PER_PIXEL = 4;
    int textureId = -1;

    public BufferedImageCoverBinder(BufferedImage image)
    {

    }

    @Override
    public void bind(Music binding)
    {
        if (textureId <= -1)
            Minecraft.getMinecraft().getTextureManager().bindTexture(Music.UNKNOWN);
        else
            glBindTexture(GL_TEXTURE_2D, textureId);
    }
}
