package dk.mrspring.music.gui.screen.overlay;

import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.effect.EffectMessage;
import dk.mrspring.music.gui.GuiSimpleButton;
import dk.mrspring.music.player.Playlist;
import dk.mrspring.music.util.TranslateHelper;
import net.minecraft.client.Minecraft;

/**
 * Created by Konrad on 11-08-2015.
 */
public class CardDeletePlaylist extends Card
{
    Playlist deleting;

    public CardDeletePlaylist(OverlayParent overlayParent, Playlist deleting)
    {
        super(overlayParent);

        parent.addCard(new ButtonCard(overlayParent));

        this.deleting = deleting;
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

    private class ButtonCard extends Card
    {
        GuiSimpleButton yes, no;

        public ButtonCard(OverlayParent overlayParent)
        {
            super(overlayParent);
            yes = new GuiSimpleButton(0, 0, 100, 20, "Yes");
            no = new GuiSimpleButton(0, 0, 100, 20, "No");
        }

        @Override
        public boolean drawBackdrop()
        {
            return false;
        }

        @Override
        public int getHeight()
        {
            return yes.getHeight();
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY, float partialTicks)
        {
//            int width = parent.getMinecraft().fontRendererObj.getStringWidth("Create") + 6;
            int third = parent.getOverlayWidth() / 3;
            yes.setWidth(third);
            no.setWidth(third);

            yes.setX(third / 2);
            no.setX((third / 2) + (2 * third));

            yes.draw(minecraft, mouseX, mouseY, partialTicks);
            no.draw(minecraft, mouseX, mouseY, partialTicks);
        }

        @Override
        public void update()
        {
//            button.setEnabled(!nameField.getText().trim().isEmpty());
//            button.update();
            yes.update();
            no.update();
        }

        @Override
        public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
        {
            if (yes.mouseDown(mouseX, mouseY, mouseButton))
            {
                LiteModMusicPlayer.musicHandler.removePlaylist(deleting);
                parent.closeOverlay();
                String message =
                        TranslateHelper.translateFormat("gui.message.on_playlist_deleted", deleting.getName());
                EffectMessage messageEffect = new EffectMessage(LiteModMusicPlayer.effects, message);
                messageEffect.stopHeight = 1;
                LiteModMusicPlayer.effects.addEffect(messageEffect);
                return true;
            } else if (no.mouseDown(mouseX, mouseY, mouseButton))
            {
                parent.closeOverlay();
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
}
