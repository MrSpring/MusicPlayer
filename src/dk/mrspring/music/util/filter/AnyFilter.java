package dk.mrspring.music.util.filter;

import dk.mrspring.music.player.Music;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;

/**
 * Created by Konrad on 27-05-2015.
 */
public class AnyFilter extends MusicFilter
{
    public AnyFilter(String filter)
    {
        super(filter);
    }

    @Override
    public boolean filter(Music music)
    {
        return !(StringUtils.isNotBlank(this.filter.trim())) || StringUtils.containsIgnoreCase(music.getName(), filter.trim()) || StringUtils.containsIgnoreCase(music.getAlbum(), filter.trim()) || StringUtils.containsIgnoreCase(music.getArtist(), filter.trim());
    }
}
