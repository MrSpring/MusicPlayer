package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.llcore.Color;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiAllMusicList;
import dk.mrspring.music.gui.screen.overlay.CardMusic;
import dk.mrspring.music.gui.screen.overlay.OverlayScreen;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.util.MenuUtils;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

/**
 * Created by MrSpring on 04-07-2015 for MC Music Player.
 */
public class AllMusicPanel extends GuiAllMusicList implements IPanel
{
    IPanelContainer parent;
    long lastClick = 0;

    public AllMusicPanel(List<Music> allMusic)
    {
        super(0, 0, 100, 100, allMusic);
    }

    @Override
    protected boolean onElementClicked(int relMouseX, int relMouseY, int mouseX, int mouseY, int mouseButton, final Music clicked)
    {
        if (mouseButton == 0)
        {
            long currentTime = System.currentTimeMillis();
            long diff = currentTime - lastClick;
            if (diff < LiteModMusicPlayer.config.double_click_time)
            {
                OverlayScreen overlay = new OverlayScreen("Music Details", (GuiScreen) parent);
                overlay.addCard(new CardMusic(overlay, clicked));
                parent.getMinecraft().displayGuiScreen(overlay);
            }
            lastClick = currentTime;
            return true;

        } else if (mouseButton == 1)
        {
            MenuUtils.MenuResult menu = MenuUtils.createMusicMenu((GuiScreen) parent, clicked);
            parent.openMenu(menu.action, menu.items);
            return true;
        } else return super.onElementClicked(relMouseX, relMouseY, mouseX, mouseY, mouseButton, clicked);
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

    // TODO: onElementClicked right-click
}
