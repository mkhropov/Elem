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
import java.util.Stack;
import stereometry.Vector;
import pathfind.*;
import player.*;

public class SmartElem extends Elem implements Worker {

    public SmartElem(World w, Block b){
        super(w, b);
        capable = new boolean[]{true, true, true};
    }

    boolean getNewOrder(){
        int i;
        Order o;
        for (i=0; i<owner.order.size(); ++i){
            o = owner.order.get(i);
            if ((!o.taken) && o.capable(this))
                break;
        }
        if (i == owner.order.size())
            return false; //procrastinate
        owner.setOrderTaken(i, this);
        return true;
    }


    @Override
    public void iterate(long dT){
        switch (action){
            case 1: //moving
                p.add(mv, (double)dT);
                if (p.dist(np) < speed*dT)
                    action = 0;
                else
                    return;
                break;
        }
        if (order != null) {
            if (path.size() == 0) {
                boolean res = true;
                if (order.type == 1)
                    res = destroyBlock(order.b);
                else if (order.type==2)
                    res = placeBlock(order.b, order.m);
			    if (res)
                    owner.setOrderDone(order, this);
                else
                    owner.setOrderCancelled(this.order, this);
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
            if (getNewOrder()){
                path = w.pf.getPath(this, b, order.b, (order.type==0));
                if (path == null)
                    owner.setOrderCancelled(this.order, this);
            } //else feel free to roam or make self-orders
        }
    }
}
