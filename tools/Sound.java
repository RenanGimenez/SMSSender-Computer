package tools;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
	
	private static boolean allowed = true;
	
	
	public static void setAllow(boolean allowed) {
		Sound.allowed = allowed;
	}
	
	public static boolean isAllowed() {
		return Sound.allowed;
	}
	
	public static void play() {
		String sound = "notification.mp3";
		Media media = new Media(new File(sound).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}

}
