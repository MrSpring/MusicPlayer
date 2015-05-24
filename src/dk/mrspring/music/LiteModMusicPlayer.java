package dk.mrspring.music;

import com.mumfrey.liteloader.Tickable;
import dk.mrspring.llcore.LLCore;
import dk.mrspring.music.gui.screen.GuiMusicHome;
import dk.mrspring.music.gui.screen.GuiScreen;
import dk.mrspring.music.overlay.Overlay;
import dk.mrspring.music.player.MusicHandler;
import dk.mrspring.music.util.AnyTimeKeyBind;
import dk.mrspring.music.util.FileUtils;
import dk.mrspring.music.util.JsonUtils;
import javafx.embed.swing.JFXPanel;
import net.minecraft.client.Minecraft;
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
            minecraft.displayGuiScreen(new GuiMusicHome(minecraft.currentScreen));

        if (!(minecraft.currentScreen instanceof GuiScreen))
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
            JsonUtils.writeToFile(configFile, config);
        }
    }

    @Override
    public void init(File configPath)
    {
        core = new LLCore("music_player");
        configFile = new File(configPath, "musicplayer.json");
        coverLocation = new File(configPath.getParent(), "musiccovers");
        if (!coverLocation.exists())
            coverLocation.mkdir();
        loadConfigFile();
        initializeToolkit();
        musicHandler = new MusicHandler(config.auto_play, new File("C:\\Users\\Konrad\\Music"));
        overlay = new Overlay();
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
