package dk.mrspring.music.player;

import dk.mrspring.music.LiteModMusicPlayer;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 27-04-2015.
 */
public class MusicHandler
{
    public Queue queue;
    List<Music> allMusic = new ArrayList<Music>();
    MusicPlayer player;

    public MusicHandler()
    {
        queue = new Queue(new ArrayList<Music>());
    }

    public MusicHandler(boolean autoPlay, File... baseFiles)
    {
        this();
        for (File folder : baseFiles)
            loadMusicFrom(folder);
        createDefaultQueue();
        if (autoPlay)
            play(queue.getCurrent());
    }

    public void createDefaultQueue()
    {
        List<Music> newQueue = new ArrayList<Music>(allMusic);
        queue.updateQueue(newQueue);
    }

    public void loadMusicFrom(File folder)
    {
        List<File> foundFiles = new ArrayList<File>();
        LiteModMusicPlayer.core.getFileLoader().addFilesToList(folder, foundFiles, false, FileFilterUtils.suffixFileFilter(".mp3", IOCase.INSENSITIVE));
        for (File file : foundFiles)
        {
            System.out.println("Found music file: " + file.getPath());
            allMusic.add(new Music(file));
        }
    }

    public Music getCurrentlyPlaying()
    {
        return queue.getCurrent();
    }

    public Music getNextUp()
    {
        return queue.getNext();
    }

    public void stopCurrentPlayer()
    {
        if (player != null)
            player.stopMusic();
    }

    public void play(Music music)
    {
        stopCurrentPlayer();
        player = new MusicPlayer(music, new Runnable()
        {
            @Override
            public void run()
            {
                playNext();
            }
        });
        player.start();
    }

    public void playNext()
    {
        play(queue.cycle());
    }

    public void toggle()
    {
        if (player != null)
            if (player.isPaused())
                player.resumeMusic();
            else player.pauseMusic();
        else play(getCurrentlyPlaying());
    }

    public void playPrevious()
    {
        play(queue.reverseCycle());
    }

    public Queue getQueue()
    {
        return queue;
    }
}
