package dk.mrspring.music.util;

import dk.mrspring.music.player.Music;

import java.util.List;

/**
 * Created by Konrad on 30-05-2015.
 */
public class MusicCollection
{
    List<Music> musicList;

    public MusicCollection(List<Music> musicList)
    {
        this.musicList = musicList;
    }

    public List<Music> getMusicList()
    {
        return musicList;
    }
}
