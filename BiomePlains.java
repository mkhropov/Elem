import java.util.Random;

public class BiomePlains extends Biome {

    Random gen;

    BiomePlains(){
        this.name = "Plains biome";
        this.gen = new Random();
    }

    Gase getAtmosphere(){
        return new GaseAir();
    }

    Temperature getTemperature(){
        return new Temperature(300);
    }

    Material getShallowMaterial(){
        return new MaterialEarth();
    }

    int getShallowDepth(){
        return 2+2*this.gen.nextInt(3);
    }

    Material getDeepMaterial(){
        return new MaterialStone();
    }
}
