package dk.mrspring.music.util;

import net.minecraft.util.StatCollector;

/**
 * Created by Konrad on 24-05-2015.
 */
public class TranslateHelper
{
    public static String translate(String toTranslate)
    {
        return StatCollector.translateToLocal(toTranslate);
    }
}
