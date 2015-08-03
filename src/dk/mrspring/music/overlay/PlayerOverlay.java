package dk.mrspring.music.overlay;

import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.Config;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.player.MusicHandler;
import dk.mrspring.music.util.Miscellaneous;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StatCollector;

import static dk.mrspring.music.overlay.OverlayPosition.CoverAlignment;
import static dk.mrspring.music.LiteModMusicPlayer.config;
import static dk.mrspring.music.LiteModMusicPlayer.core;
import static dk.mrspring.llcore.DrawingHelper.VerticalTextAlignment;
import static dk.mrspring.llcore.DrawingHelper.HorizontalTextAlignment;

/**
 * Created by Konrad on 27-04-2015.
 */
public class PlayerOverlay
{
    double nextUpProgress = 0F, sizeProgress = 0F;

    boolean expanded = false;
    boolean showNext = false;

    TextRender currentlyPlayingText;
    TextRender nextUpText;

    long showNextTil = 0;

    private int _screenWidth = 0;
    private int _screenHeight = 0;
    private int _paddingX = 0;
    private int _paddingY = 0;
    private int _coverSize = 0;
    private CoverAlignment _alignment;

    public void draw(MusicHandler musicHandler, Minecraft minecraft)
    {
        doPlayingText(musicHandler.getCurrentlyPlaying(), minecraft.fontRendererObj);
        doNextUpText(musicHandler.getNextUp(), minecraft.fontRendererObj);
        showNext = showNextTil > System.currentTimeMillis();

        double easing = config.overlay_size_easing_speed;
        this.sizeProgress = Miscellaneous.smoothDamp(expanded ? 1D : 0D, sizeProgress, easing);

        easing = config.overlay_next_up_easing_speed;
        this.nextUpProgress = Miscellaneous.smoothDamp(showNext ? 1D : 0D, nextUpProgress, easing);

        OverlayPosition position = config.overlay_position;

        ScaledResolution resolution = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        _screenWidth = resolution.getScaledWidth();
        _screenHeight = resolution.getScaledHeight();
        _paddingX = config.overlay_x_screen_padding;
        _paddingY = config.overlay_y_screen_padding;
        _coverSize = config.cover_size;
        _alignment = position.getAlignment();

        int textHeight = currentlyPlayingText.getTotalHeight(), textWidth = currentlyPlayingText.getLongestLine();

        int yOverlapTarget = 0;
        int xOverlapTarget = 0;
        int heightOverlapTarget = 0;
        int widthOverlapTarget = 0;

        switch (_alignment) // TODO: Rewrite again?
        {
            case TOP_LEFT:
                heightOverlapTarget = Math.max(0, textHeight + 10 - _coverSize);
                widthOverlapTarget = textWidth + 5;
                break;
            case TOP_CENTER:
            {
                int overlap = Math.max(0, (textWidth + 10) / 2 - (_coverSize / 2));
                xOverlapTarget = overlap;
                widthOverlapTarget = overlap;
                heightOverlapTarget = textHeight + 5;
                break;
            }
            case TOP_RIGHT:
                heightOverlapTarget = Math.max(0, textHeight + 10 - _coverSize);
                xOverlapTarget = textWidth + 5;
                break;
            case CENTER_LEFT:
            {
                int overlap = Math.max(0, (textHeight + 10) / 2 - (_coverSize / 2));
                yOverlapTarget = overlap;
                heightOverlapTarget = overlap;
                widthOverlapTarget = textWidth + 5;
                break;
            }
            case CENTER_RIGHT:
            {
                int overlap = Math.max(0, (textHeight + 10) / 2 - (_coverSize / 2));
                yOverlapTarget = overlap;
                heightOverlapTarget = overlap;
                xOverlapTarget = textWidth + 5;
                break;
            }
            case BOTTOM_LEFT:
                yOverlapTarget = Math.max(0, textHeight + 10 - _coverSize);
                widthOverlapTarget = textWidth + 5;
                break;
            case BOTTOM_CENTER:
            {
                int overlap = Math.max(0, (textWidth + 10) / 2 - (_coverSize / 2));
                xOverlapTarget = overlap;
                widthOverlapTarget = overlap;
                yOverlapTarget = textHeight + 5;
                break;
            }
            case BOTTOM_RIGHT:
                yOverlapTarget = Math.max(0, textHeight + 10 - _coverSize);
                xOverlapTarget = textWidth + 5;
                break;
        }

        int overlayWidth = _coverSize + (int) (sizeProgress * widthOverlapTarget) + (int) (sizeProgress * xOverlapTarget);
        int overlayHeight = _coverSize + (int) (sizeProgress * heightOverlapTarget) + (int) (sizeProgress * yOverlapTarget);
        int overlayX = _paddingX + position.getX(_screenWidth - _paddingX - _paddingX, _coverSize) - (int) (sizeProgress * xOverlapTarget);
        int overlayY = _paddingY + position.getY(_screenHeight - _paddingY - _paddingY, _coverSize) - (int) (sizeProgress * yOverlapTarget);

        overlayX = Math.max(_paddingX, overlayX);
        overlayY = Math.max(_paddingY, overlayY);
        overlayX = Math.min(_screenWidth - _paddingX - overlayWidth, overlayX);
        overlayY = Math.min(_screenHeight - _paddingY - overlayHeight, overlayY);

        if (nextUpProgress > 0.01)
            drawNextUpBox(overlayX, overlayY, overlayWidth, overlayHeight);
        drawTheFreakingThing(overlayX, overlayY, overlayWidth, overlayHeight, sizeProgress > 0.98);
    }

    public void drawTheFreakingThing(int x, int y, int width, int height, boolean drawText)
    {
        DrawingHelper helper = core.getDrawingHelper();
        helper.drawButtonThingy(new Quad(x, y, width, height), config.disable_overlay_gradient ? 0 : 1, true,
                config.overlay_start_color, config.overlay_start_alpha,
                config.overlay_end_color, config.overlay_end_alpha);

        int s = _coverSize - 6;
        HorizontalTextAlignment horizontal = HorizontalTextAlignment.CENTER;
        VerticalTextAlignment vertical = VerticalTextAlignment.LEFT;
        int coverX = x, textX = x + _coverSize, coverY = y, textY = y + (height / 2);
        switch (_alignment)
        {
            case TOP_RIGHT:
            case CENTER_RIGHT:
            case BOTTOM_RIGHT:
                coverX += width - _coverSize;
                textX = coverX;
                vertical = VerticalTextAlignment.RIGHT;
                break;
            case TOP_CENTER:
            case BOTTOM_CENTER:
                coverX += (width / 2) - (_coverSize / 2);
                textX = coverX + (_coverSize / 2);
                vertical = VerticalTextAlignment.CENTER;
                break;
        }
        switch (_alignment)
        {
            case TOP_CENTER:
                textY = y + _coverSize;
                horizontal = HorizontalTextAlignment.TOP;
                break;
            case CENTER_LEFT:
            case CENTER_RIGHT:
                coverY += (height / 2) - (_coverSize / 2);
                break;
            case BOTTOM_CENTER:
                textY = y + height - _coverSize;
                horizontal = HorizontalTextAlignment.BOTTOM;
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                coverY += height - _coverSize;
        }
        if (drawText)
            currentlyPlayingText.render(helper, textX, textY, 0xFFFFFF, true, vertical, horizontal);

        LiteModMusicPlayer.musicHandler.getCurrentlyPlaying().bindCover();
        helper.drawTexturedShape(new Quad(coverX + 3, coverY + 3, s, s));
    }

    public void drawNextUpBox(int x, int y, int width, int height)
    {
        int nextUpHeightTarget = nextUpText.getTotalHeight() + 10, nextUpWidthTarget = nextUpText.getLongestLine() + 10;

        int nextUpHeight = (int) (nextUpProgress * nextUpHeightTarget);
        int nextUpWidth = (int) (nextUpProgress * nextUpWidthTarget);

        int nextUpX = x, nextUpY = y;
        switch (_alignment)
        {
            case TOP_CENTER:
            case BOTTOM_CENTER:
                nextUpX = x + (width / 2) - (nextUpWidth / 2);
                break;
            case TOP_RIGHT:
            case CENTER_RIGHT:
            case BOTTOM_RIGHT:
                nextUpX = x + width - nextUpWidth;
                break;
        }
        switch (_alignment)
        {
            case TOP_LEFT:
            case TOP_CENTER:
            case TOP_RIGHT:
            case CENTER_LEFT:
            case CENTER_RIGHT:
            {
                int result = nextUpY + height + 5;
                nextUpY = result + nextUpHeightTarget > _screenHeight - _paddingY ? y - nextUpHeight - 5 : result;
                break;
            }
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT:
            {
                int result = nextUpY - nextUpHeightTarget - 5;
                nextUpY = result < _paddingY ? y + height + 5 : nextUpY - nextUpHeight - 5;
                break;
            }
        }

        nextUpX = Math.max(_paddingX, nextUpX);
        nextUpY = Math.max(_paddingY, nextUpY);
        nextUpX = Math.min(_screenWidth - _paddingX - nextUpWidth, nextUpX);
        nextUpY = Math.min(_screenHeight - _paddingY - nextUpHeight, nextUpY);

        DrawingHelper helper = core.getDrawingHelper();

        helper.drawButtonThingy(new Quad(nextUpX, nextUpY, nextUpWidth, nextUpHeight), config.disable_overlay_gradient ? 0 : 1, true,
                config.overlay_start_color, config.overlay_start_alpha,
                config.overlay_end_color, config.overlay_end_alpha);
        if (nextUpProgress > 0.98)
            nextUpText.render(LiteModMusicPlayer.core.getDrawingHelper(), nextUpX + 5, nextUpY + 5, 0xFFFFFF, true, VerticalTextAlignment.LEFT, HorizontalTextAlignment.TOP);
    }

    public void doPlayingText(Music playing, FontRenderer renderer)
    {
        String title = playing.getName();
        String album = playing.getAlbum();
        String artist = playing.getArtist();
        String rendering = StatCollector.translateToLocalFormatted("overlay.currently_playing", title, album, artist).replace("\\n", "\n").replace("§", "\u00a7");
        if (currentlyPlayingText == null)
            currentlyPlayingText = new TextRender(rendering, renderer);
        else currentlyPlayingText.setText(rendering);
    }

    public void doNextUpText(Music next, FontRenderer renderer)
    {
        String title = next.getName();
        String album = next.getAlbum();
        String artist = next.getArtist();
        String rendering = StatCollector.translateToLocalFormatted("overlay.next_up", title, album, artist).replace("\\n", "\n").replace("§", "\u00a7");
        if (nextUpText == null)
            nextUpText = new TextRender(rendering, renderer);
        else nextUpText.setText(rendering);
    }

    public void toggleExpanded()
    {
        expanded = !expanded;
    }

    public void showNext()
    {
        showNextTil = System.currentTimeMillis() + config.show_next_peek_time_millis;
        doNextUpText(LiteModMusicPlayer.musicHandler.getNextUp(), Minecraft.getMinecraft().fontRendererObj);
    }
}
