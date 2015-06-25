package dk.mrspring.music.gui.screen;

import dk.mrspring.llcore.*;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.util.FBOHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * Created by Konrad on 12-06-2015.
 */
public class FBOTester extends GuiScreen
{
    Framebuffer frameBuffer;

    public FBOTester(String title, net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super(title, previousScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        if (frameBuffer == null)
        {
            GL11.glPushMatrix();
            frameBuffer = new Framebuffer(Display.getWidth(), Display.getHeight(), false);
            frameBuffer.bindFramebuffer(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            GL11.glPushMatrix();
            if (previousScreen != null)
                previousScreen.drawScreen(mouseX, mouseY, partialTicks);
            else
            {
            helper.drawShape(new Quad(5,5,10,10).setAlpha(1F));
            helper.drawShape(new Quad(15,5,10,10).setAlpha(0.9F));
            helper.drawShape(new Quad(25,5,10,10).setAlpha(0.8F));
            helper.drawShape(new Quad(35,5,10,10).setAlpha(0.7F));
            helper.drawShape(new Quad(45,5,10,10).setAlpha(0.6F));
            helper.drawShape(new Quad(55,5,10,10).setAlpha(0.5F));
            helper.drawShape(new Quad(65,5,10,10).setAlpha(0.4F));
            helper.drawShape(new Quad(75,5,10,10).setAlpha(0.3F));
            helper.drawShape(new Quad(85,5,10,10).setAlpha(0.2F));
            helper.drawShape(new Quad(95,5,10,10).setAlpha(0.1F));
            helper.drawShape(new Quad(105,5,10,10).setAlpha(0.0F));
            helper.drawShape(new Quad(5,15,10,10).setAlpha(1F).setColor(Color.BLACK));
            helper.drawShape(new Quad(15,15,10,10).setAlpha(0.9F).setColor(Color.BLACK));
            helper.drawShape(new Quad(25,15,10,10).setAlpha(0.8F).setColor(Color.BLACK));
            helper.drawShape(new Quad(35,15,10,10).setAlpha(0.7F).setColor(Color.BLACK));
            helper.drawShape(new Quad(45,15,10,10).setAlpha(0.6F).setColor(Color.BLACK));
            helper.drawShape(new Quad(55,15,10,10).setAlpha(0.5F).setColor(Color.BLACK));
            helper.drawShape(new Quad(65,15,10,10).setAlpha(0.4F).setColor(Color.BLACK));
            helper.drawShape(new Quad(75,15,10,10).setAlpha(0.3F).setColor(Color.BLACK));
            helper.drawShape(new Quad(85,15,10,10).setAlpha(0.2F).setColor(Color.BLACK));
            helper.drawShape(new Quad(95,15,10,10).setAlpha(0.1F).setColor(Color.BLACK));
            helper.drawShape(new Quad(105,15,10,10).setAlpha(0.0F).setColor(Color.BLACK));
            helper.drawShape(new Quad(5, 5, 10, 10).setColor(Color.WHITE));
//            helper.drawButtonThingy(new Quad(20, 5, 50, 20), 0F, true);
            }
//            super.drawScreen(mouseX, mouseY, partialTicks);
            GL11.glPopMatrix();
            frameBuffer.unbindFramebuffer();
            GL11.glPopMatrix();
        } else
        {
            GL11.glPushMatrix();
            helper.setZIndex(-1);
            helper.drawShape(new Quad(0, 0, width, height).setAlpha(0.25F));
            helper.setZIndex(0);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GlStateManager.alphaFunc(GL11.GL_ONE, GL11.GL_ZERO);
//            frameBuffer.checkFramebufferComplete();
            frameBuffer.bindFramebufferTexture();
//            this.drawTexturedModalRect(0, 0, 0, 0, width, height);
            helper.drawTexturedShape(new Shape(
                    new Vector(0, 0, 0, 512),
                    new Vector(width, 0, 512, 512),
                    new Vector(width, height, 512, 0),
                    new Vector(0, height, 0, 0)
            ));
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 25, 0);
            helper.drawShape(new Quad(5, 5, 10, 10).setAlpha(1F));
            helper.drawShape(new Quad(15,5,10,10).setAlpha(0.9F));
            helper.drawShape(new Quad(25,5,10,10).setAlpha(0.8F));
            helper.drawShape(new Quad(35,5,10,10).setAlpha(0.7F));
            helper.drawShape(new Quad(45,5,10,10).setAlpha(0.6F));
            helper.drawShape(new Quad(55,5,10,10).setAlpha(0.5F));
            helper.drawShape(new Quad(65,5,10,10).setAlpha(0.4F));
            helper.drawShape(new Quad(75,5,10,10).setAlpha(0.3F));
            helper.drawShape(new Quad(85,5,10,10).setAlpha(0.2F));
            helper.drawShape(new Quad(95,5,10,10).setAlpha(0.1F));
            helper.drawShape(new Quad(105,5,10,10).setAlpha(0.0F));
            helper.drawShape(new Quad(5,15,10,10).setAlpha(1F).setColor(Color.BLACK));
            helper.drawShape(new Quad(15,15,10,10).setAlpha(0.9F).setColor(Color.BLACK));
            helper.drawShape(new Quad(25,15,10,10).setAlpha(0.8F).setColor(Color.BLACK));
            helper.drawShape(new Quad(35,15,10,10).setAlpha(0.7F).setColor(Color.BLACK));
            helper.drawShape(new Quad(45,15,10,10).setAlpha(0.6F).setColor(Color.BLACK));
            helper.drawShape(new Quad(55,15,10,10).setAlpha(0.5F).setColor(Color.BLACK));
            helper.drawShape(new Quad(65,15,10,10).setAlpha(0.4F).setColor(Color.BLACK));
            helper.drawShape(new Quad(75,15,10,10).setAlpha(0.3F).setColor(Color.BLACK));
            helper.drawShape(new Quad(85,15,10,10).setAlpha(0.2F).setColor(Color.BLACK));
            helper.drawShape(new Quad(95,15,10,10).setAlpha(0.1F).setColor(Color.BLACK));
            helper.drawShape(new Quad(105,15,10,10).setAlpha(0.0F).setColor(Color.BLACK));
            GL11.glPopMatrix();

//            helper.drawText("Waiting for buffer texture...", new Vector(5, 5), 0xFFFFFF, true, -1, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        }
    }
}
