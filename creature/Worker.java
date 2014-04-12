package creature;

import player.Order;

public interface Worker {
    boolean dig(Order o);
    boolean build(Order o);
	boolean capableOf(Order o);
}
