package dk.mrspring.music.gui.screen;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.Config;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.*;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.util.GuiHelper;
import dk.mrspring.music.util.Miscellaneous;
import dk.mrspring.music.util.TranslateHelper;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/**
 * Created by Konrad on 26-05-2015.
 */
public class GuiScreenAllMusic extends GuiScreen
{
    private int maxCoverSize = 150;
    private int minCoverSize = 40;

    public GuiScreenAllMusic(net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super("All Music", previousScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        Config config = LiteModMusicPlayer.config;

        this.enableRepeats();

        int sidePanelSize = config.gui_mm_side_panel_size;

        this.addGuiElement("search_bar", new GuiCustomTextField((width / 3) * 2, -getTopBarHeight() + 3, width / 3, getTopBarHeight() - 6, ""));
        this.addGuiElement("size_slider", new GuiSlider(3, -getTopBarHeight() + 3, width / 3, getTopBarHeight() - 6, config.gui_mm_list_entry_size).setShowHover(false));
        this.addGuiElement("back", new GuiSimpleButton(3, height - getBottomBarHeight() - getTopBarHeight() + 3, 60, getTopBarHeight() - 6, "Back"));
        this.addGuiElement("list", new GuiAllMusicList(sidePanelSize, 0, width - sidePanelSize, height - getTopBarHeight() - getBottomBarHeight(), LiteModMusicPlayer.musicHandler.getAllMusic()));
        this.addGuiElement("side_panel", new SidePanel(0, 0, sidePanelSize, height - getTopBarHeight() - getBottomBarHeight()));
    }

    private void updatePanelWidth(int newWidth)
    {
        LiteModMusicPlayer.config.gui_mm_side_panel_size = newWidth;
        GuiAllMusicList musicList = (GuiAllMusicList) getGui("list");
        musicList.setWidth(width - newWidth);
        musicList.setX(newWidth);
    }

    @Override
    public boolean updateElement(String identifier, IGui gui)
    {
        if (identifier.equals("list"))
        {
            GuiAllMusicList list = (GuiAllMusicList) gui;
            String newFilter = ((GuiCustomTextField) this.getGui("search_bar")).getText();
            list.getFilter().setFilter(newFilter);
            int value = ((GuiSlider) this.getGui("size_slider")).getValue();
            LiteModMusicPlayer.config.gui_mm_list_entry_size = value;
            int size = (int) (minCoverSize + ((maxCoverSize - minCoverSize) * ((float) value / 100F)));
            list.setEntryWidthTarget(size);
        }
        return super.updateElement(identifier, gui);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        float iconSize = getTopBarHeight() - 18;
        LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(0, 0, width, height).setColor(Color.BLACK).setAlpha(0.5F));
        super.drawScreen(mouseX, mouseY, partialTicks);
        LiteModMusicPlayer.core.getDrawingHelper().drawIcon(LiteModMusicPlayer.core.getIcon("search"), new Quad(width - iconSize - 5 - (width / 3), -getTopBarHeight() + 8, iconSize, iconSize));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        int clickZone = getTopBarHeight() - 12;
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, width - clickZone - 2 - (width / 3), 5, clickZone, clickZone))
            ((GuiCustomTextField) this.getGui("search_bar")).setFocus(true);
        else super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {
        super.guiClicked(identifier, gui, mouseX, mouseY, mouseButton);
        if (identifier.equals("back"))
            mc.displayGuiScreen(previousScreen);
    }

    public class SidePanel implements IGui
    {
        int x, y, w, h;
        int dragXOffset = -1;
        boolean resizing = false;
        GuiSimpleButton[] buttons;

        public SidePanel(int x, int y, int w, int h)
        {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;

            buttons = new GuiSimpleButton[]{
                    new GuiSimpleButton(-2, 0, w + 4, 15, "All Music"),
                    new GuiSimpleButton(-2, 0, w + 4, 15, "Albums"),
                    new GuiSimpleButton(-2, 0, w + 4, 15, "Artists")
            };
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY)
        {
            if (resizing)
            {
                this.w = (mouseX - x) - dragXOffset;
                this.w = Math.min(width - maxCoverSize - 10, w);
                this.w = Math.max(20, w);
                updatePanelWidth(w);
            }

            GL11.glPushMatrix();
            GLClippingPlanes.glEnableVerticalClipping(0, h);
            GLClippingPlanes.glEnableHorizontalClipping(x, x + w);

            DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();

            int yOffset = 4;
            for (GuiSimpleButton button : this.buttons)
            {
                if (button != null)
                {
                    int height = 25;
                    button.setWidth(w + 4);
                    button.setHeight(height);
                    button.setY(y + yOffset);
                    button.draw(minecraft, mouseX, mouseY);
                    yOffset += height + 3;
//                    GL11.glTranslatef(0, 18F, 0);
                }
            }

            helper.drawShape(new Quad(x + w - 1, y, 1, h));

            GLClippingPlanes.glDisableClipping();
            if (GuiHelper.isMouseInBounds(mouseX, mouseY, x + w - 3, y, 5, h) || resizing)
            {
                int iconWidth = 10;

                helper.setZIndex(10D);
                helper.drawShape(new Quad(mouseX - (iconWidth / 2) + 1, mouseY - 10 + 1, iconWidth, 1).setColor(Color.DK_GREY));
                helper.drawShape(new Quad(mouseX - (iconWidth / 2) - 3 + 1, mouseY - 10 + 1, mouseX - (iconWidth / 2) + 1, mouseY - 12 + 1, mouseX - (iconWidth / 2) + 1, mouseY - 7 + 1, mouseX - (iconWidth / 2) - 3 + 1, mouseY - 9 + 1).setColor(Color.DK_GREY));
                helper.drawShape(new Quad(mouseX + (iconWidth / 2) + 1, mouseY - 12 + 1, mouseX + (iconWidth / 2) + 3 + 1, mouseY - 10 + 1, mouseX + (iconWidth / 2) + 3 + 1, mouseY - 9 + 1, mouseX + (iconWidth / 2) + 1, mouseY - 7 + 1).setColor(Color.DK_GREY));

                helper.drawShape(new Quad(mouseX - (iconWidth / 2), mouseY - 10, iconWidth, 1));
                helper.drawShape(new Quad(mouseX - (iconWidth / 2) - 3, mouseY - 10, mouseX - (iconWidth / 2), mouseY - 12, mouseX - (iconWidth / 2), mouseY - 7, mouseX - (iconWidth / 2) - 3, mouseY - 9));
                helper.drawShape(new Quad(mouseX + (iconWidth / 2), mouseY - 12, mouseX + (iconWidth / 2) + 3, mouseY - 10, mouseX + (iconWidth / 2) + 3, mouseY - 9, mouseX + (iconWidth / 2), mouseY - 7));
//                helper.drawShape(new Quad(mouseX + 1, mouseY + 1, 3, 3).setColor(Color.DK_GREY));
//                helper.drawShape(new Quad(mouseX, mouseY, 3, 3));
                helper.setZIndex(0);
            }
            GL11.glPopMatrix();
        }

        @Override
        public void update()
        {
            for (GuiSimpleButton button : this.buttons)
                if (button != null)
                    button.update();
        }

        @Override
        public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
        {
            if (GuiHelper.isMouseInBounds(mouseX, mouseY, x + w - 3, y, 5, h))
            {
                this.dragXOffset = mouseX - x - w;
                return resizing = true;
            } else return false;
        }

        @Override
        public void mouseUp(int mouseX, int mouseY, int mouseButton)
        {
            resizing = false;
        }

        @Override
        public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
        {

        }

        @Override
        public void handleKeyTyped(int keyCode, char character)
        {

        }
    }
}
