package physics.material;

import graphics.Color;
import physics.Temperature;

public class Material {

    public int state;

    public Temperature tFreeze;
    public double fhCapacity;
    Material fm;

    public double hCapacity;

    public Temperature tBoil;
    public double bhCapacity;
    Material bm;

    public Color c;

//    boolean fluor;
//    Color cf;

//    boolean opaque;
//    double opacity;
    public double density;
}
