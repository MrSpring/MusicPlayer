package dk.mrspring.music.effect;

import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import org.lwjgl.opengl.GL11;

/**
 * Created by Konrad on 04-08-2015.
 */
public class EffectMessage extends Effect
{
    int ticks = 0;
    int maxTicks = 80;
    int maxHeight = 20;
    int stopHeight = maxHeight + 20;
    int scaleTicks = 10;
    double riseMultiplier = 0.6D;
    String message = "";

    public EffectMessage(EffectHandler handler, String message)
    {
        super(handler);
        this.message = message != null ? message : "";
        maxTicks = message.length() * LiteModMusicPlayer.config.message_pupop_time_per_char;
    }

    @Override
    public void draw()
    {
        float yPreOffset = 10;
        int outScaleProgress = 10 - Math.max(-(maxTicks - scaleTicks - ticks), 0);
        int inScaleProgress = Math.min(ticks, scaleTicks);
        int scaleProgress = ticks > (maxTicks / 2) ? outScaleProgress : inScaleProgress;
        int heightProgress = ticks;
        if (heightProgress > maxHeight) heightProgress -= ((ticks - maxHeight) * riseMultiplier);
        if (heightProgress > stopHeight) heightProgress = stopHeight;
        float scale = ((float) scaleProgress) / 10F;
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        int width = handler.getScreenWidth();
        GL11.glPushMatrix();
        GL11.glTranslatef(width / 2, yPreOffset + (((float) heightProgress) / 2F), 0);
        GL11.glScalef(scale, scale, scale);
        DrawingHelper.TextRenderResult result = helper.drawCenteredText(message, new Vector(/*width / 2*/0, 0), 0xFFFFFF, width, DrawingHelper.HorizontalTextAlignment.BOTTOM);
        helper.drawButtonThingy(result.asQuad().expand(5F), 0F, true);
        GL11.glPopMatrix();
        ticks++;

        if (ticks > maxTicks) handler.removeEffect(this);
    }
}
