package dk.mrspring.music;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.overlay.MultilineTextRender;
import dk.mrspring.music.util.AnyTimeKeyBind;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * Created by MrSpring on 16-06-2015 for MC Music Player.
 */
public class ConsoleOutput
{
    private static final int TEXT_PADDING = 4;
    private static final int LINE_HEIGHT = 3;

    String text = "";
    MultilineTextRender render;
    int scroll = 0;
    FontRenderer fontRenderer;
    int zOffset = 0;
    float backgroundAlpha = 0.5F;
    boolean wrap = true;
    int scrollSteps = 10;
    int textColor = 0xFFFFFF;
    boolean textShadow = true;
    int scrollBarWidth = 2;
    boolean autoScroll = true;
    private AnyTimeKeyBind scrollUp = new AnyTimeKeyBind(LiteModMusicPlayer.config.console_scroll_up_key);
    private AnyTimeKeyBind scrollDown = new AnyTimeKeyBind(LiteModMusicPlayer.config.console_scroll_down_key);

    public ConsoleOutput(FontRenderer renderer)
    {
        this.fontRenderer = renderer;
    }

    public void addLine(String line)
    {
        this.text += (text.length() > 0 ? "\n" : "") + line;
    }

    public String getText()
    {
        return text;
    }

    public void draw(Quad area)
    {
        int pad = TEXT_PADDING * 2;
        if (render == null)
            render = new MultilineTextRender(getText(), fontRenderer, wrap ? (int) (area.getWidth()) - pad : -1, false, LINE_HEIGHT);
        else render.setText(getText()).setWrapWidth(wrap ? (int) area.getWidth() - pad : -1);

        if (scrollUp.isClicked())
            this.scrollUp((int) area.getHeight());
        else if (scrollDown.isClicked())
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                this.autoScroll = true;
            else this.scrollDown((int) area.getHeight());

        if (autoScroll)
            scroll = getMaxScroll((int) area.getHeight());

        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        double oldZ = helper.getZIndex();
        helper.setZIndex(oldZ + zOffset);
        drawBackground(helper, area);
        drawScrollbar(helper, area);
        GLClippingPlanes.glEnableVerticalClipping((int) area.getY(), (int) area.getY() + (int) area.getHeight());
        GL11.glPushMatrix();
        GL11.glTranslatef(area.getX(), area.getY() - scroll, zOffset);
        render.render(helper, TEXT_PADDING, TEXT_PADDING, textColor, textShadow, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        GL11.glPopMatrix();
        helper.setZIndex(oldZ);
        GLClippingPlanes.glDisableClipping();

//        addLine("Rendered!");
    }

    private int getMaxScroll(int height)
    {
        return Math.max(0, render.getTotalHeight() - height + (2 * TEXT_PADDING));
    }

    private void drawBackground(DrawingHelper helper, Quad area)
    {
        int x = (int) area.getX();
        int y = (int) area.getY();
        int w = (int) area.getWidth();
        int h = (int) area.getHeight();

        helper.drawShape(new Quad(x + 1, y, w - 2, 1).setColor(Color.BLACK).setAlpha(backgroundAlpha));
        helper.drawShape(new Quad(x, y + 1, w, h - 2).setColor(Color.BLACK).setAlpha(backgroundAlpha));
        helper.drawShape(new Quad(x + 1, y + h - 1, w - 2, 1).setColor(Color.BLACK).setAlpha(backgroundAlpha));
    }

    private void drawScrollbar(DrawingHelper helper, Quad area)
    {
        int max = getMaxScroll((int) area.getHeight());
        if (max > 0)
        {
            int scrollX = (int) (area.getX() + area.getWidth() - scrollBarWidth);
            int height = (int) (area.getHeight() - 2);
            double progress = (double) scroll / (double) max;
            double sizeProgress = area.getHeight() / render.getTotalHeight();
            int scrollHeight = (int) (sizeProgress * (double) height);
            int scrollY = 1 + (int) (area.getY() + (int) (progress * (double) (height - scrollHeight)));
            helper.drawShape(new Quad(scrollX, scrollY, scrollBarWidth, scrollHeight));
        }
    }

    public void clampScroll(int expectedHeight)
    {
        int max = getMaxScroll(expectedHeight);
        if (scroll > max)
        {
            scroll = max;
            autoScroll = true;
        } else if (scroll < 0)
        {
            scroll = 0;
            autoScroll = false;
        } else
        {
            autoScroll = false;
        }
//        scroll = scroll > max ? max : (scroll < 0 ? 0 : scroll);
    }

    private void scrollDown(int height)
    {
        scroll += scrollSteps;
        clampScroll(height);
    }

    private void scrollUp(int height)
    {
        scroll -= scrollSteps;
        clampScroll(height);
    }
}
