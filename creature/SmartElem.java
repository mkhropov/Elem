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

    boolean moving;

    public SmartElem(World w, Block b){
        super(w, b);
        moving = false;
        capable = new boolean[]{true, true, true};
    }

    @Override
    public void iterate(long dT){
        if (order == null) {
            int i;
            Order o;
            for (i=0; i<owner.order.size(); ++i){
                o = owner.order.get(i);
                if ((!o.taken) && o.capable(this))
                    break;
            }
            if (i == owner.order.size())
                return; //procrastinate
            owner.setOrderTaken(i, this);
            path = w.pf.getPath(this, b, order.b);
        }
        if (moving){
            p.add(mv, (double)dT);
            if (p.dist(np) < speed*dT)
                moving = false;
            else
                return;
        }
        Block t;
 //       System.out.printf("iterating\n");
        if (path == null) {
			owner.setOrderDone(order, this);
            return;
		}
        if (path.size() > 0){
            t = path.pop();
            if (canMove(b, t)){
                move(t);
                moving = true;
                if (path.size()==0){
                    System.out.printf("Path walked succesfully\n");
                    owner.setOrderDone(this.order, this);
                }
            } else {
                System.out.printf("Incorrect path!\n");
                path.clear();
                owner.setOrderCancelled(this.order, this);
            }
        }
    }
}
