package dk.mrspring.music.gui.screen;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiFileExplorer;
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
        int offset = 255;

        int r = Math.min(255, Math.max(0, progress < 4 * 255 ? progress + 255 : progress - 255 - 255 - 255 - 255));
        r -= (Math.min(255, progress < 4 * 255 ? Math.max(0, progress - 255) : 0));
        int g = Math.min(255, Math.max(0, progress));
        g -= (Math.min(255, Math.max(0, progress - 255 - 255 - 255)));
        int b = Math.min(255, Math.max(0, progress - 255 - 255));
        b -= (Math.min(255, Math.max(0, progress - 255 - 255 - 255 - 255 - 255)));

        int progress2 = progress + offset;
        if (progress2 > MAX_PROGRESS) progress2 -= MAX_PROGRESS;
        int r2 = Math.min(255, Math.max(0, progress2 < 4 * 255 ? progress2 + 255 : progress2 - 255 - 255 - 255 - 255));
        r2 -= (Math.min(255, progress2 < 4 * 255 ? Math.max(0, progress2 - 255) : 0));
        int g2 = Math.min(255, Math.max(0, progress2));
        g2 -= (Math.min(255, Math.max(0, progress2 - 255 - 255 - 255)));
        int b2 = Math.min(255, Math.max(0, progress2 - 255 - 255));
        b2 -= (Math.min(255, Math.max(0, progress2 - 255 - 255 - 255 - 255 - 255)));

        int progress3 = progress + offset + offset;
        if (progress3 > MAX_PROGRESS) progress3 -= MAX_PROGRESS;
        int r3 = Math.min(255, Math.max(0, progress3 < 4 * 255 ? progress3 + 255 : progress3 - 255 - 255 - 255 - 255));
        r3 -= (Math.min(255, progress3 < 4 * 255 ? Math.max(0, progress3 - 255) : 0));
        int g3 = Math.min(255, Math.max(0, progress3));
        g3 -= (Math.min(255, Math.max(0, progress3 - 255 - 255 - 255)));
        int b3 = Math.min(255, Math.max(0, progress3 - 255 - 255));
        b3 -= (Math.min(255, Math.max(0, progress3 - 255 - 255 - 255 - 255 - 255)));

        int progress4 = progress + offset + offset + offset;
        if (progress4 > MAX_PROGRESS) progress4 -= MAX_PROGRESS;
        int r4 = Math.min(255, Math.max(0, progress4 < 4 * 255 ? progress4 + 255 : progress4 - 255 - 255 - 255 - 255));
        r4 -= (Math.min(255, progress4 < 4 * 255 ? Math.max(0, progress4 - 255) : 0));
        int g4 = Math.min(255, Math.max(0, progress4));
        g4 -= (Math.min(255, Math.max(0, progress4 - 255 - 255 - 255)));
        int b4 = Math.min(255, Math.max(0, progress4 - 255 - 255));
        b4 -= (Math.min(255, Math.max(0, progress4 - 255 - 255 - 255 - 255 - 255)));

//        System.out.println(Math.max(0, Math.min(255, progress-255)));
//        LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(0, 0, width, height).setColor(new Color(r, g, b)));
//        drawDefaultBackground();
        previousScreen.drawScreen(-1000, -1000, partialTicks);
        LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(
                new Vector(0, 0).setColor(new Color(r, g, b)),
                new Vector(width, 0).setColor(new Color(r2, g2, b2)),
                new Vector(width, height).setColor(new Color(r3, g3, b3)),
                new Vector(0, height).setColor(new Color(r4, g4, b4))
        ).setAlpha(0.5F));

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        progress++;
        if (progress > MAX_PROGRESS) progress = 0;
    }
}
