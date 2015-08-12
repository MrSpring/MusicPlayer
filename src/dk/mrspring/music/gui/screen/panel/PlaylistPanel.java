package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiPlaylist;
import dk.mrspring.music.gui.screen.GuiScreen;
import dk.mrspring.music.player.Playlist;
import dk.mrspring.music.util.GuiUtils;
import dk.mrspring.music.util.MenuUtils;
import dk.mrspring.music.util.Miscellaneous;
import dk.mrspring.music.util.TranslateHelper;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 04-07-2015 for MC Music Player.
 */
public class PlaylistPanel extends GuiPlaylist implements IPanel
{
    double target = 0D;
    double progress = 0D;
    IPanelContainer parent;

    public PlaylistPanel(Playlist music)
    {
        super(0, 0, 100, 100, music);
    }

    @Override
    public void setParent(IPanelContainer parent)
    {
        this.parent = parent;
    }

    @Override
    public void preDraw(int mouseX, int mouseY)
    {
        if (isMovingInDeleteZone() ||
                (LiteModMusicPlayer.config.show_playlist_remove_tip &&
                        (isMouseInDeleteZone(mouseX, mouseY) || isMoving())))
            target = 1D;
        else target = 0D;
        progress = Miscellaneous.smoothDamp(target, progress, 0.4D);
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        super.draw(minecraft, mouseX, mouseY);

        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        String playlistName = getPlaylist().getName();
        int top = getTopBarOffset();
        DrawingHelper.TextRenderResult result = helper.drawCenteredText(playlistName,
                new Vector(x() + (width() / 2), y() - top + 3), DrawingHelper.HorizontalTextAlignment.TOP);
        GuiUtils.drawOptionsIcon(x() + 3 + (width() + result.getLongestLine()) / 2, y() - top + 3, result.getTotalHeight(), result.getTotalHeight());

        float extraSize = 10F;
        float pg = (float) progress;
        float iconSize = 20 + (float) (extraSize * progress);
        helper.drawIcon(LiteModMusicPlayer.core.getIcon("trash_can"), new Quad(
                ((float) (x() + (width() / 2))) - (iconSize / 2F),
                ((float) (y() + height() + 5)) + (2 * pg), iconSize, iconSize));
        if (pg > 0.9F)
            helper.drawCenteredText(TranslateHelper.translateFormat("gui.playlist_editor.remove",
                            getPlaylist().getName()),
                    new Vector(x() + (width() / 2), iconSize + ((float) (y() + height() + 2)) + ((extraSize * pg))));


//        helper.drawIcon(LiteModMusicPlayer.core.getIcon("trash_can"), new Quad(x() + (width() / 2), y() + height(), 30, 30));
//        float bigIconExtra = 10;
//        float iconSize = 20 + (float) (bigIconExtra * progress);
//        float bottomBarOffset = (float) (25D * progress);
//        LiteModMusicPlayer.core.getDrawingHelper().drawIcon(LiteModMusicPlayer.core.getIcon("trash_can"), new Quad(((float) x()) + (((float) width()) / 2F) - (iconSize / 2F), ((float) (y() + height())) + (bottomBarOffset / 2F) + /*(iconSize / 2) -*/5F - (float) (progress * 6D), iconSize, iconSize));

//        if (pg > 0.9F)
//            LiteModMusicPlayer.core.getDrawingHelper().drawText(TranslateHelper.translateFormat("gui.playlist_editor.remove", getPlaylist().getName()), new Vector(x() + width() / 2, y() + height() - (bottomBarOffset / 2) + 3 + (iconSize / 2)), 0xFFFFFF, true, width() - 20, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.CENTER);
    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (mouseY < y() && mouseY > y() - getTopBarOffset())
            if (mouseButton == 0)
            {
                int textW = parent.getMinecraft().fontRendererObj.getStringWidth(getPlaylist().getName());
                int textOffset = (width() + textW) / 2;
                if (mouseX > x() + textOffset && mouseX < x() + textOffset + 16)
                {
                    openMenu();
                    return true;
                }
            } else if (mouseButton == 1)
            {
                openMenu();
                return true;
            }
        return super.mouseDown(mouseX, mouseY, mouseButton);
    }

    @Override
    public void removeMusic(int id)
    {
        super.removeMusic(id);
        LiteModMusicPlayer.config.show_playlist_remove_tip = false;
    }

    private void openMenu()
    {
        MenuUtils.MenuResult result = MenuUtils.createPlaylistMenu((GuiScreen) parent, getPlaylist());
        parent.openMenu(result.action, result.items);
    }

    @Override
    public int getBottomBarOffset()
    {
        return (int) (30D * progress);
    }

    @Override
    public int getTopBarOffset()
    {
        return 20;
    }

    @Override
    public Color getBottomBarColor()
    {
        return Color.morph(Color.BLACK, Color.RED, (float) progress);
    }

    @Override
    public Color getTopBarColor()
    {
        return Color.BLACK;
    }
}
