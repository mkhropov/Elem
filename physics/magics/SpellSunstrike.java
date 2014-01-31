package physics.magics;

import generation.morphs.Pointfold;
import graphics.Renderer;
import player.Player;
import stereometry.BoundBox;
import stereometry.Point;
import world.Block;
import world.World;

public class SpellSunstrike extends Spell{

	static{
		iconName = "IconSunstrike";
	}

	public SpellSunstrike(Player p){
		super(p);
	}

	@Override
	public int cost(){
		return 0;
	}

	@Override
	public void cast(Block b){
		World w = World.getInstance();
		int h = w.zsize-1;
		while (h>0 && (w.blockArray[b.x][b.y][h].m == null))
			h--;
		if (h==0)
			return;
		Block t = w.blockArray[b.x][b.y][h];
		Pointfold f = new Pointfold(new Point(t), 4., 10., 1.1);
//		w.biome.applyMorph(f, w);
		BoundBox bb = f.bb.intersect(w.bb);
		for (int i = (int)Math.ceil(bb.p1.x); i < (int)Math.floor(bb.p2.x); ++i)
			for (int j = (int)Math.ceil(bb.p1.y); j < (int)Math.floor(bb.p2.y); ++j)
				for (int k = (int)Math.ceil(bb.p1.z); k < (int)Math.floor(bb.p2.z); ++k)
					Renderer.getInstance().updateBlock(i, j, k);
	}
}
