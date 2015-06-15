package dk.mrspring.music.gui.screen;

import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.util.TranslateHelper;

/**
 * Created by Konrad on 26-05-2015.
 */
public class GuiScreenQueueEditor extends GuiScreenPlaylistEditor // TODO: Remove
{
    public GuiScreenQueueEditor(net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super(previousScreen, LiteModMusicPlayer.musicHandler.getQueue());
    }

    @Override
    public String getPlaylistName()
    {
        return TranslateHelper.translate("playlist.queue.name");
    }
}
