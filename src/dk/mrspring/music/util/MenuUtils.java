package dk.mrspring.music.util;

import dk.mrspring.music.gui.menu.IMenuItem;
import dk.mrspring.music.gui.menu.Menu;

/**
 * Created by Konrad on 04-08-2015.
 */
public class MenuUtils
{
    public MenuResult createMusicMenu()
    {

    }

    public class MenuResult
    {
        public IMenuItem[] items;
        public Menu.MenuAction action;

        public MenuResult(Menu.MenuAction action, IMenuItem... items)
        {
            this.items = items;
            this.action = action;
        }
    }
}
