package dk.mrspring.music.gui.screen.overlay;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.screen.GuiScreen;
import dk.mrspring.music.util.GuiUtils;
import dk.mrspring.music.util.TranslateHelper;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Konrad on 18-06-2015.
 */
public class OverlayScreen extends net.minecraft.client.gui.GuiScreen implements OverlayParent
{
    public boolean dark = true;
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
        this.cardList = this.asList(cards);
    }

    private List<Card> asList(Card... cards)
    {
        List<Card> list = new ArrayList<Card>();
        Collections.addAll(list, cards);
        return list;
    }

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

    public void addCards(Card... cards)
    {
        if (cardList == null)
            cardList = new ArrayList<Card>();
        Collections.addAll(cardList, cards);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.overlayWidth = width / 2;
        this.overlayHeight = height / 3;

        for (Card card : cardList)
            card.initGui();

        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void updateScreen()
    {
        for (Card card : cardList)
            card.update();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glPushMatrix();
        this.overlaying.drawScreen(-1000, -1000, partialTicks);
        GL11.glPopMatrix();
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();

        GL11.glPushMatrix();

        helper.setZIndex(10);
        helper.drawShape(new Quad(0, 0, width, height).setColor(Color.BLACK).setAlpha(0.5F));

        if (showTitle)
            helper.drawText(title, new Vector(width / 2, 30), getInvertedTextColor(),
                    true, width - 10, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.TOP);

        int xOffset = (width - getOverlayWidth()) / 2;

        if (LiteModMusicPlayer.config.show_overlay_screen_close_hint)
        {
            helper.drawText(TranslateHelper.translate("gui.overlay_screen.close_hint"),
                    new Vector(xOffset / 2, height / 2), 0xFFFFFF, true, xOffset,
                    DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.CENTER);
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(xOffset, height / 3 - scroll, 0);

        int listHeight = 0;
        for (Card card : cardList)
        {
            int cardHeight = card.getHeight();

            GL11.glPushMatrix();
            if (card.drawBackdrop() && cardHeight > 2)
                this.drawCard(new Quad(0, 0, overlayWidth, cardHeight));
            int sideXOffset = (width - getOverlayWidth()) / 2;
            int topYOffset = height / 3;
            card.draw(mc, mouseX - sideXOffset, mouseY - topYOffset - listHeight);
            GL11.glPopMatrix();

            GL11.glTranslatef(0, cardHeight + 10, 0);
            listHeight += cardHeight + 10;
        }

        GL11.glPopMatrix();

        if ((height / 3) < listHeight)
        {
            GuiUtils.drawScrollbar(width / 2 + (getOverlayWidth() / 2) + 4, height / 3 - 1, 7, height / 3 - 18, scroll, getMaxScroll(), listHeight);
//            double progress = Math.min(1, ((double) height / 3) / (double) (listHeight));
//            int scrollBarHeight = (int) (progress * ((height / 3) - 6));
//            progress = ((double) scroll) / ((double) getMaxScroll());
//            int scrollBarY = (height / 3) + (int) ((((height / 3) - 20) - scrollBarHeight) * progress);
//            helper.drawShape(new Quad(width / 2 + (getOverlayWidth() / 2) + 4, height / 3 - 1, 6, height / 3 - 18).setColor(Color.BLACK).setAlpha(0.75F));
//            helper.drawShape(new Quad(width / 2 + (getOverlayWidth() / 2) + 5, scrollBarY, 4, scrollBarHeight));
        }

        helper.setZIndex(0);
        GL11.glPopMatrix();
    }

    public void drawCard(Quad quad)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        float x = quad.getX(), y = quad.getY(), w = quad.getWidth(), h = quad.getHeight();
        helper.drawShape(new Quad(x, y, w, h).setColor(Color.BLACK).setAlpha(0.75F));
        helper.drawButtonThingy(new Quad(x - 2, y - 2, w + 4, h + 4), 0F, true);
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

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        int sideXOffset = (width - getOverlayWidth()) / 2;
        if (mouseX < sideXOffset || mouseX > width - sideXOffset + 10)
        {
            this.closeOverlay();
            LiteModMusicPlayer.config.show_overlay_screen_close_hint = false;
            return;
        }
        int topYOffset = height / 3;
        for (Card card : cardList)
            if (card.mouseDown(mouseX - sideXOffset, mouseY - topYOffset - scroll, mouseButton)) return;
            else topYOffset += card.getHeight() + 10;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        for (Card card : cardList) if (card != null) card.handleKeyTyped(keyCode, typedChar);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
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
