package dk.mrspring.music.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by Konrad on 26-04-2015.
 */
public class FileUtils
{
    public static boolean createFile(File file)
    {
        try
        {
            return file.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
