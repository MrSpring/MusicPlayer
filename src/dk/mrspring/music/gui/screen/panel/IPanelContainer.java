package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.music.gui.menu.IMenuItem;
import dk.mrspring.music.gui.menu.Menu;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 04-07-2015 for MC Music Player.
 */
public interface IPanelContainer
{
    void openMenu(int x, int y, Menu.MenuAction action, IMenuItem... items);

    void openPanel(IPanel newPanel);

    Minecraft getMinecraft();
}
