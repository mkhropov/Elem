package iface;

public class ButtonFaceMenuState extends ButtonFace{
	private SelectorMenu menu;
	
	public ButtonFaceMenuState(SelectorMenu menu) {
		this.menu = menu;
	}
	
	@Override
	public void draw(int x, int y, int dx, int dy){
		menu.getButtonFace().draw(x, y, dx, dy);
	}
}
