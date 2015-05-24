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
import static dk.mrspring.llcore.DrawingHelper.VerticalTextAlignment;
import static dk.mrspring.llcore.DrawingHelper.HorizontalTextAlignment;

/**
 * Created by Konrad on 27-04-2015.
 */
public class Overlay
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
        Config config = LiteModMusicPlayer.config;

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
        int overlayX = _paddingX + position.getX(_screenWidth, _coverSize) - (int) (sizeProgress * xOverlapTarget);
        int overlayY = _paddingY + position.getY(_screenHeight, _coverSize) - (int) (sizeProgress * yOverlapTarget);

        overlayX = Math.max(_paddingX, overlayX);
        overlayY = Math.max(_paddingY, overlayY);
        overlayX = Math.min(_screenWidth - _paddingX - overlayWidth, overlayX);
        overlayY = Math.min(_screenHeight - _paddingY - overlayHeight, overlayY);

        drawTheFreakingThing(overlayX, overlayY, overlayWidth, overlayHeight, sizeProgress > 0.98);
        if (nextUpProgress > 0.01)
            drawNextUpBox(overlayX, overlayY, overlayWidth, overlayHeight);





        /*showNext = showNextTil > System.currentTimeMillis();
        Config config = LiteModMusicPlayer.config;
        OverlayPosition position = config.overlay_position;

        double easing = config.overlay_size_easing_speed;
        this.sizeProgress = Miscellaneous.smoothDamp(expanded ? 1D : 0D, sizeProgress, easing);

        easing = config.overlay_next_up_easing_speed;
        this.nextUpProgress = Miscellaneous.smoothDamp(showNext ? 1D : 0D, nextUpProgress, easing);

        ScaledResolution resolution = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        final int SCREEN_WIDTH = resolution.getScaledWidth(), SCREEN_HEIGHT = resolution.getScaledHeight();
        final int PADDING_X = config.overlay_x_screen_padding, PADDING_Y = config.overlay_y_screen_padding;
        int coverSize = config.cover_size;
//        boolean centerY = true;
        config.overlay_position.

        int overlapXTarget = currentlyPlayingText.getLongestLine() + 5;
        int overlapYTarget = (currentlyPlayingText.getLines() * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) + 10 - coverSize;
        int overlapX = Math.max(0, (int) (overlapXTarget * sizeProgress));
        int overlapY = Math.max(0, (int) (overlapYTarget * sizeProgress));

        int overlayWidth = coverSize + overlapX, overlayHeight = coverSize + overlapY;

        int
                overlayX = position.getX(SCREEN_WIDTH, overlayWidth),
                overlayY = position.getY(SCREEN_HEIGHT, overlayHeight) - (centerY ? (overlayHeight - coverSize) / 2 : 0);

        overlayX = Math.max(PADDING_X, overlayX);
        overlayX = Math.min(SCREEN_WIDTH - overlayWidth + PADDING_X, overlayX);
        overlayY = Math.max(PADDING_Y, overlayY);
        overlayY = Math.min(SCREEN_HEIGHT - overlayHeight + PADDING_Y, overlayY);

        drawTheFreakingThing(overlayX, overlayY, overlayWidth, overlayHeight, position.getHorizontal(), overlapX + 2 > overlapXTarget, SCREEN_WIDTH, SCREEN_HEIGHT, config.overlay_x_screen_padding, config.overlay_y_screen_padding);*/





        /*showNext = showNextTil > System.currentTimeMillis();

        Config config = LiteModMusicPlayer.config;

        OverlayPosition position = config.overlay_position;
        Alignment alignment = position.getAlignment();

        ScaledResolution resolution = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        final int SCREEN_WIDTH = resolution.getScaledWidth() - (2 * config.overlay_x_screen_padding), SCREEN_HEIGHT = resolution.getScaledHeight() - (2 * config.overlay_y_screen_padding);
        final int PADDING_X = config.overlay_x_screen_padding, PADDING_Y = config.overlay_y_screen_padding;
        final int COVER_SIZE = config.cover_size;

        doPlayingText(musicHandler.getCurrentlyPlaying(), minecraft.fontRendererObj);
//        if (nextUpProgress > 0.01)
//            doNextUpText(musicHandler.getNextUp(), minecraft.fontRendererObj);

        double easing = config.overlay_size_easing_speed;
        this.sizeProgress = Miscellaneous.smoothDamp(expanded ? 1D : 0D, sizeProgress, easing);

        easing = config.overlay_next_up_easing_speed;
        this.nextUpProgress = Miscellaneous.smoothDamp(showNext ? 1D : 0D, nextUpProgress, easing);

        int textWidthTarget = (currentlyPlayingText.getLongestLine() + 5);
        int textHeightTarget = (currentlyPlayingText.getLines() * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) + 10 - COVER_SIZE;
        int textWidth = (int) (sizeProgress * textWidthTarget);
        int textHeight = (int) (sizeProgress * textHeightTarget);
        int height = COVER_SIZE + textHeight;
        int width = COVER_SIZE + textWidth;
        int x = config.overlay_x_screen_padding + position.getX(SCREEN_WIDTH, width) - (alignment == RIGHT ? textWidth : 0);
        int y = config.overlay_y_screen_padding + position.getY(SCREEN_HEIGHT, height);

        x = Math.max(PADDING_X, x);
        x = Math.min(SCREEN_WIDTH - width + PADDING_X, x);
        y = Math.max(PADDING_Y, y);
        y = Math.min(SCREEN_HEIGHT - height + PADDING_Y, y);

        drawTheFreakingThing(x, y, width, height, alignment, textWidth + 2 > textWidthTarget, SCREEN_WIDTH, SCREEN_HEIGHT, PADDING_X, PADDING_Y);*/
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

    public void drawTheFreakingThing(int x, int y, int width, int height, boolean drawText)
    {
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        helper.drawButtonThingy(new Quad(x, y, width, height), 0, true);

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

        LiteModMusicPlayer.musicHandler.getCurrentlyPlaying().bindCover();
        helper.drawTexturedShape(new Quad(coverX + 3, coverY + 3, s, s));
        if (drawText)
            currentlyPlayingText.render(helper, textX, textY, 0xFFFFFF, true, vertical, horizontal);

        /*switch (_alignment)
        {
            case TOP_LEFT:
                helper.drawTexturedShape(new Quad(x + 3, y + 3, s, s));
                if (drawText)
                    currentlyPlayingText.render(helper, x + _coverSize, y + 5, 0xFFFFFF, true, VerticalTextAlignment.LEFT, HorizontalTextAlignment.TOP);
                break;
            case TOP_CENTER:
                helper.drawTexturedShape(new Quad(x + (width / 2) - (_coverSize / 2) + 3, y + 3, s, s));
                if (drawText)
                    currentlyPlayingText.render(helper, x + (width / 2), y + _coverSize, 0xFFFFFF, true, VerticalTextAlignment.CENTER, HorizontalTextAlignment.TOP);
                break;
            case TOP_RIGHT:
                helper.drawTexturedShape(new Quad(x + width - _coverSize + 3, y + 3, s, s));
                if (drawText)
                    currentlyPlayingText.render(helper, x + width - _coverSize, y + 5, 0xFFFFFF, true, VerticalTextAlignment.RIGHT, HorizontalTextAlignment.TOP);
                break;
            case CENTER_LEFT:
                helper.drawTexturedShape(new Quad(x + 3, y + (height / 2) - (_coverSize / 2) + 3, s, s));
                if (drawText)
                    currentlyPlayingText.render(helper, x + _coverSize, y + (height / 2), 0xFFFFFF, true, VerticalTextAlignment.LEFT, HorizontalTextAlignment.CENTER);
                break;
            case CENTER_RIGHT:
                helper.drawTexturedShape(new Quad(x + width - _coverSize + 3, y + (height / 2) - (_coverSize / 2) + 3, s, s));
                if (drawText)
                    currentlyPlayingText.render(helper, x + width - _coverSize, y + (height / 2), 0xFFFFFF, true, VerticalTextAlignment.RIGHT, HorizontalTextAlignment.CENTER);
                break;
            case BOTTOM_LEFT:
                helper.drawTexturedShape(new Quad(x + 3, y + height - _coverSize + 3, s, s));
                if (drawText)
                    currentlyPlayingText.render(helper, x + _coverSize, y + height - 5, 0xFFFFFF, true, VerticalTextAlignment.LEFT, HorizontalTextAlignment.BOTTOM);
                break;
            case BOTTOM_CENTER:
                helper.drawTexturedShape(new Quad(x + (width / 2) - (_coverSize / 2) + 3, y + height - _coverSize + 3, s, s));
                if (drawText)
                    currentlyPlayingText.render(helper, x + (width / 2), y + height - _coverSize, 0xFFFFFF, true, VerticalTextAlignment.CENTER, HorizontalTextAlignment.BOTTOM);
                break;
            case BOTTOM_RIGHT:
                helper.drawTexturedShape(new Quad(x + width - _coverSize + 3, y + height - _coverSize + 3, s, s));
                if (drawText)
                    currentlyPlayingText.render(helper, x + width - _coverSize, y + height - 5, 0xFFFFFF, true, VerticalTextAlignment.RIGHT, HorizontalTextAlignment.BOTTOM);
                break;
        }*/

        /*Config config = LiteModMusicPlayer.config;
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        Color start = config.overlay_start_color, end = config.overlay_end_color;
        helper.drawButtonThingy(new Quad(x, y, width, height), 0.5F, true, start, 0.25F, end, 0.25F);
        HorizontalTextAlignment horizontal = config.overlay_y_text_align;
        boolean isRight = alignment == RIGHT;

        {
            int coverSize = config.cover_size - 6;
            int coverX = x + (isRight ? width - 3 - coverSize : 3);
            int coverY = y + (horizontal == HorizontalTextAlignment.TOP ? 3 : (horizontal == HorizontalTextAlignment.CENTER ? height / 2 - (coverSize / 2) : height - 3 - coverSize));
            musicHandler.getCurrentlyPlaying().bindCover();
            helper.drawTexturedShape(new Quad(coverX, coverY, coverSize, coverSize));
        }

        if (drawText)
        {
            int textWidth = (width - 5 - config.cover_size);
            boolean center = config.overlay_position.getCenterText();
            int textX = x + (center ? (isRight ? textWidth / 2 : (textWidth / 2) + config.cover_size) : (isRight ? width - config.cover_size : config.cover_size));
            int textY = y + (horizontal == HorizontalTextAlignment.TOP ? 5 : (horizontal == HorizontalTextAlignment.CENTER ? height / 2 : height - 5));
            VerticalTextAlignment verticalTextAlignment = (center ? VerticalTextAlignment.CENTER : (isRight ? VerticalTextAlignment.RIGHT : VerticalTextAlignment.LEFT));
            currentlyPlayingText.render(helper, textX, textY, 0xFFFFFF, true, verticalTextAlignment, horizontal);
        }

        if (nextUpProgress > 0.01 && nextUpText != null)
        {
            drawNextUpBox(x, y, width, height, alignment, screenWidth, screenHeight, paddingX, paddingY);
        }*/

        /*Config config = LiteModMusicPlayer.config;
        DrawingHelper helper = LiteModMusicPlayer.core.getDrawingHelper();
        Color start = config.overlay_start_color, end = config.overlay_end_color;

        DrawingHelper.HorizontalTextAlignment horiAlignment = config.overlay_y_text_align;
        DrawingHelper.VerticalTextAlignment vertAlignment = config.overlay_position.getCenterText() ?
                DrawingHelper.VerticalTextAlignment.CENTER : (alignment == RIGHT ? DrawingHelper.VerticalTextAlignment.RIGHT : DrawingHelper.VerticalTextAlignment.LEFT);

        int coverSize = config.cover_size;

        int coverX = x + (alignment == RIGHT ? width - height : 0);
        int coverY = y + (horiAlignment == DrawingHelper.HorizontalTextAlignment.TOP ? 0 : (horiAlignment == DrawingHelper.HorizontalTextAlignment.CENTER ? (height / 2 - (coverSize / 2)) : (height - coverSize)));

        helper.drawButtonThingy(new Quad(x, y, width, height), 0.5F, true, start, 0.25F, end, 0.25F);
//        helper.drawShape(new Quad(coverX + 3, y + 3, height - 6, height - 6));
        musicHandler.getCurrentlyPlaying().bindCover();
        int frame = 7;
        int frameHeight = 64;
        helper.drawTexturedShape(new Quad(coverX + 3, coverY + 3, coverSize - 6, coverSize - 6*//*new Vector[]{
                new Vector(coverX + 3, y + 3, 0, frame * frameHeight),
                new Vector(coverX + 3 + height - 6, y + 3, 512, frame * frameHeight),
                new Vector(coverX + 3 + height - 6, y + 3 + height - 6, 512, (frame + 1) * frameHeight),
                new Vector(coverX + 3, y + 3 + height - 6, 0, (frame + 1) * frameHeight)
        }*//*));

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
                textX = alignment == RIGHT ? coverX - ((width - 5 - coverSize) / 2) : coverX + coverSize + ((width - 5 - coverSize) / 2);
            } else
            {
                textX = alignment == RIGHT ? coverX : coverX + coverSize;
            }
            System.out.println("textY = " + textY + ", height / 2 = " + (height / 2) + ", y = " + y);
            currentlyPlayingText.render(helper, textX, textY, 0xFFFFFF, true, vertAlignment, horiAlignment);
        }

        if (nextUpProgress > 0.01 && nextUpText != null)
        {
            drawNextUpBox(x, y, width, height, alignment, screenWidth, screenHeight, paddingX, paddingY);
        }*/
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

        LiteModMusicPlayer.core.getDrawingHelper().drawButtonThingy(new Quad(nextUpX, nextUpY, nextUpWidth, nextUpHeight), 0, true);
        if (nextUpProgress > 0.98)
            nextUpText.render(LiteModMusicPlayer.core.getDrawingHelper(), nextUpX + 5, nextUpY + 5, 0xFFFFFF, true, VerticalTextAlignment.LEFT, HorizontalTextAlignment.TOP);

        /*Config config = LiteModMusicPlayer.config;
        OverlayPosition.NextUpAlignment heightAlignment = config.overlay_position.next_up_alignment;
        int heightTarget = 6 + (nextUpText.getLines() * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT);
        int height = (int) (nextUpProgress * heightTarget);
        int width = nextUpText.getLongestLine() + 6;
        int x = horizontal == LEFT ? overlayX : overlayX + overlayWidth - width;
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
            nextUpText.render(LiteModMusicPlayer.core.getDrawingHelper(), x + 3, y + 3, 0xFFFFFF, true, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);*/
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
        doNextUpText(LiteModMusicPlayer.musicHandler.getNextUp(), Minecraft.getMinecraft().fontRendererObj);
    }
}
