package dk.mrspring.music.util;

import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiHelper
{
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
