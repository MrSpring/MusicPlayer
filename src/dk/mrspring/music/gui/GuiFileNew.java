package dk.mrspring.music.gui;

import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.util.TranslateHelper;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 08-12-2014 for In-Game File Explorer.
 */
public class GuiFileNew extends GuiFileBase
{
    GuiSimpleButton createButton, cancelButton;
    GuiCustomTextField nameField;
    INewFileEvents events;

    public GuiFileNew(int x, int y, int w, int h, INewFileEvents events)
    {
        super(x, y, w, h);

        this.nameField = new GuiCustomTextField(x, y, w - 20, h - 10, "").setGhost(TranslateHelper.translate("gui.explorer.new_file_name"));

        this.createButton = new GuiSimpleButton(x + w - 5 - 10, y + (h / 2) - 5, 10, 10, "");
        this.cancelButton = new GuiSimpleButton(x + w - 5, y + (h / 2) - 4, 10, 10, "");

        this.events = events;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        super.draw(minecraft, mouseX, mouseY);

        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();

        this.nameField.draw(minecraft, mouseX, mouseY);

        this.createButton.draw(minecraft, mouseX, mouseY);
        helper.drawIcon(LiteModMusicPlayer.core.getIcon("check_mark"), new Quad(createButton.getX() + ((float) createButton.getWidth() / 2) - 3, createButton.getY() + ((float) createButton.getHeight() / 2) - 3, 6, 6));

        this.cancelButton.draw(minecraft, mouseX, mouseY);
        helper.drawIcon(LiteModMusicPlayer.core.getIcon("cross"), new Quad(cancelButton.getX() + ((float) cancelButton.getWidth() / 2) - 3, cancelButton.getY() + ((float) cancelButton.getHeight() / 2) - 3, 6, 6));
    }

    @Override
    public void update()
    {
        super.update();
        this.createButton.update();
        this.cancelButton.update();
        this.nameField.update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (this.nameField.mouseDown(mouseX, mouseY, mouseButton))
            return true;
        else if (this.createButton.mouseDown(mouseX, mouseY, mouseButton))
            this.events.onCreated(this, nameField.getText());
        else if (this.cancelButton.mouseDown(mouseX, mouseY, mouseButton))
            this.events.onCanceled(this, nameField.getText());
        else return false;
        return true;
    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {
        nameField.handleKeyTyped(keyCode, character);
    }

    @Override
    public void setX(int x)
    {
        super.setX(x);
        this.setPositionAndSize();
    }

    @Override
    public void setY(int y)
    {
        super.setY(y);
        this.setPositionAndSize();
    }

    @Override
    public void setWidth(int w)
    {
        super.setWidth(w);
        this.setPositionAndSize();
    }

    @Override
    public void setHeight(int h)
    {
        super.setHeight(h);
        this.setPositionAndSize();
    }

    private void setPositionAndSize()
    {
        if (h >= 30)
        {
            this.cancelButton.setWidth(h / 2 - 2);
            this.cancelButton.setHeight(h / 2 - 2);
            this.cancelButton.setX(x + w - cancelButton.getWidth());
            this.cancelButton.setY(y + h - cancelButton.getHeight());
            this.createButton.setWidth(h / 2 - 2);
            this.createButton.setHeight(h / 2 - 2);
            this.createButton.setX(x + w - createButton.getWidth());
            this.createButton.setY(y);
            this.nameField.setX(x);
            this.nameField.setY(y);
            this.nameField.setW(w - createButton.getWidth() - 3);
            this.nameField.setH(h);
        } else
        {
            this.createButton.setX(x + w - 22);
            this.createButton.setY(y);
            this.createButton.setHeight(h);
            this.cancelButton.setX(x + w - 10);
            this.cancelButton.setY(y);
            this.cancelButton.setHeight(h);
            this.nameField.setX(x);
            this.nameField.setY(y);
            this.nameField.setW(w - 24);
            this.nameField.setH(h);
        }
    }

    public interface INewFileEvents
    {
        public void onCreated(GuiFileNew guiFileNew, String path);

        public void onCanceled(GuiFileNew guiFileNew, String path);
    }
}
