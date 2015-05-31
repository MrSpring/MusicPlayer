package dk.mrspring.music.overlay;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import net.minecraft.client.gui.FontRenderer;

/**
 * Created by Konrad on 28-05-2015.
 */
public class MultilineTextRender
{
    private String rendering;
    private TextRender[] renders;
    private int wrap;
    private FontRenderer renderer;
    private boolean lines;
    private int lineHeight;

    public MultilineTextRender(String rendering, FontRenderer renderer, int wrapWidth, boolean drawLines, int lineHeight)
    {
        this.rendering = rendering;
        this.renderer = renderer;
        this.wrap = wrapWidth;
        this.lines = drawLines;
        this.lineHeight = lineHeight;
        recalculate();
    }

    public void recalculate()
    {
        String[] lines = rendering.split("\n");
        renders = new TextRender[lines.length];
        for (int i = 0; i < lines.length; i++)
        {
            String line = lines[i];
            renders[i] = new TextRender(line, renderer, wrap);
        }
    }

    public int getLongestLine()
    {
        int longest = 0;
        for (TextRender render : renders)
            if (render.getLongestLine() > longest)
                longest = render.getLongestLine();
        return longest;
    }

    public int getTotalHeight()
    {
        int height = 0;
        for (int i = 0; i < renders.length; i++)
        {
            TextRender render = renders[i];
            height += render.getTotalHeight();
            if (i + 1 < renders.length)
                height += lineHeight;
        }
        return height;
    }

    public void setText(String newText)
    {
        if (!this.rendering.equals(newText))
        {
            this.rendering = newText;
            this.recalculate();
        }
    }

    public void render(DrawingHelper helper, int x, int y, int color, boolean shadow, DrawingHelper.VerticalTextAlignment vertical, DrawingHelper.HorizontalTextAlignment horizontal)
    {
        int yOffset = 0;
        for (int i = 0; i < renders.length; i++)
        {
            TextRender render = renders[i];
            render.render(helper, x, y + yOffset, color, shadow, vertical, horizontal);
            yOffset += render.getTotalHeight();
            if (i + 1 < renders.length)
            {
                if (lines)
                {
                    if (vertical == DrawingHelper.VerticalTextAlignment.LEFT)
                    {
                        helper.drawShape(new Quad(x + 3 + 1, y + yOffset+(lineHeight/2) + 1, wrap - 6, 1).setColor(Color.DK_GREY));
                        helper.drawShape(new Quad(x + 3, y + yOffset+(lineHeight/2), wrap - 6, 1));
                    } else if (vertical == DrawingHelper.VerticalTextAlignment.CENTER)
                    {
                        helper.drawShape(new Quad(x - (render.getLongestLine() / 2) + 3 + 1, y + yOffset+(lineHeight/2) + 1, wrap - 6, 1).setColor(Color.DK_GREY));
                        helper.drawShape(new Quad(x - (render.getLongestLine() / 2) + 3, y + yOffset+(lineHeight/2), wrap - 6, 1));
                    } else
                    {
                        helper.drawShape(new Quad(x - render.getLongestLine() + 3 + 1, y + yOffset+(lineHeight/2) + 1, wrap - 6, 1).setColor(Color.DK_GREY));
                        helper.drawShape(new Quad(x - render.getLongestLine() + 3, y + yOffset+(lineHeight/2), wrap - 6, 1));
                    }
                }
                yOffset += lineHeight;
            }
        }
    }
}
