package dk.mrspring.music.gui.screen.old;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.GuiCheckbox;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.gui.interfaces.IMouseListener;
import dk.mrspring.fileexplorer.helper.GuiHelper;
import dk.mrspring.fileexplorer.helper.TranslateHelper;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.gui.screen.GuiScreen;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by MrSpring on 28-12-2014 for In-Game File Explorer.
 */
public class GuiScreenWelcome extends GuiScreen
{
    public GuiScreenWelcome(String title, net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super(title, previousScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.hideTitle().hideBars().hideDoneButton();

        int topHeight = 60;

        List<String> linesFromMessage = mc.fontRendererObj.listFormattedStringToWidth(TranslateHelper.translate("gui.screen.welcome.message").replace("\\n", "\n"), width - 30);
        topHeight += (linesFromMessage.size() * 9);

        int readLines = mc.fontRendererObj.listFormattedStringToWidth(TranslateHelper.translate("gui.screen.welcome.i_accept.read"), width - 50).size();
        int writeLines = mc.fontRendererObj.listFormattedStringToWidth(TranslateHelper.translate("gui.screen.welcome.i_accept.write"), width - 50).size();

        this.addGuiElement("terms-and-conditions", new GuiTermsAndConditions(20, topHeight, width - 40, height - topHeight - 50 - (readLines * 9) - (writeLines * 9)));
        this.addGuiElement("finish", new GuiSimpleButton(20, height - 40, 40, 20, "Done").disable());

        this.addGuiElement("accept-read", new GuiCheckbox(20, height - 65 - ((readLines - 1) * 9) - ((writeLines - 1) * 9), 10, 10, false));
        this.addGuiElement("accept-write", new GuiCheckbox(20, height - 55 - ((writeLines - 1) * 9), 10, 10, false));
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (((GuiCheckbox) this.getGui("accept-write")).isChecked())
        {
            ((GuiCheckbox) this.getGui("accept-read")).setChecked(true);
            ((GuiCheckbox) this.getGui("accept-read")).setIsEnabled(false);
        } else
            ((GuiCheckbox) this.getGui("accept-read")).setIsEnabled(true);

        ((GuiSimpleButton) this.getGui("finish")).setEnabled(((GuiTermsAndConditions) this.getGui("terms-and-conditions")).loaded() && (((GuiCheckbox) this.getGui("accept-read")).isChecked() || ((GuiCheckbox) this.getGui("accept-write")).isChecked()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();

        helper.drawButtonThingy(new Quad(10, 10, width - 20, height - 20), 1, true, Color.BLACK, 0.9F, Color.BLACK, 0.9F);

        super.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glScalef(2, 2, 2);
        helper.drawText(TranslateHelper.translate("gui.screen.welcome.title"), new Vector(width / 4, 10), 0xFFFFFF, true, -1, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.TOP);

        GL11.glScalef(0.5F, 0.5F, 0.5F);
        helper.drawText(TranslateHelper.translate("gui.screen.welcome.message").replace("\\n", "\n"), new Vector(width / 2, 50), 0xFFFFFF, true, width - 30, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.TOP);
        helper.drawText(TranslateHelper.translate("gui.screen.welcome.i_accept.read"), new Vector(32, ((GuiCheckbox) this.getGui("accept-read")).getY() + 1), 0xFFFFFF, true, width - 50, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        helper.drawText(TranslateHelper.translate("gui.screen.welcome.i_accept.write"), new Vector(32, ((GuiCheckbox) this.getGui("accept-write")).getY() + 1), 0xFFFFFF, true, width - 50, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
    }

    @Override
    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {
        super.guiClicked(identifier, gui, mouseX, mouseY, mouseButton);
        if (identifier.equals("finish"))
            this.closeAndOpenExplorer();
    }

    private void closeAndOpenExplorer()
    {
        LiteModFileExplorer.config.acceptFileReading = ((GuiCheckbox) this.getGui("accept-read")).isChecked();
        LiteModFileExplorer.config.acceptFileManipulation = ((GuiCheckbox) this.getGui("accept-write")).isChecked();
        LiteModFileExplorer.config.showWelcomeScreen = false;
        LiteModFileExplorer.saveConfig();
        mc.displayGuiScreen(new GuiScreenFileExplorer(null, new File(LiteModFileExplorer.config.startLocation)));
    }

    public class GuiTermsAndConditions implements IGui, IMouseListener
    {
        int x, y, w, h, lines, scrollHeight = 0;
        String currentText = "";

        public GuiTermsAndConditions(int xPos, int yPos, int width, int height)
        {
            this.x = xPos;
            this.y = yPos;
            this.w = width;
            this.h = height;

            try
            {
                File file = new File("igfe-conditions-" + Minecraft.getMinecraft().gameSettings.language + ".txt");
                FileUtils.copyURLToFile(new URL("http://mrspring.dk/mods/igfe/conditions.php?lang=" + Minecraft.getMinecraft().gameSettings.language), file, 2500, 2500);
                if (file.exists())
                    this.currentText = FileUtils.readFileToString(file);
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        public boolean loaded()
        {
            return !this.currentText.equals("");
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY)
        {
            DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();

            helper.drawShape(new Quad(x + 1, y, w - 2, h).setColor(Color.BLACK).setAlpha(0.5F));
            helper.drawShape(new Quad(x, y + 1, 1, h - 2).setColor(Color.BLACK).setAlpha(0.5F));
            helper.drawShape(new Quad(x + w - 1, y + 1, 1, h - 2).setColor(Color.BLACK).setAlpha(0.5F));

            GL11.glPushMatrix();
            GLClippingPlanes.glEnableClipping(x + 1, x + w - 1, y + 1, y + h - 1);
            lines = helper.drawText(currentText, new Vector(x + 5, y + 5 + scrollHeight), 0xFFFFFF, true, w - 10, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
            GLClippingPlanes.glDisableClipping();
            GL11.glPopMatrix();

            int linesHeight = lines * 9;
            if (linesHeight > h - 2)
            {
                float scrollBarHeight = h / 10;
                float range = h - 2 - scrollBarHeight;
                float scrollProgress = (float) this.scrollHeight / linesHeight;
                float scrollBarY = range * scrollProgress;
                helper.drawShape(new Quad(x, y + scrollBarY, 2, scrollHeight).setColor(Color.WHITE));
            }
        }

        @Override
        public void update()
        {
            if (h > lines * 9)
                scrollHeight = 0;
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

        @Override
        public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
        {
            if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h))
            {
                int mouseWheel = dWheelRaw / 4;
                if (mouseWheel != 0)
                    this.addScroll(-mouseWheel);
            }
        }

        private void addScroll(int height)
        {
            int maxScroll = lines * 9 - 5, minScroll = 0;

            if (scrollHeight + height > maxScroll)
                scrollHeight = maxScroll;
            else if (scrollHeight + height < minScroll)
                scrollHeight = minScroll;
        }
    }
}
