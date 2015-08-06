package dk.mrspring.music.util;

import dk.mrspring.music.LiteModMusicPlayer;
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

    private static final String PLAY_NEXT = "gui.music.main.right_click.music.play_next";
    private static final String ADD_TO_QUEUE = "gui.music.main.right_click.music.add_to_queue";
    private static final String ADD_TO_PLAYLIST = "gui.music.main.right_click.music.add_to_playlist";
    private static final String ADD_TO_NEW_PLAYLIST = "gui.music.main.right_click.music.add_to_playlist.new";
    private static final String ADD_TO_EXISTING_PLAYLIST = "gui.music.main.right_click.music.add_to_playlist.existing";

    public static MenuResult createMusicMenu(final GuiScreen previous, final Object... clicked/*final Music clicked*/)
    {
        FontRenderer mc = Minecraft.getMinecraft().fontRendererObj;
        MenuResult playlistItemResults = getAddToPlaylistMenu(previous, clicked);
        Menu.MenuAction action = new Menu.MenuActionParent(playlistItemResults.action)
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
                            LiteModMusicPlayer.musicHandler.getQueue().tryAdd(clicked);
                            break;
                        case ADD_TO_QUEUE_INDEX:
                            LiteModMusicPlayer.musicHandler.getQueue().tryAdd(clicked);
                            break;
                        case ADD_TO_PLAYLIST_INDEX:
                            if (!(clickedItem instanceof MenuItemSubMenu) || !(pressedItems.length > 1 &&
                                    pressedItems[1] instanceof IndexedMenuItem))
                                break;
                            runChildAction(offset(1, pressedItems));
                            break;
                    }
                }
            }
        };

        return new MenuResult(action, new MenuItemButton(t(PLAY_NEXT, cmm(clicked)), mc, 0),
                new MenuItemButton(t(ADD_TO_QUEUE, cmm(clicked)), mc, 1),
                new MenuItemSpacer(),
                new MenuItemSubMenu(t(ADD_TO_PLAYLIST, cmm(clicked)), mc, 2, playlistItemResults.items));
    }

    public static MenuResult getAddToPlaylistMenu(final GuiScreen previous, final Object... adding/*final Music adding*/)
    {
        FontRenderer mc = Minecraft.getMinecraft().fontRendererObj;
        List<Playlist> playlistList = LiteModMusicPlayer.musicHandler.getPlaylists();
        IMenuItem[] playlistItems = new IMenuItem[playlistList.size() + 2];
        playlistItems[0] = new MenuItemButton(t(ADD_TO_NEW_PLAYLIST, cmm(adding)), mc, 0);
        playlistItems[1] = new MenuItemSpacer();
        for (int i = 0; i < playlistList.size(); i++)
        {
            Playlist playlist = playlistList.get(i);
            playlistItems[i + 2] = new MenuItemButton(t(ADD_TO_EXISTING_PLAYLIST, cmm(adding), playlist.getName()), mc, playlist);
        }
        Menu.MenuAction action = new Menu.MenuAction()
        {
            @Override
            public void onAction(IMenuItem... pressedItems)
            {
                Object id = ((IndexedMenuItem) pressedItems[0]).getIdentifier();
                if (id instanceof Integer)
                {
                    OverlayScreen overlay = new OverlayScreen("Create Playlist", previous);
                    overlay.addCard(new CardNewPlaylist(overlay, new CardNewPlaylist.PlaylistCreated()
                    {
                        @Override
                        public void onCreated(Playlist created)
                        {
                            created.tryAdd(adding);
                            LiteModMusicPlayer.musicHandler.registerPlaylist(created);
                        }
                    }));
                    Minecraft.getMinecraft().displayGuiScreen(overlay);
                } else if (id instanceof Playlist)
                {
                    Playlist addingTo = ((Playlist) id);
                    addingTo.tryAdd(adding);
                }
            }
        };
        return new MenuResult(action, playlistItems);
    }

    private static String cmm(Object... objects) // Create Menu Message
    {
        if (objects == null || objects.length == 0) return "unknown-empty";
        StringBuilder builder = new StringBuilder(name(objects[0]));
        if (objects.length == 1) return builder.toString();
        for (int i = 1; i < objects.length; i++)
        {
            Object object = objects[i];
            if (i + 1 < objects.length) builder.append(", ");
            else builder.append(" & ");
            builder.append(name(object));
        }
        return builder.toString();
    }

    private static String cm(int width, Object... objects) // Create Message, width sensitive
    {
        if (objects == null || objects.length == 0) return "unknown-empty";
        StringBuilder builder = new StringBuilder(name(objects[0]));
        if (objects.length == 1) return builder.toString();
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer renderer = mc.fontRendererObj;
        int previousLength = 0;
        for (Object object : objects)
        {
            String named = name(object);
            int preAppend = builder.length();
            builder.append(named);
            String built = builder.toString();
            if (renderer.getStringWidth(built) > width)
            {
                builder.delete(preAppend, builder.length()).append("...");
                break;
            }
        }
        builder.insert(previousLength, " & ");
        return builder.toString();
        /*if (objects == null || objects.length == 0) return "unknown-empty";
        String current = name(objects[0]);
        String translatedBase = t(translating"gui.music.main.right_click.music.play_now");
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer renderer = mc.fontRendererObj;
        int objectsIndex = 1;
        ScaledResolution screenSize = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int mcDisplayWidth = screenSize.getScaledWidth() - 10;
        while (renderer.getStringWidth(String.format(translatedBase, current + "...")) < mcDisplayWidth &&
                objectsIndex < objects.length)
        {
            Object currentIndex = objects[objectsIndex];
            objectsIndex++;
            String name = name(currentIndex);
            current += ", " + name;
        }
        if (objectsIndex < objects.length) current += "...";
        return String.format(translatedBase, current);*/
    }

    private static String name(Object object)
    {
        if (object==null) return "unknown-null";
        else return Music.class.cast(object).getName();//((Music)object).getName();//object.getClass().toString();
        /*if (object == null) return "unknown-null";
        if (object instanceof Music) return ((Music) object).getName();
        if (object instanceof Album) return ((Album) object).getAlbumName();
        if (object instanceof Artist) return ((Artist) object).getArtistName();
        if (object instanceof Playlist) return ((Playlist) object).getName();
        if (object instanceof String) return (String) object;
        return "unknown-type";*/
    }

    private static String t(String translating, Object... format)
    {
        if (format == null || format.length == 0) return TranslateHelper.translate(translating);
        else return TranslateHelper.translateFormat(translating, format);
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
