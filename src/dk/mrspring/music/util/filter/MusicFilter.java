package dk.mrspring.music.util.filter;

import dk.mrspring.music.player.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 26-05-2015.
 */
public abstract class MusicFilter
{
    String filter;

    public MusicFilter(String filter)
    {
        this.filter = filter;
    }

    public abstract boolean filter(Music music);

    public List<Music> filterList(List<Music> toFilter)
    {
        List<Music> newList = new ArrayList<Music>();
        for (Music music : toFilter)
            if (this.filter(music))
                newList.add(music);
        return newList;
    }

    public String getFilter()
    {
        return filter;
    }

    public void setFilter(String filter)
    {
        this.filter = filter;
    }
}
