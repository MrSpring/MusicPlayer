package dk.mrspring.music.gui.menu;

/**
 * Created by Konrad on 30-07-2015.
 */
public class MenuItemSpacer implements IMenuItem
{
    @Override
    public void initializeSize()
    {

    }

    @Override
    public void update()
    {

    }

    @Override
    public void draw(int mouseX, int mouseY, int width)
    {

    }

    @Override
    public int getHeight()
    {
        return 7;
    }

    @Override
    public IMenuItem[] getClickedItems(int mouseX, int mouseY, int mouseButton)
    {
        return new IMenuItem[0];
    }

    @Override
    public int getMinWidth()
    {
        return 0;
    }

    @Override
    public boolean mouseDown(int relMouseX, int relMouseY, int mouseButton)
    {
        return false;
    }

    @Override
    public boolean isMouseHovering(int mouseX, int mouseY, int width)
    {
        return false;
    }
}
