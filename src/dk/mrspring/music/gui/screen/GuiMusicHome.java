package dk.mrspring.music.gui.screen;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiSimpleButton;
import dk.mrspring.music.gui.interfaces.IGui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 24-05-2015.
 */
public class GuiMusicHome extends GuiScreen
{
    List<IOption> options = new ArrayList<IOption>();

    public GuiMusicHome(net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super("gui.music.main.title", previousScreen);

        options.add(new IOption()
        {
            @Override
            public GuiScreen getGui()
            {
                return new GuiQueueManager(GuiMusicHome.this);
            }
        });
        options.add(new IOption()
        {
            @Override
            public GuiScreen getGui()
            {
                return new GuiQueueManager(GuiMusicHome.this);
            }
        });
        options.add(new IOption()
        {
            @Override
            public GuiScreen getGui()
            {
                return new GuiQueueManager(GuiMusicHome.this);
            }
        });
    }

    @Override
    public void initGui()
    {
        super.initGui();

        int buttonWidth = 100;
        int buttonHeight = 60;
        int padding = 5;
        int offset = (options.size() * (buttonWidth)) / 2;
        int barOffset = getBarHeight();

        for (int i = 0; i < options.size(); i++)
        {
            IOption option = options.get(i);
            this.addGuiElement("option:" + i, new GuiSimpleButton((width / 2) - offset + (buttonWidth * i) + padding, (height / 2) - (buttonHeight / 2) + padding - barOffset, buttonWidth - (2 * padding), buttonHeight - (2 * padding), "Stuff: " + i));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(0, 0, width, height).setColor(Color.BLACK).setAlpha(0.25F));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {
        if (identifier.startsWith("option:"))
        {
            int optionId = Integer.valueOf(identifier.substring(7));
            if (optionId >= 0 && optionId < options.size())
            {
                mc.displayGuiScreen(options.get(optionId).getGui());
            }
        }
    }

    private interface IOption
    {
        GuiScreen getGui();
    }
}
