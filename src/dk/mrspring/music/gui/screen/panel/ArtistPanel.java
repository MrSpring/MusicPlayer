package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.llcore.Color;
import dk.mrspring.music.gui.GuiArtistList;
import dk.mrspring.music.gui.menu.MenuItemButton;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.util.Album;
import dk.mrspring.music.util.Artist;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 04-07-2015 for MC Music Player.
 */
public class ArtistPanel extends GuiArtistList implements IPanel
{
    IPanelContainer parent;

    public ArtistPanel(Artist artist, Showing type)
    {
        super(0, 0, 100, 100, artist, type);
    }

    @Override
    public void setY(int y)
    {
        super.setY(y - getTopBarOffset());
    }

    @Override
    public void setHeight(int height)
    {
        super.setHeight(height + getTopBarOffset());
    }

    @Override
    protected boolean onElementClicked(int relMouseX, int relMouseY, int mouseX, int mouseY, int mouseButton, Object clicked)
    {
        if (mouseButton == 1 && parent != null)
        {
            String text = clicked instanceof Album ? ((Album) clicked).getAlbumName() : (clicked instanceof Music ? ((Music) clicked).getName() : "");
            Minecraft mc = parent.getMinecraft();
//            parent.openMenu(mouseX, mouseY,
//                    new MenuItemButton("IT'S WORKING!!!", mc.fontRendererObj, 0),
//                    new MenuItemButton(text, mc.fontRendererObj, 1)
//            );
            return true;
        } else return super.onElementClicked(relMouseX, relMouseY, mouseX, mouseY, mouseButton, clicked);
    }

    @Override
    public int getBottomBarOffset()
    {
        return 0;
    }

    @Override
    public int getTopBarOffset()
    {
        return Math.max(30, typeList.getHeight()+4);
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

    @Override
    public void preDraw(int mouseX, int mouseY)
    {

    }
}
