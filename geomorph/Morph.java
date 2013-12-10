package geomorph;

import stereometry.*;

abstract public class Morph {

    abstract boolean inImage(Point p);

    abstract Point preimage(Point p);
    abstract Point image(Point P);
}