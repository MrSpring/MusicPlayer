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
            GL11.glPushMatrix();
            GL11.glTranslatef(buttonWidth + 3, 0, 0);
            int yOffset = 0;
            for (int i = 0; i < items.size(); i++)
            {
                IMenuItem item = items.get(i);
                int height = item.getHeight() + 3;
                int relMouseX = mouseX - width;
                boolean hovering = GuiHelper.isMouseInBounds(relMouseX, mouseY, 0, yOffset, width, height);
                Color color = hovering ? Color.BLUE : Color.BLACK;
                if (i == 0)
                {
                    helper.drawShape(new Quad(1, 0, width - 2, 1).setColor(color).setAlpha(0.5F));
                    helper.drawShape(new Quad(0, 1, width, height - 1).setColor(color).setAlpha(0.5F));

                    helper.drawShape(new Quad(1, 1, width - 2, 1));
                    helper.drawShape(new Quad(1, 2, 1, height - 2));
                    helper.drawShape(new Quad(width - 2, 2, 1, height - 2));
                } else if (i == items.size() - 1)
                {
                    helper.drawShape(new Quad(1, height - 1, width - 2, 1).setColor(color).setAlpha(0.5F));
                    helper.drawShape(new Quad(0, 0, width, height - 1).setColor(color).setAlpha(0.5F));

                    helper.drawShape(new Quad(1, 0, width - 2, 1));          // Outline
                    helper.drawShape(new Quad(1, 0, 1, height - 1));         // Outline
                    helper.drawShape(new Quad(width - 2, 0, 1, height - 1)); // Outline
                    helper.drawShape(new Quad(1, height - 2, width - 2, 1)); // Outline
                } else
                {
                    helper.drawShape(new Quad(0, 0, width, height).setColor(color).setAlpha(0.5F)); // Background

                    helper.drawShape(new Quad(1, 0, width - 2, 1));      // Outline
                    helper.drawShape(new Quad(1, 0, 1, height));         // Outline
                    helper.drawShape(new Quad(width - 2, 0, 1, height)); // Outline
                }
                GL11.glPushMatrix();
                GL11.glTranslatef(3, 3, 0);
                item.draw(relMouseX, mouseY - yOffset, width - 6);
                GL11.glPopMatrix();
                GL11.glTranslatef(0, height, 0);
                yOffset += height;
            }
            GL11.glPopMatrix();
        }
    }

    @Override
    public boolean mouseDown(int relMouseX, int relMouseY, int mouseButton)
    {
        return expanded = super.mouseDown(relMouseX, relMouseY, mouseButton);
    }
}
