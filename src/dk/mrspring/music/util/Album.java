package dk.mrspring.music.util;

import dk.mrspring.music.player.Music;

import java.util.List;

/**
 * Created by Konrad on 30-05-2015.
 */
public class Album extends MusicCollection
{
    private String albumName;
    private String artistName;

    public Album(List<Music> musicList)
    {
        super(musicList);

        Music first = musicList.get(0);
        this.albumName = first.getAlbum();
        this.artistName = first.getArtist();
    }

    public Music getFirst()
    {
        return getMusicList().get(0);
    }

    public String getAlbumName()
    {
        return albumName;
    }

    public String getArtistName()
    {
        return artistName;
    }

    public String getRenderString()
    {
        return getAlbumName() + "\n" + getArtistName();
    }
}
