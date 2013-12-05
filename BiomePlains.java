public class BiomePlains extends Biome {

    BiomePlains(){
        this.name = "Plains biome";
    }

    public final void fillChunk(Chunk c){
        Stratum stone = new Stratum(c.width, c.width, c.depth/2);
        stone.drop(new MaterialStone(), c, 0, 0);
        Stratum earth = new Stratum(c.width, c.width, 4);
        earth.drop(new MaterialEarth(), c, 0, 0);
    }

    Temperature getTemperature(){
        return new Temperature(300);
    }
}
