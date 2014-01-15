package physics.magics;

import graphics.GraphicalSurface;
import player.Player;
import world.Block;

public class Spell {
	public Player owner;
	public GraphicalSurface icon;
	static public String iconName;
	
	static{
		iconName = "IconNotFound";
	}
	
	public Spell(Player p){
		this.owner = p;
		this.icon = new GraphicalSurface(iconName, 0.3);
	};
	
	public int cost(){return 0;};
	
	public void cast(Block b){};
}
