package dk.mrspring.music.util;

import org.lwjgl.input.Keyboard;

/**
 * Created by Konrad on 27-04-2015.
 */
public class AnyTimeKeyBind
{
    int keyCode;
    boolean checked;

    public AnyTimeKeyBind(int keyCode)
    {
        this.keyCode = keyCode;
        this.checked = false;
    }

    public boolean isHeldDown()
    {
        return Keyboard.isKeyDown(keyCode);
    }

    public boolean isClicked()
    {
        if (isHeldDown())
        {
            if (!checked)
            {
                checked = true;
                return true;
            }
        } else checked = false;
        return false;
    }
}
