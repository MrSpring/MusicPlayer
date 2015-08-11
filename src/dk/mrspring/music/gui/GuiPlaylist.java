package dk.mrspring.music.gui;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.gui.interfaces.IMouseListener;
import dk.mrspring.music.gui.interfaces.IResizable;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.player.Playlist;
import dk.mrspring.music.util.GuiUtils;
import dk.mrspring.music.util.Miscellaneous;
import dk.mrspring.music.util.TranslateHelper;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Konrad on 25-05-2015.
 */
public class GuiPlaylist implements IGui, IMouseListener, IResizable
{
    Playlist musicList;
    int x, y, width, height;
    int scroll;
    int moving = -1;
    int moveXStart = -1, moveYStart = -1;
    boolean isReadyToDelete = false;
    private int _entryWidth = 70;
    private int _entryHeight = 35;
    private int _scrollBarWidth = 7;
    private int _scrollMaxOffset = 20;

    public GuiPlaylist(int x, int y, int w, int h, Playlist music)
    {
        this.musicList = music;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public static void drawMusic(int xBase, int y, int w, int height, Music music, int padding)
    {
        int wPadding = padding * 2;
        int coverSize = height - wPadding - 4;
        int width = w - coverSize, x = xBase + coverSize + 3;
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        helper.drawButtonThingy(new Quad(xBase + padding, y + padding, w - wPadding, height - wPadding), 0F, true);
        helper.drawText(getRenderString(music), new Vector(x + padding + 3, y + (height / 2)), 0xFFFFFF, true, -1,
                DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.CENTER);
        int dragW = 10;
        helper.drawShape(new Quad(x + width - 15 - dragW, y + (height / 2) - 1 - 4, dragW, 2));
        helper.drawShape(new Quad(x + width - 15 - dragW, y + (height / 2) - 1, dragW, 2));
        helper.drawShape(new Quad(x + width - 15 - dragW, y + (height / 2) + 3, dragW, 2));
        music.bindCover();
        helper.drawTexturedShape(new Quad(xBase + padding + 2, y + padding + 2, coverSize, coverSize));
    }

    public static String getRenderString(Music music)
    {
        return TranslateHelper.translateFormat("gui.playlist_editor.list_entry",
                music.getName(), music.getArtist());
    }

    public Playlist getPlaylist()
    {
        return this.musicList;
    }

    @Override
    public void setWidth(int width)
    {
        this.width = width;
    }

    @Override
    public void setHeight(int height)
    {
        this.height = height;
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
    public int width()
    {
        return width;
    }

    @Override
    public int height()
    {
        return height;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();

//        helper.drawShape(new Quad(mouseX - 5, mouseY - y + scroll, 10, _entryHeight));

        GL11.glPushMatrix();

        GL11.glTranslatef(x, y, 0);

        GL11.glPushMatrix();

        if (musicList.size() == 0)
        {
            helper.drawText("Empty...\nExplore your library, and find some music to add!", new Vector(width / 2, height / 2), 0xFFFFFF, true, width, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.CENTER);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            return;
        }

        GLClippingPlanes.glEnableVerticalClipping(0, height);

        int relMouseY = mouseY + scroll;

        if (hasScroll())
        {
            _entryWidth = this.width - _scrollBarWidth;
            GuiUtils.drawScrollbar(width - _scrollBarWidth, y, _scrollBarWidth, height, scroll, getMaxScroll(), getListHeight());
        } else _entryWidth = this.width;


        GL11.glPushMatrix();

        GL11.glTranslatef(0, -scroll, 0);

        int currentHeight = 0;

        List<Music> list = musicList.getMusicList();
        boolean moved = moving == -1;
        for (int i = 0; i < list.size(); i++)
        {
            if (i != this.moving)
            {
                Music music = list.get(i);

                if (currentHeight + _entryHeight > relMouseY - moveYStart && !moved && mouseY < y + height)
                {
                    int diff = currentHeight + _entryHeight - (relMouseY - moveYStart);
                    double progress = Math.min(1, ((double) diff) / (double) _entryHeight);
                    double invertedProgress = (1D - progress);
                    GL11.glTranslatef(0, ((float) progress * _entryHeight), 0);
                    drawMusic(0, 0, _entryWidth, _entryHeight, music, 3);
                    GL11.glTranslatef(0, ((float) invertedProgress * _entryHeight) + _entryHeight, 0);
                    moved = true;
                } else
                {
                    drawMusic(0, 0, _entryWidth, _entryHeight, music, 3);
                    currentHeight += _entryHeight;
                    GL11.glTranslatef(0, _entryHeight, 0);
                }
            }
        }

        GL11.glPopMatrix();

        if (moving != -1 && mouseY < y + height)
        {
            double oldZ = helper.getZIndex();
            helper.setZIndex(oldZ + 10);
            Music music = list.get(moving);
            drawMusic(0, Math.max(0, mouseY - moveYStart), _entryWidth, _entryHeight, music, 1);
            helper.setZIndex(oldZ);
        }

        GLClippingPlanes.glDisableClipping();

        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }

    private boolean hasScroll()
    {
        return getListHeight() > this.height;
    }

    private int getListHeight()
    {
        return musicList.size() * _entryHeight;
    }

    private int getMaxScroll()
    {
        return getListHeight() - this.height + _scrollMaxOffset;
    }

    private void clampScroll()
    {
        scroll = Miscellaneous.clamp(scroll, 0, this.hasScroll() ? getMaxScroll() : 0);
    }

    @Override
    public void update()
    {
        if (moving == -1)
            isReadyToDelete = false;
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (moving == -1)
        {
            int offset = -scroll;
            for (int i = 0; i < musicList.size(); i++)
            {
                if (GuiUtils.isMouseInBounds(mouseX, mouseY, x + _entryWidth - 27, y + offset, _entryWidth, _entryHeight))
                {
                    this.moving = i;
                    moveXStart = mouseX - x;
                    moveYStart = mouseY + scroll - (i * _entryHeight);
                    break;
                } else offset += _entryHeight;
            }
        }
        return false;
    }

    @Override
    public void mouseUp(int mouseX, int mouseY, int mouseButton)
    {
        if (moving != -1)
        {
            if (this.isMovingInDeleteZone())
                musicList.remove(moving);
            else
            {

                int relMouseY = mouseY + scroll;
                int m = (relMouseY - 30 - moveYStart);
                int i = m / _entryHeight + (m % _entryHeight > (_entryHeight / 2) ? 1 : 0);
                i = Miscellaneous.clamp(i, 0, musicList.size() - 1);
                Music moved = musicList.remove(moving);
                musicList.add(i, moved);
            }
            moving = -1;
            moveXStart = -1;
            moveYStart = -1;
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {
        this.isReadyToDelete = moving != -1 && isMouseInDeleteZone(mouseX, mouseY);
    }

    public boolean isMouseInDeleteZone(int mouseX, int mouseY)
    {
        return mouseY > y + height;
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

    public boolean isMovingInDeleteZone()
    {
        return this.isReadyToDelete;
    }
}
