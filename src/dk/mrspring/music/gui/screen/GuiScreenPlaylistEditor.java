package dk.mrspring.music.gui.screen;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiPlaylist;
import dk.mrspring.music.gui.GuiSimpleButton;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.player.Playlist;
import dk.mrspring.music.util.Miscellaneous;
import dk.mrspring.music.util.TranslateHelper;

/**
 * Created by Konrad on 24-05-2015.
 */
public class GuiScreenPlaylistEditor extends GuiScreen
{
    Playlist playlist;
    private double progress = 0;

    public GuiScreenPlaylistEditor(net.minecraft.client.gui.GuiScreen previousScreen, Playlist playlist)
    {
        super("gui.music.queue_manager.title", previousScreen);
        this.playlist = playlist;
    }

    public String getPlaylistName()
    {
        return this.playlist.getName();
    }

    @Override
    public String getTitle()
    {
        return TranslateHelper.translateFormat("gui.playlist_editor.title", getPlaylistName());
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.addGuiElement("back", new GuiSimpleButton(3, -getTopBarHeight() + 3, 60, getTopBarHeight() - 6, "Back"));

        this.addGuiElement("music_list", new GuiPlaylist(0, 0, width, height - getTopBarHeight()-getBottomBarHeight(), LiteModMusicPlayer.musicHandler.getQueue()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(0, 0, width, height).setColor(Color.BLACK).setAlpha(0.5F));

        GuiPlaylist list = (GuiPlaylist) this.getGui("music_list");
        progress = Miscellaneous.smoothDamp(list.isMovingInDeleteZone() ? 1 : 0, progress, 0.4F);
        this.setBottomBarHeight(getTopBarHeight() + (int) (25 * progress));
        this.setBottomBarColor(Color.morph(Color.BLACK, Color.RED, (float) progress));
        list.setHeight(height - getTopBarHeight() - getBottomBarHeight());

        super.drawScreen(mouseX, mouseY, partialTicks);

        float bigIconExtra = 10;
        float iconSize = 20 + (float) (bigIconExtra * progress);
        LiteModMusicPlayer.core.getDrawingHelper().drawIcon(LiteModMusicPlayer.core.getIcon("trash_can"), new Quad(width / 2 - (iconSize / 2), height - getTopBarHeight() - (getBottomBarHeight() / 2) - (iconSize / 2) - (int) (progress * 6D), iconSize, iconSize));

        if (getBottomBarHeight() > 35)
            LiteModMusicPlayer.core.getDrawingHelper().drawText(TranslateHelper.translateFormat("gui.playlist_editor.remove", getPlaylistName()), new Vector(width / 2, height - getTopBarHeight() - (getBottomBarHeight() / 2) + 3 + (iconSize / 2)), 0xFFFFFF, true, width - 20, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.CENTER);
    }

    @Override
    public void updateScreen()
    {

        /*IGui musicList = this.getGui("music_list");
        if (musicList instanceof GuiMusicList)
        {
            GuiMusicList list = (GuiMusicList) musicList;
        }*/
        super.updateScreen();
    }

    @Override
    public boolean updateElement(String identifier, IGui gui)
    {
        /*if (gui instanceof GuiMusicList)
        {
            GuiMusicList musicList = (GuiMusicList) gui;
            musicList.setHeight();
        }*/
        return true;
    }

    @Override
    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {
        super.guiClicked(identifier, gui, mouseX, mouseY, mouseButton);
        if (identifier.equals("back"))
            mc.displayGuiScreen(previousScreen);
    }
}
