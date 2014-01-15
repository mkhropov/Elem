package physics.magics;

import creature.SmartWalkingElem;
import player.Player;
import world.Block;

public class SpellSummon extends Spell{
	
	static{
		iconName = "IconSummon";
	}
	
	public SpellSummon(Player p){
		super(p);
	}
	
	@Override
	public int cost(){
		if (owner.creature.size()<10)
			return 0;
		else
			return 1;
	}
	
	@Override
	public void cast(Block b){
		owner.spawnCreature(new SmartWalkingElem(owner.w, b));
	}
}