package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.llcore.Color;
import dk.mrspring.music.gui.GuiPlaylist;
import dk.mrspring.music.player.Playlist;
import dk.mrspring.music.util.Miscellaneous;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 04-07-2015 for MC Music Player.
 */
public class PlaylistPanel extends GuiPlaylist implements IPanel
{
    double target = 0D;
    double progress = 0D;
    IPanelContainer parent;

    public PlaylistPanel(Playlist music)
    {
        super(0, 0, 100, 100, music);
    }

    @Override
    public void setParent(IPanelContainer parent)
    {
        this.parent = parent;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        super.draw(minecraft, mouseX, mouseY);

        if (isMovingInDeleteZone())
            target = 1D;
        else target = 0D;
        progress = Miscellaneous.smoothDamp(target, progress, 0.4D);
    }

    @Override
    public int getBottomBarOffset()
    {
        return (int) (25D * progress);
    }

    @Override
    public int getTopBarOffset()
    {
        return 20;
    }

    @Override
    public Color getBottomBarColor()
    {
        return Color.morph(Color.BLACK, Color.RED, (float) progress);
    }

    @Override
    public Color getTopBarColor()
    {
        return Color.BLACK;
    }
}
