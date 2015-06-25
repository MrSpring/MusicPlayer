package dk.mrspring.music.gui.screen.overlay;

import com.mumfrey.liteloader.LiteMod;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.DrawingHelper.VerticalTextAlignment;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.overlay.MultilineTextRender;
import dk.mrspring.music.overlay.TextRender;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.util.Album;
import dk.mrspring.music.util.ArtistInfoCall;
import dk.mrspring.music.util.Miscellaneous;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static dk.mrspring.llcore.DrawingHelper.HorizontalTextAlignment.*;

/**
 * Created by Konrad on 18-06-2015.
 */
public class CardMusic extends Card
{
    Music showing;

    public CardMusic(OverlayParent overlayParent, Music showing)
    {
        super(overlayParent);

        this.showing = showing;

        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        parent.addCard(new BasicInfoCard(overlayParent));
        parent.addCard(new ArtistInfoCard(overlayParent, renderer));
        parent.addCard(new AlbumCard(overlayParent, renderer));
    }

    @Override
    public int getHeight()
    {
        return 0;
    }

    @Override
    public boolean drawBackdrop()
    {
        return false;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
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

    private class MusicView
    {
        Music drawing = null;
        int height, width;
        TextRender textRender = null;
        FontRenderer renderer;

        private MusicView(int width, Music toDraw, FontRenderer renderer)
        {
            this.drawing = toDraw;
            this.renderer = renderer;
            this.width = width;
            textRender = new TextRender(toDraw.getName() + " - §7" + toDraw.getArtist(), renderer, width);
            this.height = textRender.getTotalHeight() + 12;
            textRender.setWrapLength(width - height);
        }

        void recalculateSize()
        {
            textRender = new TextRender(drawing.getName() + " - §7" + drawing.getArtist(), renderer, width);
            this.height = textRender.getTotalHeight();
            textRender.setWrapLength(width - height);
        }

        public int getHeight()
        {
            return height;
        }

        void draw(DrawingHelper helper, int width, Music drawing, int textColor)
        {
            drawing.bindCover();
            this.width = width;
            this.textRender.setWrapLength(width - height);
            int size = height;
            helper.drawTexturedShape(new Quad(3, 3, size - 5, size - 5));
            textRender.render(helper, size, height / 2, textColor, true, VerticalTextAlignment.LEFT, CENTER);
        }
    }

    private class AlbumCard extends CardBasicInfo
    {
        MusicView[] views;
        boolean expanded = false;
        int collapsedViewCount = 10;

        public AlbumCard(OverlayParent overlayParent, FontRenderer renderer)
        {
            super(overlayParent, renderer, "Album", "");
        }

        @Override
        public void initGui()
        {
            super.initGui();

            List<Music> album = showing.getAlbumInstance().getMusicList();
            views = new MusicView[album.size()];
            System.out.println("Creating views");
            for (int i = 0; i < views.length; i++)
            {
                Music music = album.get(i);
                int width = parent.getOverlayWidth() - 4;
                System.out.println("Created view: " + i + ", for: " + music.getName() + ", width: " + width);
                views[i] = new MusicView(width, music, Minecraft.getMinecraft().fontRendererObj);
            }
        }

        @Override
        public int getHeight()
        {
            int listHeight = 0;
            for (int i = 0; i < views.length; i++)
            {
                if (!expanded && i >= collapsedViewCount)
                {
                    listHeight += 18;
                    break;
                }
                MusicView view = views[i];
                listHeight += view.getHeight();
            }
            return super.getHeight() + listHeight;
        }

        /*public int getListHeight(){
            int listHeight=0;
            for (MusicView view:views)
                listHeight+=view.getHeight();
            return listHeight;
        }*/

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY)
        {
            super.draw(minecraft, mouseX, mouseY);

            GL11.glPushMatrix();
            GL11.glTranslatef(0, super.getHeight(), 0);

            int color = parent.getDefaultTextColor();
            DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
            int lineOffset = 10;
            for (int i = 0; i < views.length; i++)
            {
                if (!expanded && i >= collapsedViewCount)
                {
                    int remaining = views.length - i;
                    helper.drawText("Show "+remaining+" more...", new Vector(parent.getOverlayWidth() / 2, 5), color, true, parent.getOverlayWidth() - 4, VerticalTextAlignment.CENTER, TOP);
                    break;
                }
                MusicView view = views[i];
                view.draw(helper, parent.getOverlayWidth() - 4, showing, color);
                GL11.glTranslatef(0, view.getHeight(), 0);
                if (i + 1 < views.length)
                    helper.drawShape(new Quad(lineOffset, 0, parent.getOverlayWidth() - (lineOffset * 2), 1));
            }

            GL11.glPopMatrix();
        }
    }

    private class ArtistInfoCard extends CardBasicInfo
    {
        private final int STEP = 15;
        String artistInfo = null;
        ArtistInfoCall infoGetter;
        int time = 0;

        public ArtistInfoCard(OverlayParent overlayParent, FontRenderer renderer)
        {
            super(overlayParent, renderer, "Artist Info", "");

            infoGetter = new ArtistInfoCall(showing.getArtist(), true, LiteModMusicPlayer.apiKey).start();
        }

        @Override
        public void update()
        {
            super.update();

            if (infoGetter.isFinished() && artistInfo == null)
                artistInfo = Miscellaneous.cleanupHTMLCode(infoGetter.getResult());

            if (artistInfo == null)
                time = time > STEP * 3 ? 1 : time + 1;
        }

        @Override
        public String getText()
        {
            if (artistInfo == null)
            {
                String text = "Loading";
                for (int i = 0; i < time; i += STEP)
                    text += ".";
                return text;
            } else return artistInfo;
        }
    }

    /*private class ArtistInfoCard extends Card
    {
        public ArtistInfoCard(OverlayParent overlayParent)
        {
            super(overlayParent);

            String artist = showing.getArtist();
//            Caller.getInstance().call()
            Collection<Artist> searchResults = Artist.search(artist, LiteModMusicPlayer.apiKey);
            Artist[] artists = searchResults.toArray(new Artist[searchResults.size()]);
            if (artists.length >= 1)
            {
                String artistName = artists[0].getName();
                Artist artistInfo = Artist.getInfo(*//*artistName*//*"Cher", LiteModMusicPlayer.apiKey);
                String wikiText = artistInfo.getWikiText();
                String output = wikiText.substring(0, wikiText.lastIndexOf("<a "));
                output = output.trim();
                System.out.println(output);
            }
        }

        @Override
        public int getHeight()
        {
            return 0;
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY)
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
    }*/

    private class BasicInfoCard extends Card
    {
        MultilineTextRender render;

        public BasicInfoCard(OverlayParent overlayParent)
        {
            super(overlayParent);
        }

        @Override
        public int getHeight()
        {
            int height = parent.getOverlayWidth() / 3;
            return render != null ? Math.max(render.getTotalHeight() + 20, height) : height;
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY)
        {
            int maxWidth = parent.getOverlayWidth();
            int coverSize = maxWidth / 3;
            if (render == null)
                render = new MultilineTextRender(
                        showing.getName() + "\n" +
                                showing.getAlbum() + "\n" +
                                showing.getArtist() + "\n",
                        parent.getMinecraft().fontRendererObj, coverSize * 2, false, 2);

            DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();

            showing.bindCover();
            helper.drawTexturedShape(new Quad(2, 2, coverSize - 4, coverSize - 4));

            int color = parent.getDefaultTextColor();

            GL11.glPushMatrix();
            GL11.glTranslatef(coverSize * 2, 0, 0);
            GL11.glScalef(2, 2, 2);
            helper.drawText("Basic Info", new Vector(0, 3), color, true, coverSize, VerticalTextAlignment.CENTER, TOP);
            GL11.glPopMatrix();

//            String title = showing.getName(), album = showing.getAlbum(), artist = showing.getArtist();
//            helper.drawText(title, new Vector(coverSize * 2, 4 + 28), color, true, coverSize * 2, VerticalTextAlignment.CENTER, TOP);
//            helper.drawText(album, new Vector(coverSize * 2, 16 + 28), color, true, coverSize * 2, VerticalTextAlignment.CENTER, TOP);
//            helper.drawText(artist, new Vector(coverSize * 2, 26 + 28), color, true, coverSize * 2, VerticalTextAlignment.CENTER, TOP);
            render.render(helper, coverSize * 2, 4 + 28, color, true, VerticalTextAlignment.CENTER, TOP);
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
}
