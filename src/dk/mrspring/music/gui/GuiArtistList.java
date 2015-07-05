package dk.mrspring.music.gui;

import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
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
public class GuiArtistList extends GuiSquareList<Object>
{
    Artist artist;
    Showing type;
    protected GuiDropDownList typeList;
    int dropDownListHeight = 30;

    public GuiArtistList(int x, int y, int w, int h, Artist artist, Showing type)
    {
        super(x, y, w, h, (List<Object>) (type == Showing.ALBUMS ? artist.getAlbums() : artist.getAllMusic()));
        this.type = type;
        this.artist = artist;
        this.typeList = new GuiDropDownList(x+3, y, w / 3, dropDownListHeight, 0,
                new GuiDropDownList.ListElement(Showing.ALBUMS.getRenderString(), Showing.ALBUMS),
                new GuiDropDownList.ListElement(Showing.MUSIC.getRenderString(), Showing.MUSIC));
    }

    @Override
    public void setX(int x)
    {
        super.setX(x);
        typeList.setX(x+3);
    }

    @Override
    public void setY(int y)
    {
        super.setY(y);
        typeList.setY(y);
    }

    @Override
    public void setWidth(int width)
    {
        super.setWidth(width);
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        double zI = helper.getZIndex();
        helper.setZIndex(zI + 5);
        typeList.setWidth(width / 3);
        helper.setZIndex(zI);
    }

    public void updateList()
    {
        this.showing = (List<Object>) (type == Showing.ALBUMS ? artist.getAlbums() : artist.getAllMusic());
    }

    @Override
    public int getYListOffset()
    {
        return dropDownListHeight;
    }

    @Override
    public int getListHeight()
    {
        return super.getListHeight() - dropDownListHeight;
    }

    @Override
    public boolean filter(Object object, String filter)
    {
        return type == Showing.ALBUMS ? object instanceof Album : object instanceof Music; // TODO: Apply filter
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        typeList.draw(minecraft, mouseX, mouseY);
        LiteModMusicPlayer.core.getDrawingHelper().drawText("Artist:\n\u00a7l\u00a7n"+artist.getArtistName(), new Vector(x+(width()/5*3), y+3),0xFFFFFF,true,-1, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.TOP);
        super.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public void update()
    {
        super.update();
        typeList.update();
        Showing fromList = (Showing) typeList.getSelectedElement().id;
        if (fromList != this.type)
        {
            this.type = fromList;
            this.updateList();
        }
    }

    @Override
    protected boolean isOtherElementsClicked(int mouseX, int mouseY, int mouseButton)
    {
        return typeList.mouseDown(mouseX, mouseY, mouseButton);
    }

    @Override
    public int drawEntry(int currentColumn, Minecraft minecraft, DrawingHelper helper, Object drawing)
    {
        return type == Showing.ALBUMS && drawing instanceof Album ? drawAlbum(currentColumn, minecraft, helper, (Album) drawing) : (drawing instanceof Music ? drawMusic(currentColumn, minecraft, helper, (Music) drawing) : 0);
    }

    @Override
    public int getEntryHeight(int currentColumn, Object drawing)
    {
        return type == Showing.ALBUMS && drawing instanceof Album ? getAlbumHeight(currentColumn, (Album) drawing) : (drawing instanceof Music ? getMusicHeight(currentColumn, (Music) drawing) : 0);
    }

    private int getAlbumHeight(int currentColumn, Album drawing)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        String text = TranslateHelper.translateFormat("gui.album_list.entry_text", drawing.getAlbumName(), drawing.getArtistName());
        text = text.replaceAll("(?i)(" + currentFilter + ")", "\u00a7e$1\u00a7r");
        MultilineTextRender render = new MultilineTextRender(text, minecraft.fontRendererObj, _entryWidth - 6 - (2 * _entrySpacing), true, 5);
        return _entryWidth + render.getTotalHeight() + 3;
    }

    private int getMusicHeight(int currentColumn, Music music)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        String text = TranslateHelper.translateFormat("gui.music_list.entry_text", music.getName(), music.getAlbum(), music.getArtist());
        text = text.replaceAll("(?i)(" + currentFilter + ")", "\u00a7e$1\u00a7r");
        MultilineTextRender render = new MultilineTextRender(text, minecraft.fontRendererObj, _entryWidth - 6 - (2 * _entrySpacing), true, 5);
        return _entryWidth + render.getTotalHeight() + 3;
    }

    @Override
    protected boolean onElementClicked(int relMouseX, int relMouseY, int mouseX, int mouseY, int mouseButton, Object clicked)
    {
        return false;
    }

    private int drawAlbum(int currentColumn, Minecraft minecraft, DrawingHelper helper, Album drawing)
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
        bindMusicCover(musics.get(0));
        helper.drawTexturedShape(new Quad(entryX + 3 + _entrySpacing, 3 + _entrySpacing, _entryWidth - 6 - (2 * _entrySpacing), _entryWidth - 6 - (2 * _entrySpacing)));
        helper.drawText(TranslateHelper.translateFormat("gui.album_list.amount_text", musics.size()), new Vector(entryX - _entrySpacing + _entryWidth - 4, 3 + _entrySpacing + _entryWidth - 6 - (2 * _entrySpacing)), 0xFFFFFF, true, _entryWidth - 6 - (2 * _entrySpacing), DrawingHelper.VerticalTextAlignment.RIGHT, DrawingHelper.HorizontalTextAlignment.BOTTOM);
        return entryHeight;
    }

    private int drawMusic(int currentColumn, Minecraft minecraft, DrawingHelper helper, Music music)
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

    private void bindMusicCover(Music music)
    {
        if (music != null)
            music.bindCover();
        else Music.bindDefaultCover();
    }

    public enum Showing
    {
        ALBUMS("albums"),
        MUSIC("songs");

        String render;

        Showing(String str)
        {
            this.render = str;
        }

        public String getRenderString()
        {
            return TranslateHelper.translate("gui.artist_list." + render + ".name").replace("\\n", "\n").replace("�", "\u00a7");
        }
    }
}
