package dk.mrspring.music.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 04-08-2015.
 */
public class EffectHandler
{
    List<Effect> currentEffects;
    List<Effect> removing;
    ScaledResolution screen;

    public EffectHandler()
    {
        currentEffects = new ArrayList<Effect>();
        removing = new ArrayList<Effect>();
    }

    public void addEffect(Effect effect)
    {
        currentEffects.add(effect);
    }

    public void removeEffect(Effect toRemove)
    {
        if (currentEffects.contains(toRemove)) removing.add(toRemove);
    }

    public int getScreenWidth()
    {
        return getScreen().getScaledWidth();
    }

    public int getScreenHeight()
    {
        return getScreen().getScaledHeight();
    }

    public ScaledResolution getScreen()
    {
        if (screen == null) updateScreen();
        return screen;
    }

    public void updateScreen()
    {
        Minecraft mc = Minecraft.getMinecraft();
        screen = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
    }

    public void draw()
    {
        for (Effect effect : removing) currentEffects.remove(effect);
        removing.clear();

        updateScreen();
        for (Effect effect : currentEffects) effect.draw();
    }
}
