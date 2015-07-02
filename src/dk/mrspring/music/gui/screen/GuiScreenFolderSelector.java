package dk.mrspring.music.gui.screen;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiFileExplorer;
import dk.mrspring.music.gui.GuiSimpleButton;
import net.minecraft.client.Minecraft;

/**
 * Created by Konrad on 27-06-2015.
 */
public class GuiScreenFolderSelector extends GuiScreen
{
    final int MAX_PROGRESS = 255 * 6;
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
        int[] c1 = getRGB(0);
        int[] c2 = getRGB(255);
        int[] c3 = getRGB(255 * 2);
        int[] c4 = getRGB(255 * 3);

        previousScreen.drawScreen(-1000, -1000, partialTicks);
        LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(
                new Vector(0, 0).setColor(new Color(c1[0], c1[1], c1[2])),
                new Vector(width, 0).setColor(new Color(c2[0], c2[1], c2[2])),
                new Vector(width, height).setColor(new Color(c3[0], c3[1], c3[2])),
                new Vector(0, height).setColor(new Color(c4[0], c4[1], c4[2]))
        ).setAlpha(0.5F));

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private int[] getRGB(int offset)
    {
        int wOffset = progress + offset;
        if (wOffset > MAX_PROGRESS) wOffset -= MAX_PROGRESS;
        int r = Math.min(255, Math.max(0, wOffset < 4 * 255 ? wOffset + 255 : wOffset - 255 - 255 - 255 - 255));
        r -= (Math.min(255, wOffset < 4 * 255 ? Math.max(0, wOffset - 255) : 0));
        int g = Math.min(255, Math.max(0, wOffset));
        g -= (Math.min(255, Math.max(0, wOffset - 255 - 255 - 255)));
        int b = Math.min(255, Math.max(0, wOffset - 255 - 255));
        b -= (Math.min(255, Math.max(0, wOffset - 255 - 255 - 255 - 255 - 255)));
        return new int[]{r, g, b};
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        progress++;
        if (progress > MAX_PROGRESS) progress = 0;
    }
}
