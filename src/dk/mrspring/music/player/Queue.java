package dk.mrspring.music.player;

import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.util.TranslateHelper;

import java.util.List;

/**
 * Created by Konrad on 27-04-2015.
 */
public class Queue extends Playlist
{
    public Queue(List<Music> music)
    {
        super("queue", music);

        for (Music piece : music)
        {
            System.out.println("Adding: " + piece.getName() + ", by: " + piece.getArtist() + ". Hashed ID: " + piece.getId());
        }
    }

    @Override
    public String getName()
    {
        return TranslateHelper.translate("playlist.queue.name");
    }

    @Override
    public Playlist add(int at, Music music)
    {
        super.add(at, music);
        LiteModMusicPlayer.musicHandler.checkForQueueChanges();
        return this;
    }

    @Override
    public Music remove(int index)
    {
        Music removed = super.remove(index);
        LiteModMusicPlayer.musicHandler.checkForQueueChanges();
        return removed;
    }

    @Override
    public Playlist remove(Music music)
    {
        super.remove(music);
        LiteModMusicPlayer.musicHandler.checkForQueueChanges();
        return this;
    }

    public Music cycle()
    {
        if (getMusicList().size() > 1)
        {
            Music current = getMusicList().remove(0);
            getMusicList().add(current);
        }
        return getCurrent();
    }

    public Music reverseCycle()
    {
        if (getMusicList().size() > 1)
        {
            Music current = getMusicList().remove(getMusicList().size() - 1);
            getMusicList().add(0, current);
        }
        return getCurrent();
    }

    public void updateQueue(List<Music> newList)
    {
        setMusicList(newList);
        LiteModMusicPlayer.musicHandler.checkForQueueChanges();
    }

    public Music getCurrent()
    {
        return getMusicList().get(0);
    }

    public Music getNext()
    {
        if (getMusicList().size() > 1)
            return getMusicList().get(1);
        else return getCurrent();
    }
}
