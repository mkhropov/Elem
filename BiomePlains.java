public class BiomePlains extends Biome {

    BiomePlains(){
        this.name = "Plains biome";
    }

    public final void fillChunk(Chunk c){
        c.dropStratum(getStratum(c.width, c.depth/2), new MaterialStone());
        c.dropStratum(getStratum(c.width, 4), new MaterialEarth());
    }

    Temperature getTemperature(){
        return new Temperature(300);
    }
}
