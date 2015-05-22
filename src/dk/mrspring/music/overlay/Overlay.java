package dk.mrspring.music.overlay;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.music.Config;
import dk.mrspring.music.LiteModMusicPlayer;
import dk.mrspring.music.player.Music;
import dk.mrspring.music.player.MusicHandler;
import dk.mrspring.music.util.Miscellaneous;

import static dk.mrspring.music.overlay.OverlayPosition.NextUpAlignment.*;
import static dk.mrspring.music.overlay.OverlayPosition.Alignment.*;
import static dk.mrspring.music.overlay.OverlayPosition.Alignment;
import static dk.mrspring.music.LiteModMusicPlayer.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StatCollector;

/**
 * Created by Konrad on 27-04-2015.
 */
public class Overlay
{
    double nextUpProgress = 0F, widthProgress = 0F;

    boolean expanded = false;
    boolean showNext = false;

    TextRender currentlyPlayingText;
    TextRender nextUpText;

    long showNextTil = 0;

    public void draw(MusicHandler musicHandler, Minecraft minecraft)
    {
        showNext = showNextTil > System.currentTimeMillis();

        Config config = LiteModMusicPlayer.config;

        OverlayPosition position = config.overlay_position;
        Alignment alignment = position.getAlignment();

        ScaledResolution resolution = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        final int SCREEN_WIDTH = resolution.getScaledWidth() - (2 * config.overlay_x_screen_padding), SCREEN_HEIGHT = resolution.getScaledHeight() - (2 * config.overlay_y_screen_padding);
        final int PADDING_X = config.overlay_x_screen_padding, PADDING_Y = config.overlay_y_screen_padding;

        doPlayingText(musicHandler.getCurrentlyPlaying(), minecraft.fontRendererObj);
        if (nextUpProgress > 0.01)
            doNextUpText(musicHandler.getNextUp(), minecraft.fontRendererObj);

        double easing = config.overlay_size_easing_speed;
        this.widthProgress = Miscellaneous.smoothDamp(expanded ? 1D : 0D, widthProgress, easing);

        easing = config.overlay_next_up_easing_speed;
        this.nextUpProgress = Miscellaneous.smoothDamp(showNext ? 1D : 0D, nextUpProgress, easing);

        int textWidthTarget = (currentlyPlayingText.getLongestLine() + 5);
        int textWidth = (int) (widthProgress * textWidthTarget);
        int height = config.cover_size;
        int width = height + textWidth;
        int x = config.overlay_x_screen_padding + position.getX(SCREEN_WIDTH, width) - (alignment == RIGHT ? textWidth : 0);
        int y = config.overlay_y_screen_padding + position.getY(SCREEN_HEIGHT, height);

        x = Math.max(PADDING_X, x);
        x = Math.min(SCREEN_WIDTH - width + PADDING_X, x);
        y = Math.max(PADDING_Y, y);
        y = Math.min(SCREEN_HEIGHT - height + PADDING_Y, y);

        drawTheFreakingThing(x, y, width, height, alignment, textWidth + 2 > textWidthTarget, SCREEN_WIDTH, SCREEN_HEIGHT, PADDING_X, PADDING_Y);
        /*FontRenderer renderer = minecraft.fontRendererObj;
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        ScaledResolution resolution = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        OverlayPosition position = LiteModMusicPlayer.config.overlay_position;
        final int SCREEN_WIDTH = resolution.getScaledWidth(), SCREEN_HEIGHT = resolution.getScaledHeight(), COVER_SIZE = LiteModMusicPlayer.config.cover_size;
        final Alignment ALIGNMENT = position.getAlignment();
        final int PADDING_X = LiteModMusicPlayer.config.overlay_x_screen_padding, PADDING_Y = LiteModMusicPlayer.config.overlay_y_screen_padding;

        final Music PLAYING = musicHandler.getCurrentlyPlaying();

        final String TITLE = PLAYING.getName();
        final String ALBUM = PLAYING.getAlbum();
        final String ARTIST = PLAYING.getArtist();

        String rendering = StatCollector.translateToLocalFormatted("overlay.currently_playing", TITLE, ALBUM, ARTIST).replace("\\n", "\n").replace("§", "\u00a7");
        if (currentlyPlayingText == null)
            currentlyPlayingText = new TextRender(rendering, renderer);
        else currentlyPlayingText.setText(rendering);
        int widthTarget = currentlyPlayingText.getLongestLine() + 6;

        double easing = LiteModMusicPlayer.config.overlay_size_easing_speed;

        if (easing < 1)
            this.widthProgress = Miscellaneous.smoothDamp(expanded ? 1D : 0D, widthProgress, easing);
        else this.widthProgress = expanded ? 1D : 0D;

        int textWidth = (int) (widthTarget * widthProgress);


        int width = COVER_SIZE + textWidth;
        int height = COVER_SIZE;
        int baseX = PADDING_X + position.getX(SCREEN_WIDTH - (2 * PADDING_X), COVER_SIZE, width), baseY = PADDING_Y + position.getY(SCREEN_HEIGHT - (2 * PADDING_Y), height);
        int x;
        int y = baseY;

        if (ALIGNMENT == RIGHT)
        {
            x = Math.min(baseX, SCREEN_WIDTH - PADDING_X - width);
        } else
        {
            x = Math.max(baseX - textWidth, PADDING_X);
        }

        int coverX = ALIGNMENT == RIGHT ? x : x + textWidth;

        helper.drawButtonThingy(new Quad(x, y, width, height), 0.5F, true, LiteModMusicPlayer.config.overlay_start_color, 0.25F, LiteModMusicPlayer.config.overlay_end_color, 0.25F);
        helper.drawShape(new Quad(coverX + 3, baseY + 3, COVER_SIZE - 6, COVER_SIZE - 6));

        if (expanded && textWidth + 3 > widthTarget)
        {
            boolean center = LiteModMusicPlayer.config.overlay_center_text;
            int textY = baseY + (center ? (height / 2) : 5), textX = ALIGNMENT == RIGHT ? coverX + COVER_SIZE : coverX;

            DrawingHelper.HorizontalTextAlignment horizontal = center ? DrawingHelper.HorizontalTextAlignment.CENTER : DrawingHelper.HorizontalTextAlignment.TOP;
            DrawingHelper.VerticalTextAlignment vertical = ALIGNMENT == RIGHT ? DrawingHelper.VerticalTextAlignment.LEFT : DrawingHelper.VerticalTextAlignment.RIGHT;

            currentlyPlayingText.render(helper, textX, textY, 0xFFFFFF, true, vertical, horizontal);
        }*/

        /*ScaledResolution resolution = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        final int SCREEN_WIDTH = resolution.getScaledWidth(), SCREEN_HEIGHT = resolution.getScaledHeight();


        final int coverSize = LiteModMusicPlayer.config.cover_size;
        final int paddingX = LiteModMusicPlayer.config.overlay_x_screen_padding, paddingY = LiteModMusicPlayer.config.overlay_y_screen_padding;

        final int startX = Math.min(
                SCREEN_WIDTH - paddingX - coverSize - textWidth,
                paddingX + (LiteModMusicPlayer.config.overlay_alignment == Config.Alignment.LEFT ? coverSize : 0));
        final int startY = paddingY +

        showNext = showNextTil > System.currentTimeMillis();

        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        Music playing = musicHandler.getCurrentlyPlaying();
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();

        String playingTitle = playing.getName();
        String playingAlbum = playing.getAlbum();
        String playingArtist = playing.getArtist();

        int titleWidth = renderer.getStringWidth(playingTitle);
        int albumWidth = renderer.getStringWidth(playingAlbum);
        int artistWidth = renderer.getStringWidth(playingArtist);

        float textWidthTarget = expanded ? Math.max(titleWidth, Math.max(albumWidth, artistWidth)) + 6 : 0;

        textWidth = Miscellaneous.smoothDamp(textWidthTarget, textWidth);
        width = height + textWidth;
        nextHeight = Miscellaneous.smoothDamp(showNext ? nextUpHeightTarget : 0, nextHeight);

        helper.drawButtonThingy(new Quad(5, 5, width, height), 0.5F, true, LiteModMusicPlayer.config.overlay_start_color, 0.25F, LiteModMusicPlayer.config.overlay_end_color, 0.5F);
        helper.drawShape(new Quad(5 + 3, 5 + 3, height - 6, height - 6).setColor(Color.GREEN));


        if (nextHeight > 0.1F)
        {
            Music next = musicHandler.getNextUp();
            String nextUp = "Next Up:";
            String message = next.getName() + " \u00a77by " + next.getArtist();
            int textWidth = Math.max(renderer.getStringWidth(nextUp), renderer.getStringWidth(message)) + 8;
            helper.drawButtonThingy(new Quad(5, 5 + height + 5, textWidth, nextHeight), 0F, true);
            if (nextHeight + 2 > nextUpHeightTarget)
            {
                helper.drawText(nextUp + "\n" + message, new Vector(5 + 4, 5 + height + 9), 0xFFFFFF, true, -1, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
            }
        }


        if (textWidth + 3 > textWidthTarget && expanded)
        {
            helper.drawText(playingTitle, new Vector(5 + height, 5 + 4), 0xFFFFFF, true, -1, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
            helper.drawText(playingAlbum, new Vector(5 + height, 5 + 4 + 11), 0xAAAAAA, true, -1, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
            helper.drawText(playingArtist, new Vector(5 + height, 5 + 4 + 22), 0xAAAAAA, true, -1, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        }*/
    }

    public void drawTheFreakingThing(int x, int y, int width, int height, Alignment alignment, boolean drawText, int screenWidth, int screenHeight, int paddingX, int paddingY)
    {
        Config config = LiteModMusicPlayer.config;
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        Color start = config.overlay_start_color, end = config.overlay_end_color;

        int coverX = x + (alignment == RIGHT ? width - height : 0);

        helper.drawButtonThingy(new Quad(x, y, width, height), 0.5F, true, start, 0.25F, end, 0.25F);
        helper.drawShape(new Quad(coverX + 3, y + 3, height - 6, height - 6));

        DrawingHelper.HorizontalTextAlignment horiAlignment = config.overlay_y_text_align;
        DrawingHelper.VerticalTextAlignment vertAlignment = config.overlay_position.getCenterText() ?
                DrawingHelper.VerticalTextAlignment.CENTER : (alignment == RIGHT ? DrawingHelper.VerticalTextAlignment.RIGHT : DrawingHelper.VerticalTextAlignment.LEFT);
        int textY = y;
        switch (horiAlignment)
        {
            case TOP:
                textY += 5;
                break;
            case CENTER:
                textY += height / 2;
                break;
            case BOTTOM:
                textY += height - 5;
                break;
        }
        if (drawText)
        {
            int textX;
            if (config.overlay_position.getCenterText())
            {
                textX = alignment == RIGHT ? coverX - ((width - 5 - height) / 2) : coverX + height + ((width - 5 - height) / 2);
            } else
            {
                textX = alignment == RIGHT ? coverX : coverX + height;
            }
            currentlyPlayingText.render(helper, textX, textY, 0xFFFFFF, true, vertAlignment, horiAlignment);
        }

        if (nextUpProgress > 0.01 && nextUpText != null)
        {
            drawNextUpBox(x, y, width, height, alignment, screenWidth, screenHeight, paddingX, paddingY);
        }
    }

    public void drawNextUpBox(int overlayX, int overlayY, int overlayWidth, int overlayHeight, Alignment alignment, int screenWidth, int screenHeight, int paddingX, int paddingY)
    {
        Config config = LiteModMusicPlayer.config;
        OverlayPosition.NextUpAlignment heightAlignment = config.overlay_position.next_up_alignment;
        int heightTarget = 6 + (nextUpText.getLines() * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT);
        int height = (int) (nextUpProgress * heightTarget);
        int width = nextUpText.getLongestLine() + 6;
        int x = alignment == LEFT ? overlayX : overlayX + overlayWidth - width;
        if (heightAlignment == TOP)
        {
            int y = overlayY - 2 - heightTarget;
            if (y < paddingY)
                heightAlignment = BOTTOM;
        } else
        {
            int y = overlayY + overlayHeight + 3;
            if (y + heightTarget > screenHeight - paddingY)
                heightAlignment = TOP;
        }
        int y = overlayY + (heightAlignment == TOP ? -3 - height : overlayHeight + 3);
        x = Math.max(x, paddingX);
        x = Math.min(x, screenWidth + paddingX - width);
        LiteModMusicPlayer.core.getDrawingHelper().drawButtonThingy(new Quad(x, y, width, height), 0F, true);
        if (height + 2 > heightTarget)
            nextUpText.render(LiteModMusicPlayer.core.getDrawingHelper(), x + 3, y + 3, 0xFFFFFF, true, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
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
        showNextTil = System.currentTimeMillis() + LiteModMusicPlayer.config.show_next_peek_time_millis;
    }
}
