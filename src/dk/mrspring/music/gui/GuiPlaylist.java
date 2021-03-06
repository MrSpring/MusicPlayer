package dk.mrspring.music.gui;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.llcore.Color;
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
    DeleteMethod deleteMethod = DeleteMethod.MOVE_LEFT;
    public double target = 0D, progress = target;
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

    public static void drawMusic(int xBase, int y, int w, int height, Music music, int padding, boolean text)
    {
        int wPadding = padding * 2;
        int coverSize = height - wPadding - 4;
        int width = w - coverSize, x = xBase + coverSize + 3;
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        helper.drawButtonThingy(new Quad(xBase + padding, y + padding, w - wPadding, height - wPadding), 0F, true);
        if (text)
            helper.drawText(getRenderString(music), new Vector(x + padding + 3, y + (height / 2)), 0xFFFFFF, true, -1,
                    DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.CENTER);
        int dragW = 10;
        helper.drawShape(new Quad(x + width - 15 - dragW, y + (height / 2) - 1 - 4, dragW, 2));
        helper.drawShape(new Quad(x + width - 15 - dragW, y + (height / 2) - 1, dragW, 2));
        helper.drawShape(new Quad(x + width - 15 - dragW, y + (height / 2) + 3, dragW, 2));
        music.bindCover();
        helper.drawTexturedShape(new Quad(xBase + padding + 2, y + padding + 2, coverSize, coverSize));
    }

    public void drawHoveringMusic(int xBase, int y, int w, int height, Music music, int padding)
    {
        double iProgress = 1D - progress;
        System.out.println(iProgress + ", " + progress);
        int removedWidth = (int) (((double) (w - height)) * iProgress);
        drawMusic(xBase + removedWidth, y, width - removedWidth, height, music, padding, iProgress < 0.9D);
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
    public void draw(Minecraft minecraft, int mouseX, int mouseY, float partialTicks)
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
                    drawMusic(0, 0, _entryWidth, _entryHeight, music, 3, false);
                    GL11.glTranslatef(0, ((float) invertedProgress * _entryHeight) + _entryHeight, 0);
                    moved = true;
                } else
                {
                    drawMusic(0, 0, _entryWidth, _entryHeight, music, 3, false);
                    currentHeight += _entryHeight;
                    GL11.glTranslatef(0, _entryHeight, 0);
                }
            }
        }

        GL11.glPopMatrix();

        if (moving != -1 && !isMouseInDeleteZone(mouseX, mouseY)/*mouseY < y + height*/)
        {
            double oldZ = helper.getZIndex();
            helper.setZIndex(oldZ + 10);
            Music music = list.get(moving);
            int drawMouseX = calcMouseXDrag(mouseX);
            drawHoveringMusic(drawMouseX, Math.max(0, mouseY - moveYStart), _entryWidth, _entryHeight, music, 1);
            helper.setZIndex(oldZ);
        }

        GLClippingPlanes.glDisableClipping();

        GL11.glPopMatrix();

        GL11.glPopMatrix();

        if (LiteModMusicPlayer.config.show_playlist_move_tip && list.size() > 0)
        {
            double oldZ = helper.getZIndex();
            helper.setZIndex(oldZ + 100);

            int xOffset = 18;
            int yOffset = 26;
            int bubbleWidth = minecraft.fontRendererObj.getStringWidth("Click and drag here to move") + 10;
            int arrowHeight = 5;
            float alpha = 0.9F;
            helper.drawShape(new Quad(x + width() - 12 - bubbleWidth, y + yOffset + arrowHeight - 1, bubbleWidth, 20 + 2).setColor(Color.BLACK).setAlpha(alpha));
            helper.drawShape(new Quad(x + width() - 12, y + yOffset + arrowHeight, 1, 20).setColor(Color.BLACK).setAlpha(alpha));
            helper.drawShape(new Quad(x + width() - 12 - bubbleWidth - 1, y + yOffset + arrowHeight, 1, 20).setColor(Color.BLACK).setAlpha(alpha));
            for (int i = 0; i < arrowHeight; i++)
            {
                if (i == 0)
                    helper.drawShape(new Quad(x + width() - xOffset - i, y + i + yOffset, 2 + (2 * i), 1).setColor(Color.BLACK).setAlpha(alpha));
                else if (i == 1)
                {
                    helper.drawShape(new Quad(x + width() - xOffset - i, y + i + yOffset, 2 + (2 * i), 1).setColor(Color.BLACK).setAlpha(alpha));
                    helper.drawShape(new Quad(x + width() - xOffset - i + 1, y + i + yOffset, 2 + (2 * i) - 2, 1));
                } else
                {
                    helper.drawShape(new Quad(x + width() - xOffset - i, y + i + yOffset, 2 + (2 * i), 1).setColor(Color.BLACK).setAlpha(alpha));
                    helper.drawShape(new Quad(x + width() - xOffset - i + 1, y + i + yOffset, 1, 1));
                    helper.drawShape(new Quad(x + width() - xOffset + arrowHeight + 1 + (i - arrowHeight) - 1, y + i + yOffset, 1, 1));
                }
            }
            helper.drawShape(new Quad(x + width() - 12 - bubbleWidth, y + yOffset + arrowHeight, bubbleWidth - arrowHeight - 4, 1));
            helper.drawShape(new Quad(x + width() - 12 - bubbleWidth, y + yOffset + arrowHeight + 20 - 1, bubbleWidth, 1).setColor(Color.LT_GREY));
            helper.drawShape(new Quad(x + width() - 12 - 1, y + yOffset + arrowHeight, 1, 20).setColor(Color.LT_GREY));
            helper.drawShape(new Quad(x + width() - 12 - bubbleWidth, y + yOffset + arrowHeight, 1, 19));
            helper.drawText("Click and drag here to move", new Vector(x + width() - 12 - 5, y + yOffset + arrowHeight + 5), 0xFFFFFF, -1, DrawingHelper.VerticalTextAlignment.RIGHT, DrawingHelper.HorizontalTextAlignment.TOP);
            helper.setZIndex(oldZ);
        }
    }

    private int calcMouseXDrag(int mouseX)
    {
        int returning = 0;
        if (deleteMethod != DeleteMethod.MOVE_LEFT) return 0;
        int mouseXOffset = mouseX - x();
        int lmx = mouseX - x();
        returning = lmx - moveXStart;
        int offset = -(mouseXOffset - width());
//        System.out.println(offset);
        if (offset <= 60) returning = (lmx - moveXStart) / 3;
        if (offset > 50 && offset <= 60)
        {
            int diff = -(width() - mouseXOffset - 60);
            double lProg = ((double) diff) / 10;
            double lReturning = returning;
            returning = lmx - moveXStart - ((int) (lProg * (lReturning * 2)));
        }
        /*if (mouseXOffset > width() - 100)
        {
        *//*if (mouseXOffset > width() - 70) *//*
            returning = (lmx - moveXStart) / 2;
        *//*else*//*
            if (mouseXOffset > width() - 70)
            {
                int diff = width() - mouseXOffset - 70;
                double lProg = ((double) diff) / 30;
                int remaining = returning * 2;

                System.out.println(mouseXOffset*//*returning*//*);
//            returning = lmx - moveXStart;
            }
        }*/
//        System.out.println(returning);
        return Math.min(returning, 0);
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
        progress = Miscellaneous.smoothDamp(target, progress, 0.4D);
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
                    LiteModMusicPlayer.config.show_playlist_move_tip = false;
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
                removeMusic(moving);
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

    public void removeMusic(int id)
    {
        musicList.remove(id);
    }

    public boolean isMoving()
    {
        return moving != -1;
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {
        this.isReadyToDelete = moving != -1 && isMouseInDeleteZone(mouseX, mouseY);
    }

    public boolean isMouseInDeleteZone(int mouseX, int mouseY)
    {
        switch (deleteMethod)
        {
            case MOVE_LEFT:
                return mouseX <= x;
            case MOVE_DOWN:
            default:
                return mouseY > y + height;
        }
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

    public enum DeleteMethod
    {
        MOVE_DOWN,
        MOVE_LEFT
    }
}
