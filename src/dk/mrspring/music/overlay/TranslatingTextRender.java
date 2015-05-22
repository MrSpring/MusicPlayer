package dk.mrspring.music.overlay;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.StatCollector;

/**
 * Created by Konrad on 29-04-2015.
 */
public class TranslatingTextRender extends TextRender
{
    String notTranslated;

    public TranslatingTextRender(String toRender, FontRenderer renderer, int wrapWidth)
    {
        super(StatCollector.translateToLocal(toRender), renderer, wrapWidth);
        this.notTranslated = toRender;
    }

    public TranslatingTextRender(String toRender, FontRenderer renderer)
    {
        this(toRender, renderer, Integer.MAX_VALUE);
    }

    @Override
    public void setText(String newRendering)
    {
        if (!newRendering.equals(notTranslated))
        {
            this.notTranslated = newRendering;
            super.setText(translateText(newRendering));
        }
    }

    public String translateText(String text)
    {
        return StatCollector.translateToLocal(text);
    }
}
