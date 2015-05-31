package dk.mrspring.music.player;

import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.util.Album;
import dk.mrspring.music.util.Artist;
import dk.mrspring.music.util.JsonUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Konrad on 27-04-2015.
 */
public class MusicHandler
{
    public Queue queue;
    List<Music> allMusic = new ArrayList<Music>();
    List<Album> allAlbums = new ArrayList<Album>();
    List<Artist> allArtists = new ArrayList<Artist>();
    Map<Integer, Music> idLookup = new HashMap<Integer, Music>();
    MusicPlayer player;

    public MusicHandler()
    {
        queue = new Queue(new ArrayList<Music>());
    }

    public MusicHandler(boolean autoPlay, File... baseFiles)
    {
//        this();
        for (File folder : baseFiles)
            loadMusicFrom(folder);
        createDefaultQueue();
        if (autoPlay)
            play(queue.getCurrent());
        Map<String, List<Music>> albumLookup = new HashMap<String, List<Music>>();
        for (Music music : allMusic)
        {
            idLookup.put(music.getId(), music);
            String album = music.getAlbum();
            if (!albumLookup.containsKey(album))
                albumLookup.put(album, new ArrayList<Music>());
            albumLookup.get(album).add(music);
        }

        for (Map.Entry<String, List<Music>> entry : albumLookup.entrySet())
            allAlbums.add(new Album(entry.getValue()));

        Map<String, List<Album>> artistLookup = new HashMap<String, List<Album>>();
        for (Album album : allAlbums)
        {
            String artist = album.getArtistName();
            if (!artistLookup.containsKey(artist))
                artistLookup.put(artist, new ArrayList<Album>());
            artistLookup.get(artist).add(album);
        }

        for (Map.Entry<String, List<Album>> entry : artistLookup.entrySet())
            allArtists.add(new Artist(entry.getValue()));
    }

    public Playlist loadPlaylistFromFile(File file)
    {
        try
        {
            Map<String, Object> json = JsonUtils.loadFromJson(file, HashMap.class);
            if (json != null)
            {
                String name = (String) json.get("name");
                List<Double> music = (List<Double>) json.get("music");
                List<Music> musicList = new ArrayList<Music>();
                for (Double id : music)
                    if (idLookup.containsKey(id.intValue()))
                        musicList.add(idLookup.get(id.intValue()));
                return new Playlist(name, musicList);
            } else return null;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void checkForQueueChanges()
    {
        if (player != null)
        {
            Music playing = player.getPlaying();
            if (queue.getCurrent() != playing)
                play(queue.getCurrent());
        }
    }

    public void createDefaultQueue()
    {
        List<Music> newQueue = new ArrayList<Music>(allMusic);
        if (queue == null)
            queue = new Queue(newQueue);
        else queue.updateQueue(newQueue);
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

    public List<Music> getAllMusic()
    {
        return this.allMusic;
    }

    public List<Album> getAllAlbums()
    {
        return allAlbums;
    }

    public List<Artist> getAllArtists()
    {
        return allArtists;
    }
}
