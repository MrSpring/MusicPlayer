package dk.mrspring.music.gui.interfaces;

import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public interface IGui
{
    public void draw(Minecraft minecraft, int mouseX, int mouseY);

    public void update();

    public boolean mouseDown(int mouseX, int mouseY, int mouseButton);

    public void mouseUp(int mouseX, int mouseY, int mouseButton);

    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick);

    public void handleKeyTyped(int keyCode, char character);
}
