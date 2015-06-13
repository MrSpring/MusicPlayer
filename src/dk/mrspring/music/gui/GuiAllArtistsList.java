package dk.mrspring.music.gui;

import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.overlay.MultilineTextRender;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.util.Album;
import dk.mrspring.music.util.Artist;
import dk.mrspring.music.util.TranslateHelper;
import net.minecraft.client.Minecraft;

import java.util.List;

/**
 * Created by Konrad on 31-05-2015.
 */
public class GuiAllArtistsList extends GuiSquareList<Artist>
{
    public GuiAllArtistsList(int x, int y, int w, int h, List<Artist> allMusic)
    {
        super(x, y, w, h, allMusic);
    }

    @Override
    public boolean filter(Artist artist, String filter)
    {
        return artist.getArtistName().contains(filter);
    }

    @Override
    public int getEntryHeight(int currentColumn, Artist thing)
    {
        return 0;
    }

    @Override
    public int drawEntry(int currentColumn, Minecraft minecraft, DrawingHelper helper, Artist drawing)
    {
        String key = currentFilter;
        String text = TranslateHelper.translateFormat("gui.artist_list.entry_text", drawing.getArtistName());
        text = text.replaceAll("(?i)(" + key + ")", "\u00a7e$1\u00a7r");
        MultilineTextRender render = new MultilineTextRender(text, minecraft.fontRendererObj, _entryWidth - 6 - (2 * _entrySpacing), true, 5);
        int entryHeight = _entryWidth + render.getTotalHeight() + 3;
        int entryX = currentColumn * _entryWidth;
        helper.drawButtonThingy(new Quad(entryX + _entrySpacing, _entrySpacing, _entryWidth - (2 * _entrySpacing), entryHeight - (2 * _entrySpacing)), 0, true);
        render.render(helper, entryX + 3 + _entrySpacing, _entryWidth - _entrySpacing, 0xFFFFFF, true, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        List<Album> albums = drawing.getAlbums();
        if (albums.size() > 1)
        {
            float coverX = entryX + 3 + _entrySpacing, coverY = 3 + _entrySpacing, coverWidth = _entryWidth - 6 - (2 * _entrySpacing), coverHeight = _entryWidth - 6 - (2 * _entrySpacing);
            int size = albums.size();
            Album one = albums.get(0), two = albums.get(1), three = size > 2 ? albums.get(2) : null, four = size > 3 ? albums.get(3) : null;
            bindMusicCover(one);
            helper.drawTexturedShape(new Quad(coverX, coverY, coverWidth / 2, coverHeight / 2));
            bindMusicCover(two);
            helper.drawTexturedShape(new Quad(coverX + (coverWidth / 2), coverY, coverWidth / 2, coverHeight / 2));
            bindMusicCover(three);
            helper.drawTexturedShape(new Quad(coverX, coverY + (coverHeight / 2), coverWidth / 2, coverHeight / 2));
            bindMusicCover(four);
            helper.drawTexturedShape(new Quad(coverX + (coverWidth / 2), coverY + (coverHeight / 2), coverWidth / 2, coverHeight / 2));
        } else
        {
            bindMusicCover(albums.get(0));
            helper.drawTexturedShape(new Quad(entryX + 3 + _entrySpacing, 3 + _entrySpacing, _entryWidth - 6 - (2 * _entrySpacing), _entryWidth - 6 - (2 * _entrySpacing)));
        }
        helper.drawText(TranslateHelper.translateFormat("gui.artist_list.amount_text", albums.size(), drawing.getMusicCount()), new Vector(entryX - _entrySpacing + _entryWidth - 4, 3 + _entrySpacing + _entryWidth - 6 - (2 * _entrySpacing)), 0xFFFFFF, true, -1, DrawingHelper.VerticalTextAlignment.RIGHT, DrawingHelper.HorizontalTextAlignment.BOTTOM);
        return entryHeight;
    }

    @Override
    protected boolean onElementClicked(int relMouseX, int relMouseY, int globalMouseX, int globalMouseY, int mouseButton, Artist clicked)
    {
        return false;
    }

    private void bindMusicCover(Album music)
    {
        if (music != null)
            music.getFirst().bindCover();
        else Music.bindDefaultCover();
    }
}
