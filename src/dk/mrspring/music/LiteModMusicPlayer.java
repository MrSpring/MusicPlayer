package dk.mrspring.music;

import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import dk.mrspring.llcore.LLCore;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.effect.EffectHandler;
import dk.mrspring.music.gui.screen.GuiScreen;
import dk.mrspring.music.gui.screen.GuiScreenAllMusic;
import dk.mrspring.music.gui.screen.overlay.OverlayScreen;
import dk.mrspring.music.overlay.PlayerOverlay;
import dk.mrspring.music.player.MusicHandler;
import dk.mrspring.music.player.Playlist;
import dk.mrspring.music.util.AnyTimeKeyBind;
import dk.mrspring.music.util.FileUtils;
import dk.mrspring.music.util.Icons;
import dk.mrspring.music.util.JsonUtils;
import dk.mrspring.music.util.filter.FileTypeFilter;
import dk.mrspring.updater.core.UpdateChecker;
import dk.mrspring.updater.gui.GuiScreenUpdater;
import javafx.embed.swing.JFXPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Konrad on 26-04-2015.
 */
public class LiteModMusicPlayer implements Tickable
{
    public static MusicHandler musicHandler;

    public static LLCore core;
    public static File configFile;
    public static File coverLocation;
    public static File playlistLocation;
    public static Config config;
    public static PlayerOverlay playerOverlay;
    public static ConsoleOutput log;
    public static EffectHandler effects;

    public static String apiKey = "77fdf3c6e86950cb4baf5c41218bdc33";

    public static boolean disableKeys = false;
    public static boolean showConsole = false;
    public static boolean showedFolderSelector = false;

    AnyTimeKeyBind reloadConfig = new AnyTimeKeyBind(Keyboard.KEY_F5);
    AnyTimeKeyBind expandMiniPlayer = new AnyTimeKeyBind(Keyboard.KEY_P);
    AnyTimeKeyBind showNextUp = new AnyTimeKeyBind(Keyboard.KEY_O);

    AnyTimeKeyBind previous = new AnyTimeKeyBind(Keyboard.KEY_J);
    AnyTimeKeyBind playPause = new AnyTimeKeyBind(Keyboard.KEY_K);
    AnyTimeKeyBind next = new AnyTimeKeyBind(Keyboard.KEY_L);

    AnyTimeKeyBind openMM = new AnyTimeKeyBind(Keyboard.KEY_M);
    AnyTimeKeyBind toggleConsole = new AnyTimeKeyBind(Keyboard.KEY_F9);

    public static void initializeToolkit()
    {
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new JFXPanel();
                latch.countDown();
            }
        });
        try
        {
            latch.await();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
    {
//        if (!showedFolderSelector && musicHandler == null)
//        {
//            showedFolderSelector = true;
//            minecraft.displayGuiScreen(new GuiScreenWelcome(minecraft.currentScreen));
//            if (config.show_startup_dialog)
//            {
//                config.show_startup_dialog = false;
//                minecraft.displayGuiScreen(new GuiScreenWelcome(minecraft.currentScreen)); // TODO: Show welcome screen, redirect to folder selector afterwards
//            } else
//                minecraft.displayGuiScreen(new GuiScreenFolderSelector(minecraft.currentScreen));
//        }

        if (toggleConsole.isClicked())
            showConsole = !showConsole;

        if (showConsole)
        {
            ScaledResolution res = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
            log.draw(new Quad(5, 5, (res.getScaledWidth() - 10) / 3, (res.getScaledHeight() - 10) / 3));
        }

        if (musicHandler != null)
        {
            if (!disableKeys)
            {
                if (reloadConfig.isClicked())
                    loadConfigFile();
                if (expandMiniPlayer.isClicked())
                    playerOverlay.toggleExpanded();
                if (showNextUp.isClicked())
                    playerOverlay.showNext();

                if (previous.isClicked())
                    musicHandler.playPrevious();
                if (playPause.isClicked())
                    musicHandler.toggle();
                if (next.isClicked())
                    musicHandler.playNext();

                if (openMM.isClicked())
                    minecraft.displayGuiScreen(/*new GuiScreenFolderSelector(minecraft.currentScreen)*/new GuiScreenAllMusic(minecraft.currentScreen));
            }

            if (!(minecraft.currentScreen instanceof GuiScreen) && !(minecraft.currentScreen instanceof OverlayScreen) && !(minecraft.currentScreen instanceof GuiScreenUpdater))
                playerOverlay.draw(musicHandler, minecraft);
        }

        if (openMM.isClicked())
            minecraft.displayGuiScreen(/*new GuiScreenWelcome(minecraft.currentScreen)*/new GuiScreenAllMusic(minecraft.currentScreen));

        effects.draw();
    }

    @Override
    public String getVersion()
    {
        return "1.0.0";
    }

    private void loadConfigFile()
    {
        System.out.println("Loading config!");
        if (configFile != null)
        {
            if (!configFile.exists())
                FileUtils.createFile(configFile);
            if (configFile.exists())
                config = JsonUtils.loadFromJson(configFile, Config.class);
            if (config == null)
                config = new Config();
            config.validateConfig();
            saveConfig();
        }
    }

    private void loadPlaylist()
    {
        try
        {
            if (playlistLocation.exists())
            {
                File[] found = core.getFileLoader().getFilesInFolder(playlistLocation, true, new FileTypeFilter(".json"));
                for (File file : found)
                {
                    Playlist result = musicHandler.registerPlaylistFromFile(file);
                    if (result != null)
                        log.addLine("Found playlist: \"" + result.getName() + "\".");
                    else log.addLine("Failed to load playlist from: \"" + file.getName() + "\".");
                }
            } else playlistLocation.mkdir();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void saveConfig()
    {
        if (configFile != null)
        {
            if (!configFile.exists())
                FileUtils.createFile(configFile);
            JsonUtils.writeToFile(configFile, config);
        }
    }

    private void savePlaylist()
    {
        List<Playlist> lists = musicHandler.getPlaylists();
        for (Playlist playlist : lists)
        {
            try
            {
                String json = playlist.toJson();
                int id = playlist.getId();
                File playlistFile = new File(playlistLocation, id + ".json");
                core.getFileLoader().writeToFile(playlistFile, json);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void saveAll()
    {
        saveConfig();
        savePlaylist();
    }

    private void clearAll()
    {
        clearCoverCache();
    }

    private void clearCoverCache()
    {
//        coverLocation.delete();
        try
        {
            org.apache.commons.io.FileUtils.deleteDirectory(coverLocation);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void init(File configPath)
    {
        UpdateChecker.getInstance().setMCVersion(Minecraft.getMinecraft().getVersion());
        UpdateChecker.getInstance().registerUpdateMod("tkm", "Kitchen", "1.3.11");
        UpdateChecker.getInstance().registerUpdateMod("igfe", "In-Game File Explorer", "0.1.0-BETA");
        UpdateChecker.getInstance().registerUpdateMod("musicplayer", getName(), getVersion());
        core = new LLCore("music_player");
        core.registerIcon(Icons.trash, "trash_can");
        core.registerIcon(Icons.search, "search");
        core.registerIcon(Icons.right_arrow, "right_arrow");
        configFile = new File(configPath, "musicplayer.json");
        playlistLocation = new File(LiteLoader.getGameDirectory(), "playlist");
        coverLocation = new File(LiteLoader.getGameDirectory(), "musiccovers");
        if (!coverLocation.exists())
            coverLocation.mkdir();
        loadConfigFile();
        initializeToolkit();
        log = new ConsoleOutput(Minecraft.getMinecraft().fontRendererObj);
        log.zOffset = 1;
        log.addLine("Initialized Music Player mod successfully.");
        if (config.hasValidMusicLocation())
            musicHandler = new MusicHandler(config.auto_play, new File(config.music_location));
        loadPlaylist();
        playerOverlay = new PlayerOverlay();
        effects = new EffectHandler();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                saveAll();
                if (config.clear_cover_cache_on_shutdown)
                    clearAll();
            }
        }));
    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath)
    {

    }

    @Override
    public String getName()
    {
        return "MC Music Player";
    }

    public static void reloadMusicHandler(String openFolder)
    {
        if (musicHandler != null) musicHandler.stopCurrentPlayer();
        musicHandler = new MusicHandler(config.auto_play, new File(openFolder));
    }
}
