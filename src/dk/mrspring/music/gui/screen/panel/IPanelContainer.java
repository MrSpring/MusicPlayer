package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.music.gui.menu.IMenuItem;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 04-07-2015 for MC Music Player.
 */
public interface IPanelContainer
{
    public void openMenu(int x, int y, IMenuItem... items);

    public void openPanel(IPanel newPanel);

    public Minecraft getMinecraft();
}
