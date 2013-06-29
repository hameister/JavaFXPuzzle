package org.hameister.javafx.puzzle;

public class PuzzleFieldStyle {
	private String style;
	
	public PuzzleFieldStyle(String style) {
		super();
		this.style = style;
	}

	public String getStyle() {
		return style;
	}
	
	public void setStyle(String style) {
		this.style = style;
	}
	
	@Override
	public String toString() {
		return style;
	}
}
