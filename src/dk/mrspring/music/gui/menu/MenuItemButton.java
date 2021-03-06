package dk.mrspring.music.gui.menu;

import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiClickable;
import dk.mrspring.music.overlay.TextRender;
import net.minecraft.client.gui.FontRenderer;

import static dk.mrspring.llcore.DrawingHelper.HorizontalTextAlignment.TOP;
import static dk.mrspring.llcore.DrawingHelper.VerticalTextAlignment.LEFT;

/**
 * Created by Konrad on 09-06-2015.
 */
public class MenuItemButton extends IndexedMenuItem
{
    // TODO: Fix long names
    String drawing;
    TextRender render;
    GuiClickable clickListener;

    public MenuItemButton(String text, FontRenderer renderer, Object identifier)
    {
        super(identifier);
        this.drawing = text;
        this.render = new TextRender(drawing, renderer);
    }

    @Override
    public void initializeSize()
    {
        clickListener = new GuiClickable(0, 0, render.getLongestLine() + 6, getHeight());
    }

    @Override
    public void update()
    {

    }

    @Override
    public void draw(int mouseX, int mouseY, int width)
    {
        this.setWidth(width);
        render.render(LiteModMusicPlayer.core.getDrawingHelper(), 3, 3, 0xFFFFFF, true, LEFT, TOP);
    }

    public void setWidth(int newWidth)
    {
        this.clickListener.setWidth(Math.max(newWidth, getMinWidth()));
    }

    @Override
    public int getHeight()
    {
        return render.getTotalHeight() + 6;
    }

    @Override
    public IMenuItem[] getClickedItems(int mouseX, int mouseY, int width)
    {
        return new IMenuItem[]{this};
    }

    @Override
    public int getMinWidth()
    {
        return render.getLongestLine() + 6;
    }

    @Override
    public boolean mouseDown(int relMouseX, int relMouseY, int mouseButton, int width)
    {
        this.setWidth(width);
        return clickListener.mouseDown(relMouseX, relMouseY, mouseButton);
    }

    @Override
    public boolean isMouseHovering(int mouseX, int mouseY, int width)
    {
        this.setWidth(width);
        return clickListener.isHovering(mouseX, mouseY);
    }
}
