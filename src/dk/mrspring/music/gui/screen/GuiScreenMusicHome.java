package dk.mrspring.music.gui.screen;

import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.gui.menu.Menu;
import dk.mrspring.music.gui.menu.MenuItemButton;
import dk.mrspring.music.gui.menu.MenuItemSubMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 24-05-2015.
 */
public class GuiScreenMusicHome extends GuiScreen // TODO: Remove
{
    List<IOption> options = new ArrayList<IOption>();

    public GuiScreenMusicHome(net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super("gui.music.main.title", previousScreen);

        options.add(new IOption()
        {
            @Override
            public GuiScreen getGui()
            {
                return new GuiScreenQueueEditor(GuiScreenMusicHome.this);
            }
        });
        options.add(new IOption()
        {
            @Override
            public GuiScreen getGui()
            {
                return new GuiScreenAllMusic(GuiScreenMusicHome.this);
            }
        });
        options.add(new IOption()
        {
            @Override
            public GuiScreen getGui()
            {
                return new GuiScreenQueueEditor(GuiScreenMusicHome.this);
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
        int barOffset = getTopBarHeight();

//        for (int i = 0; i < options.size(); i++)
//        {
//            IOption option = options.get(i);
//            this.addGuiElement("option:" + i, new GuiSimpleButton((width / 2) - offset + (buttonWidth * i) + padding, (height / 2) - (buttonHeight / 2) + padding - barOffset, buttonWidth - (2 * padding), buttonHeight - (2 * padding), "Stuff: " + i));
//        }
        this.addGuiElement("menu", new Menu(5, 5, width, height,
                new MenuItemButton("Do stuff...", mc.fontRendererObj, 0),
                new MenuItemButton("Do some other stuff...", mc.fontRendererObj, 1),
                new MenuItemSubMenu("Make new this:", mc.fontRendererObj,
                        new MenuItemButton("Something one", mc.fontRendererObj, 2),
                        new MenuItemButton("Something two", mc.fontRendererObj, 3),
                        new MenuItemButton("Something three", mc.fontRendererObj, 4),
                        new MenuItemSubMenu("Oh hey, another sub.menu:", mc.fontRendererObj,
                                new MenuItemButton("Another one!", mc.fontRendererObj, 6),
                                new MenuItemButton("And another!", mc.fontRendererObj, 7),
                                new MenuItemButton("Another one!", mc.fontRendererObj, 8),
                                new MenuItemButton("And another!", mc.fontRendererObj, 9)
                                ),
                        new MenuItemSubMenu("Oh hey, another sub.menu:", mc.fontRendererObj,
                                new MenuItemButton("Another one!", mc.fontRendererObj, 6),
                                new MenuItemButton("And another!", mc.fontRendererObj, 7),
                                new MenuItemButton("Another one!", mc.fontRendererObj, 8),
                                new MenuItemButton("And another!", mc.fontRendererObj, 9)
                        ),
                        new MenuItemSubMenu("Oh hey, another sub.menu:", mc.fontRendererObj,
                                new MenuItemButton("Another one!", mc.fontRendererObj, 6),
                                new MenuItemButton("And another!", mc.fontRendererObj, 7),
                                new MenuItemButton("Another one!", mc.fontRendererObj, 8),
                                new MenuItemButton("And another!", mc.fontRendererObj, 9)
                        )
                ),
                new MenuItemButton("Something after the sub-menu thing", mc.fontRendererObj, 5)
        ));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
//        LiteModMusicPlayer.core.getDrawingHelper().drawShape(new Quad(0, 0, width, height).setColor(Color.BLACK).setAlpha(0.5F));
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
