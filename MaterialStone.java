import graphics.Color;

class MaterialStone extends Material {
    MaterialStone() {
        tFreeze = new Temperature(4000);
        tBoil =  new Temperature(5000);

        color = new Color(0.4f, 0.4f, 0.4f);
    }
}
