package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.llcore.Color;
import dk.mrspring.music.gui.GuiAllMusicList;
import dk.mrspring.music.gui.screen.overlay.CardMusic;
import dk.mrspring.music.gui.screen.overlay.OverlayScreen;
import dk.mrspring.music.player.Music;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

/**
 * Created by MrSpring on 04-07-2015 for MC Music Player.
 */
public class AllMusicPanel extends GuiAllMusicList implements IPanel
{
    IPanelContainer parent;

    public AllMusicPanel(List<Music> allMusic)
    {
        super(0, 0, 100, 100, allMusic);
    }

    @Override
    protected boolean onElementClicked(int relMouseX, int relMouseY, int mouseX, int mouseY, int mouseButton, Music clicked)
    {
        System.out.println("Clicked");
        if (mouseButton == 0)
        {
            OverlayScreen overlay = new OverlayScreen("Music Details", (GuiScreen) parent);
            overlay.addCard(new CardMusic(overlay, clicked));
            parent.getMinecraft().displayGuiScreen(overlay);
            return true;
        } else return super.onElementClicked(relMouseX, relMouseY, mouseX, mouseY, mouseButton, clicked);
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
