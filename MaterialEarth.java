class MaterialEarth extends Material {
    MaterialEarth() {
        tFreeze = new Temperature(3000);
        tBoil =  new Temperature(4000);

        color = 1;
    }
}
