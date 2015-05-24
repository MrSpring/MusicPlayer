package dk.mrspring.music.gui.screen.old;

import dk.mrspring.fileexplorer.gui.GuiMultiLineTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/**
 * Created by MrSpring on 10-11-2014 for In-Game File Explorer.
 */
public class GuiScreenTextEditor extends GuiScreen
{
    GuiMultiLineTextField textField;

    String text;

    public GuiScreenTextEditor(String text)
    {
        this.text = text;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        textField = new GuiMultiLineTextField(10, 10, width - 20, height - 20, text);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        textField.draw(Minecraft.getMinecraft(), mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        textField.handleKeyTyped(keyCode, typedChar);
    }
}
