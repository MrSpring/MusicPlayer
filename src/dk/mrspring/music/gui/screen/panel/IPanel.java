package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.llcore.Color;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.gui.interfaces.IResizable;

/**
 * Created by MrSpring on 04-07-2015 for MC Music Player.
 */
public interface IPanel extends IGui, IResizable
{
    int getBottomBarOffset();

    int getTopBarOffset();

    Color getBottomBarColor();

    Color getTopBarColor();

    void setParent(IPanelContainer parent);

    void preDraw(int mouseX, int mouseY);
}
