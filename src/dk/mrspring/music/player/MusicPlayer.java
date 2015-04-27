package dk.mrspring.music.player;

import com.sun.istack.internal.NotNull;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

import static dk.mrspring.music.player.PlayerState.*;

/**
 * Created by Karoline on 27-04-2015.
 */
public class MusicPlayer extends Thread
{
    private PlayerState currentState = LOADING;
    private Media playing;
    private MediaPlayer player;

    public MusicPlayer(@NotNull File file)
    {
        this.playing = new Media(file.toURI().toASCIIString());
        currentState = LOADING;
        try
        {
            player = new MediaPlayer(playing);
            player.setOnEndOfMedia(new Runnable()
            {
                @Override
                public void run()
                {
                    currentState = FINISHED;
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

    public File getPlaying()
    {
        return new File(playing.getSource());
    }
}
