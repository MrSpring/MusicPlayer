package dk.mrspring.music.gui;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.util.GuiUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

/**
 * Created by MrSpring on 14-11-2014 for In-Game File Explorer.
 */
public class GuiCustomTextField implements IGui
{
    final int PADDING = 3;
    String text;
    String ghost = "";
    int x, y, w, h;
    boolean focused, enabled = true;
    int selectionStart = 0;
    int cursorPos = 0;
    int scroll = 0;

    public GuiCustomTextField(int x, int y, int width, int height, String text)
    {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        this.text = text;
    }

    public String getGhost()
    {
        return ghost;
    }

    public GuiCustomTextField setGhost(String ghost)
    {
        this.ghost = ghost;
        return this;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        LiteModMusicPlayer.core.getDrawingHelper().drawButtonThingy(new Quad(x, y, w, h), focused || !enabled ? 1 : 0, enabled, Color.BLACK, 0.85F, Color.BLACK, 0.85F);

        if (selectionStart != cursorPos)
        {
            int minSelectionOrCursor = Math.min(cursorPos, selectionStart);
            int maxSelectionOrCursor = Math.max(cursorPos, selectionStart);

            int selectionRenderStart = minecraft.fontRendererObj.getStringWidth(getText().substring(0, minSelectionOrCursor));
            int selectionRenderWidth = minecraft.fontRendererObj.getStringWidth(getText().substring(minSelectionOrCursor, maxSelectionOrCursor));

            GLClippingPlanes.glEnableClipping(x + PADDING - 1, x + w - PADDING + 1, y, y + h);
            LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(x + PADDING + selectionRenderStart - scroll - 1, y + PADDING - 1, selectionRenderWidth + 1, h - (2 * PADDING) + 2).setColor(Color.BLUE).setAlpha(0.9F));
            GLClippingPlanes.glDisableClipping();
        }

//        int textY = y + (h / 2) - 4;

//        GLClippingPlanes.glEnableClipping(x + PADDING, x + w - PADDING, y, y + h);
        GLClippingPlanes.glEnableHorizontalClipping(x + PADDING, x + w - PADDING);

        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
//        minecraft.fontRendererObj.drawString(getText(), x + PADDING - scroll, textY, 0xFFFFFF, false);
        helper.drawText(getText(), new Vector(x + PADDING, y + (h / 2)), 0xFFFFFF, false, -1, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.CENTER);

        GLClippingPlanes.glDisableClipping();

        int cursorX = 0;
        if (focused)
        {
            if (cursorPos > 0 && cursorPos < getText().length())
                cursorX = minecraft.fontRendererObj.getStringWidth(getText().substring(0, cursorPos));
            else if (cursorPos == getText().length())
                cursorX = minecraft.fontRendererObj.getStringWidth(getText());
        } else if (getText().length() == 0)
//            minecraft.fontRendererObj.drawString(getGhost(), x + PADDING, textY, 0xBFBFBF, true);
            helper.drawText(getGhost(), new Vector(x + (w / 2), y + (h / 2)), 0xBFBFBF, true, -1, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.CENTER);

        if (focused)
//            minecraft.fontRendererObj.drawString("|", x + cursorX + PADDING - 1 - scroll, textY, 0xFF0000, false);
            helper.drawText("|", new Vector(x + cursorX + PADDING - 1 - scroll, y + (h / 2)), 0xFF0000, false, -1, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.CENTER);

        this.drawScrollBar(minecraft);
    }

    private void drawScrollBar(Minecraft minecraft)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();

        float scrollX = x + PADDING, scrollY = y + h - PADDING - 1, scrollXEnd = x - 2 * PADDING;

        int textWidth = minecraft.fontRendererObj.getStringWidth(getText());
        float renderStartProgressThrough = (float) scroll / (float) textWidth;
        float renderEndProgressThrough = (((float) scroll + w - (2 * PADDING)) / (float) textWidth);

        scrollX += w * renderStartProgressThrough;
        scrollXEnd += Math.min(w * renderEndProgressThrough, w + PADDING);

        float scrollWidth = scrollXEnd - scrollX;

        if (scrollWidth < w - (2 * PADDING))
            helper.drawShape(new Quad(scrollX, scrollY, scrollWidth, 1));
//        helper.drawShape(new Quad(scrollX + 1, scrollY + 1, scrollXEnd - scrollX, 1).setColor(Color.DK_GREY));
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setW(int w)
    {
        this.w = w;
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public int getW()
    {
        return w;
    }

    public int getH()
    {
        return h;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getSelection()
    {
        return getText().substring(Math.min(selectionStart, cursorPos), Math.max(selectionStart, cursorPos));
    }

    public void writeString(String string)
    {
        StringBuilder builder = new StringBuilder(getText());
        builder.insert(cursorPos, string);
        setText(builder.toString());
        setCursorPos(cursorPos + string.length());
    }

    public void writeCharacter(char character)
    {
        this.writeString(String.valueOf(character));
    }

    @Override
    public void update()
    {
        LiteModMusicPlayer.disableKeys = getFocused();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        setFocus(GuiUtils.isMouseInBounds(mouseX, mouseY, x, y, w, h));
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
        if (focused) // TODO: Add blinking cursor
            if (keyCode == Keyboard.KEY_RIGHT)
                setCursorPos(cursorPos + 1, !isShiftDown());
            else if (keyCode == Keyboard.KEY_LEFT)
                setCursorPos(cursorPos - 1, !isShiftDown());
            else if (keyCode == Keyboard.KEY_BACK)
                this.handleDelete(false);
            else if (keyCode == Keyboard.KEY_DELETE)
                this.handleDelete(true);
            else if (keyCode == Keyboard.KEY_V && isCtrlDown())
                this.pasteText();
            else if (keyCode == Keyboard.KEY_C && isCtrlDown())
                this.copyText();
            else if (keyCode == Keyboard.KEY_X && isCtrlDown())
                this.cutText();
            else if (keyCode == Keyboard.KEY_HOME)
                this.setCursorPos(0);
            else if (keyCode == Keyboard.KEY_END)
                this.setCursorPos(getText().length());
            else if (keyCode == Keyboard.KEY_RETURN)
                this.focused = false;
            else if (!Character.isISOControl(character))
                this.writeCharacter(character);
    }

    private boolean isShiftDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    private boolean isCtrlDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    private void delete(int deleteStart, int deleteEnd)
    {
        int actualStart = Math.max(deleteStart, 0);
        StringBuilder builder = new StringBuilder(getText());
        builder.delete(actualStart, Math.min(getText().length(), deleteEnd));
        setText(builder.toString());
        setCursorPos(actualStart);
    }

    private void delete(int amount)
    {
        delete(Math.min(cursorPos + amount, cursorPos), Math.max(cursorPos + amount, cursorPos));
    }

    private void deleteSelection()
    {
        delete(Math.min(selectionStart, cursorPos), Math.max(selectionStart, cursorPos));
    }

    private void handleDelete(boolean isDeleteKey)
    {
        if (selectionStart != cursorPos)
            deleteSelection();
        else delete(isDeleteKey ? 1 : -1);
    }

    private void pasteText()
    {
        String fromClipboard = LiteModMusicPlayer.core.getClipboardHelper().paste();
        this.writeString(fromClipboard);
    }

    private void copyText()
    {
        String selection = this.getSelection();
        if (!selection.isEmpty())
            LiteModMusicPlayer.core.getClipboardHelper().copy(selection);
    }

    private void cutText()
    {
        this.copyText();
        this.deleteSelection();
    }

    public void setCursorPos(int cursorPos)
    {
        this.setCursorPos(cursorPos, true);
    }

    public void setCursorPos(int newCursorPosition, boolean moveSelection)
    {
        if (newCursorPosition < 0)
            this.cursorPos = 0;
        else if (newCursorPosition > getText().length())
            this.cursorPos = getText().length();
        else this.cursorPos = newCursorPosition;

        if (moveSelection)
            this.selectionStart = cursorPos;

        int renderWindowMin = scroll;
        int renderWindowMax = renderWindowMin + w - (2 * PADDING);

        int cursorRenderPosition = Minecraft.getMinecraft().fontRendererObj.getStringWidth(getText().substring(0, cursorPos));

        if (cursorRenderPosition > (renderWindowMax - (2 * PADDING)))
            scroll = cursorRenderPosition - (w - (4 * PADDING));
        else if (cursorRenderPosition < renderWindowMin + (2 * PADDING))
            scroll = cursorRenderPosition - (2 * PADDING);

        if (scroll < 0)
            scroll = 0;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setFocus(boolean focus)
    {
        this.focused = focus;
    }

    public boolean getFocused()
    {
        return focused;
    }
}
