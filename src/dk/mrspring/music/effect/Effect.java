package dk.mrspring.music.effect;

/**
 * Created by Konrad on 04-08-2015.
 */
public abstract class Effect
{
    public EffectHandler handler;

    public Effect(EffectHandler handler)
    {
        this.handler = handler;
    }

    public abstract void draw();
}
