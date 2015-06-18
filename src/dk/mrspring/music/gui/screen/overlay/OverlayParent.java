package dk.mrspring.music.gui.screen.overlay;

/**
 * Created by Konrad on 18-06-2015.
 */
public interface OverlayParent
{
    int getOverlayWidth();

    int getDefaultTextColor();

    int getInvertedTextColor();

    void addCard(Card card);
}
