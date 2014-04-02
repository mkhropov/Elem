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
import physics.material.Material;

public class SmartWalkingElem extends SmartElem implements Worker {

    public SmartWalkingElem(Block b){
        super(b);
        fall();
    }

    final void fall(){
		World w = World.getInstance();
        int i = b.z;
        while (i > 0)
            if (w.m[b.x][b.y][--i] != Material.MATERIAL_NONE)
                break;
		if (i!=(b.z-1)){
			setBlock(w.getBlock(b.x, b.y, i+1), true);
			mv = new Vector(0., 0., 0.);
			action = ACTION_FALL;
		}
    }

	@Override
	public void update(){
		fall();
	}

    @Override
    public boolean canWalk(Block b){
		World w = World.getInstance();
        return (b.z!=0) &&
            (w.m[b.x][b.y][b.z-1] != Material.MATERIAL_NONE) &&
            (w.m[b.x][b.y][b.z] == Material.MATERIAL_NONE);// || (b.m.w < (1. - Elem.size)));
    }
}
