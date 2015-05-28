package dk.mrspring.music.gui;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.gui.interfaces.IMouseListener;
import dk.mrspring.music.overlay.TextRender;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.util.GuiHelper;
import dk.mrspring.music.util.filter.AnyFilter;
import dk.mrspring.music.util.filter.MusicFilter;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Konrad on 26-05-2015.
 */
public class GuiAllMusicList implements IGui, IMouseListener
{
    int x, y, width, height;
    List<Music> showing;
    int _coverSize = 70;
    int _entryWidth = _coverSize;
    int _entrySpacing = 3;
    int _entryWidthTarget = 100;
    int _scrollMaxOffset = 20;
    int _scrollBarWidth = 10;
    int scroll = 0;
    int listHeight = 0;
    MusicFilter filter = new AnyFilter("");

    public GuiAllMusicList(int x, int y, int w, int h, List<Music> allMusic)
    {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.showing = allMusic;
    }

    public void setEntryWidthTarget(int entryWidthTarget)
    {
        if (this._entryWidthTarget != entryWidthTarget)
        {
            this._entryWidthTarget = entryWidthTarget;
            this.clampScroll();
        } else this._entryWidthTarget = entryWidthTarget;
    }

    public MusicFilter getFilter()
    {
        return filter;
    }

    public void setFilter(MusicFilter filter)
    {
        this.filter = filter;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        GL11.glPushMatrix();
        GLClippingPlanes.glEnableVerticalClipping(0, height);
        GL11.glTranslatef(x, y, 0);
        int listWidth = width - (this.hasScroll() ? _scrollBarWidth : 0);
        int columns = listWidth / _entryWidthTarget;
        _entryWidth = listWidth / columns;
        if (hasScroll())
        {
            double progress = ((double) height) / (double) (getListHeight() + _scrollMaxOffset);
            int scrollBarHeight = (int) (progress * (height - 6));
            progress = ((double) scroll) / ((double) getMaxScroll());
            int scrollBarY = y + 3 + (int) (((height - 6) - scrollBarHeight) * progress);
            helper.drawShape(new Quad(width - _scrollBarWidth, scrollBarY, _scrollBarWidth - 3, scrollBarHeight));
        }
        float remaining = listWidth % columns;
        GL11.glTranslatef(remaining / 2F, -scroll, 0);
        int currentColumn = 0;
        int rowHeight = 0;
        int totalHeight = 0;
        for (Music music : this.showing)
        {
            if (filter != null)
                if (!filter.filter(music))
                    continue;
            String key = filter != null ? filter.getFilter() : "";
//            String name = StringUtils.replace(music.getName(), key, "\u00a7e" + key + "\u00a7r");
            String name = music.getName();
            name = name.replaceAll("(?i)(" + key + ")", "\u00a7e$1\u00a7r");
            TextRender render = new TextRender(name, minecraft.fontRendererObj, _entryWidth - 6 - (2 * _entrySpacing));
            int entryHeight = _entryWidth + render.getTotalHeight() + 3;
            int entryX = currentColumn * _entryWidth;
            helper.drawButtonThingy(new Quad(entryX + _entrySpacing, _entrySpacing, _entryWidth - (2 * _entrySpacing), entryHeight - (2 * _entrySpacing)), 0, true);
            render.render(helper, entryX + 3 + _entrySpacing, _entryWidth - _entrySpacing, 0xFFFFFF, true, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
            music.bindCover();
            helper.drawTexturedShape(new Quad(entryX + 3 + _entrySpacing, 3 + _entrySpacing, _entryWidth - 6 - (2 * _entrySpacing), _entryWidth - 6 - (2 * _entrySpacing)));
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

    private boolean hasScroll()
    {
        return getListHeight() > this.height;
    }

    private int getListHeight()
    {
        return listHeight;
    }

    private int getMaxScroll()
    {
        return getListHeight() - this.height + _scrollMaxOffset;
    }

    private void clampScroll()
    {
        scroll = Math.min(scroll, this.hasScroll() ? getMaxScroll() : 0);
        scroll = Math.max(scroll, 0);
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

    @Override
    public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, width, height))
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

    public void setWidth(int width)
    {
        if (width != this.width)
        {
            this.width = width;
            this.clampScroll();
        } else this.width = width;
    }

    public void setX(int x)
    {
        this.x = x;
    }
}
