package dk.mrspring.music.gui.interfaces;

import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public interface IGui
{
    void draw(Minecraft minecraft, int mouseX, int mouseY, float partialTicks);

    void update();

    boolean mouseDown(int mouseX, int mouseY, int mouseButton);

    void mouseUp(int mouseX, int mouseY, int mouseButton);

    void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick);

    void handleKeyTyped(int keyCode, char character);
}
