package dk.mrspring.music.gui.screen.overlay;

import dk.mrspring.music.gui.interfaces.IGui;

/**
 * Created by Konrad on 18-06-2015.
 */
public abstract class Card implements IGui
{
    OverlayParent parent;

    public Card(OverlayParent overlayParent)
    {
        this.parent = overlayParent;
    }

    public abstract int getHeight();

    public boolean drawBackdrop(){
        return true;
    }

    public boolean handleMouseWheel(int mouseX, int mouseY, int mouseWheel)
    {
        return false;
    }
}
