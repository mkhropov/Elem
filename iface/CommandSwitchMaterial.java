package iface;

import physics.material.Material;

public class CommandSwitchMaterial implements Command {
	public char material;

	public CommandSwitchMaterial(char material){
		if (material >= Material.MATERIAL_MAX)
			this.material = Material.MATERIAL_MARBLE; //FIXME or exception
		else
			this.material = material;
	}

	public void execute(){
//		System.out.println("New material is "+((int)material));
		Interface.getInstance().setBuildMaterial(material);
	}

	public boolean isActive(){
		return (material == Interface.getInstance().buildMaterial);
	}
}
