package dk.mrspring.music.gui.screen.old;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.GuiImageViewer;
import dk.mrspring.fileexplorer.helper.TranslateHelper;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.gui.screen.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;

/**
 * Created by MrSpring on 11-11-2014 for In-Game File Explorer.
 */
public class GuiScreenImageViewer extends GuiScreen
{
    final File image;
    float closeMessageOpacity = 1F;

    public GuiScreenImageViewer(String title, net.minecraft.client.gui.GuiScreen previousScreen, File imagePath)
    {
        super(title, previousScreen);
        this.image = imagePath;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.addGuiElement("image_viewer", new GuiImageViewer(image, 0, 0, width, height).centerImage());
        this.hideTitle().hideBars().hideDoneButton();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
        helper.drawShape(new Quad(0, 0, width, height).setColor(Color.BLACK));
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glPushMatrix();
        GL11.glScalef(2, 2, 1);
        float fullHeight = 30;
        int yOffset = 16 - (int) (closeMessageOpacity * fullHeight);
        if (yOffset < 0)
            yOffset = 0;
//        helper.drawIcon(DrawingHelper.hoverIcon, width / 4 - (mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.screen.image_viewer.exit_fullscreen")) / 2) - 5, -yOffset, mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.screen.image_viewer.exit_fullscreen")) + 10, 16, false);
        helper.drawButtonThingy(new Quad(width / 4 - (mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.screen.image_viewer.exit_fullscreen")) / 2) - 5, -yOffset, mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.screen.image_viewer.exit_fullscreen")) + 10, 16), 0, false);
        helper.drawText(TranslateHelper.translate("gui.screen.image_viewer.exit_fullscreen"), new Vector(width / 4, 4 - yOffset), 0xFFFFFF, true, -1, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.TOP);
        GL11.glPopMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (mouseButton == 0)
            mc.displayGuiScreen(previousScreen);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
//        closeMessageOpacity = 0.75F;
        if (closeMessageOpacity > 0)
            closeMessageOpacity -= 0.025;
        else closeMessageOpacity = 0;
    }
}
