package dk.mrspring.music.util;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by MrSpring on 02-07-2015 for MC Music Player.
 */
public class FileTypeFilter implements FileFilter
{
    String extension = null;
    boolean dot = false;

    public FileTypeFilter(String type)
    {
        if (type == null)
            throw new IllegalArgumentException("Filtering exception cannot be null");
        this.extension = type;
        dot = extension.contains(".");
    }

    @Override
    public boolean accept(File pathname)
    {
        String pathNameExtension = FileUtils.getFileExtension(pathname, dot);
        return pathNameExtension.equals(this.extension);
    }
}
