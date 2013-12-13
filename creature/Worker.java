package creature;

import world.*;
import physics.material.*;

public interface Worker {
    boolean destroyBlock(Block b);
    boolean placeBlock(Block b, Material m);
}
