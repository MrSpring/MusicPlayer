package dk.mrspring.music.player;

import dk.mrspring.music.util.Artist;
import dk.mrspring.music.util.JsonUtils;
import dk.mrspring.music.util.MusicCollection;

import java.util.*;

/**
 * Created by Konrad on 27-04-2015.
 */
public class Playlist extends MusicCollection
{
    String name;
    int id;

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
        if (id == 0) id = hashCode();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", getName());
        map.put("id", getId());
        List<Music> musicList = getMusicList();
        List<Integer> list = new ArrayList<Integer>();
        for (Music music : musicList)
            list.add(music.getId());
        map.put("music", list);
        return JsonUtils.toJsonCode(map);
    }

    public void addAll(MusicCollection collection)
    {
        addAll(collection.getMusicList());
    }

    public void addAll(List<Music> musicList)
    {
        getMusicList().addAll(musicList);
    }

    public int getId()
    {
        return id;
    }

    public Playlist setId(int id)
    {
        this.id = id;
        return this;
    }

    public void tryAdd(Object[] objects)
    {
        for (Object object : objects)
        {
            if (object instanceof Music) this.add((Music) object);
            if (object instanceof MusicCollection)
            {
                MusicCollection collection = (MusicCollection) object;
                for (Music music : collection.getMusicList())
                    this.add(music);
            }
            if (object instanceof Artist)
            {
                Artist artist = (Artist) object;
                this.tryAdd(artist.getAlbums().toArray());
            }
        }
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
