import java.util.Random;

public abstract class Biome {

    public String name;
    public Random gen;

    Biome(){
        name = "abstract biome";
        gen = new Random();
    }

    public abstract void fillWorld(World w);

    Temperature getTemperature(){
        return new Temperature(0);
    }
}
