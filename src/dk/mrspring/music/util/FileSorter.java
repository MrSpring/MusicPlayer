package dk.mrspring.music.util;

import dk.mrspring.llcore.FileLoader;
import dk.mrspring.music.LiteModMusicPlayer;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Konrad on 09-02-2015.
 */
public class FileSorter
{
    private static Comparator<File> nameComparator;
    private static Comparator<File> typeComparator;
    private static Comparator<File> dateComparator;
    private static Comparator<File> dirComparator;

    public static void load()
    {
        setNameComparator(new Comparator<File>()
        {
            @Override
            public int compare(File file1, File file2)
            {
                return file1.getName().compareToIgnoreCase(file2.getName());
            }
        });

        setTypeComparator(new Comparator<File>()
        {
            @Override
            public int compare(File o1, File o2)
            {
                String ext1 = FileUtils.getFileExtension(o1.getName(), false), ext2 = FileUtils.getFileExtension(o2.getName(), false);
                return ext1.compareToIgnoreCase(ext2);
            }
        });

        setDateComparator(new Comparator<File>()
        {
            @Override
            public int compare(File file1, File file2)
            {
                return Long.compare(file1.lastModified(), file2.lastModified());
            }
        });

        setDirComparator(new Comparator<File>()
        {
            @Override
            public int compare(File file1, File file2)
            {
                if (file1.isDirectory() && !file2.isDirectory())
                    return -1;
                else if (!file1.isDirectory() && file2.isDirectory())
                    return 1;
                else return nameComparator.compare(file1, file2);
            }
        });
    }

    public static void setNameComparator(Comparator<File> nameComparator)
    {
        FileSorter.nameComparator = nameComparator;
    }

    public static void setTypeComparator(Comparator<File> typeComparator)
    {
        FileSorter.typeComparator = typeComparator;
    }

    public static void setDateComparator(Comparator<File> dateComparator)
    {
        FileSorter.dateComparator = dateComparator;
    }

    public static void setDirComparator(Comparator<File> dirComparator)
    {
        FileSorter.dirComparator = dirComparator;
    }

    public static void sortFiles(File[] files, SortingType type)
    {
        System.out.println("Sorting files");
        switch (type)
        {
            case NAME:
                sortByName(files);
                break;
            case TYPE:
                sortByType(files);
                break;
            case DATE:
                sortByDate(files);
                break;
        }
    }

    private static void sortByName(File[] files)
    {
        sortUsingComparator(files, nameComparator);
        if (LiteModMusicPlayer.config.sort_folders)
            sortFolders(files);
    }

    private static void sortByType(File[] files)
    {
        sortUsingComparator(files, typeComparator);
    }

    private static void sortByDate(File[] files)
    {
        sortUsingComparator(files, dateComparator);
    }

    private static void sortFolders(File[] files)
    {
        sortUsingComparator(files, dirComparator);
    }

    private static void sortUsingComparator(File[] files, Comparator<File> comparator)
    {
        Arrays.sort(files, comparator);
    }

    public enum SortingType
    {
        NAME, TYPE, DATE
    }
}
