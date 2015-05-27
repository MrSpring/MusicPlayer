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
    int _entryWidthTarget = 70;
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
        int[] columnHeights = new int[columns];

        for (Music music : this.showing)
        {
            if (filter != null)
                if (!filter.filter(music))
                    continue;
            int currentColumn = 0;
            for (int i = 0; i < columns; i++)
                if (columnHeights[i] < columnHeights[currentColumn])
                    currentColumn = i;
            TextRender render = new TextRender(music.getName(), minecraft.fontRendererObj, _entryWidth - 6 - (2 * _entrySpacing));
            int entryHeight = _entryWidth + render.getTotalHeight() + 3;
            int entryX = currentColumn * _entryWidth, entryY = columnHeights[currentColumn];
            helper.drawButtonThingy(new Quad(entryX + _entrySpacing, entryY + _entrySpacing, _entryWidth - (2 * _entrySpacing), entryHeight - (2 * _entrySpacing)), 0, true);
            render.render(helper, entryX + 3 + _entrySpacing, entryY + _entryWidth - _entrySpacing, 0xFFFFFF, true, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
            music.bindCover();
            helper.drawTexturedShape(new Quad(entryX + 3 + _entrySpacing, entryY + 3 + _entrySpacing, _entryWidth - 6 - (2 * _entrySpacing), _entryWidth - 6 - (2 * _entrySpacing)));
            columnHeights[currentColumn] += entryHeight;
        }
        GLClippingPlanes.glDisableClipping();
        GL11.glPopMatrix();
        this.listHeight = columnHeights[0];
        for (int i = 1; i < columnHeights.length; i++)
            if (columnHeights[i] > listHeight)
                listHeight = columnHeights[i];
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
}
