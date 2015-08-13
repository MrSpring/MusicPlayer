package dk.mrspring.music.gui;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.util.FileSize;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.nio.file.Files;
import java.util.Date;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiFile extends GuiFileBase
{
    GuiSimpleButton button;
    boolean wrapName = true;
    FileSize cachedFileSize;
    private Runnable onFileOpened = new Runnable()
    {
        @Override
        public void run()
        {

        }
    };

    public GuiFile(int xPos, int yPos, int width, int height, String path, RenderType renderType)
    {
        super(xPos, yPos, width, height);

        this.filePath = path;
        button = new GuiSimpleButton(x, y, w, h, "");
        this.renderType = renderType;
    }

    public GuiFile(int xPos, int yPos, int width, int height, File path, RenderType type)
    {
        this(xPos, yPos, width, height, path.getPath(), type);
    }

    public GuiFile setOnFileOpened(Runnable onFileOpened)
    {
        this.onFileOpened = onFileOpened;
        return this;
    }

    /*public FileType getFileType()
    {
        if (filePath != null)
        {
            String extension = FileLoader.getFileExtension(filePath, true);
            if (extension.equals("") || new File(filePath).isDirectory())
                return LiteModFileExplorer.getFileType("directory");
            else return LiteModFileExplorer.getFileType(extension);
        } else return LiteModFileExplorer.getFileType("unknown");
    }*/

    public boolean isDirectory()
    {
        return filePath != null && new File(filePath).isDirectory();
    }

    public void setX(int x)
    {
        super.setX(x);
        this.button.setX(x);
    }

    public void setY(int y)
    {
        super.setY(y);
        this.button.setY(y);
    }

    public void setWidth(int w)
    {
        super.setWidth(w);
        this.button.setWidth(w);
    }

    public void setHeight(int h)
    {
        super.setHeight(h);
        this.button.setHeight(h);
    }

    public boolean isSelected()
    {
        return this.button.isHighlighted();
    }

    public void setRenderType(RenderType renderType)
    {
        this.renderType = renderType;
    }

    public File getFile()
    {
        return new File(filePath);
    }

    public String getShortFileName()
    {
        return getFile().getName();
    }

    public long getLastEdit()
    {
        return getFile().lastModified();
    }

    public Date getLastEditDate()
    {
        return new Date(getLastEdit());
    }

    public FileSize getFileSize()
    {
        if (this.cachedFileSize == null)
        {
            try
            {
                this.cachedFileSize = new FileSize(Files.size(getFile().toPath()));
            } catch (Exception e)
            {
                System.err.println("Failed getting file size for file: \"" + getFile().toString() + "\":");
                e.printStackTrace();
            } finally
            {
                if (cachedFileSize == null)
                    cachedFileSize = new FileSize(0);
            }
        }
        return cachedFileSize;
    }

    public void setWrapName(boolean wrapName)
    {
        this.wrapName = wrapName;
    }

    public boolean wrapName()
    {
        return wrapName;
    }

    private void renderSquare(Minecraft minecraft)
    {
        int nameHeight = 16;
        int nameWidth = minecraft.fontRendererObj.getStringWidth(this.getShortFileName());
        float iconSize = Math.min(w, h - nameHeight);
        int nameY = y + h - nameHeight + (nameHeight / 2) - 4;
        if (nameWidth > w - 8 && wrapName())
        {
            nameHeight += 9;
            nameY -= 9;
        }

        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        helper.drawIcon(this.isDirectory() ? LiteModMusicPlayer.core.getIcon("folder") : LiteModMusicPlayer.core.getIcon("unknown"), new Quad(x - ((iconSize - w) / 2), y, iconSize, iconSize));

        helper.drawShape(new Quad(x, y + h - nameHeight, w, 1).setColor(Color.WHITE));
        helper.drawShape(new Quad(x, y + h - nameHeight - 1, w, 1).setColor(Color.LT_GREY));
        if (wrapName())
        {
            minecraft.fontRendererObj.drawSplitString(this.getShortFileName(), x + 5, nameY + 1, w - 8, 0x4C4C4C);
            minecraft.fontRendererObj.drawSplitString(this.getShortFileName(), x + 4, nameY, w - 8, 0xFFFFFF);
        } else minecraft.fontRendererObj.drawString(this.getShortFileName(), x + 4, nameY, 0xFFFFFF, true);
    }

    private void renderBigList(Minecraft minecraft, int mouseX, int mouseY, float partialTicks)
    {
        float iconSize = LiteModMusicPlayer.config.explorer_icon_size;

        button.setWidth(w);
        button.setHeight(h);
        button.setX(x);
        button.setY(y);
        button.draw(minecraft, mouseX, mouseY, partialTicks);

        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();


        String rendering = this.getShortFileName();
        if (LiteModMusicPlayer.config.show_file_edit_below_name)
            rendering += "\n\u00a77" + getLastEditDate().toString();
        if (LiteModMusicPlayer.config.show_file_size_below_name && !getFile().isDirectory())
            rendering += "\n\u00a77" + getFileSize().toString();
        DrawingHelper.TextRenderResult result = helper.drawText(rendering, new Vector(x + (int) iconSize + 3, y + (h / 2)), 0xFFFFFF, true, w - (int) iconSize - 6, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.CENTER);
        this.h = result.getTotalHeight() + ((int) iconSize - 9);

        helper.drawShape(new Quad(x + iconSize - 2, y + 1, 1, h - 2).setColor(Color.LT_GREY));
        helper.drawShape(new Quad(x + iconSize - 1, y + 1, 1, h - 2).setColor(Color.WHITE));

        helper.drawIcon(this.isDirectory() ? LiteModMusicPlayer.core.getIcon("folder") : LiteModMusicPlayer.core.getIcon("unknown"), new Quad(x + 2, y + 2 + (h / 2 - (iconSize / 2)), iconSize - 4, iconSize - 4));
    }

    private void renderList(Minecraft minecraft)
    {
        minecraft.fontRendererObj.drawString(this.getShortFileName(), x + 3, y + (h / 2) - 4, 0xFFFFFF, true);
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY, float partialTicks)
    {
        //DrawingHelper.drawButtonThingy(x, y, w, h, Color.BLACK, 0.25F, Color.WHITE, 1F);
//        button.draw(minecraft, mouseX, mouseY);

        RenderType type = this.getRenderType();
        switch (type)
        {
            case SQUARE_GRID:
                renderSquare(minecraft);
                break;
            case LONG_GRID:
                renderBigList(minecraft, mouseX, mouseY, partialTicks);
                break;
            case LIST:
                renderList(minecraft);
                break;
        }
    }

    @Override
    public void update()
    {
        button.update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (this.button.mouseDown(mouseX, mouseY, mouseButton))
        {
            this.button.makeHighlighted();
//            long systemTime = System.currentTimeMillis();
//            if (systemTime - timeAtLastClick < 250)
//            {
//                this.onFileOpened.run();
//            }
//            this.timeAtLastClick = System.currentTimeMillis();
            return true;
        } else
        {
            this.button.stopBeingHighlighted();
            return false;
        }
    }
}
