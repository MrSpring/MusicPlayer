package dk.mrspring.music.util;

import dk.mrspring.music.LiteModMusicPlayer;

import java.util.Arrays;

/**
 * Created by Konrad on 26-04-2015.
 */
public class Miscellaneous
{
    public static float clamp01(float current)
    {
        return current > 1F ? 1F : (current < 0 ? 0 : current);
    }

    public static double min(double current, double min)
    {
        return current < min ? min : current;
    }

    public static long min(long current, long min)
    {
        return current < min ? min : current;
    }

    public static int min(int current, int min)
    {
        return current < min ? min : current;
    }

    public static int clamp(int current, int min, int max)
    {
        return current > max ? max : (current < min ? min : current);
    }

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

    public static <T> T[] merge(T[] one, T[] two)
    {
        if (one == null) return two;
        else if (two == null) return one;
        T[] newArray = Arrays.copyOf(one, one.length + two.length);
        System.arraycopy(two, 0, newArray, one.length, two.length);
        return newArray;
    }
}
