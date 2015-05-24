package dk.mrspring.music.overlay;

import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Vector;
import net.minecraft.client.gui.FontRenderer;

import java.util.List;

/**
 * Created by Konrad on 29-04-2015.
 */
public class TextRender
{
    private String rendering;
    private int longestWidth, lines;
    private FontRenderer renderer;
    private int wrap;

    public TextRender(String toRender, FontRenderer renderer, int wrapWidth)
    {
        this.renderer = renderer;
        this.rendering = toRender;
        this.wrap = wrapWidth;
        this.recalculate();
    }

    public TextRender(String toRender, FontRenderer renderer)
    {
        this(toRender, renderer, Integer.MAX_VALUE);
    }

    public void recalculate()
    {
        int longest = 0;

        List<String> splitString = this.renderer.listFormattedStringToWidth(rendering, wrap);
        lines = splitString.size();
        for (String string : splitString)
        {
            int stringLength = renderer.getStringWidth(string);
            if (stringLength > longest)
                longest = stringLength;
        }

        this.longestWidth = longest;
    }

    public void setText(String newRendering)
    {
        if (!this.rendering.equals(newRendering))
        {
            this.rendering = newRendering;
            this.recalculate();
        }
    }

    public void setRenderer(FontRenderer renderer)
    {
        this.renderer = renderer;
        this.recalculate();
    }

    public void setWrapLength(int wrap)
    {
        if (this.wrap != wrap)
        {
            this.wrap = wrap;
            this.recalculate();
        }
    }

    public int getLongestLine()
    {
        return longestWidth;
    }

    public int getLines()
    {
        return lines;
    }

    public int getTotalHeight()
    {
        return getLines() * renderer.FONT_HEIGHT;
    }

    public void render(DrawingHelper helper, int x, int y, int color, boolean shadow, DrawingHelper.VerticalTextAlignment vertical, DrawingHelper.HorizontalTextAlignment horizontal)
    {
        helper.drawText(rendering, new Vector(x, y), color, shadow, this.wrap, vertical, horizontal);
    }
}
