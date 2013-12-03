class Material {
    public Temperature tFreeze;
    public Temperature tBoil;

    public int color; //texture pack?

    Material() {
        tFreeze = new Temperature(9000000);
        tBoil =  new Temperature(9000000);

        color = 0;
    }
}
