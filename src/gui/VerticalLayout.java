package gui;

import org.lwjgl.input.Mouse;


public class VerticalLayout implements View {

	private int midPoint;
	private View topChild;
	private View bottomChild;

	public void setTopView(View view) {
		topChild = view;
	}

	public void setBottomView(View view) {
		bottomChild = view;
	}

	public void setMidPoint(int midPoint) {
		this.midPoint = midPoint;
	}

	public void update() {
		topChild.update();
		bottomChild.update();
	}

	public void onFocus(int x, int y) {
		if (Mouse.getY() >= midPoint) {
			topChild.onFocus(x, y);
		} else {
			bottomChild.onFocus(x, y);
		}
	}

	public View getTopChild(){
		return topChild;
	}
	public View getBottomChild(){
		return bottomChild;
	}
	public void render() {
		topChild.render();
		bottomChild.render();
	}

}
