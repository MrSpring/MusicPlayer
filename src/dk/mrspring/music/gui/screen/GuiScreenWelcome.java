package dk.mrspring.music.gui.screen;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.util.GuiUtils;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Konrad on 12-08-2015.
 */
public class GuiScreenWelcome extends GuiScreen
{
    int progress = 0;
    int transition = 0;
    int steps = 2;
    float fTransition = 0F;

    public GuiScreenWelcome(net.minecraft.client.gui.GuiScreen previous)
    {
        super("Welcome", previous);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.hideBottomBar().hideTopBar().doPreviousScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        GuiUtils.drawRainbowSquare(progress, 0, 0, width, height);

        /*fTransition += partialTicks;

        float extra = ((float) steps) * partialTicks;
        float localTransition = transition + extra;
        float y1 = localTransition;
        float y2 = transition;
        float y3 = fTransition;
        super.drawScreen(mouseX, mouseY, partialTicks);
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        helper.drawShape(new Quad(10, 10 + y1, 10, 10))
                .drawShape(new Quad(30, 10 + y2, 10, 10))
                .drawShape(new Quad(50, 10 + y3, 10, 10));*/

        float extra = ((float) steps) * partialTicks;
        float localTransition = transition + extra;

        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        helper.setZIndex(10);
        int windowWidth = Math.min(width - 20, 410), windowHeight = Math.min(height - 20, 220);
        int windowX = (width - windowWidth) / 2, windowY = (height - windowHeight) / 2;
        float alpha = 0.875F;
        helper.drawButtonThingy(new Quad(windowX, windowY, windowWidth, windowHeight), 1, true, Color.BLACK, alpha, Color.BLACK, alpha);

        boolean doTransition = localTransition != 0F && localTransition != 200F;
        if (doTransition)
        {
            float t = (Math.min(100, localTransition)) / 100F;
            float inT = (Math.max(0, localTransition - 100)) / 100F;
            if (localTransition > 0 && localTransition <= 100)
                GLClippingPlanes.glEnableVerticalClipping((int) (windowY + (t * ((float) windowHeight))), windowY + windowHeight);
            else if (localTransition < 200)
                GLClippingPlanes.glEnableVerticalClipping(windowY - 1, (int) (windowY + (inT * ((float) windowHeight))));
        }

        if (localTransition <= 100)
            drawWelcomeScreen(windowX, windowY, windowWidth, windowHeight);
        else drawFolderScreen(windowX, windowY, windowWidth, windowHeight);

        if (doTransition && ((localTransition > 0 && localTransition <= 100) || localTransition < 200))
            GLClippingPlanes.glDisableClipping();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    private void drawFolderScreen(int windowX, int windowY, int windowWidth, int windowHeight)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        glPushMatrix();
        float s = 2F;
        glScalef(s, s, s);
        helper.drawCenteredText("Folders!", new Vector(width / 4, windowY / 2 + 6));
        glPopMatrix();

        helper.drawText("This is the folder screen. This is the folder screen. This is the folder screen." +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. " +
                "This is the folder screen. This is the folder screen. This is the folder screen. ", new Vector(windowX, windowY + 30), 0xFFFFFF, windowWidth);
    }

    private void drawWelcomeScreen(int windowX, int windowY, int windowWidth, int windowHeight)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        glPushMatrix();
        float s = 2F;
        glScalef(s, s, s);
        helper.drawCenteredText("Welcome!", new Vector(width / 4, windowY / 2 + 6));
        glPopMatrix();

        helper.drawText("This is the welcome screen. This is the welcome screen. This is the welcome screen." +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. " +
                "This is the welcome screen. This is the welcome screen. This is the welcome screen. ", new Vector(windowX, windowY + 30), 0xFFFFFF, windowWidth);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        progress = GuiUtils.increaseProgress(progress, 2);
        if (transition < 200) transition += steps;
    }
}
