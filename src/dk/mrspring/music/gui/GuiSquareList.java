package dk.mrspring.music.gui;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.gui.interfaces.IMouseListener;
import dk.mrspring.music.gui.interfaces.IResizable;
import dk.mrspring.music.util.GuiUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Konrad on 26-05-2015.
 */
public abstract class GuiSquareList<T> implements IGui, IMouseListener, IResizable
{
    int x, y, width, height;
    List<T> showing;
    int _coverSize = 70;
    int _entryWidth = _coverSize;
    int _entrySpacing = 3;
    int _entryWidthTarget = 100;
    int _scrollMaxOffset = 20;
    int _scrollBarWidth = 7;
    int scroll = 0;
    int listHeight = 0;
    //    MusicFilter filter = new AnyFilter("");
    String currentFilter = "";

    public GuiSquareList(int x, int y, int w, int h, List<T> allMusic)
    {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.showing = allMusic;
    }

    public abstract boolean filter(T object, String filter);

    public void setEntryWidthTarget(int entryWidthTarget)
    {
        if (this._entryWidthTarget != entryWidthTarget)
        {
            this._entryWidthTarget = entryWidthTarget;
            this.clampScroll();
        } else this._entryWidthTarget = entryWidthTarget;
    }

    public int getXListOffset()
    {
        return 0;
    }

    public int getYListOffset()
    {
        return 0;
    }

    public int getListWidth()
    {
        return width;
    }

    public int getListHeight()
    {
        return height;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        int width = getListWidth(), height = getListHeight();

        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        GL11.glPushMatrix();
        GL11.glTranslatef(x + getXListOffset(), y + getYListOffset(), 0);
        GLClippingPlanes.glEnableVerticalClipping(0, height);
        int listWidth = width - (this.hasScroll() ? _scrollBarWidth : 0);
        int columns = listWidth / _entryWidthTarget;
        _entryWidth = listWidth / columns;
        if (hasScroll())
            GuiUtils.drawScrollbar(width - _scrollBarWidth, y, _scrollBarWidth, height, scroll, getMaxScroll(), getRenderedListHeight() + _scrollMaxOffset);
        float remaining = listWidth % columns;
        GL11.glTranslatef(remaining / 2F, -scroll, 0);
        int currentColumn = 0;
        int rowHeight = 0;
        int totalHeight = 0;
        for (T music : this.showing)
        {
//            if (filter != null)
//                if (!filter.filter(music))
//                    continue;
            if (!filter(music, currentFilter))
                continue;
            int entryHeight = drawEntry(currentColumn, minecraft, helper, music);
            if (entryHeight > rowHeight)
                rowHeight = entryHeight;
            currentColumn++;
            if (currentColumn >= columns)
            {
                currentColumn = 0;
                GL11.glTranslatef(0, rowHeight, 0);
                totalHeight += rowHeight;
                rowHeight = 0;
            }
        }
        GLClippingPlanes.glDisableClipping();
        GL11.glPopMatrix();
        this.listHeight = totalHeight + rowHeight;
    }

    public void setFilter(String newFilter)
    {
        this.currentFilter = newFilter;
    }

    public abstract int getEntryHeight(int currentColumn, T thing);

    /**
     * Draws the entry and returns the same as #getEntryHeight
     */
    public abstract int drawEntry(int currentColumn, Minecraft minecraft, DrawingHelper helper, T drawing);
    /*{
        String key = filter != null ? filter.getFilter().trim() : "";
        String text = TranslateHelper.translateFormat("gui.music_list.entry_text", music.getName(), music.getAlbum(), music.getArtist());
        text = text.replaceAll("(?i)(" + key + ")", "\u00a7e$1\u00a7r");
        MultilineTextRender render = new MultilineTextRender(text, minecraft.fontRendererObj, _entryWidth - 6 - (2 * _entrySpacing), true, 5);
        int entryHeight = _entryWidth + render.getTotalHeight() + 3;
        int entryX = currentColumn * _entryWidth;
        helper.drawButtonThingy(new Quad(entryX + _entrySpacing, _entrySpacing, _entryWidth - (2 * _entrySpacing), entryHeight - (2 * _entrySpacing)), 0, true);
        render.render(helper, entryX + 3 + _entrySpacing, _entryWidth - _entrySpacing, 0xFFFFFF, true, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        music.bindCover();
        helper.drawTexturedShape(new Quad(entryX + 3 + _entrySpacing, 3 + _entrySpacing, _entryWidth - 6 - (2 * _entrySpacing), _entryWidth - 6 - (2 * _entrySpacing)));
    }*/

    private boolean hasScroll()
    {
        return getRenderedListHeight() > this.getListHeight();
    }

    private int getRenderedListHeight()
    {
        return listHeight;
    }

    private int getMaxScroll()
    {
        return getRenderedListHeight() - this.getListHeight() + _scrollMaxOffset;
    }

    private void clampScroll()
    {
        scroll = Math.min(scroll, this.hasScroll() ? getMaxScroll() : 0);
        scroll = Math.max(scroll, 0);
    }

    protected abstract boolean onElementClicked(int relMouseX, int relMouseY, int globalMouseX, int globalMouseY, int mouseButton, T clicked);

    protected boolean isOtherElementsClicked(int mouseX, int mouseY, int mouseButton)
    {
        return false;
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        int listWidth = width - (this.hasScroll() ? _scrollBarWidth : 0);
        int columns = listWidth / _entryWidthTarget;
        int currentColumn = 0;
        int rowHeight = 0;
        int currentYOffset = 0;
        int relMouseX = mouseX - x, relMouseY = mouseY - y;
        for (T music : this.showing)
        {
            if (!filter(music, currentFilter))
                continue;
            int entryHeight = getEntryHeight(currentColumn, music);
            // Mouse check
            int entryX = currentColumn * _entryWidth, entryY = currentYOffset;
            if (GuiUtils.isMouseInBounds(relMouseX, relMouseY, entryX, entryY, _entryWidth, entryHeight))
                if (this.onElementClicked(relMouseX - entryX, relMouseY - entryY, mouseX, mouseY, mouseButton, music))
                    return true;
            if (entryHeight > rowHeight)
                rowHeight = entryHeight;
            currentColumn++;
            if (currentColumn >= columns)
            {
                currentColumn = 0;
                currentYOffset += rowHeight;
                rowHeight = 0;
            }
        }
        return isOtherElementsClicked(mouseX, mouseY, mouseButton);
//        return onElementClicked(mouseX - x, mouseY - y, mouseButton, showing.get(0));
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
    public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
    {
        if (GuiUtils.isMouseInBounds(mouseX, mouseY, x, y, width, height))
        {
            int mouseWheel = dWheelRaw;
            mouseWheel /= 4;
            if (mouseWheel != 0)
                this.addScroll(-mouseWheel);
        }
    }

    private void addScroll(int scroll)
    {
        this.scroll += scroll;
        this.clampScroll();
    }

    @Override
    public void setHeight(int height)
    {
        this.height = height;
    }

    @Override
    public void setWidth(int width)
    {
        if (width != this.width)
        {
            this.width = width;
            this.clampScroll();
        } else this.width = width;
    }

    @Override
    public void setX(int x)
    {
        this.x = x;
    }

    @Override
    public void setY(int y)
    {
        this.y = y;
    }

    @Override
    public int y()
    {
        return y;
    }

    @Override
    public int x()
    {
        return x;
    }

    @Override
    public int width()
    {
        return width;
    }

    @Override
    public int height()
    {
        return height;
    }

    public abstract class Filter<O>
    {
        String filter;

        public Filter(String filter)
        {
            this.filter = filter;
        }

        public String getFilter()
        {
            return filter;
        }

        public void setFilter(String filter)
        {
            this.filter = filter;
        }

        abstract boolean filter(O object);
    }
}
