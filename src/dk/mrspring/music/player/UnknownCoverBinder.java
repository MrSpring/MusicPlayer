package dk.mrspring.music.player;

import net.minecraft.client.Minecraft;

/**
 * Created by Konrad on 24-05-2015.
 */
public class UnknownCoverBinder implements ICoverBinder
{
    @Override
    public void bind(Music binding)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(Music.UNKNOWN);
    }
}
