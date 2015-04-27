package dk.mrspring.music;

import com.mumfrey.liteloader.Tickable;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.LLCore;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.player.MusicHandler;
import dk.mrspring.music.util.AnyTimeKeyBind;
import dk.mrspring.music.util.FileUtils;
import dk.mrspring.music.util.JsonUtils;
import dk.mrspring.music.util.Miscellaneous;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.io.File;

/**
 * Created by Konrad on 26-04-2015.
 */
public class LiteModMusicPlayer implements Tickable
{
    public static final float MAX_WIDTH = 200F, HEIGHT = 50F;

    public static MusicHandler musicHandler;

    public static LLCore core;
    public static File configFile;
    public static Config config;

//    boolean reloaded = false;
//    boolean toggledExpanded = false;
//    boolean toggledShowNext = false;

    AnyTimeKeyBind reloadConfig = new AnyTimeKeyBind(Keyboard.KEY_F5);
    AnyTimeKeyBind expandMiniplayer = new AnyTimeKeyBind(Keyboard.KEY_P);
    AnyTimeKeyBind showNextUp = new AnyTimeKeyBind(Keyboard.KEY_O);
    AnyTimeKeyBind togglePlaying = new AnyTimeKeyBind(Keyboard.KEY_I);

    float width = 0F;
    float nextHeight = 0F;

    boolean size, showNext;

    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
    {
        width = Miscellaneous.smoothDamp(size ? MAX_WIDTH : HEIGHT, width);
        nextHeight = Miscellaneous.smoothDamp(showNext ? 13 : 0, nextHeight);

        DrawingHelper helper = core.getDrawingHelper();

        helper.drawButtonThingy(new Quad(5, 5, width, HEIGHT), 0.5F, true, config.overlay_start_color, 0.25F, config.overlay_end_color, 0.5F);
        helper.drawShape(new Quad(5 + 3, 5 + 3, 50 - 6, 50 - 6).setColor(Color.GREEN));

        if (nextHeight > 0.1F)
        {
            helper.drawShape(new Quad(5, 5 + HEIGHT - 1, 1, nextHeight).setColor(Color.BLACK).setAlpha(0.25F)).drawShape(new Quad(5 + 30 + 1, 5 + HEIGHT, 1, Math.max(nextHeight - 1, 0)).setColor(Color.BLACK).setAlpha(0.25F));
            helper.drawShape(new Quad(5 + 1, 5 + HEIGHT, 30, nextHeight).setColor(Color.BLACK).setAlpha(0.25F));
            helper.drawShape(new Quad(5 + 1, 5 + HEIGHT - 1, 1, nextHeight)).drawShape(new Quad(5 + 30, 5 + HEIGHT - 1, 1, nextHeight).setColor(Color.LT_GREY)).drawShape(new Quad(5 + 1, 5 + HEIGHT + nextHeight - 2, 30, 1).setColor(Color.LT_GREY));
        }

        if (reloadConfig.isClicked())
            loadConfigFile();
        if (expandMiniplayer.isClicked())
            size = !size;
        if (showNextUp.isClicked())
            showNext = !showNext;
        if (togglePlaying.isClicked())
            musicHandler.toggle();
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
        loadConfigFile();
        musicHandler = new MusicHandler(new File("D:\\Music"));
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
