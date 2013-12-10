import graphics.Color;

class Material {
    public Temperature tFreeze;
    public Temperature tBoil;

    public Color color; //texture pack?

    Material() {
        tFreeze = new Temperature(9000000);
        tBoil =  new Temperature(9000000);

        color = new Color(0.5f, 0.5f, 0.5f);
    }
}
