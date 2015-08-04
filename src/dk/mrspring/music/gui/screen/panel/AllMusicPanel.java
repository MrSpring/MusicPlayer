package dk.mrspring.music.gui.screen.panel;

import dk.mrspring.llcore.Color;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiAllMusicList;
import dk.mrspring.music.gui.menu.*;
import dk.mrspring.music.gui.screen.overlay.CardMusic;
import dk.mrspring.music.gui.screen.overlay.CardNewPlaylist;
import dk.mrspring.music.gui.screen.overlay.OverlayScreen;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.player.Playlist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

/**
 * Created by MrSpring on 04-07-2015 for MC Music Player.
 */
public class AllMusicPanel extends GuiAllMusicList implements IPanel
{
    IPanelContainer parent;
    long lastClick = 0;

    private static final int PLAY_NEXT_INDEX = 0;
    private static final int ADD_TO_QUEUE_INDEX = 1;
    private static final int ADD_TO_PLAYLIST_INDEX = 2;

    public AllMusicPanel(List<Music> allMusic)
    {
        super(0, 0, 100, 100, allMusic);
    }

    @Override
    protected boolean onElementClicked(int relMouseX, int relMouseY, int mouseX, int mouseY, int mouseButton, final Music clicked)
    {
        if (mouseButton == 0)
        {
            long currentTime = System.currentTimeMillis();
            long diff = currentTime - lastClick;
            if (diff < LiteModMusicPlayer.config.double_click_time)
            {
                OverlayScreen overlay = new OverlayScreen("Music Details", (GuiScreen) parent);
                overlay.addCard(new CardMusic(overlay, clicked));
                parent.getMinecraft().displayGuiScreen(overlay);
            }
            lastClick = currentTime;
            return true;

        } else if (mouseButton == 1)
        {
            FontRenderer mc = parent.getMinecraft().fontRendererObj;
            List<Playlist> playlistList = LiteModMusicPlayer.musicHandler.getPlaylists();
            IMenuItem[] playlistItems = new IMenuItem[playlistList.size() + 2];
            playlistItems[0] = new MenuItemButton("Create New Playlist", mc, 0);
            playlistItems[1] = new MenuItemSpacer();
            for (int i = 0; i < playlistList.size(); i++)
            {
                Playlist playlist = playlistList.get(i);
                playlistItems[i + 2] = new MenuItemButton(playlist.getName(), mc, playlist);
            }
            parent.openMenu(mouseX, mouseY, new Menu.MenuAction()
                    {
                        @Override
                        public void onAction(IMenuItem... pressedItems)
                        {
                            if (pressedItems.length > 0 && pressedItems[0] != null &&
                                    pressedItems[0] instanceof IndexedMenuItem)
                            {
                                IndexedMenuItem clickedItem = (IndexedMenuItem) pressedItems[0];
                                System.out.println(clickedItem.getClass().getName());
                                int clickedIndex = (Integer) clickedItem.getIdentifier();
                                switch (clickedIndex)
                                {
                                    case PLAY_NEXT_INDEX:
                                        System.out.println("Play next");
                                        LiteModMusicPlayer.musicHandler.getQueue().addNext(clicked);
                                        break;
                                    case ADD_TO_QUEUE_INDEX:
                                        System.out.println("Add to queue");
                                        LiteModMusicPlayer.musicHandler.getQueue().add(clicked);
                                        break;
                                    case ADD_TO_PLAYLIST_INDEX:
                                        System.out.println("Something playlist");
                                        if (!(clickedItem instanceof MenuItemSubMenu) || !(pressedItems.length > 1 &&
                                                pressedItems[1] instanceof IndexedMenuItem))
                                            break;
                                        System.out.println("Something playlist");
                                        Object id = ((IndexedMenuItem) pressedItems[1]).getIdentifier();
                                        if (id instanceof Integer)
                                        {
                                            System.out.println("Creating new Playlist");
                                            OverlayScreen overlay = new OverlayScreen("Create Playlist", (GuiScreen) parent);
                                            overlay.addCard(new CardNewPlaylist(overlay, new CardNewPlaylist.PlaylistCreated()
                                            {
                                                @Override
                                                public void onCreated(Playlist created)
                                                {
                                                    created.add(clicked);
                                                    LiteModMusicPlayer.musicHandler.registerPlaylist(created);
                                                }
                                            }));
                                            Minecraft.getMinecraft().displayGuiScreen(overlay);
                                        } else if (id instanceof Playlist)
                                        {
                                            System.out.println("Adding to playlist: "+((Playlist)id).getName());
                                            ((Playlist) id).add(clicked);
                                        }
                                        break;
                                }
                                /*if (pressedItems[0] != null && pressedItems[0] instanceof MenuItemSubMenu)
                                {
                                    MenuItemSubMenu subMenu = (MenuItemSubMenu) pressedItems[0];
                                    subMenu
                                }*/
                            }
                        }
                    },
                    new MenuItemButton("Play Next", mc, 0),
                    new MenuItemButton("Add to Queue", mc, 1),
                    new MenuItemSpacer(),
                    new MenuItemSubMenu("Add to Playlist...", mc, 2, playlistItems));
            return true;
        } else return super.onElementClicked(relMouseX, relMouseY, mouseX, mouseY, mouseButton, clicked);
    }

    @Override
    public void setParent(IPanelContainer parent)
    {
        this.parent = parent;
    }

    @Override
    public int getBottomBarOffset()
    {
        return 0;
    }

    @Override
    public int getTopBarOffset()
    {
        return 0;
    }

    @Override
    public Color getBottomBarColor()
    {
        return Color.BLACK;
    }

    @Override
    public Color getTopBarColor()
    {
        return Color.BLACK;
    }

    // TODO: onElementClicked right-click
}
