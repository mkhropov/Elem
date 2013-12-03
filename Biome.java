public class Biome {
    public String name;

    Biome(){
        name = "abstract biome";
    }

    Gase getAtmosphere(){
        return new Gase();
    }

    Temperature getTemperature(){
        return new Temperature(0);
    }

    Material getShallowMaterial(){
        return new Material();
    }

    int getShallowDepth(){
        return 0;
    }

    Material getDeepMaterial(){
        return new Material();
    }
}
