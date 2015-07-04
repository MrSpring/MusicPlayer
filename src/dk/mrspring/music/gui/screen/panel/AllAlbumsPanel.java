package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.llcore.Color;
import dk.mrspring.music.gui.GuiAllAlbumsList;
import dk.mrspring.music.util.Album;

import java.util.List;

/**
 * Created by MrSpring on 04-07-2015 for MC Music Player.
 */
public class AllAlbumsPanel extends GuiAllAlbumsList implements IPanel
{
    IPanelContainer parent;

    public AllAlbumsPanel(List<Album> allMusic)
    {
        super(0, 0, 100, 100, allMusic);
    }

    @Override
    public int getBottomBarOffset()
    {
        return 0;
    }

    @Override
    public int getTopBarOffset()
    {
        return 0;
    }

    @Override
    public Color getBottomBarColor()
    {
        return Color.BLACK;
    }

    @Override
    public Color getTopBarColor()
    {
        return Color.BLACK;
    }

    @Override
    public void setParent(IPanelContainer parent)
    {
        this.parent = parent;
    }

    // TODO: onElementClicked right-click
}
