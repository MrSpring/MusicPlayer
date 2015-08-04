package dk.mrspring.music.gui.screen;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiSimpleButton;
import dk.mrspring.music.gui.interfaces.IDelayedDraw;
import dk.mrspring.music.gui.interfaces.IDrawable;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.gui.interfaces.IMouseListener;
import dk.mrspring.music.gui.menu.IMenuItem;
import dk.mrspring.music.gui.menu.Menu;
import dk.mrspring.music.util.TranslateHelper;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MrSpring on 27-11-2014 for In-Game File Explorer.
 */
public class GuiScreen extends net.minecraft.client.gui.GuiScreen
{
    protected net.minecraft.client.gui.GuiScreen previousScreen;
    String title = "untitled", subtitle = "";
    boolean repeats = false;
    private HashMap<String, IGui> guiHashMap;
    private boolean showTopBar = true;
    private boolean showBottomBar = true;
    private boolean drawCenteredTitle = true;
    private boolean useDefaultDoneButton = true;
    private int topBarHeight = 30, bottomBarHeight = 30;
    private Color topBarColor = Color.BLACK, bottomBarColor = Color.BLACK;
    private int mouseXAtLastFrame = 0, mouseYAtLastFrame = 0;
    private Menu openMenu = null;
    private boolean bypassInit;

    public GuiScreen(String title, net.minecraft.client.gui.GuiScreen previousScreen)
    {
        this.title = title;
        this.previousScreen = previousScreen;
    }

    public Menu getOpenMenu()
    {
        return openMenu;
    }

    public void openMenu(int mouseX, int mouseY, Menu.MenuAction action, IMenuItem... items)
    {
        if (items != null && items.length > 0)
        {
            this.openMenu = new Menu(this, mouseX, mouseY, width, height, items).setAction(action);
        }
    }

    public GuiScreen bypassInit(boolean bypass)
    {
        this.bypassInit = bypass;
        return this;
    }

    public boolean doesBypassInit()
    {
        return this.bypassInit;
    }

    public void closeMenu()
    {
        this.openMenu = null;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.guiHashMap = new HashMap<String, IGui>();

        this.addGuiElement("done_button", new GuiSimpleButton(0, 0, 40, 20, "Done").hideBackground());
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        this.mc = mc;
        this.itemRender = mc.getRenderItem();
        this.fontRendererObj = mc.fontRendererObj;
        this.width = width;
        this.height = height;
        this.buttonList.clear();
        if (!bypassInit)
            this.initGui();
    }

    public void addGuiElement(String identifier, IGui gui)
    {
        if (!this.guiHashMap.containsKey(identifier))
            this.guiHashMap.put(identifier, gui);
    }

    public GuiScreen enableRepeats()
    {
        return this.setRepeats(true);
    }

    public GuiScreen disableRepeats()
    {
        return this.setRepeats(false);
    }

    public GuiScreen setRepeats(boolean repeats)
    {
        this.repeats = repeats;
        Keyboard.enableRepeatEvents(repeats);
        return this;
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    public void removeElement(String identifier)
    {
        if (this.guiHashMap.containsKey(identifier))
            this.guiHashMap.remove(identifier);
    }

    public void drawCenteredTitle()
    {
        String translatedTitle = TranslateHelper.translate(this.getTitle());

        int textPosY = 10;

        if (this.drawSubTitle())
            textPosY -= 4;

        float titleWidth = mc.fontRendererObj.getStringWidth("\u00a7l" + translatedTitle);
        float underlineOverflow = 5F;
        float underlinePosX = (width / 2) - (titleWidth / 2) - underlineOverflow;

        this.drawCenteredString(mc.fontRendererObj, "§l" + translatedTitle, this.width / 2, textPosY, 0xFFFFFF);

        if (this.drawSubTitle())
            this.drawCenteredSubTitle();

//        DrawingHelper.drawQuad(underlinePosX + 1, textPosY + 9, titleWidth + (underlineOverflow * 2), 1, Color.DK_GREY, 1F);
//        DrawingHelper.drawQuad(underlinePosX, textPosY + 8, titleWidth + (underlineOverflow * 2), 1, Color.WHITE, 1F);

        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        helper.drawShape(new Quad(underlinePosX + 1, textPosY + 9, titleWidth + (underlineOverflow * 2), 1).setColor(Color.DK_GREY));
        helper.drawShape(new Quad(underlinePosX, textPosY + 8, titleWidth + (underlineOverflow * 2), 1).setColor(Color.WHITE));
    }

    public String getTitle()
    {
        return title;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public GuiScreen setSubtitle(String subtitle)
    {
        this.subtitle = subtitle;
        if (this.topBarHeight < 30)
            this.topBarHeight = 30;
        return this;
    }

    public void drawCenteredSubTitle()
    {
        String translatedSubTitle = TranslateHelper.translate(this.getSubtitle());
        int textPosY = this.topBarHeight / 2 - 10;
        this.drawCenteredString(mc.fontRendererObj, translatedSubTitle, this.width / 2, textPosY + 10, 0xFFFFFF);
    }

    public boolean drawSubTitle()
    {
        return !this.subtitle.equals("");
    }

    public void setTopBarColor(Color topBarColor)
    {
        this.topBarColor = topBarColor;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // TODO: Fix Done button

        super.drawScreen(mouseX, mouseY, partialTicks);

        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        if (this.showTopBar)
        {
            helper.drawShape(new Quad(0, 0, width, getTopBarHeight() - 1).setColor(topBarColor).setAlpha(0.5F));
            helper.drawShape(new Quad(0, getTopBarHeight() - 1, width, 1).setColor(Color.WHITE));
            if (this.drawCenteredTitle)
                this.drawCenteredTitle();
            GL11.glTranslatef(0, topBarHeight, 0);
        }

        if (this.showBottomBar)
        {
            helper.drawShape(new Quad(0, height - getTopBarHeight() - getBottomBarHeight() + 1, width, getBottomBarHeight() - 1).setColor(bottomBarColor).setAlpha(0.5F));
            helper.drawShape(new Quad(0, height - getTopBarHeight() - getBottomBarHeight(), width, 1).setColor(Color.WHITE));
        }

        /*if (this.useDefaultDoneButton)
        {
            ((GuiSimpleButton) this.getGui("done_button")).setY(height - barHeight + (barHeight / 2) - 9);
            ((GuiSimpleButton) this.getGui("done_button")).setX((barHeight / 2) - 10);
            this.getGui("done_button").draw(mc, mouseX, actualMouseY);
        }*/

        int mouseYOffset = 0;
        if (this.showTopBar)
            mouseYOffset += topBarHeight;

        List<IDrawable> drawables = new ArrayList<IDrawable>();

        int currentMouseX = mouseX, currentMouseY = mouseY;
        if (this.openMenu != null && this.openMenu.isMouseHovering(mouseX, mouseY - mouseYOffset))
            currentMouseX = currentMouseY = -1000;

        for (Map.Entry<String, IGui> entry : guiHashMap.entrySet())
        {
            String identifier = entry.getKey();
            IGui element = entry.getValue();

            if (this.drawGui(identifier, element, mouseX, mouseY - mouseYOffset) && !identifier.equals("done_button"))
            {
                element.draw(mc, currentMouseX, currentMouseY - mouseYOffset);
                if (element instanceof IDelayedDraw)
                {
                    drawables.add(((IDelayedDraw) element).getDelayedDrawable());
                }
            }
        }

        if (drawables.size() > 0)
        {
            for (IDrawable drawable : drawables)
                drawable.draw(mc, mouseX, mouseY);
        }

        if (this.openMenu != null)
            this.openMenu.draw(mc, mouseX, mouseY - mouseYOffset);

        this.mouseXAtLastFrame = mouseX;
        this.mouseYAtLastFrame = mouseY;
    }

    public int getTopBarHeight()
    {
        return topBarHeight;
    }

    public void setTopBarHeight(int topBarHeight)
    {
        this.topBarHeight = topBarHeight;
    }

    public int getBottomBarHeight()
    {
        return bottomBarHeight;
    }

    public void setBottomBarHeight(int bottomBarHeight)
    {
        this.bottomBarHeight = bottomBarHeight;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        for (Map.Entry<String, IGui> entry : guiHashMap.entrySet())
        {
            String identifier = entry.getKey();
            IGui element = entry.getValue();

            if (identifier.equals("done_button"))
                element.update();
            else if (this.updateElement(identifier, element))
                element.update();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        for (Map.Entry<String, IGui> entry : this.guiHashMap.entrySet())
        {
            String identifier = entry.getKey();
            IGui gui = entry.getValue();

            if (this.listensForKey(identifier, gui))
                gui.handleKeyTyped(keyCode, typedChar);
        }
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        int dWheel = Mouse.getDWheel();
        for (IGui iGui : this.guiHashMap.values())
            if (iGui instanceof IMouseListener)
            {
                int mouseY = mouseYAtLastFrame;
                if (this.showTopBar)
                    mouseY -= getTopBarHeight();
                ((IMouseListener) iGui).handleMouseWheel(mouseXAtLastFrame, mouseY, dWheel);
            }
        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        try
        {
            int mouseYOffset = 0;
            if (this.showTopBar)
                mouseYOffset += topBarHeight;

            if (this.openMenu != null)
                if (this.openMenu.mouseDown(mouseX, mouseY - mouseYOffset, mouseButton))
                {
                    System.out.println("mouse down true");
                    return;
                }
                else {
                    System.out.println("mouse down false");
                    closeMenu();
                }
            for (Map.Entry<String, IGui> entry : this.guiHashMap.entrySet())
            {
                String identifier = entry.getKey();
                IGui gui = entry.getValue();
                if (gui.mouseDown(mouseX, mouseY - mouseYOffset, mouseButton))
                    this.guiClicked(identifier, gui, mouseX, mouseY, mouseButton);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException
    {
        if (Keyboard.getEventKeyState())
        {
            this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        for (Map.Entry<String, IGui> entry : this.guiHashMap.entrySet())
        {
            int yOffset = this.showTopBar ? getTopBarHeight() : 0;
            IGui gui = entry.getValue();
            gui.mouseUp(mouseX, mouseY - yOffset, state);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        for (Map.Entry<String, IGui> entry : this.guiHashMap.entrySet())
        {
            int yOffset = this.showTopBar ? getTopBarHeight() : 0;
            IGui gui = entry.getValue();
            gui.mouseClickMove(mouseX, mouseY - yOffset, clickedMouseButton, timeSinceLastClick);
        }
    }

    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {

    }

    public GuiScreen hideDoneButton()
    {
        this.useDefaultDoneButton = false;
        return this;
    }

    public void replaceGui(String identifier, IGui gui)
    {
        this.guiHashMap.put(identifier, gui);
    }

    public boolean listensForKey(String identifier, IGui gui)
    {
        return true;
    }

    public GuiScreen hideTopBar()
    {
        this.showTopBar = false;
        return this;
    }

    public GuiScreen hideBottomBar()
    {
        this.showBottomBar = false;
        return this;
    }

    public GuiScreen hideTitle()
    {
        this.drawCenteredTitle = false;
        return this;
    }

    public IGui getGui(String identifier)
    {
        if (this.guiHashMap.containsKey(identifier))
            return this.guiHashMap.get(identifier);
        else return null;
    }

    public boolean drawGui(String identifier, IGui gui, int mouseX, int mouseY)
    {
        return true;
    }

    public boolean updateElement(String identifier, IGui gui)
    {
        return true;
    }

    public Color getBottomBarColor()
    {
        return bottomBarColor;
    }

    public Color getTopBarColor()
    {
        return topBarColor;
    }

    public void setBottomBarColor(Color bottomBarColor)
    {
        this.bottomBarColor = bottomBarColor;
    }
}
