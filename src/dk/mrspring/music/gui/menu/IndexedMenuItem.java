package dk.mrspring.music.gui.menu;

/**
 * Created by Konrad on 07-06-2015.
 */
public abstract class IndexedMenuItem implements IMenuItem
{
    Object identifier;

    public IndexedMenuItem(Object identifier)
    {
        this.identifier = identifier;
    }

    public Object getIdentifier()
    {
        return identifier;
    }
}
