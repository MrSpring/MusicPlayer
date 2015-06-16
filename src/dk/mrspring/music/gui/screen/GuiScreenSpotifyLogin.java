package dk.mrspring.music.gui.screen;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.NewReleases;
import com.wrapper.spotify.models.SimpleAlbum;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.GuiCustomTextField;
import dk.mrspring.music.gui.GuiSimpleButton;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.overlay.MultilineTextRender;
import net.minecraft.client.gui.FontRenderer;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by MrSpring on 15-06-2015 for MC Music Player.
 */
public class GuiScreenSpotifyLogin extends GuiScreen
{
    int progress = 0;
    ConsoleOutput output;

    public GuiScreenSpotifyLogin(net.minecraft.client.gui.GuiScreen currentScreen)
    {
        super("Spotify Login", currentScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.hideTopBar().hideBottomBar();
        this.addGuiElement("username", new GuiCustomTextField(width / 2 - 100, height / 2 - 20 - 3, 200, 20, "").setGhost("Username"));
        this.addGuiElement("password", new GuiCustomTextField(width / 2 - 100, height / 2, 200, 20, "").setGhost("Password"));
        this.addGuiElement("login", new GuiSimpleButton(width / 2 - 30, height / 2 + 20 + 3, 60, 20, "Login"));
        this.addGuiElement("test", new GuiSimpleButton(width / 2 - 30, height / 2 + 40 + 6, 60, 20, "Login"));
        output = new ConsoleOutput("Gui Opened", mc.fontRendererObj, width / 2 - 110, true, 5);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        progress++;
//        helper.drawText(status, new Vector(width / 2 - 100, height / 2 + 20 + 6 + 20), 0xFFFFFF, true, 200, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        output.render(helper, 5, 5, 0xFFFFFF, true, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        helper.drawShape(new Quad(progress, 5, 10, 10));
    }

    private class ConsoleOutput extends MultilineTextRender
    {
        public ConsoleOutput(String rendering, FontRenderer renderer, int wrapWidth, boolean drawLines, int lineHeight)
        {
            super(rendering, renderer, wrapWidth, drawLines, lineHeight);
        }

        public void addLine(String line)
        {
            this.setText(getText() + "\n" + line);
        }
    }

    @Override
    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {
        if (identifier.equals("login"))
        {
//            synchronized (Thread.class)
//            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Api api = LiteModMusicPlayer.spotifyApi;
                        String[] scopes = new String[]{"user-read-private", "user-read-email"};
                        String scopeText = "Scopes: \"";
                        for (int i = 0; i < scopes.length; i++)
                            if (i + 1 >= scopes.length)
                                scopeText += scopes[i] + "\".";
                            else scopeText += scopes[i] + "\", \"";
                        List<String> scopeList = Arrays.asList(scopes);
                        String state = getUrlContent("http://mrspring.dk/mods/music/spotify_generatestate.php");
                        scopeText += " State: \"" + state + "\".";
                        output.addLine(scopeText);
                        String authorizeUrl = api.createAuthorizeURL(scopeList, state);
                        output.addLine("Got authorize URL: \"" + authorizeUrl + "\".");
                        try
                        {
                            URI uri = new URI(authorizeUrl);
                            Desktop.getDesktop().browse(uri); // TODO: Replace with in-game browser
                            output.addLine("Successfully opened browser window. Waiting for key...");
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        } catch (URISyntaxException e)
                        {
                            e.printStackTrace();
                        }

                        String url = "http://mrspring.dk/mods/music/spotify_getstate.php?state=" + state;
                        String respond = getUrlContent(url);
                        while (respond.equals("waiting"))
                        {
                            try
                            {
                                Thread.sleep(1000);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }

                            respond = getUrlContent(url);
                        }

                        output.addLine("Found key: \"" + respond + "\".");
                    }
                }).start();
//            }

            /*new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    AlbumRequest request = api.getAlbum("7e0ij2fpWaxOEHv5fUYZjd").build();
                    SettableFuture<Album> albumFuture = request.getAsync();
                    Futures.addCallback(albumFuture, new FutureCallback<Album>()
                    {
                        @Override
                        public void onSuccess(Album album)
                        {
                            String albumName = album.getName();
                            output.addLine("Got album: " + albumName);
                        }

                        @Override
                        public void onFailure(Throwable throwable)
                        {
                            output.addLine("Failed:\n" + throwable.getLocalizedMessage());
                        }
                    });
                }
            }).start();*/
        } else if (identifier.equals("test"))
        {
            Api api = LiteModMusicPlayer.spotifyApi;
            SettableFuture<NewReleases> request = api.getNewReleases().build().getAsync();
            Futures.addCallback(request, new FutureCallback<NewReleases>()
            {
                @Override
                public void onSuccess(NewReleases newReleases)
                {
                    List<SimpleAlbum> albums = newReleases.getAlbums().getItems();
                    for (SimpleAlbum album:albums)
                        output.addLine(album.getName());
                }

                @Override
                public void onFailure(Throwable throwable)
                {
                    output.addLine("Failed: "+throwable.getLocalizedMessage());
                    throwable.printStackTrace();
                }
            });
        }
    }

    private String getUrlContent(String url)
    {
        try
        {
            InputStream stream = new URL(url).openStream();
            String str = IOUtils.toString(stream);
            IOUtils.closeQuietly(stream);
            return str;
        } catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }
}
