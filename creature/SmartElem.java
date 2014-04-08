package creature;

import world.*;
import pathfind.*;
import player.*;
import java.util.ArrayList;

public class SmartElem extends Elem implements Worker {

    public SmartElem(Block b){
        super(b);
        capable = new boolean[]{true, true, true, true};
        declinedOrders = new ArrayList<>();
    }

	public void update(){
	}

    boolean getNewOrder(){
        int i;
        Order o;
		order = null;
        for (i=0; (i<owner.order.size()) && (order ==null); ++i){
            o = owner.order.get(i);
            if (o.declined == 0)
                this.declinedOrders.remove(o);
            if (o.taken)
                continue;
			if (!declinedOrders.contains(o)){
				path = null;
				if (o.capable(this)) {
					Condition cond;
					switch (o.type) {
						case Order.ORDER_MOVE:
							cond = new ConditionPlace(o.b);
							break;
						case Order.ORDER_DIG:
						case Order.ORDER_BUILD:
							cond = new ConditionReach(o.b, this);
							break;
						case Order.ORDER_TAKE:
							cond = new ConditionItem(o.it);
							break;
						default:
							cond = new ConditionNone();
							break;
					}
					path = Pathfinder.getInstance().getPath(this, b, cond);
					if (path != null)
						owner.setOrderTaken(o, this);
					else
						owner.setOrderDeclined(o, this);
			} else
					owner.setOrderDeclined(o, this);
			}
        }
        return (i != owner.order.size());
    }


    @Override
    public void iterate(long dT){
        switch (action){
			case ACTION_NONE: break;
            case ACTION_MOVE:
                if (p.dist(np) < speed*dT) {
					p = np;
                    action = ACTION_NONE;
				} else {
					p.add(mv, (double)dT);
                    return;
				}
                break;
			case ACTION_FALL:
				mv.add(World.getInstance().gravity, dT/1000.);
				if (Math.abs(p.z - np.z) < mv.len()){
					p = np;
					action = ACTION_NONE;
				} else {
					p.add(mv, 1.);
					return;
				}
        }
        if (order != null) {
            if (path.size() == 0) {
                boolean res;
				switch (order.type){
					case Order.ORDER_MOVE:
						res = this.b.isSame(order.b); break;
					case Order.ORDER_DIG:
						res = destroyBlock(order.b); break;
					case Order.ORDER_BUILD:
						res = placeBlock(order.b, order.m); break;
					case Order.ORDER_TAKE:
						res = take(order.it); break;
					default:
						res = false; break;
				}
                if (res)
                    owner.setOrderDone(order, this);
                else
                    owner.setOrderDeclined(this.order, this);
            } else {
                Block t = path.pop();
                if (canMove(b, t)){
                    move(t);
                } else {
                    path.clear();
					if ((order.type == Order.ORDER_BUILD) &&
						(item != null))
						drop();
                    owner.setOrderCancelled(this.order, this);
                }
            }
        } else {
            if (!getNewOrder()){
				//feel free to roam or make self-orders
            }
        }
    }
}
