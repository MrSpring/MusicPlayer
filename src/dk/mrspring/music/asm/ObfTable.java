package dk.mrspring.music.asm;

import com.mumfrey.liteloader.core.runtime.Obf;

/**
 * Created by MrSpring on 07-07-2015 for MC Music Player.
 */
public class ObfTable extends Obf
{
    public ObfTable(String seargeName, String obfName, String mcpName)
    {
        super(seargeName, obfName, mcpName);
    }


    public ObfTable(String seargeName, String obfName)
    {
        super(seargeName, obfName, seargeName);
    }
}
