package dk.mrspring.music.player;

import dk.mrspring.music.util.JsonUtils;

import java.util.*;

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

    public Music remove(int index)
    {
        return this.getList().remove(index);
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

    public String getName()
    {
        return this.name;
    }

    public String toJson()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", getName());
        List<Music> musicList = getList();
        List<Integer> list = new ArrayList<Integer>();
        for (Music music : musicList)
            list.add(music.getId());
        map.put("music", list);
        return JsonUtils.toJsonCode(map);
    }
}
