package dk.mrspring.music.player;

import com.sun.istack.internal.NotNull;
import dk.mrspring.music.LiteModMusicPlayer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

import static dk.mrspring.music.player.PlayerState.*;

/**
 * Created by Karoline on 27-04-2015.
 */
public class MusicPlayer extends Thread
{
    private PlayerState currentState = LOADING;
    public final Music playing;
    private MediaPlayer player;

    public MusicPlayer(@NotNull Music music, final Runnable finishedCallback)
    {
        this.playing = music;
        Media media = playing.asMedia();
        currentState = LOADING;
        try
        {
            player = new MediaPlayer(media);
            player.setOnEndOfMedia(new Runnable()
            {
                @Override
                public void run()
                {
                    currentState = FINISHED;
                    finishedCallback.run();
                }
            });
            player.setOnHalted(new Runnable()
            {
                @Override
                public void run()
                {
                    currentState = STOPPED;
                }
            });
            currentState = INITIALIZED;
        } catch (Exception e)
        {
            e.printStackTrace();
            currentState = FAILED;
        }
    }

    @Override
    public void run()
    {
        currentState = PLAYING;
        try
        {
            player.play();
        } catch (Exception e)
        {
            currentState = FAILED;
            e.printStackTrace();
        }
    }

    public boolean isPlaying()
    {
        return currentState == PLAYING;
    }

    public boolean isDone()
    {
        return currentState == FAILED || currentState == FINISHED || currentState == STOPPED;
    }

    public boolean isPaused()
    {
        System.out.println("returning: " + (currentState == PAUSED));
        return currentState == PAUSED;
    }

    public MusicPlayer pauseMusic()
    {
        System.out.println(currentState);
        if (this.currentState == PLAYING)
        {
            this.currentState = PAUSED;
            this.player.pause();
        }
        return this;
    }

    public MusicPlayer resumeMusic()
    {
        if (this.currentState == PAUSED)
        {
            this.currentState = PLAYING;
            Duration current = player.getCurrentTime();
            this.player.seek(current.subtract(new Duration(LiteModMusicPlayer.config.resume_time_millis)));
            this.player.play();
        }
        return this;
    }

    public MusicPlayer stopMusic()
    {
        this.player.stop();
        return this;
    }

    public PlayerState getCurrentState()
    {
        return currentState;
    }

    public Music getPlaying()
    {
        return playing;
    }
}
