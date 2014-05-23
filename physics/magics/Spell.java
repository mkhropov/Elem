package physics.magics;

import core.Data;
import graphics.Texture;
import player.Player;
import world.Block;

public class Spell {
	public Player owner;
	public Texture icon;
	static public String iconName;
	
	static{
		iconName = "IconNotFound";
	}
	
	public Spell(Player p){
		this.owner = p;
		this.icon = Data.Textures.get(iconName);
	};
	
	public int cost(){return 0;};
	
	public void cast(Block b){};
}
