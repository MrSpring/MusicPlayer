package dk.mrspring.music.gui.interfaces;

/**
 * Created by Konrad on 29-05-2015.
 */
public interface IResizable
{
    void setX(int newX);

    void setY(int newY);

    void setWidth(int newWidth);

    void setHeight(int newHeight);

    int x();

    int y();

    int height();

    int width();
}
