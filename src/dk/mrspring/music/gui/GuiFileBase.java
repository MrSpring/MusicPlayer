package dk.mrspring.music.gui;

import dk.mrspring.music.gui.interfaces.IGui;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 08-12-2014 for In-Game File Explorer.
 */
public class GuiFileBase implements IGui
{
    int x, y, w, h;
    String filePath;
    RenderType renderType;

    public GuiFileBase(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {

    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        return false;
    }

    @Override
    public void mouseUp(int mouseX, int mouseY, int mouseButton)
    {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {

    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {

    }

    public RenderType getRenderType()
    {
        return renderType;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setWidth(int w)
    {
        this.w = w;
    }

    public void setHeight(int h)
    {
        this.h = h;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getHeight()
    {
        return h;
    }

    public int getWidth()
    {
        return w;
    }

    public enum RenderType
    {
        LIST,
        SQUARE_GRID,
        LONG_GRID
    }
}
