package dk.mrspring.music.util;

/**
 * Created by Konrad on 29-04-2015.
 */
public class FormatString
{
    String string;
    Object[] format;

    public FormatString(String string, Object... format)
    {
        this.string = string;
        this.format = format;
    }

    @Override
    public String toString()
    {
        return String.format(string, format);
    }
}
