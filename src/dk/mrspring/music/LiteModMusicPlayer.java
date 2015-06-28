package dk.mrspring.music;

import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import dk.mrspring.llcore.LLCore;
import dk.mrspring.music.gui.screen.*;
import dk.mrspring.music.gui.screen.overlay.OverlayScreen;
import dk.mrspring.music.overlay.Overlay;
import dk.mrspring.music.player.MusicHandler;
import dk.mrspring.music.player.Playlist;
import dk.mrspring.music.util.AnyTimeKeyBind;
import dk.mrspring.music.util.FileUtils;
import dk.mrspring.music.util.Icons;
import dk.mrspring.music.util.JsonUtils;
import javafx.embed.swing.JFXPanel;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Konrad on 26-04-2015.
 */
public class LiteModMusicPlayer implements Tickable
{
    public static MusicHandler musicHandler;

    public static LLCore core;
    public static File configFile;
    public static Config config;
    public static Overlay overlay;
    public static File coverLocation;

    public static String apiKey = "api";

    public static boolean disableKeys = false;

    public static Playlist testerList;

    AnyTimeKeyBind reloadConfig = new AnyTimeKeyBind(Keyboard.KEY_F5);
    AnyTimeKeyBind expandMiniPlayer = new AnyTimeKeyBind(Keyboard.KEY_P);
    AnyTimeKeyBind showNextUp = new AnyTimeKeyBind(Keyboard.KEY_O);

    AnyTimeKeyBind previous = new AnyTimeKeyBind(Keyboard.KEY_J);
    AnyTimeKeyBind playPause = new AnyTimeKeyBind(Keyboard.KEY_K);
    AnyTimeKeyBind next = new AnyTimeKeyBind(Keyboard.KEY_L);

    AnyTimeKeyBind openMM = new AnyTimeKeyBind(Keyboard.KEY_M);

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
        if (config.show_startup_dialog)
        {
//            config.show_startup_dialog = false;
//            minecraft.displayGuiScreen(new GuiScreenFolderSelector(minecraft.currentScreen));
        }

        if (!disableKeys)
        {
            if (reloadConfig.isClicked())
                loadConfigFile();
            if (expandMiniPlayer.isClicked())
                overlay.toggleExpanded();
            if (showNextUp.isClicked())
                overlay.showNext();

            if (previous.isClicked())
                musicHandler.playPrevious();
            if (playPause.isClicked())
                musicHandler.toggle();
            if (next.isClicked())
                musicHandler.playNext();

            if (openMM.isClicked())
                minecraft.displayGuiScreen(new GuiScreenFolderSelector(minecraft.currentScreen)/*new GuiScreenAllMusic(minecraft.currentScreen)*/);
        }

        if (!(minecraft.currentScreen instanceof GuiScreen) && !(minecraft.currentScreen instanceof OverlayScreen))
            overlay.draw(musicHandler, minecraft);
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

    private void saveConfig()
    {
        if (configFile != null)
        {
            if (!configFile.exists())
                FileUtils.createFile(configFile);
            JsonUtils.writeToFile(configFile, config);
        }
    }

    @Override
    public void init(File configPath)
    {
        core = new LLCore("music_player");
        core.registerIcon(Icons.trash, "trash_can");
        core.registerIcon(Icons.search, "search");
        core.registerIcon(Icons.right_arrow, "right_arrow");
        configFile = new File(configPath, "musicplayer.json");
        coverLocation = new File(LiteLoader.getGameDirectory(), "musiccovers");
        if (!coverLocation.exists())
            coverLocation.mkdir();
        loadConfigFile();
        initializeToolkit();
        musicHandler = new MusicHandler(config.auto_play, new File(System.getProperty("user.home"), "Music"));
        /*testerList = new Playlist("TestList", musicHandler.getAllMusic());
//        JsonUtils.writeToFile(new File(configPath, "playlist.json"), testerList.toJson());
        try
        {
            core.getFileLoader().writeToFile(new File(configPath, "playlist.json"), testerList.toJson());
        } catch (IOException e)
        {
            e.printStackTrace();
        }*/
        testerList = musicHandler.loadPlaylistFromFile(new File(configPath, "playlist.json"));
        overlay = new Overlay();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                saveConfig();
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
}
