package dk.mrspring.music.util;

import dk.mrspring.music.player.Music;

import java.util.Iterator;
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

    public MusicCollection setMusicList(List<Music> newMusicList)
    {
        this.musicList = newMusicList;
        return this;
    }

    public Iterator<Music> getIterator(){
        return musicList.iterator();
    }
}
