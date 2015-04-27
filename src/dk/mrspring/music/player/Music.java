package dk.mrspring.music.player;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;

/**
 * Created by Konrad on 27-04-2015.
 */
public class Music
{
    String name, album, artist;
    final File musicFile;

    public Music(File file)
    {
        this.musicFile = file;
    }

    public File getMusicFile()
    {
        return musicFile;
    }

    public String getName()
    {
        if (name == null)
            name = getKey(FieldKey.TITLE);
        return name;
    }

    public String getAlbum()
    {
        if (album == null)
            album = getKey(FieldKey.ALBUM);
        return album;
    }

    public String getArtist()
    {
        if (artist == null)
            artist = getKey(FieldKey.ARTIST);
        return artist;
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
}
