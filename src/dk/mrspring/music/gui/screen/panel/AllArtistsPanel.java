package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.llcore.Color;
import dk.mrspring.music.gui.GuiAllArtistsList;
import dk.mrspring.music.util.Artist;

import java.util.List;

/**
 * Created by MrSpring on 04-07-2015 for MC Music Player.
 */
public class AllArtistsPanel extends GuiAllArtistsList implements IPanel
{
    IPanelContainer parent;

    public AllArtistsPanel(List<Artist> allMusic)
    {
        super(0, 0, 100, 100, allMusic);
    }

    @Override
    public void setParent(IPanelContainer parent)
    {
        this.parent = parent;
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

    // TODO: onElementClicked right-click
}
