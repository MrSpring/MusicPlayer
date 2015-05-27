package dk.mrspring.music.cover;

import dk.mrspring.music.player.Music;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

/**
 * Created by Konrad on 24-05-2015.
 */
public class TextureCoverBinder implements ICoverBinder
{
    int textureId;

    public TextureCoverBinder(int textureId)
    {
        this.textureId = textureId;
    }

    public int getTextureId()
    {
        return textureId;
    }

    public void setTextureId(int textureId)
    {
        this.textureId = textureId;
    }

    @Override
    public void bind(Music binding)
    {
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
//        TextureUtil.bindTexture(textureId);
        GlStateManager.bindTexture(textureId);
    }
}
