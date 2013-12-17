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


public class SmartElem extends Elem implements Worker {

    public SmartElem(World w, Block b){
        super(w, b);
    }
    
    @Override
    public void iterate(long dT){
        if (order == null) {
            int i;
            for (i=0; i<owner.order.size(); ++i)
                if (!owner.order.get(i).taken)
                    break;
            if (i == owner.order.size())
                return; //procrastinate
            owner.setOrderTaken(i, this);
            path = w.pf.getPath(this, b, order.b);
        }
        p.add(mv, (double)dT);
        if (p.dist(np) < speed*dT){
            mv.toZero();
        Block t;
 //       System.out.printf("iterating\n");
        if (path == null)
            return;
        if (path.size() > 0){
            t = path.pop();
            if (canMove(b, t)){
                move(t);
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
}
