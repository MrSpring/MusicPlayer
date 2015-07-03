package dk.mrspring.music.gui.screen;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiFileExplorer;
import dk.mrspring.music.gui.GuiSimpleButton;
import dk.mrspring.music.util.GuiHelper;
import net.minecraft.client.Minecraft;

/**
 * Created by Konrad on 27-06-2015.
 */
public class GuiScreenFolderSelector extends GuiScreen
{
    int progress = 0;

    public GuiScreenFolderSelector(net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super("Select Music Folder", previousScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        addGuiElement("explorer", new GuiFileExplorer(0, 0, width, height - getTopBarHeight() - 8 /*- getBottomBarHeight()*/, System.getProperty("user.home")).setShowBackground(false));
        addGuiElement("select_folder", new GuiSimpleButton(width - 5 - 100, height - getTopBarHeight() - getBottomBarHeight() + 5, 100, getBottomBarHeight() - 10, "gui.explorer.use_folder"));
    }

    @Override
    public void onResize(Minecraft mcIn, int p_175273_2_, int p_175273_3_)
    {
        super.onResize(mcIn, p_175273_2_, p_175273_3_);
        previousScreen.onResize(mcIn, p_175273_2_, p_175273_3_);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        previousScreen.drawScreen(-1000, -1000, partialTicks);
        GuiHelper.drawRainbowSquare(progress, 0, 0, width, height);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    public void updateScreen()
    {
        super.updateScreen();
        GuiHelper.increaseProgress(progress, 2);
    }
}
