package dk.mrspring.music.gui.screen.overlay;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiCustomTextField;
import dk.mrspring.music.player.Playlist;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 02-07-2015 for MC Music Player.
 */
public class CardNewPlaylist extends Card
{
    PlaylistCreated onCreated = null;
    GuiCustomTextField nameField;

    public CardNewPlaylist(OverlayParent overlayParent, PlaylistCreated onCreated)
    {
        super(overlayParent);

        this.onCreated = onCreated;
        this.nameField = new GuiCustomTextField(0, 0, 100, 20, "").setGhost("Enter Playlist Name...");
    }

    @Override
    public int getHeight()
    {
        return nameField.getH() + 20;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        GLClippingPlanes.glDisableClipping();
        nameField.setW(parent.getOverlayWidth());
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        double z = helper.getZIndex();
        helper.setZIndex(z+10);
        nameField.draw(minecraft, mouseX, mouseY);
        helper.setZIndex(z);
    }

    @Override
    public void update()
    {
        nameField.update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        System.out.println("Mouse X: " + mouseX + ", Mouse Y: " + mouseY);
        return nameField.mouseDown(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseUp(int mouseX, int mouseY, int mouseButton)
    {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {

    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {
        nameField.handleKeyTyped(keyCode, character);
    }

    public static interface PlaylistCreated
    {
        public void onCreated(Playlist created);
    }
}
