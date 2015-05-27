package dk.mrspring.music.util.filter;

import dk.mrspring.music.player.Music;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Konrad on 26-05-2015.
 */
public class TitleFilter extends MusicFilter
{
    public TitleFilter(String filter)
    {
        super(filter);
    }

    @Override
    public boolean filter(Music music)
    {
        return !(StringUtils.isNotBlank(this.filter.trim())) || StringUtils.containsIgnoreCase(music.getName(), filter.trim());
    }
}
