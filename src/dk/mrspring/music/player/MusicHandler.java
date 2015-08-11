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
    List<Playlist> playlists = new ArrayList<Playlist>();
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
        {
            List<Music> musicList = entry.getValue();
            Album newAlbum = new Album(musicList);
            for (Music music : musicList)
                music.setAlbumInstance(newAlbum);
            allAlbums.add(newAlbum);
        }

        Map<String, List<Album>> artistLookup = new HashMap<String, List<Album>>();
        for (Album album : allAlbums)
        {
            String artist = album.getArtistName();
            if (!artistLookup.containsKey(artist))
                artistLookup.put(artist, new ArrayList<Album>());
            artistLookup.get(artist).add(album);
        }

        for (Map.Entry<String, List<Album>> entry : artistLookup.entrySet())
        {
            List<Album> albumList = entry.getValue();
            Artist newArtist = new Artist(albumList);
            for (Album album : albumList)
                for (Music music : album.getMusicList())
                    music.setArtistInstance(newArtist);
            allArtists.add(newArtist);
        }
    }

    public Playlist createPlaylistFromFile(File file)
    {
        try
        {
            Map<String, Object> json = JsonUtils.loadFromJson(file, HashMap.class);
            if (json != null)
            {
                System.out.println("Created map.");
                String name = (String) json.get("name");
                System.out.println("Got a name: " + name);
                Double dId = (Double) json.get("id");
                int id = dId.intValue();
                System.out.println("Got an id: " + id);
                List<Double> music = (List<Double>) json.get("music");
                List<Music> musicList = new ArrayList<Music>();
                for (Double songId : music)
                    if (idLookup.containsKey(songId.intValue()))
                        musicList.add(idLookup.get(songId.intValue()));
                Playlist newPlaylist = new Playlist(name, musicList).setId(id);
                System.out.println("Playlist created...");
                return newPlaylist;
            } else return null;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Playlist registerPlaylistFromFile(File file)
    {
        Playlist fromFile = createPlaylistFromFile(file);
        if (fromFile != null)
            registerPlaylist(fromFile);
        return fromFile;
    }

    public Playlist createNewPlaylist(String name)
    {
        Playlist newPlaylist = new Playlist(name);
        registerPlaylist(newPlaylist);
        return newPlaylist;
    }

    public MusicHandler registerPlaylist(Playlist list)
    {
        this.playlists.add(list);
        return this;
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
//            System.out.println("Found music file: " + file.getPath());
            LiteModMusicPlayer.log.addLine("Found music file: " + file.getName());
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

    public List<Playlist> getPlaylists()
    {
        return playlists;
    }

    public MusicHandler removePlaylist(Playlist removing)
    {
        if (playlists.contains(removing)) playlists.remove(removing);
        return this;
    }
}
