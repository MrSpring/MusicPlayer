package dk.mrspring.music.gui.menu;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.util.GuiUtils;
import dk.mrspring.music.util.Miscellaneous;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Konrad on 09-06-2015.
 */
public class MenuItemSubMenu extends MenuItemButton
{
    List<IMenuItem> items;
    boolean expanded = false;
    long hoverStart = 0;
    int width = 0;

    public MenuItemSubMenu(String text, FontRenderer renderer, Object id, IMenuItem... items)
    {
        super(text, renderer, id);

        this.items = Arrays.asList(items);
    }

    @Override
    public void initializeSize()
    {
        super.initializeSize();
        for (IMenuItem item : items)
        {
            item.initializeSize();
            int itemWidth = item.getMinWidth();
            this.width = Math.max(this.width, itemWidth);
        }
    }

    @Override
    public int getMinWidth()
    {
        return super.getMinWidth() + getHeight() + 2;
    }

    @Override
    public void draw(int mouseX, int mouseY, int buttonWidth)
    {
        expanded = mouseX < buttonWidth ?
                GuiUtils.isMouseInBounds(mouseX, mouseY, 0, 0, buttonWidth, getHeight()) :
                expanded;

        super.draw(mouseX, mouseY, buttonWidth);
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        helper.drawIcon(LiteModMusicPlayer.core.getIcon("right_arrow"), new Quad(buttonWidth - getHeight() + 3, 3, getHeight() - 6, getHeight() - 6));
        if (expanded)
        {
            int relMouseX = mouseX - buttonWidth, relMouseY = mouseY;
            GL11.glPushMatrix();
            GL11.glTranslatef(buttonWidth + 4, 0, 0);
            int yOffset = 0;
            for (int i = 0; i < items.size(); i++)
            {
                IMenuItem item = items.get(i);
                int itemHeight = item.getHeight();
                int localMouseX = relMouseX, localMouseY = relMouseY - yOffset;
                boolean hovering = item.isMouseHovering(localMouseX, localMouseY, width);//GuiUtils.isMouseInBounds(localMouseX, localMouseY, 0, 0, width, itemHeight);
                Color color = hovering ? Color.BLUE : Color.BLACK;
                float a = 0.75F;
                helper.drawShape(new Quad(-2, 0, width + 4, itemHeight).setColor(color).setAlpha(a));
                if (i == 0)
                    helper.drawShape(new Quad(-1, -1, width + 2, 1).setColor(color).setAlpha(a));
                else if (i == items.size() - 1)
                    helper
                            .drawShape(new Quad(-2, itemHeight, width + 4, 1).setColor(color).setAlpha(a))
                            .drawShape(new Quad(-1, itemHeight + 1, width + 2, 1).setColor(color).setAlpha(a));
                drawOutline(helper, -1, 0, width + 2, itemHeight);
                GL11.glPushMatrix();
                item.draw(localMouseX, localMouseY, width);
                GL11.glPopMatrix();
                GL11.glTranslatef(0, itemHeight, 0);
                yOffset += itemHeight;
            }
            GL11.glPopMatrix();
        }
    }

    private void drawOutline(DrawingHelper helper, int x, int y, int w, int h)
    {
        helper.drawShape(new Quad(x, y, w, 1));
        helper.drawShape(new Quad(x, y + h, w, 1));

        helper.drawShape(new Quad(x, y, 1, h));
        helper.drawShape(new Quad(x + w - 1, y, 1, h));
    }

    @Override
    public IMenuItem[] getClickedItems(int mouseX, int mouseY, int width)
    {
        IMenuItem[] one = new IMenuItem[]{this};
        IMenuItem[] two = null;
        int yOffset = 0;
        for (IMenuItem item : items)
            if (item.isMouseHovering(mouseX - width, mouseY - yOffset, width))
            {
                two = item.getClickedItems(mouseX - width, mouseY - yOffset, width);
                break;
            } else yOffset += item.getHeight();
        if (two != null && two.length > 0) one = Miscellaneous.merge(one, two);
        return one.length > 1 ? one : null;
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton, int buttonWidth)
    {
//        if (expanded)
//        {
//        }
        if (!expanded || mouseX < buttonWidth) return super.mouseDown(mouseX, mouseY, mouseButton, buttonWidth);
        int yOffset = 0;
        for (IMenuItem item : items)
        {
            if (item.mouseDown(mouseX - buttonWidth, mouseY - yOffset, mouseButton, width)) return true;
            else yOffset += item.getHeight();
        }
        return false;
        /*if (super.mouseDown(relMouseX, relMouseY, mouseButton, width))
        {
            return true;
        } else
        {

        }*/
    }
}
