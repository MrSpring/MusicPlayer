package dk.mrspring.music.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Konrad on 27-04-2015.
 */
public class Playlist
{
    String name;
    List<Music> musicList;

    public Playlist(String name, List<Music> music)
    {
        this.name = name;
        this.musicList = music;
    }

    public Playlist(String name, Music... musics)
    {
        this.name = name;
        this.musicList = new ArrayList<Music>();
        Collections.addAll(musicList, musics);
    }

    public Playlist add(int at, Music music)
    {
        this.musicList.add(at, music);
        return this;
    }

    public Playlist add(Music music)
    {
        this.musicList.add(music);
        return this;
    }

    public Playlist remove(Music music)
    {
        this.musicList.remove(music);
        return this;
    }

    public boolean isEmpty()
    {
        return musicList != null && musicList.size() > 0;
    }

    public List<Music> getList()
    {
        return this.musicList;
    }

    public int size()
    {
        return getList().size();
    }
}
