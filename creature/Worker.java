package creature;

import world.*;
import physics.material.*;
import player.Order;

public interface Worker {
    boolean digBlock(Order o);
    boolean placeBlock(Order o);
}
