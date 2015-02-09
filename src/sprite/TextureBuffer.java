package sprite;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class TextureBuffer {

	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	
	
	
	/** Loads all the remaining sprites */
	public static void loadRemainingTextures() {
		String name;
		for (SpriteType spriteType : SpriteType.values()) {
			name = spriteType.getName();
			if (textures.get(name) == null) {
				textures.put(name, loadTexture(name));
			}
		}
	}
	
	public static Texture getTexture(String name) {
		Texture texture = textures.get(name);
		if (texture == null) {
			texture = loadTexture(name);
			textures.put(name, texture);
		}

		return texture;
	}
	private static Texture loadTexture(String path) {
		try {
			return TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/sprites/" + path + ".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
