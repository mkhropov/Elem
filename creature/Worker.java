package creature;

import world.*;
import physics.material.*;
import player.Order;

public interface Worker {
    boolean destroy(Block b);
    boolean build(Block b);
	boolean capableOf(Order o);
}
