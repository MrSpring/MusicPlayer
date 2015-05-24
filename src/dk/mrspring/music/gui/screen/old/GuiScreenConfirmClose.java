package dk.mrspring.music.gui.screen.old;

import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.music.gui.screen.GuiScreen;

/**
 * Created by Konrad on 10-02-2015.
 */
public class GuiScreenConfirmClose extends GuiScreen
{
    ResultHandler handler;
    String fileName;

    public GuiScreenConfirmClose(String title, net.minecraft.client.gui.GuiScreen previousScreen, ResultHandler handler, String fileName)
    {
        super(title, previousScreen);
        this.handler = handler;
        this.fileName = fileName;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.addGuiElement("save", new GuiSimpleButton(0, 0, 50, 24, "gui.screen.save_before_closing.save"));
        this.addGuiElement("dont", new GuiSimpleButton(0, 0, 50, 24, "gui.screen.save_before_closing.dont_save"));
        this.addGuiElement("cancel", new GuiSimpleButton(0, 0, 50, 24, "gui.screen.save_before_closing.cancel"));

        this.hideBars().hideTitle().hideDoneButton();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
//        DrawingHelper.drawIcon(DrawingHelper.hoverIcon, width / 2 - 100, height / 2 - 70, 200, 140, false);
//
//        String translatedMessage = TranslateHelper.translate("gui.screen.save_before_closing.message").replace("\\n", "\n");
//        String preFileName = translatedMessage.split("###")[0];
//        String postFileName = translatedMessage.split("###")[1];
//
//        DrawingHelper.drawSplitCenteredString(mc.fontRendererObj, width / 2, height / 2 - 60, TranslateHelper.translate("gui.screen.save_before_closing.title"), 0xFFFFFF, 190, true);
//        int preLines = DrawingHelper.drawSplitCenteredString(mc.fontRendererObj, width / 2, height / 2 - 40, preFileName, 0xFFFFFF, 190, true);
//
//        DrawingHelper.drawQuad(width / 2 - 100 + 11, height / 2 - 35 + (preLines * 9), 200 - 22, 15, Color.BLACK, 1F);
//        DrawingHelper.drawQuad(width / 2 - 100 + 10, height / 2 - 35 + (preLines * 9) + 1, 1, 13, Color.BLACK, 1F);
//        DrawingHelper.drawQuad(width / 2 + 100 - 11, height / 2 - 35 + (preLines * 9) + 1, 1, 13, Color.BLACK, 1F);
//
//        DrawingHelper.drawSplitCenteredString(mc.fontRendererObj, width / 2, height / 2 - 32 + (preLines * 9), fileName, 0xFFFFFF, 190, true);
//
//        DrawingHelper.drawSplitCenteredString(mc.fontRendererObj, width / 2, height / 2 - 30 + (preLines * 9) + 14, postFileName, 0xFFFFFF, 190, true);
//
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {
        super.guiClicked(identifier, gui, mouseX, mouseY, mouseButton);

        if (identifier.equals("save"))
        {
            handler.save(previousScreen);
            mc.displayGuiScreen(null);
        } else if (identifier.equals("dont"))
        {
            handler.dontSave(previousScreen);
            mc.displayGuiScreen(null);
        } else if (identifier.equals("cancel"))
        {
            handler.cancel(previousScreen);
            mc.displayGuiScreen(previousScreen);
        }
    }

    @Override
    public boolean drawGui(String identifier, IGui gui, int mouseX, int mouseY)
    {
        if (identifier.equals("save"))
        {
            ((GuiSimpleButton) gui).setX(width / 2 - 80);
            ((GuiSimpleButton) gui).setY(height / 2 + 70 - (25 + 5));
        } else if (identifier.equals("dont"))
        {
            ((GuiSimpleButton) gui).setX(width / 2 - 80 + 50 + 5);
            ((GuiSimpleButton) gui).setY(height / 2 + 70 - (25 + 5));
        } else if (identifier.equals("cancel"))
        {
            ((GuiSimpleButton) gui).setX(width / 2 - 80 + 50 + 5 + 50 + 5);
            ((GuiSimpleButton) gui).setY(height / 2 + 70 - (25 + 5));
        } else return super.drawGui(identifier, gui, mouseX, mouseY);
        return true;
    }

    public interface ResultHandler
    {
        public void save(net.minecraft.client.gui.GuiScreen parentScreen);

        public void dontSave(net.minecraft.client.gui.GuiScreen parentScreen);

        public void cancel(net.minecraft.client.gui.GuiScreen parentScreen);
    }
}
