public class BiomePlains extends Biome {

    BiomePlains(){
        this.name = "Plains biome";
    }

    public final void fillChunk(Chunk c){
        dropStratum(getStratum(c.width, c.depth/2), new MaterialStone(), c, 0, 0);
        dropStratum(getStratum(c.width, 4), new MaterialEarth(), c, 0, 0);
    }

    Temperature getTemperature(){
        return new Temperature(300);
    }
}
