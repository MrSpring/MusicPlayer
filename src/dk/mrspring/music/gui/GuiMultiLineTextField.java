package dk.mrspring.music.gui;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.gui.interfaces.IMouseListener;
import dk.mrspring.music.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * Created by MrSpring on 10-11-2014 for In-Game File Explorer.
 */
public class GuiMultiLineTextField implements IGui, IMouseListener
{
    int x, y, w, h;
    String text;
    String line;
    boolean focused, drawBackground = true;
    int cursorPos;
    int flashCount = 0;
    int padding = 4;
    int scrollHeight = 0;
    int lines = 0;

    int cursorLine = 0, cursorRelativePos = 0;

    public GuiMultiLineTextField(int x, int y, int w, int h, String text)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.text = text;
        this.focused = false;
        this.cursorPos = 0;
        this.loadCursorPosition(Minecraft.getMinecraft().fontRendererObj);
    }

    public GuiMultiLineTextField hideBackground()
    {
        drawBackground = false;
        padding = 0;
        return this;
    }

    public GuiMultiLineTextField showBackground()
    {
        drawBackground = true;
        padding = 4;
        return this;
    }

    private void loadCursorPosition(FontRenderer renderer)
    {
        int xOffset = 0;
        if (lines * 9 > h)
            xOffset += 5;

        List<String> lines = renderer.listFormattedStringToWidth(text, w - (padding * 2) - xOffset);
        int lineStartIndexInText = 0;
        for (int i = 0; i < lines.size(); i++)
        {
            String line = lines.get(i) + "\n";
            if (cursorPos >= lineStartIndexInText + line.length())
            {
                lineStartIndexInText += line.length();
            } else
            {
                cursorRelativePos = cursorPos - lineStartIndexInText;
                cursorLine = i;
                this.line = (String) renderer.listFormattedStringToWidth(text, w - (padding * 2) - xOffset).get(cursorLine);
                this.flashCount = 0;
                break;
            }
        }
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY, float partialTicks)
    {
        int xOffset = 0, yOffset = 0;
        if (lines * 9 > h)
        {
            xOffset += 5;
            yOffset = -scrollHeight;
        }

        if (drawBackground)
            LiteModMusicPlayer.core.getDrawingHelper().drawButtonThingy(new Quad(x, y, w, h), focused ? 1 : 0, true, Color.BLACK, 0.85F, Color.BLACK, 0.85F);

        GLClippingPlanes.glEnableClipping(x, x + w, y + padding, y + h - padding);

        lines = LiteModMusicPlayer.core.getDrawingHelper().drawText(text, new Vector(x + padding + xOffset, y + padding + yOffset), 0xFFFFFF, true, w - (padding * 2) - xOffset, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP).lines;
        GLClippingPlanes.glDisableClipping();
        String cutLine = line.substring(0, cursorRelativePos);
        int cursorXOffset = minecraft.fontRendererObj.getStringWidth(cutLine) + xOffset;

        if (focused && !(flashCount > 10))
            minecraft.fontRendererObj.drawString("|", x + cursorXOffset + padding - 1, y + (cursorLine * 9) + padding + yOffset, 0xFF0000, false);

        if (lines * 9 > h)
            this.drawScrollBar();
    }

    private void drawScrollBar()
    {
        float scrollBarYRange = (h - 40 - (padding * 2));
        float maxScrollHeight = getMaxScrollHeight();
        float scrollProgress = (float) this.scrollHeight / maxScrollHeight;
        float scrollBarY = scrollBarYRange * scrollProgress + padding;
        LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(x + padding, y + scrollBarY + 1, 3, 40).setColor(Color.DK_GREY));
        LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(x + padding - 1, y + scrollBarY, 3, 40).setColor(Color.WHITE));
    }

    @Override
    public void update()
    {
        flashCount++;
        if (flashCount > 20)
            flashCount = 0;
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        this.focused = GuiUtils.isMouseInBounds(mouseX, mouseY, x, y, w, h);
        this.flashCount = 0;
        return focused;
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
        if (focused)
            if (keyCode == Keyboard.KEY_RIGHT)
                this.setCursorPos(this.cursorPos + 1);
            else if (keyCode == Keyboard.KEY_LEFT)
                this.setCursorPos(this.cursorPos - 1);
            else if (keyCode == Keyboard.KEY_BACK)
                this.backspace();
            else if (keyCode == Keyboard.KEY_DELETE)
                this.delete();
            else if (keyCode == Keyboard.KEY_V && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)))
                this.paste();
            else if (keyCode == Keyboard.KEY_RETURN)
                this.writeCharacter('\n');
            else if (!Character.isISOControl(character))
                this.writeCharacter(character);
    }

    public void writeCharacter(char character)
    {
        this.writeString(String.valueOf(character));
    }

    public void writeString(String string)
    {
        StringBuilder builder = new StringBuilder(this.text);
        builder.insert(this.cursorPos, string);
        this.setText(builder.toString());
        this.setCursorPos(this.cursorPos + string.length());
    }

    private void paste()
    {
        String fromClipboard = LiteModMusicPlayer.core.getClipboardHelper().paste();
        if (fromClipboard != null)
            this.writeString(fromClipboard);
    }

    private void backspace()
    {
        if (this.cursorPos > 0)
        {
            StringBuilder builder = new StringBuilder(this.text);
            builder.delete(this.cursorPos - 1, this.cursorPos);
            this.setText(builder.toString());
            this.setCursorPos(cursorPos - 1);
        }
    }

    private void delete()
    {
        if (this.cursorPos <= this.text.length())
        {
            StringBuilder builder = new StringBuilder(this.text);
            builder.delete(this.cursorPos, this.cursorPos + 1);
            this.setText(builder.toString());
        }
    }

    private void setCursorPos(int cursorPos)
    {
        this.cursorPos = cursorPos;

        if (this.cursorPos < 0)
            this.cursorPos = 0;
        if (this.cursorPos > text.length())
            this.cursorPos = text.length();

        this.loadCursorPosition(Minecraft.getMinecraft().fontRendererObj);
    }

    public void setText(String text, boolean resetCursorPos)
    {
        this.text = text;
        this.flashCount = 0;
        if (resetCursorPos)
        {
            this.cursorPos = 0;
            this.loadCursorPosition(Minecraft.getMinecraft().fontRendererObj);
        }
    }

    public void setText(String text)
    {
        this.setText(text, false);
    }

    public void setWidth(int width)
    {
        this.w = width;
    }

    public void setHeight(int height)
    {
        this.h = height;
    }

    public String getText()
    {
        return text;
    }

    public void addScroll(int amount)
    {
        int maxScrollHeight = getMaxScrollHeight(), minScrollHeight = 0, scrollHeightAfterAddition = this.scrollHeight + amount;

        if (scrollHeightAfterAddition > maxScrollHeight)
            scrollHeightAfterAddition = maxScrollHeight;
        else if (scrollHeightAfterAddition < minScrollHeight)
            scrollHeightAfterAddition = minScrollHeight;

        this.scrollHeight = scrollHeightAfterAddition;
    }

    private int getMaxScrollHeight()
    {
        return (lines * 9) - h + padding + padding;
    }

    @Override
    public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
    {
        if (GuiUtils.isMouseInBounds(mouseX, mouseY, x, y, w, h))
        {
            int mouseWheel = dWheelRaw;
            mouseWheel /= 4;
            if (mouseWheel != 0)
                this.addScroll(-mouseWheel);
        }
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getX()
    {
        return x;
    }

    public void setY(int y)
    {
        this.y = y;
    }
}
