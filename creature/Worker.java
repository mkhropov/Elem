package creature;

import player.Order;

public interface Worker {
    boolean dig(Action action);
    boolean build(Action action);
	boolean capableOf(Order o);
}
