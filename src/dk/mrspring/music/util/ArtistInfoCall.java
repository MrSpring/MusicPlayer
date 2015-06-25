package dk.mrspring.music.util;

import de.umass.lastfm.*;
import de.umass.lastfm.Artist;

import java.util.Collection;

/**
 * Created by Konrad on 24-06-2015.
 */
public class ArtistInfoCall
{
    private final String ARTIST;
    private final String KEY;
    private final boolean SEARCH;
    private Throwable error = null;
    private String result = null;
    private State current = State.NOT_INITIATED;

    public ArtistInfoCall(String artist, boolean doSearch, String apiKey)
    {
        this.ARTIST = artist;
        this.KEY = apiKey;
        this.SEARCH = doSearch;
    }

    public ArtistInfoCall start()
    {
        new ArtistInfoGetter().start();
        return this;
    }

    public boolean hasErrored()
    {
        return error != null && current == State.ERROR;
    }

    public boolean hasResult()
    {
        return result != null;
    }

    public String getResult()
    {
        return result;
    }

    public boolean isFinished()
    {
        return hasResult() && current == State.FINISHED;
    }

    public Throwable getError()
    {
        return error;
    }

    public State getCurrentState()
    {
        return current;
    }

    public enum State
    {
        NOT_INITIATED,
        LOADING,
        ERROR,
        FINISHED
    }

    private class ArtistInfoGetter extends Thread
    {
        public ArtistInfoGetter()
        {
            super(new Runnable()
            {
                @Override
                public void run()
                {
                    String lookingFor = ARTIST;
                    current = ArtistInfoCall.State.LOADING;
                    try
                    {
                        if (SEARCH)
                        {
                            Collection<Artist> searchResult = Artist.search(lookingFor, KEY);
                            Artist[] artists = searchResult.toArray(new Artist[searchResult.size()]);
                            lookingFor = artists[0].getName();
                        }
                        de.umass.lastfm.Artist returned = de.umass.lastfm.Artist.getInfo(lookingFor, KEY);
                        String wikiTextRaw = returned.getWikiText();
                        wikiTextRaw = wikiTextRaw.substring(0, wikiTextRaw.lastIndexOf("<a "));
                        String wikiText = wikiTextRaw.trim();
                        current = ArtistInfoCall.State.FINISHED;
                        result = wikiText;
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        error = e;
                        current = ArtistInfoCall.State.ERROR;
                    }
                }
            });
        }
    }
}
