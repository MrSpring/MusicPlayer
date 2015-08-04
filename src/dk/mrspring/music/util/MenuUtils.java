package dk.mrspring.music.util;

import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.effect.EffectMessage;
import dk.mrspring.music.gui.menu.*;
import dk.mrspring.music.gui.screen.overlay.CardNewPlaylist;
import dk.mrspring.music.gui.screen.overlay.OverlayScreen;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.player.Playlist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

/**
 * Created by Konrad on 04-08-2015.
 */
public class MenuUtils
{
    private static final int PLAY_NEXT_INDEX = 0;
    private static final int ADD_TO_QUEUE_INDEX = 1;
    private static final int ADD_TO_PLAYLIST_INDEX = 2;

    public static MenuResult createMusicMenu(final Music clicked, final GuiScreen previous)
    {
        FontRenderer mc = Minecraft.getMinecraft().fontRendererObj;
        List<Playlist> playlistList = LiteModMusicPlayer.musicHandler.getPlaylists();
        IMenuItem[] playlistItems = new IMenuItem[playlistList.size() + 2];
        playlistItems[0] = new MenuItemButton("Create New Playlist", mc, 0);
        playlistItems[1] = new MenuItemSpacer();
        for (int i = 0; i < playlistList.size(); i++)
        {
            Playlist playlist = playlistList.get(i);
            playlistItems[i + 2] = new MenuItemButton(playlist.getName(), mc, playlist);
        }
        Menu.MenuAction action = new Menu.MenuAction()
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
                            LiteModMusicPlayer.musicHandler.getQueue().addNext(clicked);
                            break;
                        case ADD_TO_QUEUE_INDEX:
                            LiteModMusicPlayer.musicHandler.getQueue().add(clicked);
                            break;
                        case ADD_TO_PLAYLIST_INDEX:
                            if (!(clickedItem instanceof MenuItemSubMenu) || !(pressedItems.length > 1 &&
                                    pressedItems[1] instanceof IndexedMenuItem))
                                break;
                            Object id = ((IndexedMenuItem) pressedItems[1]).getIdentifier();
                            if (id instanceof Integer)
                            {
                                OverlayScreen overlay = new OverlayScreen("Create Playlist", previous);
                                overlay.addCard(new CardNewPlaylist(overlay, new CardNewPlaylist.PlaylistCreated()
                                {
                                    @Override
                                    public void onCreated(Playlist created)
                                    {
                                        created.add(clicked);
                                        LiteModMusicPlayer.musicHandler.registerPlaylist(created);
                                        String playlistName = created.getName(), message = TranslateHelper.
                                                translateFormat("gui.message.on_playlist_created", playlistName);
                                        EffectMessage messageEffect = new EffectMessage
                                                (LiteModMusicPlayer.effects, message);
                                        messageEffect.stopHeight = 1;
                                        LiteModMusicPlayer.effects.addEffect(messageEffect);
                                    }
                                }));
                                Minecraft.getMinecraft().displayGuiScreen(overlay);
                            } else if (id instanceof Playlist)
                                ((Playlist) id).add(clicked);
                            break;
                    }
                }
            }
        };

        return new MenuResult(action, new MenuItemButton("Play Next", mc, 0),
                new MenuItemButton("Add to Queue", mc, 1),
                new MenuItemSpacer(),
                new MenuItemSubMenu("Add to Playlist...", mc, 2, playlistItems));
    }

    public static class MenuResult
    {
        public IMenuItem[] items;
        public Menu.MenuAction action;

        public MenuResult(Menu.MenuAction action, IMenuItem... items)
        {
            this.items = items;
            this.action = action;
        }
    }
}
