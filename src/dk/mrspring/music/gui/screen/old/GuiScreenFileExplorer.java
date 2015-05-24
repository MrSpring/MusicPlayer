package dk.mrspring.music.gui.screen.old;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.GuiFileExplorer;
import dk.mrspring.fileexplorer.gui.editor.Editor;
import dk.mrspring.fileexplorer.gui.editor.FileType;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.loader.FileLoader;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.gui.screen.GuiScreen;

import java.io.File;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiScreenFileExplorer extends GuiScreen
{
    String startFrom;
    String openFileType = "";
    boolean initialized = false;
    int explorerHeightOffset = 0;

    public GuiScreenFileExplorer(net.minecraft.client.gui.GuiScreen previousScreen, File path)
    {
        super("File Explorer", previousScreen);
        startFrom = path.getPath();
    }

    @Override
    public void initGui()
    {
        if (!initialized)
        {
            super.initGui();

            this.addGuiElement("explorer", new GuiFileExplorer(5, 5, 260, height - 10, startFrom)
                    .setShowPath(LiteModFileExplorer.config.showOpenDirectory)
                    .setShowBackground(LiteModFileExplorer.config.showExplorerBackground)
                    .setOnFileOpened(new GuiFileExplorer.IOnFileOpened()
                    {
                        @Override
                        public void onOpened(File file)
                        {
                            GuiScreenFileExplorer.this.openFile(file);
                        }
                    }));

            this.hideBars().hideTitle().enableRepeats();
            initialized = true;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        LiteModFileExplorer.core.getDrawingHelper().drawShape(new Quad(0, 0, width, height).setColor(Color.BLACK).setAlpha(0.5F));
        super.drawScreen(mouseX, mouseY, partialTicks);
        IGui gui = this.getGui("editor");
        if (gui != null)
        {
            LiteModFileExplorer.core.getDrawingHelper().drawVerticalLine(new Vector(259, 10), height - 20, 1, true);
            Editor editor = (Editor) gui;
            String openFile = editor.getOpenFileName();
            if (openFile != null)
                if (!openFile.isEmpty())
                    LiteModFileExplorer.core.getDrawingHelper().drawText("Open file:\n\u00a77" + openFile, new Vector(258 + 10, 5), 0xFFFFFF, true, -1, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        }

        /*if (LiteModFileExplorer.config.showOpenDirectory)
        {
            gui = this.getGui("explorer");
            if (gui != null)
            {
                String openFile = ((GuiFileExplorer) gui).getCurrentPath();
                this.explorerHeightOffset = (9 * LiteModFileExplorer.core.getDrawingHelper().drawText("Open directory:\n§7" + openFile, new Vector(5, height - 5), 0xFFFFFF, true, 258, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.BOTTOM)) + 4;
            }
        } else this.explorerHeightOffset = 0;*/
    }

    public void openFile(File file)
    {
        String extension = FileLoader.getFileExtension(file.getName(), true);
        FileType fileType = LiteModFileExplorer.getFileType(extension);

        Editor editor = fileType.getNewEditor(268, 5, width - 213, height - 10, file);
        String name = fileType.getName();

        if (editor != null)
        {
            if (!this.openFileType.equals(""))
                this.removeElement("editor");
            this.openFileType = name;
            this.addGuiElement("editor", editor);
        }
    }

    @Override
    public boolean updateElement(String identifier, IGui gui)
    {
        if (identifier.equals("editor"))
        {
            Editor editor = (Editor) gui;
            editor.update(258 + 10, 25, width - 243 - 30, height - 30);
        }

        if (identifier.equals("explorer") && gui instanceof GuiFileExplorer)
            ((GuiFileExplorer) gui).setHeight(height - 10);

        return true;
    }
}
