package ST;

import java.net.URL;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class Sound {
	
	private double bgmVolume = 0.5;
	private double seVolume = 0.5;
	private int keepBgmValue = 50;
	private int keepSeValue = 50;
	
	BasicPlayer p = new BasicPlayer();
	BasicPlayer q = new BasicPlayer();
	BasicPlayer r = new BasicPlayer();
	
	
	public void startBGM() {
		try {
			bgmVolume = (double) keepBgmValue / 100;
			URL bgm = this.getClass().getResource("mainBGM.mp3");
			
			p.stop();
			p.open(bgm);
			p.play();
			p.setGain(bgmVolume);
			p.setPan(0.5);
		} catch(Exception e) {
			System.out.println("BGMを再生出来ませんでした");
			e.printStackTrace();
		}
	}
	
	public void pauseBGM() {
		try {
			p.pause();
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}
	
	public void resumeBGM() {
		try {
			p.resume();
			p.setGain(bgmVolume);
		} catch(Exception e) {
			System.out.println("BGMを一時停止解除出来ませんでした");
			e.printStackTrace();
		}
	}
	
	public void stopBGM() {
		try {
			p.stop();
		} catch(Exception e) {
			System.out.println("BGMを停止出来ませんでした");
			e.printStackTrace();
		}
	}
	
	public void checkBGMinPlaying() {
		if(p.getStatus() == BasicPlayer.STOPPED) {
			startBGM();
		}
	}
	
	public void startSE(String key) {
		try {
			seVolume = (double) keepSeValue / 100;
			double higtVolume = seVolume*1.25 <= 1.0 ? seVolume*1.25 : 1.0;
			URL se = this.getClass().getResource("jump.mp3");
			
			switch(key) {
			case "jump":
				URL se1 = this.getClass().getResource("jump.mp3");
				se = se1;
				break;
			case "goal":
				URL se2 = this.getClass().getResource("bell.mp3");
				se = se2;
				break;
			case "fall":
				URL se3 = this.getClass().getResource("falling.mp3");
				se = se3;
				break;
			case "lift":
				URL se4 = this.getClass().getResource("lift.mp3");
				se = se4;
				seVolume = higtVolume;
				break;
			case "landing":
				URL se5 = this.getClass().getResource("landing.mp3");
				se = se5;
				seVolume = higtVolume;
				break;
			case "djump":
				URL se6 = this.getClass().getResource("doublejump.mp3");
				se = se6;
				break;
			case "gotext":
				URL se7 = this.getClass().getResource("gameoverTEXT.mp3");
				se = se7;
				seVolume = higtVolume;
				break;
			default:
				URL se0 = this.getClass().getResource("lift.mp3");
				se = se0;
			}
			
			if(key.equals("lift")) {
				
				r.stop();
				r.open(se);
				r.play();
				r.setGain(seVolume); 
				r.setPan(0.5);
				
			}
			
			q.stop();
			q.open(se);
			q.play();
			q.setGain(seVolume); 
			q.setPan(0.5);
		} catch(Exception e) {
			System.out.println("SEを再生出来ませんでした");
			e.printStackTrace();
		}
	}
	
	void stopSE() {
		try {
			q.stop();
		} catch (BasicPlayerException e) {
			e.printStackTrace();
			System.out.println("SEを停止出来ませんでした");
		}
	}
}
