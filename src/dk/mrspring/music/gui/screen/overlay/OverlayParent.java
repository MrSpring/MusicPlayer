package dk.mrspring.music.gui.screen.overlay;

import net.minecraft.client.Minecraft;

/**
 * Created by Konrad on 18-06-2015.
 */
public interface OverlayParent
{
    int getOverlayWidth();

    int getDefaultTextColor();

    int getInvertedTextColor();

    void addCard(Card card);

    void addCards(Card... cards);

    Minecraft getMinecraft();

    void closeOverlay();

    void clampScroll();
}
