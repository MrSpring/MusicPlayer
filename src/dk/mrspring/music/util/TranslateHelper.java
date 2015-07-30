package dk.mrspring.music.util;

import com.mumfrey.liteloader.core.LiteLoader;
import net.minecraft.util.StatCollector;

/**
 * Created by Konrad on 24-05-2015.
 */
public class TranslateHelper
{
    public static String translate(String toTranslate)
    {
        String translated = StatCollector.translateToLocal(toTranslate);
        if (LiteLoader.isDevelopmentEnvironment()) translated = translated.replace("§", "\u00a7");
        return translated.replace("\\n", "\n");
    }

    public static String translateFormat(String toTranslate, Object... format)
    {
        String translated = StatCollector.translateToLocalFormatted(toTranslate, format);
        if (LiteLoader.isDevelopmentEnvironment()) translated = translated.replace("§", "\u00a7");
        return translated.replace("\\n", "\n");
    }
}
