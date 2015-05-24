package dk.mrspring.music.overlay;

/**
 * Created by Konrad on 30-04-2015.
 */
public class OverlayPosition
{
    public Preset preset = Preset.TOP_LEFT;
    public int x = 0, y = 0;
    CoverAlignment cover_alignment = CoverAlignment.TOP_LEFT;

    public CoverAlignment getAlignment()
    {
        return this.preset == Preset.CUSTOM ? this.cover_alignment : preset.coverAlignment;
    }

    public int getX(int screenWidth, int size)
    {
        switch (preset)
        {
            case TOP_LEFT:
            case BOTTOM_LEFT:
            case LEFT_CENTER:
                return 0;
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
            case RIGHT_CENTER:
                return screenWidth;
            case BOTTOM_CENTER:
            case TOP_CENTER:
                return (screenWidth / 2) - (size / 2);
            default:
                return this.x;
        }
    }

    public int getY(int screenHeight, int size)
    {
        switch (preset)
        {
            case TOP_LEFT:
            case TOP_RIGHT:
            case TOP_CENTER:
                return 0;
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
            case BOTTOM_CENTER:
                return screenHeight;
            case LEFT_CENTER:
            case RIGHT_CENTER:
                return (screenHeight / 2) - (size / 2);
            default:
                return this.y;
        }
    }

    public enum Preset
    {
        TOP_LEFT(CoverAlignment.TOP_LEFT),
        TOP_RIGHT(CoverAlignment.TOP_RIGHT),
        TOP_CENTER(CoverAlignment.TOP_CENTER),
        BOTTOM_LEFT(CoverAlignment.BOTTOM_LEFT),
        BOTTOM_RIGHT(CoverAlignment.BOTTOM_RIGHT),
        BOTTOM_CENTER(CoverAlignment.BOTTOM_CENTER),
        LEFT_CENTER(CoverAlignment.CENTER_LEFT),
        RIGHT_CENTER(CoverAlignment.CENTER_RIGHT),
        CUSTOM(CoverAlignment.TOP_LEFT);

        public final CoverAlignment coverAlignment;

        Preset(CoverAlignment cover)
        {
            this.coverAlignment = cover;
        }
    }

    public enum CoverAlignment
    {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        CENTER_LEFT,
        CENTER_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT
    }
}
