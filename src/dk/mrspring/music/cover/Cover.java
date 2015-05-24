package dk.mrspring.music.cover;

import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.util.ByteBufferedImage;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.util.TextureLoader;
import net.minecraft.client.Minecraft;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Konrad on 24-05-2015.
 */
public class Cover
{
    ICoverBinder coverBinder;
    Thread imageLoaderThread;
    ByteBufferedImage bufferedImageBuffer;

    public void loadFrom(Music music)
    {
        if (imageLoaderThread == null)
        {
            imageLoaderThread = new Thread(new ImageLoader(music));
            imageLoaderThread.start();
        }
    }

    public void bindCover(Music music)
    {
        if (coverBinder != null)
            coverBinder.bind(music);
        else if (bufferedImageBuffer != null)
        {
            int textureId = TextureLoader.injectTexture(bufferedImageBuffer);
            this.coverBinder = new TextureCoverBinder(textureId);
            this.coverBinder.bind(music);
        } else Minecraft.getMinecraft().getTextureManager().bindTexture(Music.UNKNOWN);
    }

    public synchronized void fromByteBufferedImage(ByteBufferedImage image)
    {
        this.bufferedImageBuffer = image;
    }

    public class ImageLoader implements Runnable
    {
        File from;
        File save;

        public ImageLoader(Music mp3ToLoadFrom)
        {
            this.from = mp3ToLoadFrom.getMusicFile();
            this.save = new File(LiteModMusicPlayer.coverLocation, mp3ToLoadFrom.getArtist() + "_" + mp3ToLoadFrom.getName() + ".png");
        }

        @Override
        public void run()
        {
            BufferedImage image = TextureLoader.getFromMP3File(from, save);
            System.out.println("Loaded image...");
            if (image == null)
            {
                System.out.println("BufferedImage was null...");
                coverBinder = new UnknownCoverBinder();
            } else
            {
                System.out.println("BufferedImage was not null... loading texture...");
                ByteBufferedImage texture = TextureLoader.toByteBuffer(image);
                if (texture != null)
                {
                    System.out.println("Texture was loaded...");
                    fromByteBufferedImage(texture);
                }
            }
        }
    }
}
