package Audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Effects {
	private Clip song;
	private String path = "sound\\";
	private int count;

	public Effects() {
		try {
			song = AudioSystem.getClip();
		} catch (Exception ex) {
			System.out.println("Error with playing sound");
		}
	}

	public static void main(String[] args) {
		Effects a = new Effects();
		a.playPang();
		a.playSong();
		a.stopSong();
	}

	public void playLaser() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path + "Laser_Shoot.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public void playPang() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path + "pang.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public void playDeath() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path + "Powerup.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public void playSong() {
		try {
			AudioInputStream battle = AudioSystem.getAudioInputStream(new File(path + "song.wav").getAbsoluteFile());
			if (count == 0) {
				song.open(battle);
				count++;
			}
			song.loop(30);
		} catch (Exception ex) {
		}
	}

	public void stopSong() {
		song.stop();
		song.setFramePosition(0);
	}
}
