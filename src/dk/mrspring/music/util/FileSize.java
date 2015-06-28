package dk.mrspring.music.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Konrad on 18-02-2015.
 */
public class FileSize
{
    private static final DecimalFormat KB_FORMAT = new DecimalFormat("0.##");
    private static final DecimalFormat MB_FORMAT = new DecimalFormat("0.##");
    private static final DecimalFormat GB_FORMAT = new DecimalFormat("0.##");
    long bytes;

    public FileSize(long bytes)
    {
        this.bytes = bytes;
    }

    @Override
    public String toString()
    {
        if (bytes < 1024)
        {
            return String.valueOf(bytes) + " B";
        } else if (bytes < 1048576)
        {
            BigDecimal kb = new BigDecimal(bytes).setScale(2, BigDecimal.ROUND_HALF_UP);
            kb = kb.divide(new BigDecimal(1024), BigDecimal.ROUND_HALF_UP);
            return KB_FORMAT.format(kb) + " KB";
        } else if (bytes < 1073741824)
        {
            BigDecimal mb = new BigDecimal(bytes).setScale(2, BigDecimal.ROUND_HALF_UP);
            mb = mb.divide(new BigDecimal(1048576), BigDecimal.ROUND_HALF_UP);
            return MB_FORMAT.format(mb) + " MB";
        } else
        {
            BigDecimal gb = new BigDecimal(bytes).setScale(2, BigDecimal.ROUND_HALF_UP);
            gb = gb.divide(new BigDecimal(1073741824), BigDecimal.ROUND_HALF_UP);
            return GB_FORMAT.format(gb) + " GB";
        }
    }
}
