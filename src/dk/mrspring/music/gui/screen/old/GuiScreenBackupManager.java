package dk.mrspring.music.gui.screen.old;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.backup.BackupEntry;
import dk.mrspring.fileexplorer.backup.BackupManager;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.gui.interfaces.IMouseListener;
import dk.mrspring.fileexplorer.helper.TranslateHelper;
import dk.mrspring.fileexplorer.loader.FileLoader;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import dk.mrspring.music.gui.screen.GuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by MrSpring on 11-01-2015 for In-Game File Explorer - 1.8.0.
 */
public class GuiScreenBackupManager extends GuiScreen
{
    int imageTextLines = 0;

    public GuiScreenBackupManager(String title, net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super(title, previousScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.hideBars().hideTitle();
        this.addGuiElement("done", new GuiSimpleButton(width - 40 - 10, height - 20 - 10, 40, 20, "gui.screen.backup_manager.done"));
        this.addGuiElement("list", new GuiBackupList(10, 10, Math.min(width - 20, 220), height - 20, LiteModFileExplorer.backupManager));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        LiteModFileExplorer.core.getDrawingHelper().drawShape(new Quad(0, 0, width, height).setColor(Color.BLACK).setAlpha(0.5F));
        super.drawScreen(mouseX, mouseY, partialTicks);
        float imageWidth = width - 220 - 30;
        if (imageWidth > 20)
        {
            mc.getTextureManager().bindTexture(new ResourceLocation("fileexplorer", "cats/dog.png"));
            DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
            float maxImageHeight = height - 20 - 30;
            float imageHeight = Math.min((((float) 456 / (float) 600) * imageWidth), maxImageHeight);
            imageWidth = (600F / 456F) * imageHeight;
            helper.setAllowTextures(true);
            LiteModFileExplorer.core.getDrawingHelper().drawTexturedShape(new Quad(240, 10, imageWidth, imageHeight).setColor(Color.BLACK));
            helper.setAllowTextures(false);
            helper.drawText(TranslateHelper.translate("gui.screen.backup_manager.dog"), new Vector(245, 7 + imageHeight), 0xFFFFFF, true, (int) imageWidth - 10, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.BOTTOM);
        }
    }

    @Override
    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {
        if (identifier.equals("done"))
        {
            mc.displayGuiScreen(this.previousScreen);
        } else super.guiClicked(identifier, gui, mouseX, mouseY, mouseButton);
    }

    public class GuiBackupList implements IGui, IMouseListener
    {
        int x, y, width, height;
        List<GuiBackupEntry> backupEntries;
        int scrollHeight = 0, listHeight = 0;

        public GuiBackupList(int x, int y, int width, int height, BackupManager manager)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;

            backupEntries = new ArrayList<GuiBackupEntry>();

            updateList(manager);
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY)
        {
            int yOffset = 0;
            if (this.listHeight > height)
                yOffset = -scrollHeight;
            this.listHeight = 0;
            for (GuiBackupEntry entry : backupEntries)
            {
                int height = entry.draw(minecraft, mouseX, mouseY, x, y + yOffset, width);
                yOffset += height + 5;
                this.listHeight += height + 5;
            }
            if (this.listHeight > this.height)
                this.drawScrollBar();
        }

        private void drawScrollBar()
        {
            float scrollBarYRange = (height - 40);
            float maxScrollHeight = getMaxScrollHeight();
            float scrollProgress = (float) this.scrollHeight / maxScrollHeight;
            float scrollBarY = scrollBarYRange * scrollProgress;
            DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
            helper.drawShape(new Quad(x - 5, y + scrollBarY + 1, 2, 40).setColor(Color.DK_GREY));
            helper.drawShape(new Quad(x - 6, y + scrollBarY, 2, 40).setColor(Color.WHITE));
        }

        private int getMaxScrollHeight()
        {
            return listHeight - height - 5;
        }

        @Override
        public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
        {
            int mouseWheel = dWheelRaw;
            mouseWheel /= 4;
            if (mouseWheel != 0)
                this.addScroll(-mouseWheel);
        }

        public void addScroll(int amount)
        {
            int maxScrollHeight = getMaxScrollHeight(), minScrollHeight = 0, scrollHeightAfterAddition = this.scrollHeight + amount;

            if (scrollHeightAfterAddition > maxScrollHeight)
                scrollHeightAfterAddition = maxScrollHeight;
            else if (scrollHeightAfterAddition < minScrollHeight)
                scrollHeightAfterAddition = minScrollHeight;

            this.scrollHeight = scrollHeightAfterAddition;
        }

        public void updateList(BackupManager manager)
        {
            for (BackupEntry entry : manager.entries)
                backupEntries.add(new GuiBackupEntry(entry));
        }

        @Override
        public void update()
        {
            for (GuiBackupEntry entry : backupEntries)
                entry.update();
        }

        @Override
        public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
        {
            for (Iterator<GuiBackupEntry> iterator = backupEntries.iterator(); iterator.hasNext(); )
            {
                GuiBackupEntry entry = iterator.next();
                if (entry.mouseDown(mouseX, mouseY, mouseButton))
                {
                    BackupEntry backupEntry = LiteModFileExplorer.backupManager.restoreBackup(entry.backupID);
                    if (backupEntry != null)
                    {
                        FileLoader.restoreBackup(backupEntry.getBackupFile(), backupEntry.getOriginalFile());
                        iterator.remove();
                    }
                }
            }
            return false;
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

        }
    }

    public class GuiBackupEntry
    {
        GuiSimpleButton restoreButton;
        String name;
        String date;
        String cause;
        int backupID;
        int height = 0;

        public GuiBackupEntry(BackupEntry entry)
        {
            name = entry.getOriginalFile().getName();
            date = entry.getBackupDate().toString();
            cause = entry.getCause();
            backupID = entry.getBackupID();
            restoreButton = new GuiSimpleButton(0, 0, 50, 0, "gui.screen.backup_manager.restore");
        }

        public int draw(Minecraft minecraft, int mouseX, int mouseY, int xPosition, int yPosition, int width)
        {
            DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();

            helper.drawButtonThingy(new Quad(xPosition, yPosition, width, this.height), 0, false);

            restoreButton.setX(xPosition + width - restoreButton.getWidth() - 4);
            restoreButton.setY(yPosition + 4);

            restoreButton.draw(minecraft, mouseX, mouseY);

            String rendering = name + "\n\u00a77" + date + "\n" + TranslateHelper.translate("backup_cause." + cause);

            int lines = helper.drawText(rendering, new Vector(xPosition + 5, yPosition + 5), 0xFFFFFF, true, width - 10 - restoreButton.getWidth(), DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);

            height = lines * 9 + 9;
            restoreButton.setHeight(height - 8);

            return this.height;
        }

        public void update()
        {
            restoreButton.update();
        }

        public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
        {
            return restoreButton.mouseDown(mouseX, mouseY, mouseButton);
        }
    }
}
