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
    List<Music> allMusic = new ArrayList<>();
    MusicPlayer player;

    public MusicHandler()
    {

    }

    public MusicHandler(File... baseFiles)
    {
        this();
        for (File folder : baseFiles)
            loadMusicFrom(folder);
        try
        {
            player = new MusicPlayer(allMusic.get(0).getMusicFile());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (player != null)
            player.start();
    }

    public void loadMusicFrom(File folder)
    {
        List<File> foundFiles = new ArrayList<>();
        LiteModMusicPlayer.core.getFileLoader().addFilesToList(folder, foundFiles, false, FileFilterUtils.suffixFileFilter(".mp3", IOCase.INSENSITIVE));
        for (File file : foundFiles)
        {
            System.out.println("Found music file: " + file.getPath());
            allMusic.add(new Music(file));
        }
    }

    public void toggle()
    {
        if (player != null)
        {
            if (player.isPaused())
                player.resumeMusic();
            else player.pauseMusic();
//            player.togglePaused();
        }
    }
}
