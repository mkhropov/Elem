class Gase {
    boolean isToxic;
    boolean flammable;
    Temperature tBurn;
    double pressure;

    Gase() {
        isToxic = false;
        flammable = false;
        tBurn = new Temperature(0);
        pressure = 0.;
    }
}
