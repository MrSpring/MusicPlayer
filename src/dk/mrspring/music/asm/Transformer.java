package dk.mrspring.music.asm;

import com.mumfrey.liteloader.core.runtime.Obf;
import com.mumfrey.liteloader.transformers.event.Event;
import com.mumfrey.liteloader.transformers.event.EventInfo;
import com.mumfrey.liteloader.transformers.event.EventInjectionTransformer;
import com.mumfrey.liteloader.transformers.event.MethodInfo;
import com.mumfrey.liteloader.transformers.event.inject.MethodHead;
import dk.mrspring.updator.gui.GuiMainMenuHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;

/**
 * Created by MrSpring on 07-07-2015 for MC Music Player.
 */
public class Transformer /*implements IClassTransformer*/ extends EventInjectionTransformer
{
    //    @Override
//    public byte[] transform(String s, String s1, byte[] bytes)
//    {
//        System.out.println("Transforming");
//        return bytes;
//    }
    private static Obf c_GuiMainMenu = new ObfTable("net.minecraft.client.gui.GuiMainMenu", "bxq");
    private static Obf m_initGui = new ObfTable("func_73866_w_", "b", "initGui");
    private static Obf m_actionPerformed = new ObfTable("func_146284_a", "a", "actionPerformed");

    @Override
    protected void addEvents()
    {
        System.out.println("Adding events");

        Event onInitGui = Event.getOrCreate("onInitMainMenu");
        MethodInfo initGuiInfo = new MethodInfo(c_GuiMainMenu, m_initGui, Void.TYPE);
        this.addEvent(onInitGui, initGuiInfo, new MethodHead());
        onInitGui.addListener(new MethodInfo("dk.mrspring.music.asm.Transformer", "initGui"));

        Event onActionPerformed = Event.getOrCreate("onActionPerformedMainMenu");
        MethodInfo actionPerformedInfo = new MethodInfo(c_GuiMainMenu, m_actionPerformed, Void.TYPE, GuiButton.class);
        this.addEvent(onActionPerformed, actionPerformedInfo, new MethodHead());
        onActionPerformed.addListener(new MethodInfo("dk.mrspring.music.asm.Transformer", "actionPerformed"));
    }

    public static void initGui(EventInfo<GuiMainMenu> info)
    {
        System.out.println("init");
        GuiMainMenu menu = info.getSource();
        GuiMainMenuHandler.onInitMainMenuGui(menu);
    }

    public static void actionPerformed(EventInfo<GuiMainMenu> info, GuiButton button)
    {
        System.out.println("actionPerformed");
        GuiMainMenuHandler.onActionPerformed(button);
    }
}
