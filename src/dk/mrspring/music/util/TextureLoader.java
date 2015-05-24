package dk.mrspring.music.util;

import dk.mrspring.music.player.ByteBufferedImage;
import dk.mrspring.music.player.Cover;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Konrad on 24-05-2015.
 */
public class TextureLoader
{
    public static BufferedImage getFromMP3File(File mp3, File saveLocation)
    {
        try
        {
            if (saveLocation.exists())
                return ImageIO.read(saveLocation);
            else
            {
                AudioFile f = AudioFileIO.read(mp3);
                TagField coverArtField = f.getTag().getFirstField(FieldKey.COVER_ART);
                if (coverArtField != null)
                {
                    FrameBodyAPIC bodyAPIC = (FrameBodyAPIC) ((ID3v23Frame) coverArtField).getBody();
                    byte[] rawImage = (byte[]) bodyAPIC.getObjectValue(DataTypes.OBJ_PICTURE_DATA);
                    BufferedImage bufferedImage = ImageIO.read(ImageIO.createImageInputStream(new ByteArrayInputStream(rawImage)));
                    saveLocation.createNewFile();
                    ImageIO.write(bufferedImage, "png", saveLocation);
                    return bufferedImage;
                } else return null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param image BufferedImage to turn into a byte buffer.
     * @return Returns a ByteBuffer made from the image. The ByteBuffer is ready to be put into a OpenGL texture.
     */
    public static ByteBufferedImage toByteBuffer(BufferedImage image)
    {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        return new ByteBufferedImage(buffer, image.getWidth(), image.getHeight());

        /*int textureId = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        return textureId;*/
    }

    public static int injectTexture(ByteBufferedImage image)
    {
        int textureId = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.buffer);

        return textureId;
    }

    /*String fileName = this.getArtist() + "_" + this.getTitle() + ".png";
    File file = new File(LiteModMCPlayer.coverLocation.getAbsolutePath() + "/" + fileName);

    if (file.exists())
    {
        this.cover = ImageIO.read(file);
        this.textureId = TextureLoader.loadTexture(this.cover);
    } else
    {
        AudioFile f = AudioFileIO.read(this.baseFile);
        TagField coverArtField = f.getTag().getFirstField(FieldKey.COVER_ART);
        if (coverArtField != null)
        {
            FrameBodyAPIC bodyAPIC = (FrameBodyAPIC) ((ID3v23Frame) coverArtField).getBody();
            byte[] rawImage = (byte[]) bodyAPIC.getObjectValue(DataTypes.OBJ_PICTURE_DATA);
            BufferedImage bufferedImage = ImageIO.read(ImageIO.createImageInputStream(new ByteArrayInputStream(rawImage)));
            file.createNewFile();
            this.cover = bufferedImage;
            ImageIO.write(bufferedImage, "png", file);
            this.textureId = TextureLoader.loadTexture(this.cover);
        }
    }*/
}
