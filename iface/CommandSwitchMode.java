package iface;

public class CommandSwitchMode implements Command {

	public static final int MODE_SPAWN = 0;
	public static final int MODE_DIG   = 1;
	public static final int MODE_BUILD = 2;
	public static final int MODE_MAX   = 3;

	public int mode;

	public CommandSwitchMode(int mode){
		if (mode >= MODE_MAX)
			this.mode = MODE_SPAWN; //FIXME or exception
		else
			this.mode = mode;
	}

	public void execute(){
		Interface.getInstance().setCommandMode(mode);
//		System.out.println("commandMode switched to "+type);
	}

	public boolean isActive(){
		return (mode == Interface.getInstance().commandMode);
	}
}
