package dk.mrspring.music.gui;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.llcore.*;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.gui.interfaces.IGui;
import dk.mrspring.music.gui.interfaces.IMouseListener;
import dk.mrspring.music.util.FileSorter;
import dk.mrspring.music.util.GuiHelper;
import dk.mrspring.music.util.TranslateHelper;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrSpring on 03-12-2014 for In-Game File Explorer.
 */
public class GuiFileExplorer implements IGui, IMouseListener
{
    int x, y, w, h, listHeight;
    boolean showControls = true;
    boolean showBackground = true;
    boolean showPath = true;
    List<GuiFileBase> guiFiles;
    String currentPath;
    IOnFileOpened onFileOpened;
    int pathX = 0, pathY = 0;

    boolean drawPath = false;
    GuiSimpleButton[] pathButtons;
    int scroll = 0;

    GuiSimpleButton openFile;

    GuiSimpleButton refreshList;
    GuiSimpleButton newFolder;
    GuiSimpleButton upOne;

    public GuiFileExplorer(int xPos, int yPos, int width, int height, String path)
    {
        x = xPos;
        y = yPos;
        w = width;
        h = height;
        currentPath = new File(path).getAbsolutePath();

        guiFiles = new ArrayList<GuiFileBase>();

        openFile = new GuiSimpleButton(x, y, 60, 20, "gui.explorer.open").disable();
        refreshList = new GuiSimpleButton(x, y, 60, 20, "gui.explorer.refresh");
        newFolder = new GuiSimpleButton(x, y, 60, 20, "gui.explorer.create_new")/*.setIcon(LiteModMusicPlayer.core.getIcon("new_file"))*/;
        upOne = new GuiSimpleButton(x, y, 60, 20, "gui.explorer.go_up");

//        this.newFolder.setEnabled(LiteModMusicPlayer.config.acceptFileManipulation);
//        this.deleteFile.setEnabled(LiteModFileExplorer.config.acceptFileManipulation);

        this.refreshList();
    }

    public GuiFileExplorer setOnFileOpened(IOnFileOpened onFileOpened)
    {
        this.onFileOpened = onFileOpened;
        return this;
    }

    public GuiFileExplorer setShowBackground(boolean showBackground)
    {
        this.showBackground = showBackground;
        return this;
    }

    public GuiFileExplorer setShowControls(boolean showControls)
    {
        this.showControls = showControls;
        return this;
    }

    public GuiFileExplorer setPathEditorPosition(int x, int y)
    {
        this.pathX = x;
        this.pathY = y;
        this.drawPath = true;
        this.refreshList();
        return this;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setWidth(int w)
    {
        this.w = w;
    }

    public void setHeight(int h)
    {
        this.h = h;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        int width = w;
        listHeight = h;

        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();

        if (showControls)
        {
            width -= 75;
            helper.drawVerticalLine(new Vector(x + width + 4 + 3, y + 6), h - 25 - 9, 1, true);
            this.drawControls(minecraft, mouseX, mouseY, x + width + 11);
        }

        if (showPath)
        {
            String openFile = getCurrentAbsolutePath();
            listHeight -= (9 * helper.drawText(TranslateHelper.translate("gui.explorer.open_directory") + ":\n\u00a77" + openFile, new Vector(x + 3, y + listHeight), 0xFFFFFF, true, width, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.BOTTOM)) + 4;
        }

        if (showBackground)
            helper.drawButtonThingy(new Quad(x - 2, y - 2, width + 11, listHeight + 4), 0, true);

        int yOffset = -scroll+3, xOffset = 2;
//        helper.drawShape(new Quad(x, y-1, width - xOffset+10, 1));

        GLClippingPlanes.glEnableClipping(x, x + width + 5, y, y + listHeight);
        int totalHeight = this.getListHeight();
        if (totalHeight > listHeight)
            xOffset = 8 + 3;
        if (guiFiles.size() > 0)
        {
            for (GuiFileBase guiFile : guiFiles)
            {
                if (!drawGuiFile(xOffset, yOffset, listHeight, guiFile))
                {
                    guiFile.setX(xOffset + x);
                    guiFile.setY(yOffset + y);
                    guiFile.setWidth(width + 5 - xOffset);
                    guiFile.draw(minecraft, mouseX, mouseY);
                }
                yOffset += guiFile.getHeight() + 5;
            }
        } else
        {
            int textMaxLength = w;
            if (this.showControls)
                textMaxLength -= 75;

            int textX = x + textMaxLength / 2, textY = y + 10;
            helper.drawText(TranslateHelper.translate("gui.explorer.no_files"), new Vector(textX, textY), 0xFFFFFF, true, textMaxLength, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.TOP);
        }
        GLClippingPlanes.glDisableClipping();

        if (totalHeight > listHeight)
        {
            double progress = ((double) scroll) / ((double) getMaxScroll());
            int scrollbarRange = listHeight - 2 - 6;
            double sizeProgress = ((double) listHeight) / ((double) getListHeight());
            int scrollbarHeight = (int) (sizeProgress * ((double) scrollbarRange));
            int scrollbarY = (int) (progress * (double) (scrollbarRange - scrollbarHeight));

            System.out.println(listHeight + ", " + h + ", " + scroll + ", " + getMaxScroll());

            float alpha = 0.5F;
            helper.drawShape(new Quad(3, 4, 6, scrollbarRange).setColor(Color.BLACK).setAlpha(alpha));
            helper.drawShape(new Quad(4, 3, 4, 1).setColor(Color.BLACK).setAlpha(alpha));
            helper.drawShape(new Quad(4, listHeight - 4, 4, 1).setColor(Color.BLACK).setAlpha(alpha));
            helper.drawShape(new Quad(4, scrollbarY + 4, 4, scrollbarHeight));
        }
    }

    private void drawControls(Minecraft minecraft, int mouseX, int mouseY, int xPos)
    {
        this.openFile.setX(xPos);
        this.openFile.setY(y + 3);
        this.openFile.draw(minecraft, mouseX, mouseY);

        this.refreshList.setX(xPos);
        this.refreshList.setY(y + 3 + 25);
        this.refreshList.setWidth(60);
        this.refreshList.draw(minecraft, mouseX, mouseY);

        this.newFolder.setX(xPos);
        this.newFolder.setY(y + 3 + 50);
        this.newFolder.draw(minecraft, mouseX, mouseY);

        this.upOne.setX(xPos);
        this.upOne.setY(y + 3 + 75);
        this.upOne.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public void update()
    {
        int totalHeight = this.guiFiles.size() * 35;
        if (totalHeight < this.h)
            scroll = 0;

        for (GuiFileBase guiFile : this.guiFiles)
        {
            guiFile.update();
        }

        if (showControls)
        {
            this.openFile.update();
            this.refreshList.update();
            this.newFolder.update();
            this.upOne.update();
        }

        if (drawPath)
        {
            if (this.pathButtons != null)
                if (this.pathButtons.length > 0)
                    for (GuiSimpleButton button : this.pathButtons)
                        button.update();
        }
    }

    private boolean drawGuiFile(int xOffset, int yOffset, int height, GuiFileBase file)
    {
        boolean isFileTooHigh = yOffset < -70;
        boolean isFileTooLow = yOffset > height + 40;
        return (isFileTooHigh || isFileTooLow);
    }

    public GuiFileExplorer setShowPath(boolean showPath)
    {
        this.showPath = showPath;
        return this;
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h))
        {
            if (this.openFile.mouseDown(mouseX, mouseY, mouseButton))
                return this.openSelectedFile();
            else if (this.refreshList.mouseDown(mouseX, mouseY, mouseButton))
                this.refreshList();
            else if (this.newFolder.mouseDown(mouseX, mouseY, mouseButton))
                this.createNewFile();
            else if (this.upOne.mouseDown(mouseX, mouseY, mouseButton))
                this.goUpOne();
            else if (this.isPathClicked(mouseX, mouseY, mouseButton))
                return true;
            else
            {
                boolean returnFromHere = false;
                this.openFile.disable();
                for (GuiFileBase guiFile : this.guiFiles)
                    if (guiFile.mouseDown(mouseX, mouseY, mouseButton) && guiFile instanceof GuiFile)
                    {
                        if (!((GuiFile) guiFile).isDirectory())
                            openFile.enable();
                        returnFromHere = true;
                    }

                if (returnFromHere)
                    return true;
            }
        }
        return false;
    }

    private boolean isPathClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (this.drawPath)
            if (this.pathButtons != null)
                if (this.pathButtons.length > 0)
                {
                    String pathSoFar = "";
                    for (GuiSimpleButton button : this.pathButtons)
                    {
                        pathSoFar += button.text;
                        if (button.mouseDown(mouseX, mouseY, mouseButton))
                        {
                            this.openFile(new File(pathSoFar));
                            return true;
                        } else pathSoFar += File.separator;
                    }
                }
        return false;
    }

    private void goUpOne()
    {
        int lastIndexOfSeperator = this.currentPath.lastIndexOf(File.separator);
        if (lastIndexOfSeperator > 1)
        {
            String goTo = this.currentPath.substring(0, lastIndexOfSeperator + 1);
            this.openFile(new File(goTo));
        }
    }

    private boolean openSelectedFile()
    {
        for (GuiFileBase guiFile : this.guiFiles)
        {
            if (guiFile instanceof GuiFile)
                if (((GuiFile) guiFile).isSelected())
                {
                    this.openFile(((GuiFile) guiFile).getFile());
                    return true;
                }
        }
        return false;
    }

    private void openFile(File file)
    {
        if (file != null)
        {
            if (file.exists())
            {
                if (file.isDirectory())
                {
                    this.currentPath = file.toString();
                    this.refreshList();
                    this.openFile.disable();
                    this.scroll = 0;
                } else
                {
                    if (this.onFileOpened != null)
                    {
                        this.onFileOpened.onOpened(file);
                    }
                }
            }
        }
    }

    private void refreshList()
    {
        this.guiFiles = new ArrayList<GuiFileBase>();

        File[] filesAtCurrentPath = LiteModMusicPlayer.core.getFileLoader().getFilesInFolder(new File(currentPath), false, FileLoader.DEFAULT_FILTER);

//        FileLoader.addFiles(currentPath, filesAtCurrentPath, false);

        FileSorter.sortFiles(filesAtCurrentPath, LiteModMusicPlayer.config.file_sort_type);

        for (File file : filesAtCurrentPath)
        {
            guiFiles.add(new GuiFile(0, 0, w - 10, 30, file, GuiFile.RenderType.LONG_GRID).setOnFileOpened(new Runnable()
            {
                @Override
                public void run()
                {
                    GuiFileExplorer.this.openSelectedFile();
                }
            }));
        }

        String actualPath = new File(currentPath).getPath();
        String[] foldersToPath = actualPath.split("\\" + File.separator);

        if (foldersToPath.length > 0)
        {
            this.pathButtons = new GuiSimpleButton[foldersToPath.length];
            int xOffset = 0;
            for (int i = 0; i < foldersToPath.length; i++)
            {
                String folderName = foldersToPath[i];
                int folderWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(folderName) + 6;
                this.pathButtons[i] = new GuiSimpleButton(pathX + xOffset, pathY, folderWidth, 14, folderName);
                xOffset += folderWidth + 6;
            }
        }
    }

    private void createNewFile()
    {
        this.guiFiles.add(0, new GuiFileNew(0, 0, w - 10, 20, new GuiFileNew.INewFileEvents()
        {
            @Override
            public void onCreated(GuiFileNew newFile, String path)
            {
                GuiFileExplorer.this.onFileCreated(newFile, path);
            }

            @Override
            public void onCanceled(GuiFileNew guiFileNew, String path)
            {
                GuiFileExplorer.this.cancelNewFile(guiFileNew);
            }
        }));
        this.scroll = 0;
    }

    private void onFileCreated(GuiFileNew newFile, String path)
    {
        try
        {
            File file = new File(this.currentPath + File.separator + path);
            int lastDot = path.lastIndexOf('.');
            if (!file.exists())
                if (lastDot >= 0)
                {
                    if (file.createNewFile())
                        guiFiles.add(0, new GuiFile(0, 0, w - 10, 30, file, GuiFileBase.RenderType.LONG_GRID));
                } else if (file.mkdir())
                    guiFiles.add(0, new GuiFile(0, 0, w - 10, 30, file, GuiFileBase.RenderType.LONG_GRID));

            guiFiles.remove(newFile);
        } catch (IOException e)
        {
            System.err.println("Failed to create file: \"" + path + "\":");
            e.printStackTrace();
        }
    }

    private void cancelNewFile(GuiFileNew guiFileNew)
    {
        this.guiFiles.remove(guiFileNew);
    }

    @Override
    public void mouseUp(int mouseX, int mouseY, int mouseButton)
    {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {

    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {
        for (GuiFileBase guiFile : this.guiFiles)
        {
            GuiFileNew newGuiFile = guiFile instanceof GuiFileNew ? ((GuiFileNew) guiFile) : null;
            if (newGuiFile != null)
                newGuiFile.handleKeyTyped(keyCode, character);
        }
    }

    private void addScroll(int scrollHeight)
    {
        int maxScrollHeight = getMaxScroll(), minScrollHeight = 0, scrollHeightAfterAddition = this.scroll + scrollHeight;

        if (scrollHeightAfterAddition > maxScrollHeight)
            scrollHeightAfterAddition = maxScrollHeight;
        else if (scrollHeightAfterAddition < minScrollHeight)
            scrollHeightAfterAddition = minScrollHeight;

        this.scroll = scrollHeightAfterAddition;
    }

    private int getMaxScroll()
    {
        return getListHeight() - listHeight+10;
    }

    private int getListHeight()
    {
        int height = 0;
        if (guiFiles.size() > 0)
            for (GuiFileBase guiFile : guiFiles)
//                if (!drawGuiFile(5, height, guiFile))
                height += guiFile.getHeight() + 5;
        return height;
    }

    @Override
    public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h))
        {
            int mouseWheel = dWheelRaw;
            mouseWheel /= 4;
            if (mouseWheel != 0)
                this.addScroll(-mouseWheel);
        }
    }

    public String getCurrentPath()
    {
        return currentPath;
    }

    public String getCurrentAbsolutePath()
    {
        return new File(getCurrentPath()).getAbsolutePath();
    }

    public interface IOnFileOpened
    {
        public void onOpened(File file);
    }
}
