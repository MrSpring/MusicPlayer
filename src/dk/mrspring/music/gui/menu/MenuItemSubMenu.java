package dk.mrspring.music.gui.menu;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.util.GuiHelper;
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
    int width = 0;

    public MenuItemSubMenu(String text, FontRenderer renderer, IMenuItem... items)
    {
        super(text, renderer, null);

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
        return super.getMinWidth() + getHeight()+2;
    }

    @Override
    public void draw(int mouseX, int mouseY, int buttonWidth)
    {
        expanded = mouseX < buttonWidth ?
                GuiHelper.isMouseInBounds(mouseX, mouseY, 0, 0, buttonWidth, getHeight()) :
                expanded;

        super.draw(mouseX, mouseY, buttonWidth);
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        helper.drawIcon(LiteModMusicPlayer.core.getIcon("right_arrow"), new Quad(buttonWidth - getHeight() + 3, 3, getHeight() - 6, getHeight() - 6));
        if (expanded)
        {
            int relMouseX = mouseX-buttonWidth,relMouseY=mouseY;
            GL11.glPushMatrix();
            GL11.glTranslatef(buttonWidth + 4, 0, 0);
            int yOffset = 0;
            for (int i = 0; i < items.size(); i++)
            {
                IMenuItem item = items.get(i);
                int itemHeight = item.getHeight();
                int localMouseX = relMouseX, localMouseY = relMouseY - yOffset;
                boolean hovering = GuiHelper.isMouseInBounds(localMouseX, localMouseY, 0, 0, width, itemHeight);
                Color color = hovering?Color.BLUE:Color.BLACK;
                helper.drawShape(new Quad(-2, 0, width + 4, itemHeight).setColor(color).setAlpha(0.5F));
//            if (item instanceof MenuItemSubMenu)
//                helper.drawShape(new Quad(0, 0, width, itemHeight).setColor(Color.BLACK).setAlpha(0.5F));
                if (i == 0)
                    helper.drawShape(new Quad(-1, -1, width + 2, 1).setColor(color).setAlpha(0.5F));
                else if (i == items.size() - 1)
                    helper
                            .drawShape(new Quad(-2, itemHeight, width + 4, 1).setColor(color).setAlpha(0.5F))
                            .drawShape(new Quad(-1, itemHeight + 1, width + 2, 1).setColor(color).setAlpha(0.5F));
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
    public boolean mouseDown(int relMouseX, int relMouseY, int mouseButton)
    {
        return expanded = super.mouseDown(relMouseX, relMouseY, mouseButton);
    }
}
