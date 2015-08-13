package dk.mrspring.music.gui.screen.overlay;

import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.overlay.MultilineTextRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import static dk.mrspring.llcore.DrawingHelper.HorizontalTextAlignment.TOP;

/**
 * Created by Konrad on 24-06-2015.
 */
public class CardBasicInfo extends Card
{
    MultilineTextRender render;
    String title, text;

    public CardBasicInfo(OverlayParent overlayParent, FontRenderer renderer, String title, String text)
    {
        super(overlayParent);

        this.title = title;
        this.text = text;
        render = new MultilineTextRender(text, renderer, parent.getOverlayWidth()-4, false, 20);
    }

    public String getText()
    {
        return text;
    }

    @Override
    public int getHeight()
    {
        int height = 28;
        return render != null ? Math.max(render.getTotalHeight() + height+5, height) : height;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY, float partialTicks)
    {
        render.setWrapWidth(parent.getOverlayWidth()-4);
        render.setText(getText());

        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        int color = parent.getDefaultTextColor();
//        GL11.glPushMatrix();
//        GL11.glTranslatef(coverSize * 2, 0, 0);
//        GL11.glScalef(2, 2, 2);
//        helper.drawText(title, new Vector(0, 3), color, true, coverSize, DrawingHelper.VerticalTextAlignment.CENTER, TOP);
//        GL11.glPopMatrix();
        this.drawTitle(title, 2F, color);

        render.render(helper, 2, 4 + 28, color, true, DrawingHelper.VerticalTextAlignment.LEFT, TOP);
    }

    public void drawTitle(String title, float scale, int color)
    {
        int width = parent.getOverlayWidth();
        GL11.glPushMatrix();
        GL11.glTranslatef(width / 2, 0, 0);
        GL11.glScalef(scale, scale, scale);
        LiteModMusicPlayer.core.getDrawingHelper().drawText(title, new Vector(0, 3), color, true, width / (int) scale, DrawingHelper.VerticalTextAlignment.CENTER, TOP);
        GL11.glPopMatrix();
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
}
