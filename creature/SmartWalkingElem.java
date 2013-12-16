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


public class SmartWalkingElem extends SmartElem implements Worker {

    public SmartWalkingElem(World w, Block b){
        super(w, b);
        fall();
    }

    void fall(){
        int i = b.z;
        while (i > 0)
            if (w.blockArray[b.x][b.y][--i].m != null)
                break;
        setBlock(w.blockArray[b.x][b.y][i+1], true);
        p = np;
    }

    @Override
    public boolean canWalk(Block b){
        return (b.z!=0) &&
            (w.blockArray[b.x][b.y][b.z-1].m != null) &&
            ((b.m == null) || (b.m.w < (1. - Elem.size)));
    }
}
