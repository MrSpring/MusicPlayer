package dk.mrspring.music.gui.screen;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiAllMusicList;
import dk.mrspring.music.gui.GuiCustomTextField;
import dk.mrspring.music.gui.GuiPlaylist;
import dk.mrspring.music.gui.GuiSimpleButton;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.util.Miscellaneous;
import dk.mrspring.music.util.TranslateHelper;

/**
 * Created by Konrad on 26-05-2015.
 */
public class GuiScreenAllMusic extends GuiScreen
{
    public GuiScreenAllMusic(net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super("All Music", previousScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.addGuiElement("search_bar", new GuiCustomTextField((width / 3) * 2, -getTopBarHeight() + 3, width / 3, getTopBarHeight() - 6, ""));
        this.addGuiElement("back", new GuiSimpleButton(3, -getTopBarHeight() + 3, 60, getTopBarHeight() - 6, "Back"));
        this.addGuiElement("list", new GuiAllMusicList(0, 0, width, height - getTopBarHeight() - getBottomBarHeight(), LiteModMusicPlayer.musicHandler.getAllMusic()));
    }

    @Override
    public boolean updateElement(String identifier, IGui gui)
    {
        if (identifier.equals("list"))
        {
            GuiAllMusicList list = (GuiAllMusicList) gui;
            String newFilter = ((GuiCustomTextField) this.getGui("search_bar")).getText();
            list.getFilter().setFilter(newFilter);
        }
        return super.updateElement(identifier, gui);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(0, 0, width, height).setColor(Color.BLACK).setAlpha(0.5F));
        super.drawScreen(mouseX, mouseY, partialTicks);
        float iconSize = getTopBarHeight() - 18;
        LiteModMusicPlayer.core.getDrawingHelper().drawIcon(LiteModMusicPlayer.core.getIcon("search"), new Quad(width - iconSize - 5 - (width / 3), -getTopBarHeight() + 8, iconSize, iconSize));
    }

    @Override
    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {
        super.guiClicked(identifier, gui, mouseX, mouseY, mouseButton);
        if (identifier.equals("back"))
            mc.displayGuiScreen(previousScreen);
    }
}
