package dk.mrspring.music.gui;

import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.overlay.MultilineTextRender;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.util.Album;
import dk.mrspring.music.util.TranslateHelper;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Konrad on 30-05-2015.
 */
public class GuiAllAlbumsList extends GuiSquareList<Album>
{
    public GuiAllAlbumsList(int x, int y, int w, int h, List<Album> allMusic)
    {
        super(x, y, w, h, allMusic);
    }

    @Override
    public boolean filter(Album album, String filter)
    {
        return StringUtils.containsIgnoreCase(album.getAlbumName(), filter) || StringUtils.containsIgnoreCase(album.getArtistName(), (filter));
    }

    @Override
    public int getEntryHeight(int currentColumn, Album thing)
    {
        return 0;
    }

    @Override
    public int drawEntry(int currentColumn, Minecraft minecraft, DrawingHelper helper, Album drawing)
    {
        String key = currentFilter;
        String text = TranslateHelper.translateFormat("gui.album_list.entry_text", drawing.getAlbumName(), drawing.getArtistName());
        text = text.replaceAll("(?i)(" + key + ")", "\u00a7e$1\u00a7r");
        MultilineTextRender render = new MultilineTextRender(text, minecraft.fontRendererObj, _entryWidth - 6 - (2 * _entrySpacing), true, 5);
        int entryHeight = _entryWidth + render.getTotalHeight() + 3;
        int entryX = currentColumn * _entryWidth;
        helper.drawButtonThingy(new Quad(entryX + _entrySpacing, _entrySpacing, _entryWidth - (2 * _entrySpacing), entryHeight - (2 * _entrySpacing)), 0, true);
        render.render(helper, entryX + 3 + _entrySpacing, _entryWidth - _entrySpacing, 0xFFFFFF, true, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        List<Music> musics = drawing.getMusicList();
        /*if (musics.size() > 1)
        {
            int coverX = entryX + 3 + _entrySpacing, coverY = 3 + _entrySpacing, coverWidth = _entryWidth - 6 - (2 * _entrySpacing), coverHeight = _entryWidth - 6 - (2 * _entrySpacing);
            int size = musics.size();
            Music one = musics.get(0), two = musics.get(1), three = size > 2 ? musics.get(2) : null, four = size > 3 ? musics.get(3) : null;
            bindMusicCover(one);
            helper.drawTexturedShape(new Quad(coverX, coverY, coverWidth / 2, coverHeight / 2));
            bindMusicCover(two);
            helper.drawTexturedShape(new Quad(coverX + (coverWidth / 2), coverY, coverWidth / 2, coverHeight / 2));
            bindMusicCover(three);
            helper.drawTexturedShape(new Quad(coverX, coverY + (coverHeight / 2), coverWidth / 2, coverHeight / 2));
            bindMusicCover(four);
            helper.drawTexturedShape(new Quad(coverX + (coverWidth / 2), coverY + (coverHeight / 2), coverWidth / 2, coverHeight / 2));
        } else
        {*/
        bindMusicCover(musics.get(0));
        helper.drawTexturedShape(new Quad(entryX + 3 + _entrySpacing, 3 + _entrySpacing, _entryWidth - 6 - (2 * _entrySpacing), _entryWidth - 6 - (2 * _entrySpacing)));
        helper.drawText(TranslateHelper.translateFormat("gui.album_list.amount_text", musics.size()), new Vector(entryX - _entrySpacing + _entryWidth - 4, 3 + _entrySpacing + _entryWidth - 6 - (2 * _entrySpacing)), 0xFFFFFF, true, _entryWidth - 6 - (2 * _entrySpacing), DrawingHelper.VerticalTextAlignment.RIGHT, DrawingHelper.HorizontalTextAlignment.BOTTOM);
//        }
        return entryHeight;
    }

    @Override
    protected boolean onElementClicked(int relMouseX, int relMouseY, int mouseX, int mouseY, int mouseButton, Album clicked)
    {
        return false;
    }

    private void bindMusicCover(Music music)
    {
        if (music != null)
            music.bindCover();
        else Music.bindDefaultCover();
    }
}
