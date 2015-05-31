package dk.mrspring.music.player;

import dk.mrspring.music.cover.Cover;
import javafx.scene.media.Media;
import javafx.util.Duration;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;

/**
 * Created by Konrad on 27-04-2015.
 */
public class Music
{
    public static final ResourceLocation UNKNOWN = new ResourceLocation("musicplayer:unknown.png");
    public static final ResourceLocation LOADING = new ResourceLocation("musicplayer:loading.png");

    final File musicFile;
    MusicAttribute name, album, artist;
    Media media;
    Cover cover;

    protected Music(File file)
    {
        this.musicFile = file;
    }

    public File getMusicFile()
    {
        return musicFile;
    }

    public String getName()
    {
        return getNameAttribute().getValue();
    }

    public Music setName(String newName)
    {
        getNameAttribute().setValue(newName);
        return this;
    }

    private MusicAttribute getNameAttribute()
    {
        if (name == null)
            name = new MusicAttribute("title", getKey(FieldKey.TITLE));
        return name;
    }

    public String getAlbum()
    {
        if (album == null)
            album = new MusicAttribute("album", getKey(FieldKey.ALBUM));
        return album.getValue();
    }

    public String getArtist()
    {
        if (artist == null)
            artist = new MusicAttribute("artist", getKey(FieldKey.ARTIST));
        return artist.getValue();
    }

    private String getKey(FieldKey key)
    {
        String value = "";
        try
        {
            AudioFile file = AudioFileIO.read(musicFile);
            value = file.getTag().getFirst(key);
            value = value.trim();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return value;
    }

    public Media asMedia()
    {
        if (this.media == null)
            this.media = new Media(getMusicFile().toURI().toASCIIString());
        return this.media;
    }

    public Duration getLength()
    {
        return asMedia().getDuration();
    }

    public void bindCover()
    {
        if (cover == null)
        {
            this.cover = new Cover();
            this.cover.loadFrom(this);
        } else cover.bindCover(this);
    }

    public int getId()
    {
        String str = getName() + getArtist();
        return str.hashCode();
    }

    public static void bindDefaultCover()
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(Music.UNKNOWN);
    }

    public static class MusicAttribute
    {
        final String name, originalValue;
        String value;

        MusicAttribute(String name, String value)
        {
            this.name = name;
            this.value = value;
            this.originalValue = value;
        }

        public boolean isValueOverriden()
        {
            return this.value.equals(originalValue);
        }

        public String getName()
        {
            return name;
        }

        public String getOriginalValue()
        {
            return originalValue;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String newValue)
        {
            this.value = newValue;
        }
    }
}
