package dk.mrspring.music.gui.screen.overlay;

import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.effect.EffectMessage;
import dk.mrspring.music.gui.GuiCustomTextField;
import dk.mrspring.music.gui.GuiSimpleButton;
import dk.mrspring.music.player.Playlist;
import dk.mrspring.music.util.TranslateHelper;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import static dk.mrspring.llcore.DrawingHelper.HorizontalTextAlignment.TOP;

/**
 * Created by Konrad on 11-08-2015.
 */
public class CardRenamePlaylist extends Card
{
    PlaylistRenamed onRenamed = null;
    GuiCustomTextField nameField;
    Playlist renaming;

    public CardRenamePlaylist(OverlayParent overlayParent, Playlist renaming)
    {
        super(overlayParent);

        parent.addCard(new NameCard(parent));
        parent.addCard(new ButtonCard(parent));

        this.nameField = new GuiCustomTextField(0, 0, 100, 20, renaming.getName()).setGhost("New Playlist Name...");
        this.renaming = renaming;
    }

    public CardRenamePlaylist setOnRenamed(PlaylistRenamed onRenamed)
    {
        this.onRenamed = onRenamed;
        return this;
    }

    private class NameCard extends Card
    {
        public NameCard(OverlayParent overlayParent)
        {
            super(overlayParent);
        }

        @Override
        public int getHeight()
        {
            return nameField.getH() + 55;
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY, float partialTicks)
        {
            int width = parent.getOverlayWidth();
            GL11.glPushMatrix();
            GL11.glTranslatef(width / 2, 0, 0);
            float s = 2F;
            GL11.glScalef(s, s, s);
            LiteModMusicPlayer.core.getDrawingHelper().drawText("New Playlist", new Vector(0, 3), 0xFFFFFF, true, width / (int) s, DrawingHelper.VerticalTextAlignment.CENTER, TOP);
            GL11.glPopMatrix();
            int p = 30;
            int oWidth = parent.getOverlayWidth();
            LiteModMusicPlayer.core.getDrawingHelper().drawHorizontalLine(new Vector(p / 3, 35), oWidth - (p / 3 * 2), 1, true);
            nameField.setW(oWidth - p);
            nameField.setX(p / 2);
            nameField.setY(45);
            DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
            double z = helper.getZIndex();
            helper.setZIndex(z + 10);
            nameField.draw(minecraft, mouseX, mouseY, partialTicks);
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
    }

    private class ButtonCard extends Card
    {
        GuiSimpleButton button;

        public ButtonCard(OverlayParent overlayParent)
        {
            super(overlayParent);
            button = new GuiSimpleButton(0, 0, 100, 20, "Create");
        }

        @Override
        public boolean drawBackdrop()
        {
            return false;
        }

        @Override
        public int getHeight()
        {
            return button.getHeight();
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY, float partialTicks)
        {
            int width = parent.getMinecraft().fontRendererObj.getStringWidth("Create") + 6;
            button.setX(parent.getOverlayWidth() / 2 - (width / 2));
            button.setWidth(width);
            button.draw(minecraft, mouseX, mouseY, partialTicks);
        }

        @Override
        public void update()
        {
            button.setEnabled(!nameField.getText().trim().isEmpty());
            button.update();
        }

        @Override
        public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
        {
            if (button.mouseDown(mouseX, mouseY, mouseButton))
            {
                renaming.setName(nameField.getText());
                parent.closeOverlay();
                String message =
                        TranslateHelper.translateFormat("gui.message.on_playlist_renamed", renaming.getName());
                EffectMessage messageEffect = new EffectMessage(LiteModMusicPlayer.effects, message);
                messageEffect.stopHeight = 1;
                LiteModMusicPlayer.effects.addEffect(messageEffect);
                return true;
            } else return false;
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

        }
    }

    @Override
    public int getHeight()
    {
        return 0;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY, float partialTicks)
    {

    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        return false;
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

    }

    public interface PlaylistRenamed
    {
        void onRenamed(Playlist renamed);
    }
}
