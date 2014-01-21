package iface;

public class Command {

	public static final int COMMAND_SPAWN = 0;
	public static final int COMMAND_DIG   = 1;
	public static final int COMMAND_BUILD = 2;
	public static final int COMMAND_MAX   = 3;

	public int type;

	public Command(int type){
		if (type >= COMMAND_MAX)
			this.type = COMMAND_SPAWN; //FIXME or exception
		else
			this.type = type;
	}

	public void execute(){
		Interface.getInstance().commandMode = type;
		System.out.println("commandMode switched to "+type);
	}
}
