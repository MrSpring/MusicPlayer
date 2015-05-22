package dk.mrspring.music.util;

import net.minecraft.util.StatCollector;

/**
 * Created by Konrad on 29-04-2015.
 */
public class TranslateFormatString extends FormatString
{
    public TranslateFormatString(String string, Object... format)
    {
        super(string, format);
    }

    @Override
    public String toString()
    {
        return StatCollector.translateToLocalFormatted(string, format);
    }
}
