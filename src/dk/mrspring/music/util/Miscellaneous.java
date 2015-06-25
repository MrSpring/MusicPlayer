package dk.mrspring.music.util;

import dk.mrspring.music.LiteModMusicPlayer;

/**
 * Created by Konrad on 26-04-2015.
 */
public class Miscellaneous
{
    public static float smoothDamp(float target, float current, float easing)
    {
        if (easing < 1 && !LiteModMusicPlayer.config.disable_animations && current != target)
        {
            float difference, distance;
            difference = target - current;
            if (difference > 0)
            {
                distance = (float) Math.sqrt(difference * difference);
                return current + (distance * easing);
            } else
            {
                distance = (float) Math.sqrt(-difference * -difference);
                return current + (-distance * easing);
            }
        } else return target;
    }

    public static double smoothDamp(double target, double current, double easing)
    {
        if (easing < 1 && !LiteModMusicPlayer.config.disable_animations && current != target)
        {
            double difference, distance;
            difference = target - current;
            if (difference > 0)
            {
                distance = Math.sqrt(difference * difference);
                return current + (distance * easing);
            } else
            {
                distance = Math.sqrt(-difference * -difference);
                return current + (-distance * easing);
            }
        } else return target;
    }

    public static String cleanupHTMLCode(String html)
    {
        return html.replaceAll("\\<.*?>", "").replaceAll("&amp;", "&");
    }
}
