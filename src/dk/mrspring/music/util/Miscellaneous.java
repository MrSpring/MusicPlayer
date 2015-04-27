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
//        return current + (distance * 0.15F);
    }
}
