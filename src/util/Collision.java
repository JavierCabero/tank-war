package util;

import core.GameObject;

public class Collision {

	public static boolean rectangleCollision(int x, int y, int width, int height, int x2, int y2, int width2, int height2) {
		int halfWidth = width / 2;
		int halfHeight = height / 2;
		int halfWidth2 = width2 / 2;
		int halfHeight2 = height2 / 2;

		x += halfWidth;
		y += halfHeight;
		x2 += halfWidth2;
		y2 += halfHeight2;
		int diffX = x < x2 ? x2 - x : x - x2;
		int diffY = y < y2 ? y2 - y : y - y2;
		return diffX < halfWidth + halfWidth2 && diffY < halfHeight + halfHeight2;
	}

	public static boolean rectangleCollision(GameObject o1, GameObject o2) {
		int halfWidth = o1.getHalfWidth();
		int halfHeight = o1.getHalfHeight();
		int halfWidth2 = o2.getHalfWidth();
		int halfHeight2 = o2.getHalfHeight();

		int x = o1.getX() + halfWidth;
		int y = o1.getY() + halfHeight;
		int x2 = o2.getX() + halfWidth2;
		int y2 = o2.getY() + halfHeight2;
		int diffX = x < x2 ? x2 - x : x - x2;
		int diffY = y < y2 ? y2 - y : y - y2;
		return diffX < halfWidth + halfWidth2 && diffY < halfHeight + halfHeight2;
	}

}
