package dk.mrspring.music.util;

/**
 * Created by Konrad on 26-04-2015.
 */
public class Miscellaneous
{
    public static float smoothDamp(float target, float current)
    {
        float difference, distance;
        difference = target - current;
        if (difference > 0)
        {
            distance = (float) Math.sqrt(difference * difference);
            return current + (distance * 0.15F);
        } else
        {
            distance = (float) Math.sqrt(-difference * -difference);
            return current + (-distance * 0.15F);
        }
    }

    public static double smoothDamp(double target, double current, double easing)
    {
        if (easing < 1)
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
}
