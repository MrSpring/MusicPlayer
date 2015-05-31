package dk.mrspring.music.gui;

import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.overlay.MultilineTextRender;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.util.TranslateHelper;
import dk.mrspring.music.util.filter.AnyFilter;
import dk.mrspring.music.util.filter.MusicFilter;
import net.minecraft.client.Minecraft;

import java.util.List;

/**
 * Created by Konrad on 30-05-2015.
 */
public class GuiAllMusicList extends GuiSquareList<Music>
{
    MusicFilter filter = new AnyFilter(currentFilter);

    public GuiAllMusicList(int x, int y, int w, int h, List<Music> allMusic)
    {
        super(x, y, w, h, allMusic);
    }

    @Override
    public int drawEntry(int currentColumn, Minecraft minecraft, DrawingHelper helper, Music music)
    {
        String key = currentFilter;
        String text = TranslateHelper.translateFormat("gui.music_list.entry_text", music.getName(), music.getAlbum(), music.getArtist());
        text = text.replaceAll("(?i)(" + key + ")", "\u00a7e$1\u00a7r");
        MultilineTextRender render = new MultilineTextRender(text, minecraft.fontRendererObj, _entryWidth - 6 - (2 * _entrySpacing), true, 5);
        int entryHeight = _entryWidth + render.getTotalHeight() + 3;
        int entryX = currentColumn * _entryWidth;
        helper.drawButtonThingy(new Quad(entryX + _entrySpacing, _entrySpacing, _entryWidth - (2 * _entrySpacing), entryHeight - (2 * _entrySpacing)), 0, true);
        render.render(helper, entryX + 3 + _entrySpacing, _entryWidth - _entrySpacing, 0xFFFFFF, true, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        music.bindCover();
        helper.drawTexturedShape(new Quad(entryX + 3 + _entrySpacing, 3 + _entrySpacing, _entryWidth - 6 - (2 * _entrySpacing), _entryWidth - 6 - (2 * _entrySpacing)));
        return entryHeight;
    }

    @Override
    public void setFilter(String newFilter)
    {
        super.setFilter(newFilter);
        this.filter.setFilter(newFilter);
    }

    @Override
    public boolean filter(Music object, String filter)
    {
        return this.filter.filter(object);
    }
}
