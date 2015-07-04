package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.llcore.Color;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.gui.interfaces.IResizable;

/**
 * Created by MrSpring on 04-07-2015 for MC Music Player.
 */
public interface IPanel extends IGui, IResizable
{
    public int getBottomBarOffset();

    public int getTopBarOffset();

    public Color getBottomBarColor();

    public Color getTopBarColor();

    public void setParent(IPanelContainer parent);
}
