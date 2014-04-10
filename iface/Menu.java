package iface;

public abstract class Menu {
	private boolean visible = false;
	public Menu(){}

	public void show() {
		visible = true;
	}

	public void hide() {
		visible = false;
	}

	public void toggle() {
		visible = !visible;
	}

	public void draw() {
		if(visible) _draw();
	}

	public abstract void _draw();
	public abstract void click(int x, int y, int mButton);
	public abstract boolean hover (int x, int y);
}
