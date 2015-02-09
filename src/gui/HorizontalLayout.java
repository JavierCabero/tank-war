package gui;

import org.lwjgl.input.Mouse;


public class HorizontalLayout implements View {

	private int midPoint;
	private View leftChild;
	private View rightChild;

	public void setLeftView(View view) {
		leftChild = view;
	}

	public void setRightView(View view) {
		rightChild = view;
	}

	public void setMidPoint(int midPoint) {
		this.midPoint = midPoint;
	}

	public void update() {
		leftChild.update();
		rightChild.update();
	}

	public View getLeftChild() {
		return leftChild;
	}

	public View getRightChild() {
		return rightChild;
	}

	public void onFocus(int x, int y) {
		if (Mouse.getX() < midPoint) {
			leftChild.onFocus(x, y);
		} else {
			rightChild.onFocus(x, y);
		}
	}

	public void render() {
		leftChild.render();
		rightChild.render();
	}

}
