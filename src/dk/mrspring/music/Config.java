package dk.mrspring.music;

import dk.mrspring.llcore.Color;
import dk.mrspring.music.overlay.OverlayPosition;
import dk.mrspring.music.util.FileSorter;
import dk.mrspring.music.util.Miscellaneous;
import org.lwjgl.input.Keyboard;

/**
 * Created by Konrad on 26-04-2015.
 */
public class Config
{
    public Color overlay_start_color = Color.CYAN;
    public Color overlay_end_color = Color.BLUE;
    public float overlay_start_alpha = 0.5F;
    public float overlay_end_alpha = 0.5F;
    public boolean disable_overlay_gradient = false;
    public double resume_time_millis = 500;
    public long show_next_peek_time_millis = 5000;
    public int cover_size = 50;
    public int overlay_x_screen_padding = 5;
    public int overlay_y_screen_padding = 5;
    public boolean auto_play = false;
    public OverlayPosition overlay_position = new OverlayPosition();
    public float overlay_size_easing_speed = 0.15F;
    public float overlay_next_up_easing_speed = 0.15F;
    public boolean disable_animations = false;
    public int gui_mm_list_entry_size = 50;
    public int gui_mm_side_panel_size = 90;
    public int explorer_icon_size = 20;
    public boolean show_file_edit_below_name = true;
    public boolean show_file_size_below_name = true;
    public boolean sort_folders = true;
    public FileSorter.SortingType file_sort_type = FileSorter.SortingType.NAME;
    public boolean show_startup_dialog = true;
    public int console_scroll_up_key = Keyboard.KEY_PRIOR;
    public int console_scroll_down_key = Keyboard.KEY_NEXT;
    public boolean clear_cover_cache_on_shutdown = false;

    public void validateConfig()
    {
        Config defaultConfig = new Config();

        if (overlay_start_color == null) overlay_start_color = defaultConfig.overlay_start_color;
        if (overlay_end_color == null) overlay_end_color = defaultConfig.overlay_end_color;
        overlay_start_alpha = Miscellaneous.clamp01(overlay_start_alpha);
        overlay_end_alpha = Miscellaneous.clamp01(overlay_end_alpha);
        resume_time_millis = Miscellaneous.min(resume_time_millis, 0);
        show_next_peek_time_millis = Miscellaneous.min(show_next_peek_time_millis, 0);
        if (cover_size < 0) cover_size = defaultConfig.cover_size;
        if (overlay_position == null) overlay_position = defaultConfig.overlay_position;
        overlay_size_easing_speed = Miscellaneous.clamp01(overlay_size_easing_speed);
        overlay_next_up_easing_speed = Miscellaneous.clamp01(overlay_next_up_easing_speed);
        gui_mm_list_entry_size = Miscellaneous.clamp(gui_mm_list_entry_size, 0, 100);
        if (file_sort_type == null) file_sort_type = defaultConfig.file_sort_type;
    }
}
