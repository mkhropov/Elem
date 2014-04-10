package iface;

public abstract class Menu extends Element {
	private Element parent;

	public Menu(Element p){
		this.visible = false;
		this.active = false;
		parent = p;
	}

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
		if (visible && parent.visible && parent.active) _draw();
	}

	abstract void _draw();
	public abstract void click(int x, int y, int mButton);
	public abstract boolean hover (int x, int y);
}
