package dk.mrspring.music.gui.screen;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.Config;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.*;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.gui.interfaces.IResizable;
import dk.mrspring.music.gui.menu.MenuItemButton;
import dk.mrspring.music.gui.screen.overlay.CardMusic;
import dk.mrspring.music.gui.screen.overlay.CardNewPlaylist;
import dk.mrspring.music.gui.screen.overlay.OverlayScreen;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.player.Playlist;
import dk.mrspring.music.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 26-05-2015.
 */
public class GuiScreenAllMusic extends GuiScreen// implements GuiScreenAllMusic.IPanelContainer
{
    private int maxCoverSize = 150;
    private int minCoverSize = 40;
    private double progress = 0D;

    public GuiScreenAllMusic(net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super("All Music", previousScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        Config config = LiteModMusicPlayer.config;

        this.enableRepeats().hideBottomBar();

        int sidePanelSize = config.gui_mm_side_panel_size;

        this.addGuiElement("search_bar", new GuiCustomTextField((width / 3) * 2, -getTopBarHeight() + 3, width / 3, getTopBarHeight() - 6, "").setGhost("Search"));
        this.addGuiElement("size_slider", new GuiSlider(3, -getTopBarHeight() + 3, width / 3, getTopBarHeight() - 6, config.gui_mm_list_entry_size).setShowHover(false));
        this.addGuiElement("back", new GuiSimpleButton(3, height - getBottomBarHeight() - getTopBarHeight() + 3, 60, getTopBarHeight() - 6, "Back"));
        openPanel(new AllMusicPanel(LiteModMusicPlayer.musicHandler.getAllMusic()));//(new ArtistPanel(LiteModMusicPlayer.musicHandler.getAllArtists().get(1), GuiArtistList.Showing.ALBUMS));
        this.addGuiElement("side_panel", new SidePanel(0, 0, sidePanelSize, height - getTopBarHeight() - getBottomBarHeight()));
    }

    public void openPanel(IPanel newPanel)
    {
        int sidePanelSize = LiteModMusicPlayer.config.gui_mm_side_panel_size;
        newPanel.setX(sidePanelSize);
        newPanel.setY(0);
        newPanel.setWidth(width - sidePanelSize);
        newPanel.setHeight(height - getTopBarHeight()-getBottomBarHeight());
        this.replaceGui("panel", newPanel);
    }

    private void updatePanelWidth(int newWidth)
    {
        LiteModMusicPlayer.config.gui_mm_side_panel_size = newWidth;
        IResizable list = (IResizable) getGui("panel");
        list.setWidth(width - newWidth);
        list.setX(newWidth);
    }

    @Override
    public boolean updateElement(String identifier, IGui gui)
    {
        if (identifier.equals("panel") && gui instanceof GuiSquareList)
        {
            GuiSquareList list = (GuiSquareList) gui;
            String newFilter = ((GuiCustomTextField) this.getGui("search_bar")).getText();
            list.setFilter(newFilter);
            int value = ((GuiSlider) this.getGui("size_slider")).getValue();
            LiteModMusicPlayer.config.gui_mm_list_entry_size = value;
            int size = (int) (minCoverSize + ((maxCoverSize - minCoverSize) * ((float) value / 100F)));
            list.setEntryWidthTarget(size);
        }
        return super.updateElement(identifier, gui);
    }

    @Override
    public int getBottomBarHeight()
    {
        return super.getBottomBarHeight();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        float iconSize = getTopBarHeight() - 18;
        helper.drawShape(new Quad(0, 0, width, height).setColor(Color.BLACK).setAlpha(0.5F));
        IResizable resizable = (IResizable) getGui("panel");
        double target = 0;
        if (resizable instanceof GuiPlaylist)
        {
            GuiPlaylist playlist = (GuiPlaylist) resizable;
            if (playlist.isMovingInDeleteZone())
                target = 1D;
            this.progress = Miscellaneous.smoothDamp(target, progress, 0.4);
            playlist.setHeight(height - getTopBarHeight() - getBottomBarHeight());
        } else this.progress = Miscellaneous.smoothDamp(target, progress, 0.4);

        this.setBottomBarHeight(getTopBarHeight() + (int) (25 * progress));
        this.setBottomBarColor(Color.morph(Color.BLACK, Color.RED, (float) progress));

        int panelWidth = ((SidePanel) this.getGui("side_panel")).w;
        int bottomHeight = (int) (25 * progress);

        helper.drawShape(new Quad(panelWidth, height - 29 - bottomHeight, width - panelWidth, bottomHeight).setAlpha(0.5F).setColor(this.getBottomBarColor()));
        helper.drawShape(new Quad(panelWidth, height - 30 - bottomHeight, width - panelWidth, 1));

        helper.drawShape(new Quad(0, height - 29, width, 29).setAlpha(0.5F).setColor(this.getBottomBarColor()));
        helper.drawShape(new Quad(0, height - 30, panelWidth, 1));
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (resizable instanceof GuiPlaylist)
        {
            GuiPlaylist playlist = (GuiPlaylist) resizable;
            float bigIconExtra = 10;
            float size = 20 + (float) (bigIconExtra * progress);
            float xPos = panelWidth + ((width - panelWidth) / 2);
            helper.drawIcon(LiteModMusicPlayer.core.getIcon("trash_can"), new Quad(xPos - (size / 2), height - getTopBarHeight() - (getBottomBarHeight() / 2) - (size / 2) - (int) (progress * 6D), size, size));

            if (getBottomBarHeight() > 35)
                helper.drawText(TranslateHelper.translateFormat("gui.playlist_editor.remove", playlist.getPlaylist().getName()), new Vector(xPos, height - getTopBarHeight() - (getBottomBarHeight() / 2) + 3 + (size / 2)), 0xFFFFFF, true, width - 20, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.CENTER);
        }

        helper.drawIcon(LiteModMusicPlayer.core.getIcon("search"), new Quad(width - iconSize - 5 - (width / 3), -getTopBarHeight() + 8, iconSize, iconSize));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        int clickZone = getTopBarHeight() - 12;
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, width - clickZone - 2 - (width / 3), 5, clickZone, clickZone))
            ((GuiCustomTextField) this.getGui("search_bar")).setFocus(true);
        else super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {
        super.guiClicked(identifier, gui, mouseX, mouseY, mouseButton);
        if (identifier.equals("back"))
            mc.displayGuiScreen(previousScreen);
    }

    public interface IPanel extends IGui, IResizable
    {

    }

    private class ArtistPanel extends GuiArtistList implements IPanel
    {
        public ArtistPanel(Artist artist, Showing type)
        {
            super(0, 0, 100, 100, artist, type);
        }

        @Override
        protected boolean onElementClicked(int relMouseX, int relMouseY, int mouseX, int mouseY, int mouseButton, Object clicked)
        {
            if (mouseButton == 1)
            {
                String text = clicked instanceof Album ? ((Album) clicked).getAlbumName() : (clicked instanceof Music ? ((Music) clicked).getName() : "");
                openMenu(mouseX, mouseY,
                        new MenuItemButton("IT'S WORKING!!!", mc.fontRendererObj, 0),
                        new MenuItemButton(text, mc.fontRendererObj, 1)
                );
                return true;
            } else return super.onElementClicked(relMouseX, relMouseY, mouseX, mouseY, mouseButton, clicked);
        }
    }

    private class AllMusicPanel extends GuiAllMusicList implements IPanel
    {
        public AllMusicPanel(List<Music> allMusic)
        {
            super(0, 0, 100, 100, allMusic);
        }

        @Override
        protected boolean onElementClicked(int relMouseX, int relMouseY, int mouseX, int mouseY, int mouseButton, Music clicked)
        {
            System.out.println("Clicked");
            if (mouseButton == 0)
            {
                OverlayScreen overlay = new OverlayScreen("Music Details", GuiScreenAllMusic.this);
                overlay.addCard(new CardMusic(overlay, clicked));
                mc.displayGuiScreen(overlay);
                return true;
            } else return super.onElementClicked(relMouseX, relMouseY, mouseX, mouseY, mouseButton, clicked);
        }

        // TODO: onElementClicked right-click
    }

    private class AllAlbumsPanel extends GuiAllAlbumsList implements IPanel
    {
        public AllAlbumsPanel(List<Album> allMusic)
        {
            super(0, 0, 100, 100, allMusic);
        }

        // TODO: onElementClicked right-click
    }

    private class AllArtistPanel extends GuiAllArtistsList implements IPanel
    {
        public AllArtistPanel(List<Artist> allMusic)
        {
            super(0, 0, 100, 100, allMusic);
        }

        // TODO: onElementClicked right-click
    }

    private class PlaylistPanel extends GuiPlaylist implements IPanel
    {
        public PlaylistPanel(Playlist music)
        {
            super(0, 0, 100, 100, music);
        }
    }

    public class SidePanel implements IGui
    {
        int x, y, w, h;
        int dragXOffset = -1;
        boolean resizing = false;
        GuiSimpleButton[] buttons;
        GuiSimpleButton[] playlists;
        GuiSimpleButton newPlaylist;

        public SidePanel(int x, int y, int w, int h)
        {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;

            buttons = new GuiSimpleButton[]{
                    new GuiSimpleButton(-2, 0, w + 4, 15, "All Music"),
                    new GuiSimpleButton(-2, 0, w + 4, 15, "Albums"),
                    new GuiSimpleButton(-2, 0, w + 4, 15, "Artists")
            };
            playlistButtons();
            newPlaylist = new GuiSimpleButton(-2, 0, w + 4, 15, "New Playlist");
        }

        private void playlistButtons()
        {
            List<Playlist> lists = LiteModMusicPlayer.musicHandler.getPlaylists();
            List<GuiSimpleButton> simpleButtons = new ArrayList<GuiSimpleButton>();
            for (Playlist list : lists)
            {
                String listName = list.getName();
                GuiSimpleButton button = new GuiSimpleButton(-2, 0, w + 4, 15, listName);
                simpleButtons.add(button);
            }
            playlists = simpleButtons.toArray(new GuiSimpleButton[simpleButtons.size()]);
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY)
        {
            if (resizing)
            {
                this.w = (mouseX - x) - dragXOffset;
                this.w = Math.min(width - maxCoverSize - 10, w);
                this.w = Math.max(20, w);
                updatePanelWidth(w);
            }

            GL11.glPushMatrix();
            GLClippingPlanes.glEnableVerticalClipping(0, h);
            GLClippingPlanes.glEnableHorizontalClipping(x, x + w);

            DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();

            int yOffset = 4;
            yOffset += drawButtons(this.buttons, yOffset, minecraft, mouseX, mouseY, 25);

            if (this.playlists.length > 0)
            {
                yOffset += 6;

                helper.drawShape(new Quad(x + 6, y + yOffset + 4, w - 12 - 1, 1));
                helper.drawShape(new Quad(x + 6, y + yOffset + 5, w - 12 - 1, 1).setColor(Color.DK_GREY));
                yOffset += 20;
                helper.drawText("Playlists", new Vector(x + (w / 2), y + yOffset), 0xFFFFFF, true, width, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.BOTTOM);

                yOffset += 8;

                yOffset += drawButtons(this.playlists, yOffset, minecraft, mouseX, mouseY, 25);
            }

            yOffset += 4;

            helper.drawShape(new Quad(x + 6, y + yOffset, w - 12 - 1, 1));
            helper.drawShape(new Quad(x + 6, y + yOffset + 1, w - 12 - 1, 1).setColor(Color.DK_GREY));

            yOffset += 8;

            newPlaylist.setWidth(w + 4);
            newPlaylist.setHeight(25);
            newPlaylist.setY(y + yOffset);
            newPlaylist.draw(minecraft, mouseX, mouseY);

            GLClippingPlanes.glDisableClipping();
            helper.drawShape(new Quad(x + w - 1, y, 1, h));
            if (GuiHelper.isMouseInBounds(mouseX, mouseY, x + w - 3, y, 5, h) || resizing)
            {
                int iconWidth = 10;

                helper.setZIndex(10D);
                helper.drawShape(new Quad(mouseX - (iconWidth / 2) + 1, mouseY - 10 + 1, iconWidth, 1).setColor(Color.DK_GREY));
                helper.drawShape(new Quad(mouseX - (iconWidth / 2) - 3 + 1, mouseY - 10 + 1, mouseX - (iconWidth / 2) + 1, mouseY - 12 + 1, mouseX - (iconWidth / 2) + 1, mouseY - 7 + 1, mouseX - (iconWidth / 2) - 3 + 1, mouseY - 9 + 1).setColor(Color.DK_GREY));
                helper.drawShape(new Quad(mouseX + (iconWidth / 2) + 1, mouseY - 12 + 1, mouseX + (iconWidth / 2) + 3 + 1, mouseY - 10 + 1, mouseX + (iconWidth / 2) + 3 + 1, mouseY - 9 + 1, mouseX + (iconWidth / 2) + 1, mouseY - 7 + 1).setColor(Color.DK_GREY));

                helper.drawShape(new Quad(mouseX - (iconWidth / 2), mouseY - 10, iconWidth, 1));
                helper.drawShape(new Quad(mouseX - (iconWidth / 2) - 3, mouseY - 10, mouseX - (iconWidth / 2), mouseY - 12, mouseX - (iconWidth / 2), mouseY - 7, mouseX - (iconWidth / 2) - 3, mouseY - 9));
                helper.drawShape(new Quad(mouseX + (iconWidth / 2), mouseY - 12, mouseX + (iconWidth / 2) + 3, mouseY - 10, mouseX + (iconWidth / 2) + 3, mouseY - 9, mouseX + (iconWidth / 2), mouseY - 7));
                helper.setZIndex(0);
            }
            GL11.glPopMatrix();
        }

        private int drawButtons(GuiSimpleButton[] buttons, int offset, Minecraft minecraft, int mouseX, int mouseY, int height)
        {
            int yOffset = 0;
            for (GuiSimpleButton button : buttons)
                if (button != null)
                {
                    button.setWidth(w + 4);
                    button.setHeight(height);
                    button.setY(y + yOffset + offset);
                    button.draw(minecraft, mouseX, mouseY);
                    yOffset += height + 3;
                }
            return yOffset;
        }

        @Override
        public void update()
        {
            for (GuiSimpleButton button : this.buttons) if (button != null) button.update();
            if (playlists.length != LiteModMusicPlayer.musicHandler.getPlaylists().size()) playlistButtons();
            for (GuiSimpleButton button : this.playlists) if (button != null) button.update();
            newPlaylist.update();
        }

        @Override
        public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
        {
            if (GuiHelper.isMouseInBounds(mouseX, mouseY, x + w - 3, y, 5, h))
            {
                this.dragXOffset = mouseX - x - w;
                return resizing = true;
            } else
            {
                if (buttons[0].mouseDown(mouseX, mouseY, mouseButton))
                    openPanel(new AllMusicPanel(LiteModMusicPlayer.musicHandler.getAllMusic()));
                else if (buttons[1].mouseDown(mouseX, mouseY, mouseButton))
                    openPanel(new AllAlbumsPanel(LiteModMusicPlayer.musicHandler.getAllAlbums()));
                else if (buttons[2].mouseDown(mouseX, mouseY, mouseButton))
                    openPanel(new AllArtistPanel(LiteModMusicPlayer.musicHandler.getAllArtists()));
                else
                {
                    for (int i = 0; i < playlists.length; i++)
                    {
                        GuiSimpleButton button = playlists[i];
                        if (button != null && button.mouseDown(mouseX, mouseY, mouseButton))
                        {
                            Playlist list = LiteModMusicPlayer.musicHandler.getPlaylists().get(i);
                            openPanel(new PlaylistPanel(list));
                            return true;
                        }
                    }
                    if (newPlaylist.mouseDown(mouseX, mouseY, mouseButton))
                    {
                        OverlayScreen overlay = new OverlayScreen("New Playlist", GuiScreenAllMusic.this);
                        overlay.addCard(new CardNewPlaylist(overlay, new CardNewPlaylist.PlaylistCreated()
                        {
                            @Override
                            public void onCreated(Playlist created)
                            {
                                LiteModMusicPlayer.musicHandler.registerPlaylist(created);
                                openPanel(new PlaylistPanel(created));
                            }
                        }));
                        mc.displayGuiScreen(overlay);
                        return true;
                    }
                    return false;
                }
                return true;
            }
        }

        @Override
        public void mouseUp(int mouseX, int mouseY, int mouseButton)
        {
            resizing = false;
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
}
