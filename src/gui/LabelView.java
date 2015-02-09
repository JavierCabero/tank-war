package gui;

import sprite.SpriteType;
import structure.SquareView;
import util.TextPrinter;
import core.GameObject;

public class LabelView extends GameObject implements SquareView {


	String label = null;
	
	public LabelView(int x, int y, SpriteType spriteType, String label){
		super(x, y, spriteType);
		this.label = label;
	}
	public LabelView(int x, int y, SpriteType spriteType) {
		super(x, y, spriteType);
	}

	public void render() {
		super.render();
		TextPrinter.drawString(getX() + 8, getY() + getHalfHeight() - 8, label);
	}

	public void setCurrentText(String string) {
		label = string;
	}

	public void onFocus(int x, int y) {
		// Nuthing
	}
}
