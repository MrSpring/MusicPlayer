package dk.mrspring.music.gui.screen.overlay;

import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.DrawingHelper.VerticalTextAlignment;
import dk.mrspring.llcore.DrawingHelper.HorizontalTextAlignment;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.overlay.MultilineTextRender;
import dk.mrspring.music.overlay.TextRender;
import dk.mrspring.music.player.Music;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.Collection;

import static dk.mrspring.llcore.DrawingHelper.VerticalTextAlignment.*;
import static dk.mrspring.llcore.DrawingHelper.HorizontalTextAlignment.*;

/**
 * Created by Konrad on 18-06-2015.
 */
public class CardMusic extends Card
{
    Music showing;

    public CardMusic(OverlayParent overlayParent, Music showing)
    {
        super(overlayParent);

        this.showing = showing;

        parent.addCard(new BasicInfoCard(overlayParent));
    }

    @Override
    public int getHeight()
    {
        return 0;
    }

    @Override
    public boolean drawBackdrop()
    {
        return false;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
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

    private class BasicInfoCard extends Card
    {
        MultilineTextRender render;

        public BasicInfoCard(OverlayParent overlayParent)
        {
            super(overlayParent);
        }

        @Override
        public int getHeight()
        {
            int height = parent.getOverlayWidth() / 3;
            return render != null ? Math.max(render.getTotalHeight() + 20, height) : height;
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY)
        {
            int maxWidth = parent.getOverlayWidth();
            int coverSize = maxWidth / 3;
            if (render == null)
                render = new MultilineTextRender(
                        showing.getName() + "\n" +
                                showing.getAlbum() + "\n" +
                                showing.getArtist() + "\n",
                        parent.getMinecraft().fontRendererObj, coverSize * 2, false, 2);

            DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();

            showing.bindCover();
            helper.drawTexturedShape(new Quad(2, 2, coverSize - 4, coverSize - 4));

            int color = parent.getDefaultTextColor();

            GL11.glPushMatrix();
            GL11.glTranslatef(coverSize * 2, 0, 0);
            GL11.glScalef(2, 2, 2);
            helper.drawText("Basic Info", new Vector(0, 3), color, true, coverSize, VerticalTextAlignment.CENTER, TOP);
            GL11.glPopMatrix();

//            String title = showing.getName(), album = showing.getAlbum(), artist = showing.getArtist();
//            helper.drawText(title, new Vector(coverSize * 2, 4 + 28), color, true, coverSize * 2, VerticalTextAlignment.CENTER, TOP);
//            helper.drawText(album, new Vector(coverSize * 2, 16 + 28), color, true, coverSize * 2, VerticalTextAlignment.CENTER, TOP);
//            helper.drawText(artist, new Vector(coverSize * 2, 26 + 28), color, true, coverSize * 2, VerticalTextAlignment.CENTER, TOP);
            render.render(helper, coverSize * 2, 4 + 28, color, true, VerticalTextAlignment.CENTER, TOP);
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
}
