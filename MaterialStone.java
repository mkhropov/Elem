class MaterialStone extends Material {
    MaterialStone() {
        tFreeze = new Temperature(4000);
        tBoil =  new Temperature(5000);

        color = 2;
    }
}
