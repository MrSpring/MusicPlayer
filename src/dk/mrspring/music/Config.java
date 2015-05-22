package dk.mrspring.music;

import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.music.overlay.OverlayPosition;

/**
 * Created by Konrad on 26-04-2015.
 */
public class Config
{
    public Color overlay_start_color = Color.CYAN;
    public Color overlay_end_color = Color.BLUE;
    public double resume_time_millis = 500;
    public long show_next_peek_time_millis = 5000;
    public int cover_size = 50;
    public int overlay_x_screen_padding = 5;
    public int overlay_y_screen_padding = 5;
    public boolean auto_play = false;
    public OverlayPosition overlay_position = new OverlayPosition();
    public float overlay_size_easing_speed = 0.15F;
    public float overlay_next_up_easing_speed = 0.15F;
    public DrawingHelper.HorizontalTextAlignment overlay_y_text_align = DrawingHelper.HorizontalTextAlignment.CENTER;
}
