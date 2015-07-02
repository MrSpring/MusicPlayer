package dk.mrspring.music;

import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import dk.mrspring.llcore.LLCore;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.gui.screen.GuiScreen;
import dk.mrspring.music.gui.screen.GuiScreenAllMusic;
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
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import javax.swing.*;
import java.io.File;
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
    public static ConsoleOutput log;

    public static String apiKey = "api";

    public static boolean disableKeys = false;
    public static boolean showConsole = false;

    public static Playlist testerList;

    AnyTimeKeyBind reloadConfig = new AnyTimeKeyBind(Keyboard.KEY_F5);
    AnyTimeKeyBind expandMiniPlayer = new AnyTimeKeyBind(Keyboard.KEY_P);
    AnyTimeKeyBind showNextUp = new AnyTimeKeyBind(Keyboard.KEY_O);

    AnyTimeKeyBind previous = new AnyTimeKeyBind(Keyboard.KEY_J);
    AnyTimeKeyBind playPause = new AnyTimeKeyBind(Keyboard.KEY_K);
    AnyTimeKeyBind next = new AnyTimeKeyBind(Keyboard.KEY_L);

    AnyTimeKeyBind openMM = new AnyTimeKeyBind(Keyboard.KEY_M);
    AnyTimeKeyBind toggleConsole=new AnyTimeKeyBind(Keyboard.KEY_F9);

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

        if (toggleConsole.isClicked())
            showConsole = !showConsole;

        if (showConsole)
        {
            ScaledResolution res = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
            log.draw(new Quad(5, 5, (res.getScaledWidth()-10)/3, (res.getScaledHeight()-10)/3));
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
                minecraft.displayGuiScreen(/*new GuiScreenFolderSelector(minecraft.currentScreen)*/new GuiScreenAllMusic(minecraft.currentScreen));
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
        log = new ConsoleOutput(Minecraft.getMinecraft().fontRendererObj);
        log.zOffset = 1;
        log.addLine("Initialized Music Player mod successfully.");
        musicHandler = new MusicHandler(config.auto_play, new File(System.getProperty("user.home"), "Music"));
        Playlist playlistOne = musicHandler.createNewPlaylist("Playlist 1");
        playlistOne.addAll(musicHandler.getAllAlbums().get(0).getMusicList());
        musicHandler.createNewPlaylist("Another playlist");
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
                saveAll();
            }
        }));
    }

    private void saveAll(){

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
