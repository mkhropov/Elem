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
import stereometry.Vector;


public class SmartWalkingElem extends SmartElem implements Worker {

    public SmartWalkingElem(World w, Block b){
        super(w, b);
        fall();
    }

    final void fall(){
        int i = b.z;
        while (i > 0)
            if (w.blockArray[b.x][b.y][--i].m != null)
                break;
		if (i!=(b.z-1)){
			setBlock(w.blockArray[b.x][b.y][i+1], true);
			mv = new Vector(0., 0., 0.);
			action = ACTION_FALL;
		}
    }

	@Override
	void update(){
		fall();
	}

    @Override
    public boolean canWalk(Block b){
        return (b.z!=0) &&
            (w.blockArray[b.x][b.y][b.z-1].m != null) &&
            ((b.m == null) || (b.m.w < (1. - Elem.size)));
    }
}
