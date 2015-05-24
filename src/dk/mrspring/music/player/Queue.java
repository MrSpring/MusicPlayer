package dk.mrspring.music.player;

import java.util.List;

/**
 * Created by Konrad on 27-04-2015.
 */
public class Queue extends Playlist
{
    public Queue(List<Music> music)
    {
        super("queue", music);
    }

    public Music cycle()
    {
        if (musicList.size() > 1)
        {
            Music current = musicList.remove(0);
            musicList.add(current);
        }
        return getCurrent();
    }

    public Music reverseCycle()
    {
        if (musicList.size() > 1)
        {
            Music current = musicList.remove(musicList.size() - 1);
            musicList.add(0, current);
        }
        return getCurrent();
    }

    public void updateQueue(List<Music> newList)
    {
        this.musicList = newList;
    }

    public Music getCurrent()
    {
        return musicList.get(0);
    }

    public Music getNext()
    {
        if (musicList.size() > 1)
            return musicList.get(1);
        else return getCurrent();
    }
}
