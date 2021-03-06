package dk.mrspring.music.player;

import com.google.common.collect.Lists;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.util.MusicCollection;
import dk.mrspring.music.util.TranslateHelper;

import java.util.List;

/**
 * Created by Konrad on 27-04-2015.
 */
public class Queue extends Playlist
{
    private MusicCollection playingFrom = null;

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

    public void updateQueue(MusicCollection collection)
    {
        playingFrom = collection;
        updateQueue(collection.getMusicList());
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

    public Queue addNext(MusicCollection collection)
    {
        return this.addNext(collection.getMusicList());
    }

    public Queue addNext(List<Music> musicList)
    {
        List<Music> reversed = Lists.reverse(musicList);
        for (Music music : reversed) addNext(music);
        return this;
    }

    public Queue addNext(Music clicked)
    {
        this.add(1, clicked);
        return this;
    }
}
