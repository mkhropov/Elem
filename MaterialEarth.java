import graphics.Color;

class MaterialEarth extends Material {
    MaterialEarth() {
        tFreeze = new Temperature(3000);
        tBoil =  new Temperature(4000);

        color = new Color(0.6f, 0.45f, 0.25f);
    }
}
