package dk.mrspring.music.gui.screen;

import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiFileExplorer;
import dk.mrspring.music.gui.GuiSimpleButton;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.util.GuiUtils;

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

        this.doPreviousScreen();

        addGuiElement("explorer", new GuiFileExplorer(0, 0, width, height - getTopBarHeight() - 8, System.getProperty("user.home")).setShowBackground(false));
        addGuiElement("select_folder", new GuiSimpleButton(width - 5 - 100, height - getTopBarHeight() - getBottomBarHeight() + 5, 100, getBottomBarHeight() - 10, "gui.explorer.use_folder"));
    }

    @Override
    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {
        super.guiClicked(identifier, gui, mouseX, mouseY, mouseButton);
        if (identifier.equals("select_folder"))
        {
            String openFolder = ((GuiFileExplorer) getGui("explorer")).getCurrentAbsolutePath();
            LiteModMusicPlayer.config.music_location = openFolder;
            LiteModMusicPlayer.reloadMusicHandler(openFolder);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        GuiUtils.drawRainbowSquare(progress, 0, 0, width, height);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
