package dk.mrspring.music.player;

import dk.mrspring.music.util.JsonUtils;
import dk.mrspring.music.util.MusicCollection;

import java.util.*;

/**
 * Created by Konrad on 27-04-2015.
 */
public class Playlist extends MusicCollection
{
    String name;

    public Playlist(String name, List<Music> music)
    {
        super(music);
        this.name = name;
    }

    public Playlist(String name, Music... musics)
    {
        super(new ArrayList<Music>(Arrays.asList(musics)));
        this.name = name;
    }

    public Playlist add(int at, Music music)
    {
        this.getMusicList().add(at, music);
        return this;
    }

    public Playlist add(Music music)
    {
        this.getMusicList().add(music);
        return this;
    }

    public Playlist remove(Music music)
    {
        this.getMusicList().remove(music);
        return this;
    }

    public Music remove(int index)
    {
        return this.getMusicList().remove(index);
    }

    public boolean isEmpty()
    {
        return getMusicList() != null && getMusicList().size() > 0;
    }

    public int size()
    {
        return getMusicList().size();
    }

    public String getName()
    {
        return this.name;
    }

    public String toJson()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", getName());
        List<Music> musicList = getMusicList();
        List<Integer> list = new ArrayList<Integer>();
        for (Music music : musicList)
            list.add(music.getId());
        map.put("music", list);
        return JsonUtils.toJsonCode(map);
    }

    public void addAll(List<Music> musicList)
    {
        getMusicList().addAll(musicList);
    }
}
