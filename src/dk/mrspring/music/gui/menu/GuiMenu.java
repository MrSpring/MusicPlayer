package dk.mrspring.music.gui.menu;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.util.GuiHelper;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Konrad on 07-06-2015.
 */
public class GuiMenu implements IGui
{
    Size limitations;
    Size size;
    List<IMenuItem> items;
    MenuAction onClick;

    public GuiMenu(int mouseX, int mouseY, int screenWidth, int screenHeight, IMenuItem... items)
    {
        this.items = Arrays.asList(items);
        this.alignWithMouse(mouseX, mouseY, screenWidth, screenHeight);
    }

    public void alignWithMouse(int mouseX, int mouseY, int screenWidth, int screenHeight)
    {
        this.alignWithMouse(mouseX, mouseY, 0, 0, screenWidth, screenHeight);
    }

    public void alignWithMouse(int mouseX, int mouseY, int screenX, int screenY, int screenWidth, int screenHeight)
    {
        if (limitations == null)
            limitations = new Size(screenX, screenY, screenWidth, screenHeight);

        if (size == null)
        {
            size = new Size(mouseX, mouseY);
            calculateSize();
        }
    }

    public void calculateSize()
    {
        for (IMenuItem item : items)
        {
            item.initializeSize();
            int itemHeight = item.getHeight(), itemWidth = item.getMinWidth();
            size.h += itemHeight;
            size.w = Math.max(size.w, itemWidth);
        }
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        GL11.glPushMatrix();
        GL11.glTranslatef(size.x + 2, size.y + 2, 0);
        int yOffset = 0;
        int relMouseX = mouseX - size.x - 2, relMouseY = mouseY - size.y - 2;
        for (int i = 0; i < items.size(); i++)
        {
            IMenuItem item = items.get(i);
            int itemHeight = item.getHeight();
            int localMouseX = relMouseX, localMouseY = relMouseY - yOffset;
            boolean hovering = GuiHelper.isMouseInBounds(localMouseX, localMouseY, 0, 0, size.w, itemHeight);
            Color color = hovering?Color.BLUE:Color.BLACK;
            helper.drawShape(new Quad(-2, 0, size.w + 4, itemHeight).setColor(color).setAlpha(0.5F));
//            if (item instanceof MenuItemSubMenu)
//                helper.drawShape(new Quad(0, 0, size.w, itemHeight).setColor(Color.BLACK).setAlpha(0.5F));
            if (i == 0)
                helper.drawShape(new Quad(-1, -1, size.w + 2, 1).setColor(color).setAlpha(0.5F));
            else if (i == items.size() - 1)
                helper
                        .drawShape(new Quad(-2, itemHeight, size.w + 4, 1).setColor(color).setAlpha(0.5F))
                        .drawShape(new Quad(-1, itemHeight + 1, size.w + 2, 1).setColor(color).setAlpha(0.5F));
            drawOutline(helper, -1, 0, size.w + 2, itemHeight);
            GL11.glPushMatrix();
            item.draw(localMouseX, localMouseY, size.w);
            GL11.glPopMatrix();
            GL11.glTranslatef(0, itemHeight, 0);
            yOffset += itemHeight;
        }
        GL11.glPopMatrix();
    }

    private void drawOutline(DrawingHelper helper, int x, int y, int w, int h)
    {
        helper.drawShape(new Quad(x, y, w, 1));
        helper.drawShape(new Quad(x, y + h, w, 1));

        helper.drawShape(new Quad(x, y, 1, h));
        helper.drawShape(new Quad(x + w - 1, y, 1, h));
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (onClick == null)
            return GuiHelper.isMouseInBounds(mouseX, mouseY, size.asQuad());
        else
        {
            for (IMenuItem item : items)
            {
                int relMouseY = mouseY - size.y, relMouseX = mouseX - size.x;
                if (item.mouseDown(relMouseX, relMouseY, mouseButton))
                {
                    onClick.onAction(item.getClickedItems(relMouseX, relMouseY, mouseButton));
                    return true;
                }
            }
            return false;
        }
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

    public interface MenuAction
    {
        /**
         * Gets called when something is pressed in the menu.
         *
         * @param pressedItems The item that was pressed, and the sub-menus it takes to find it.
         */
        void onAction(IMenuItem... pressedItems);
    }

    public class Size
    {
        public int x, y, w, h;

        public Size()
        {
            this(0, 0, 0, 0);
        }

        public Size(int x, int y)
        {
            this(x, y, 0, 0);
        }

        public Size(int x, int y, int w, int h)
        {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public Quad asQuad()
        {
            return new Quad(x, y, w, h);
        }
    }
}
