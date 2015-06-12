package dk.mrspring.music.gui.menu;

/**
 * Created by Konrad on 07-06-2015.
 */
public interface IMenuItem
{
    void initializeSize();

    void update();

    void draw(int mouseX, int mouseY, int width);

    int getHeight();

    IMenuItem[] getClickedItems(int mouseX, int mouseY, int mouseButton);

    /**
     * @return Returns the minimum width this menu item needs.
     */
    int getMinWidth();

    boolean mouseDown(int relMouseX, int relMouseY, int mouseButton);
}
