package dk.mrspring.music.util.filter;

import dk.mrspring.music.player.Music;

/**
 * Created by Konrad on 26-05-2015.
 */
public class ArtistFilter extends MusicFilter
{
    public ArtistFilter(String filter)
    {
        super(filter);
    }

    @Override
    public boolean filter(Music music)
    {
        return !this.filter.isEmpty() && music.getArtist().contains(this.filter.trim());
    }
}
