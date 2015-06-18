package dk.mrspring.music.gui.screen.overlay;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.gui.interfaces.IMouseListener;
import dk.mrspring.music.gui.screen.GuiScreen;
import dk.mrspring.music.util.GuiHelper;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Konrad on 18-06-2015.
 */
public class OverlayScreen extends net.minecraft.client.gui.GuiScreen implements OverlayParent
{
    public boolean dark = false;
    String title = "";
    boolean showTitle;
    int overlayWidth, overlayHeight;
    int scroll;
    int mouseXAtLastFrame, mouseYAtLastFrame;
    net.minecraft.client.gui.GuiScreen overlaying;
    List<Card> cardList = new ArrayList<Card>();

    public OverlayScreen(String title, net.minecraft.client.gui.GuiScreen previousScreen, Card... cards)
    {
        this.overlaying = previousScreen;
        this.title = title;
        List<Card> list = new ArrayList<Card>();
        Collections.addAll(list, cards);
        this.cardList = list;
        dark = true;
//        this.cardList = this.asList(cards);
    }

    /*private List<Card> asList(Card... cards)
    {
        List<Card> list = new ArrayList<Card>();
        Collections.addAll(list, cards);
        return list;
    }*/

    @Override
    public Minecraft getMinecraft()
    {
        return mc;
    }

    @Override
    public int getDefaultTextColor()
    {
        return dark ? 0xFFFFFF : 0x3F3F3F;
    }

    @Override
    public int getInvertedTextColor()
    {
        return 0xFFFFFF;
    }

    @Override
    public void addCard(Card card)
    {
        if (cardList == null)
            cardList = new ArrayList<Card>();
        this.cardList.add(card);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.overlayWidth = width / 3;
        this.overlayHeight = height / 3;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);


        GL11.glPushMatrix();
        this.overlaying.drawScreen(-1000, -1000, partialTicks);
        GL11.glPopMatrix();

        int x = width - overlayWidth / 2;
        int y = height - overlayHeight / 2;

        GL11.glPushMatrix();

        LiteModMusicPlayer.core.getDrawingHelper().setZIndex(10);
        LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(0, 0, width, height).setColor(Color.BLACK).setAlpha(0.5F));

        if (showTitle)
            LiteModMusicPlayer.core.getDrawingHelper().drawText(title, new Vector(width / 2, 30), getInvertedTextColor(),
                    true, width - 10, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.TOP);

        GL11.glPushMatrix();
        GL11.glTranslatef(width / 3, height / 3 - scroll, 0);

        for (Card card : cardList)
        {
            int cardHeight = card.getHeight();

            GL11.glPushMatrix();
            if (card.drawBackdrop())
                this.drawCard(new Quad(0, 0, overlayWidth, cardHeight));
            card.draw(mc, mouseX, mouseY);
            GL11.glPopMatrix();

            GL11.glTranslatef(0, cardHeight + 10, 0);
        }

        GL11.glPopMatrix();
        LiteModMusicPlayer.core.getDrawingHelper().setZIndex(0);
        GL11.glPopMatrix();
    }

    public void drawCard(Quad quad)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        float x = quad.getX(), y = quad.getY(), w = quad.getWidth(), h = quad.getHeight();
        helper.drawShape(new Quad(x, y, w, h).setColor(Color.BLACK).setAlpha(0.75F));
        helper.drawButtonThingy(new Quad(x - 2, y - 2, w + 4, h + 4), 0F, true);
        /*Color backgroundColor = dark ? Color.BLACK : Color.WHITE;
        float backgroundAlpha = dark ? 0.75F : 1F;
        float x = quad.getX(), y = quad.getY(), w = quad.getWidth(), h = quad.getHeight();
        if (!dark)
        {
            helper.drawShape(new Quad(x, y + h - 1, w, 2).setColor(Color.BLACK).setAlpha(0.5F));
            helper.drawShape(new Quad(x + 1, y + h + 1, w - 2, 1).setColor(Color.BLACK).setAlpha(0.5F));
        }
        helper.drawShape(new Quad(x + 1, y, w - 2, 1).setColor(backgroundColor).setAlpha(backgroundAlpha));
        helper.drawShape(new Quad(x, y + 1, w, h - 2).setColor(backgroundColor).setAlpha(backgroundAlpha));
        helper.drawShape(new Quad(x + 1, y + h - 1, w - 2, 1).setColor(backgroundColor).setAlpha(backgroundAlpha));*/
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        int dWheel = Mouse.getDWheel();
        for (Card card : cardList)
        {
            if (card.handleMouseWheel(mouseXAtLastFrame, mouseYAtLastFrame, dWheel))
                return;
        }
        this.handleScroll(dWheel);
        super.handleMouseInput();
    }

    public void handleScroll(int dWheelRaw)
    {
        int mouseWheel = dWheelRaw;
        mouseWheel /= 4;
        if (mouseWheel != 0)
            this.addScroll(-mouseWheel);
    }

    private void addScroll(int scroll)
    {
        this.scroll += scroll;
        this.clampScroll();
    }

    private void clampScroll()
    {
        scroll = Math.min(scroll, this.hasScroll() ? getMaxScroll() : 0);
        scroll = Math.max(scroll, 0);
    }

    private boolean hasScroll()
    {
        return getListHeight() > this.overlayHeight;
    }

    private int getListHeight()
    {
        int height = 0;
        for (Card card : cardList)
            height += card.getHeight() + 10;
        return height;
    }

    private int getMaxScroll()
    {
        return getListHeight() - this.overlayHeight;
    }

    public void closeOverlay()
    {
        boolean bypassesInit = false;
        GuiScreen asScreen = null;
        if (this.overlaying instanceof GuiScreen)
        {
            asScreen = (GuiScreen) this.overlaying;
            bypassesInit = asScreen.doesBypassInit();
            asScreen.bypassInit(true);
        }

        mc.displayGuiScreen(this.overlaying);

        if (asScreen != null)
            asScreen.bypassInit(bypassesInit);
    }

    @Override
    public int getOverlayWidth()
    {
        return this.overlayWidth;
    }
}
