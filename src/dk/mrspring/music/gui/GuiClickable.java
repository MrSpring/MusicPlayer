package dk.mrspring.music.gui;

import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.gui.interfaces.IResizable;
import dk.mrspring.music.util.GuiUtils;
import net.minecraft.client.Minecraft;

/**
 * Created by Konrad on 09-06-2015.
 */
public class GuiClickable implements IGui, IResizable
{
    int x, y, w, h;
    int button = -1;

    public GuiClickable(int x, int y, int w, int h)
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
        return (button == -1 || mouseButton == button) && isHovering(mouseX, mouseY);
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

    @Override
    public void setX(int newX)
    {
        this.x = newX;
    }

    @Override
    public void setY(int newY)
    {
        this.y = newY;
    }

    @Override
    public void setWidth(int newWidth)
    {
        this.w = newWidth;
    }

    @Override
    public void setHeight(int newHeight)
    {
        this.h = newHeight;
    }

    @Override
    public int x()
    {
        return x;
    }

    @Override
    public int y()
    {
        return y;
    }

    @Override
    public int height()
    {
        return h;
    }

    @Override
    public int width()
    {
        return w;
    }

    public boolean isHovering(int mouseX, int mouseY)
    {
        return GuiUtils.isMouseInBounds(mouseX, mouseY, x, y, w, h);
    }
}
