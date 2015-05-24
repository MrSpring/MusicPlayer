package dk.mrspring.music.cover;

import dk.mrspring.music.player.Music;
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
