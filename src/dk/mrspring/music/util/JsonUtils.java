package dk.mrspring.music.util;

import dk.mrspring.music.LiteModMusicPlayer;

import java.io.File;
import java.io.IOException;

/**
 * Created by Konrad on 26-04-2015.
 */
public class JsonUtils
{
    public static <T> T loadFromJson(File jsonFile, Class<? extends T> type)
    {
        try
        {
            String fileContents = LiteModMusicPlayer.core.getFileLoader().getContentsFromFile(jsonFile);
            return LiteModMusicPlayer.core.getJsonHandler().loadFromJson(fileContents, type);
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String toJsonCode(Object object)
    {
        return LiteModMusicPlayer.core.getJsonHandler().toJson(object);
    }

    public static boolean writeToFile(File file, Object object)
    {
        try
        {
            String jsonCode = toJsonCode(object);
            return LiteModMusicPlayer.core.getFileLoader().writeToFile(file, jsonCode);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
