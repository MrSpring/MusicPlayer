package dk.mrspring.music.util;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiUtils
{
    public static final int MAX_PROGRESS = 255 * 6;

    public static void drawOptionsIcon(int x, int y, int width, int height)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();

        float size = (((float) height) / 5F), xOffset = (((float) width) - size) / 2F;

        helper.drawShape(new Quad(x + xOffset, y, size, size))
                .drawShape(new Quad(x + xOffset, y + (size * 2), size, size))
                .drawShape(new Quad(x + xOffset, y + (size * 4), size, size));
    }

    public static void drawScrollbar(int x, int y, int width, int height, int scroll, int maxScroll, int listHeight)
    {
        drawScrollbar(x, y, width, height, scroll, maxScroll, listHeight, 1F);
    }

    public static void drawScrollbar(int x, int y, int width, int height, int scroll, int maxScroll, int listHeight, float alpha)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();

        int scrollBarWidth = width - 2;
        double progress = ((double) height) / (double) (listHeight);
        int scrollBarHeight = (int) (progress * (height - 2));
        progress = ((double) scroll) / ((double) maxScroll);
        int scrollBarY = y + 1 + (int) (((height - 2) - scrollBarHeight) * progress);
        helper.drawShape(new Quad(x, y, width, height).setColor(Color.BLACK).setAlpha(alpha * 0.5F));
        helper.drawShape(new Quad(x + 1, scrollBarY + 1, scrollBarWidth, scrollBarHeight - 2).setAlpha(alpha));
        helper.drawShape(new Quad(x + 2, scrollBarY, scrollBarWidth - 2, 1).setAlpha(alpha));
        helper.drawShape(new Quad(x + 2, scrollBarY + scrollBarHeight - 1, scrollBarWidth - 2, 1).setAlpha(alpha).setColor(Color.LT_GREY));
        helper.drawShape(new Quad(x + width - 2, scrollBarY + scrollBarHeight - 2, 1, 1).setAlpha(alpha).setColor(Color.LT_GREY));
        helper.drawShape(new Quad(x + 1, scrollBarY + scrollBarHeight - 2, 1, 1).setAlpha(alpha).setColor(Color.LT_GREY));
    }

    public static void drawRainbowSquare(int progress, float x, float y, float width, float height)
    {
        int[] c1 = getRGB(progress, 0);
        int[] c2 = getRGB(progress, 255);
        int[] c3 = getRGB(progress, 255 * 2);
        int[] c4 = getRGB(progress, 255 * 3);

        LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(
                new Vector(x, y).setColor(new Color(c1[0], c1[1], c1[2])),
                new Vector(width, y).setColor(new Color(c2[0], c2[1], c2[2])),
                new Vector(width, height).setColor(new Color(c3[0], c3[1], c3[2])),
                new Vector(x, height).setColor(new Color(c4[0], c4[1], c4[2]))
        ).setAlpha(0.5F));
    }

    public static int increaseProgress(int currentProgress, int amount)
    {
        int newProgress = currentProgress + amount;
        if (newProgress > MAX_PROGRESS) newProgress -= MAX_PROGRESS;
        return newProgress;
    }

    private static int[] getRGB(int progress, int offset)
    {
        int wOffset = progress + offset;
        if (wOffset > MAX_PROGRESS) wOffset -= MAX_PROGRESS;
        int r = Math.min(255, Math.max(0, wOffset < 4 * 255 ? wOffset + 255 : wOffset - 255 - 255 - 255 - 255));
        r -= (Math.min(255, wOffset < 4 * 255 ? Math.max(0, wOffset - 255) : 0));
        int g = Math.min(255, Math.max(0, wOffset));
        g -= (Math.min(255, Math.max(0, wOffset - 255 - 255 - 255)));
        int b = Math.min(255, Math.max(0, wOffset - 255 - 255));
        b -= (Math.min(255, Math.max(0, wOffset - 255 - 255 - 255 - 255 - 255)));
        return new int[]{r, g, b};
    }

    public static boolean isMouseInBounds(int mouseX, int mouseY, int posX, int posY, int width, int height)
    {
        return isMouseInBounds(new Vector(mouseX, mouseY), new Quad(posX, posY, width, height));
    }

    public static boolean isMouseInBounds(int mouseX, int mouseY, Quad area)
    {
        return isMouseInBounds(new Vector(mouseX, mouseY), area);
    }

    public static boolean isMouseInBounds(Vector mouse, int posX, int posY, int width, int height)
    {
        return isMouseInBounds(mouse, new Quad(posX, posY, width, height));
    }

    public static boolean isMouseInBounds(Vector mouse, Quad area)
    {
        return mouse.getX() >= area.getX() && mouse.getY() >= area.getY() && mouse.getX() < area.getX() + area.getWidth() && mouse.getY() < area.getY() + area.getHeight();
    }
}
