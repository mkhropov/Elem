CC=javac

OBJ =  Temperature.class
OBJ += Material.class MaterialStone.class MaterialEarth.class
OBJ += Gase.class GaseAir.class
OBJ += Biome.class BiomePlains.class
OBJ += Wall.class Block.class Chunk.class World.class

all: $(OBJ)

%.class: %.java
	$(CC) $<

.PHONY: all clean

clean:
	rm -f *.class
