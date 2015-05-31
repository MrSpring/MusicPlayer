package dk.mrspring.music.util;

import dk.mrspring.music.player.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 30-05-2015.
 */
public class Artist
{
    List<Album> albums;
    List<Music> allMusic;
    String artistName;

    public Artist(List<Album> albums)
    {
        this.albums = albums;
        this.artistName = albums.get(0).getArtistName();
        this.allMusic = new ArrayList<Music>();
        for (Album album : albums)
            allMusic.addAll(album.getMusicList());
    }

    public String getArtistName()
    {
        return artistName;
    }

    public List<Album> getAlbums()
    {
        return albums;
    }

    public String getRenderString()
    {
        return getArtistName();
    }

    public int getMusicCount()
    {
        int amount = 0;
        for (Album album : albums)
            amount += album.getMusicList().size();
        return amount;
    }

    public List<Music> getAllMusic()
    {
        return allMusic;
    }
}
