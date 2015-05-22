package dk.mrspring.music.overlay;

/**
 * Created by Konrad on 30-04-2015.
 */
public class OverlayPosition
{
    public Preset preset = Preset.TOP_LEFT;
    public int x = 0, y = 0;
    public Alignment alignment = Alignment.LEFT;
    public NextUpAlignment next_up_alignment = NextUpAlignment.BOTTOM;
    public boolean center_text = false;

    public Alignment getAlignment()
    {
        if (preset == Preset.CUSTOM)
            return this.alignment;
        else return preset.alignment;
    }

    public int getX(int screenWidth, int fullWidth)
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
                return screenWidth/* - fullWidth*/;
            case BOTTOM_CENTER:
            case TOP_CENTER:
                return (screenWidth / 2) - (fullWidth / 2);
            default:
                return this.x;
        }
    }

    public int getY(int screenHeight, int height)
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
                return screenHeight - height;
            case LEFT_CENTER:
            case RIGHT_CENTER:
                return (screenHeight / 2) - (height / 2);
            default:
                return this.y;
        }
    }

    public boolean getCenterText()
    {
        if (preset == Preset.CUSTOM)
            return this.center_text;
        else return preset.centerText;
    }

    public enum Preset
    {
        TOP_LEFT(Alignment.LEFT, NextUpAlignment.BOTTOM, false),
        TOP_RIGHT(Alignment.RIGHT, NextUpAlignment.BOTTOM, false),
        TOP_CENTER(Alignment.LEFT, NextUpAlignment.BOTTOM, true),
        BOTTOM_LEFT(Alignment.LEFT, NextUpAlignment.TOP, false),
        BOTTOM_RIGHT(Alignment.RIGHT, NextUpAlignment.TOP, false),
        BOTTOM_CENTER(Alignment.LEFT, NextUpAlignment.TOP, true),
        LEFT_CENTER(Alignment.LEFT, NextUpAlignment.BOTTOM, false),
        RIGHT_CENTER(Alignment.RIGHT, NextUpAlignment.BOTTOM, false),
        CUSTOM(Alignment.LEFT, NextUpAlignment.BOTTOM, false);

        public final Alignment alignment;
        public final NextUpAlignment nextUpAlignment;
        public final boolean centerText;

        Preset(Alignment alignment, NextUpAlignment nextUpAlignment, boolean center)
        {
            this.alignment = alignment;
            this.nextUpAlignment = nextUpAlignment;
            this.centerText = center;
        }
    }

    public enum Alignment
    {
        LEFT,
        RIGHT
    }

    public enum NextUpAlignment
    {
        TOP,
        BOTTOM
    }
}
