package gui;

import sprite.SpriteType;
import util.Action;
import util.TextPrinter;

public class EditableTextView extends Button {

	String currentText = null;
	
	public EditableTextView(int x, int y, SpriteType spriteType, Action ca) {
		super(x, y, spriteType, ca);
	}
	
	public EditableTextView(int x, int y, SpriteType spriteType) {
		super(x, y, spriteType);
	}

	public void render(){
		super.render();
		TextPrinter.drawString(getX() + 8, getY() + getHalfHeight() - 8 , currentText);
	}

	public void setCurrentText(String string) {
		currentText = string;
	}

	public void setOnClickAction(Action clickAction) {
		this.clickAction = clickAction;
	}

}
