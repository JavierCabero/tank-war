package audio;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class AudioBuffer
{
	private static HashMap<AudioType, Audio> audios = new HashMap<AudioType, Audio>();

	public static Audio getAudio(AudioType at) {
		Audio audio = audios.get(at);
		if (audio == null) {
			audio = loadAudio(at.getName());
			audios.put(at, audio);
		}
		return audio;
	}

	public static void loadRemainingSounds() {
		for (AudioType audioType : AudioType.values()) {
			if (audios.get(audioType) == null) {
				audios.put(audioType, loadAudio(audioType.getName()));
			}
		}
	}
	

	private static Audio loadAudio(String string) {
		try {
			return AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("/res/sound/" + string + ".wav"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
