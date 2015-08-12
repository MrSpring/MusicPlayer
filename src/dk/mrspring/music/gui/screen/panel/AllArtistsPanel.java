package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.llcore.Color;
import dk.mrspring.music.gui.GuiAllArtistsList;
import dk.mrspring.music.gui.GuiArtistList;
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
    public void preDraw(int mouseX, int mouseY)
    {

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
    protected boolean onElementClicked(int relMouseX, int relMouseY, int globalMouseX, int globalMouseY, int mouseButton, Artist clicked)
    {
        if (mouseButton == 0)
        {
            parent.openPanel(new ArtistPanel(clicked, GuiArtistList.Showing.ALBUMS));
            return true;
        } else return false;
    }

    // TODO: onElementClicked right-click
}
