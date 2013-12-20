/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package creature;

/**
 *
 * @author Михаил
 */

import world.*;
import pathfind.*;
import player.*;
import java.util.ArrayList;

public class SmartElem extends Elem implements Worker {

    public SmartElem(World w, Block b){
        super(w, b);
        capable = new boolean[]{true, true, true};
        declinedOrders = new ArrayList<>();
    }

	void update(){
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
						case Order.ORDER_PLACE:
							cond = new ConditionReach(o.b, this);
							break;
						default:
							cond = new ConditionNone();
							break;
					}
					path = w.pf.getPath(this, b, cond);
				}
				if (path != null)
					owner.setOrderTaken(o, this);
				else
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
                p.add(mv, (double)dT);
                if (p.dist(np) < speed*dT) {
					p = np;
                    action = ACTION_NONE;
				} else
                    return;
                break;
			case ACTION_FALL:
				mv.add(w.gravity, dT/1000.);
				p.add(mv, 1.);
				if (p.dist(np) < mv.len()){
					p = np;
					action = ACTION_NONE;
				} else {
					return;
				}
        }
        if (order != null) {
            if (path.size() == 0) {
                boolean res = true;
                if (order.type == 1){
                    res = destroyBlock(order.b);
					update();
                } else if (order.type==2)
                    res = placeBlock(order.b, order.m);
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
