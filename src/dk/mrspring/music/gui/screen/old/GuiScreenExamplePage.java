package dk.mrspring.music.gui.screen.old;

import dk.mrspring.music.gui.screen.GuiScreen;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiScreenExamplePage extends GuiScreen
{
    private String buttonID = "button";
    private String numberID = "number_field";
    private String sliderID = "slider";
    private String tFieldID = "text_field";
    private String checkBID = "check_box";
    private String cFieldID = "custom_text_field";
    private String explorerID = "file_explorer";

    public GuiScreenExamplePage(net.minecraft.client.gui.GuiScreen currentScreen)
    {
        super("gui.screen.example_page.name", currentScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.addGuiElement(sliderID, new GuiSlider(60, 40, 120, 30, 50));
//        this.addGuiElement(buttonID, new GuiSimpleButton(10, 10, 40, 40, "Button"));
//        this.addGuiElement(numberID, new GuiNumberField(60, 50, 100, 32, 10.0));
//        this.addGuiElement(tFieldID, new GuiEditableTextField(60 + 75 + 10, 10, 240, 20, "This is Edible!", mc.fontRendererObj));
//        this.addGuiElement(checkBID, new GuiCheckbox(10, 10 + 40 + 10, 10, 10, false));
        this.addGuiElement(cFieldID, new GuiCustomTextField(10, 10 + 60 + 10, 200, 15, "Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text! Long Text!"));
//        this.addGuiElement(explorerID, new GuiFileExplorer(10, 105, 300, height - 60 - 10 - 105, "C:"));

        this.setSubtitle("gui.screen.example_page.description").hideBars().hideDoneButton().enableRepeats();
    }
}
