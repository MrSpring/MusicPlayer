package dk.mrspring.music.util;

import dk.mrspring.llcore.Icon;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Shape;
import dk.mrspring.llcore.Vector;

/**
 * Created by Konrad on 26-05-2015.
 */
public class Icons
{
    public static Icon trash = new Icon(1, 1,
            new Quad(
                    new Vector(0.2F, 0.8F),
                    new Vector(0.8F, 0.8F),
                    new Vector(0.8F, 0.9F),
                    new Vector(0.2F, 0.9F)),
            new Quad(
                    new Vector(0.75F, 0.2F),
                    new Vector(0.9F, 0.2F),
                    new Vector(0.8F, 0.9F),
                    new Vector(0.7F, 0.8F)),
            new Quad(
                    new Vector(0.1F, 0.2F),
                    new Vector(0.25F, 0.2F),
                    new Vector(0.3F, 0.8F),
                    new Vector(0.2F, 0.9F)),
            new Quad(
                    new Vector(0.4F, 0.2F),
                    new Vector(0.6F, 0.2F),
                    new Vector(0.575F, 0.8F),
                    new Vector(0.425F, 0.8F)),
            new Quad(
                    new Vector(0.05F, 0.15F),
                    new Vector(0.95F, 0.15F),
                    new Vector(0.95F, 0.3F),
                    new Vector(0.05F, 0.3F)),
            new Quad(
                    new Vector(0.35F, 0F),
                    new Vector(0.65F, 0F),
                    new Vector(0.65F, 0.1F),
                    new Vector(0.35F, 0.1F))
    );

    public static Icon search = new Icon(1, 1,
            new Quad(0.8F, 0F, 1F, 0.2F, 0.9F, 0.3F, 0.7F, 0.1F),
            new Quad(0.85F, 0.2F, 1F, 0.2F, 1F, 0.5F, 0.85F, 0.5F),
            new Quad(0.9F, 0.4F, 1F, 0.5F, 0.8F, 0.7F, 0.7F, 0.6F),
            new Quad(0.5F, 0.55F, 0.8F, 0.55F, 0.8F, 0.7F, 0.5F, 0.7F),
            new Quad(0.4F, 0.4F, 0.6F, 0.6F, 0.5F, 0.7F, 0.3F, 0.5F),
            new Quad(0.3F, 0.2F, 0.45F, 0.2F, 0.45F, 0.5F, 0.3F, 0.5F),
            new Quad(0.3F, 0.2F, 0.5F, 0F, 0.6F, 0.1F, 0.4F, 0.3F),
            new Quad(0.5F, 0F, 0.8F, 0F, 0.8F, 0.15F, 0.5F, 0.15F),
            new Quad(0.4F, 0.5F, 0.5F, 0.6F, 0.1F, 1F, 0F, 0.9F)
    );
}
