package ST;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MainFrame extends EnumScreen implements Runnable, KeyListener, ActionListener {
	
	public static final int WIDTH = 640;//視野の幅です。mapWIDTHを上回るとエラー
	public static final int HEIGHT = 480;//視野の高さです。mapHEIGHTを上回るとエラー
	public static final int TILE_SIZE = 32;
	public static final int COL = Map.map[1].length;//列
	public static final int ROW = Map.map.length;//行
	public static final int mapWIDTH = TILE_SIZE * COL;//960
	public static final int mapHEIGHT = TILE_SIZE * ROW;//640
	
	public int offsetX;
	public int offsetY;
	public int firstTileX;
	public int firstTileY;
	public int lastTileX;
	public int lastTileY;
	
	private static final int WAVETIMER_MAX = 200; // 波の画像の一周の間隔を定義している
	private static final int GAMEOVERANIMATIONTIMER_MAX = 1500;//ゲームオーバー画面の表示を順序つけるために使用
	public static final int mapNameTimer_MAX = 200;
	private static int wavePictureTimer;
	private static int gameoverAnimationTimer;
	public static int mapNameTimer;
	
	public static int jumpCoolTimeCounter;
	public static boolean downPushing;
	public static boolean leftPushing;
	public static boolean rightPushing;
	
	private Thread gameLoop;
	
	private Image wave, wave_next, wave_last, dirt_1, dirt_2, glass, cloud, cloud_left, cloud_center, cloud_right, cloudF, cloud_leftF, cloud_centerF, cloud_rightF, bricks_abovesea, bricks_pipe, bricks_undersea, bricks_connect, bricks_besidesea, leaves, lift;
	
	//private Timer timer;
	
	private static Bicycle bicycle = new Bicycle();
	
	private static Sound sound = new Sound();
	
	static TyariScreen scene = TyariScreen.TITLE;
	
	public MainFrame() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		addKeyListener(this);
		gameLoop = new Thread(this);
		gameLoop.start();
		loadImage();
		
		//scene = EnumScreen.TyariScreen.TITLE;
		//GenerateNewMap.GenerateMap();
		/*
		timer = new Timer(1, this);
        timer.start();
        */
	}
	
	private void loadImage() {
		
		URL url_wave = this.getClass().getResource("wave.png");
		ImageIcon iiw = new ImageIcon(url_wave);
		wave = iiw.getImage();
		
		URL url_wavenext = this.getClass().getResource("wave_next.png");
		ImageIcon iiw_n = new ImageIcon(url_wavenext);
		wave_next = iiw_n.getImage();
		
		URL url_wavelast = this.getClass().getResource("wave_last.png");
		ImageIcon iiw_l = new ImageIcon(url_wavelast);
		wave_last = iiw_l.getImage();
		
		URL url_dirt_1 = this.getClass().getResource("dirt_1.png");
		ImageIcon iid_1 = new ImageIcon(url_dirt_1);
		dirt_1 = iid_1.getImage();
		
		URL url_dirt_2 = this.getClass().getResource("dirt_2.png");
		ImageIcon iid_2 = new ImageIcon(url_dirt_2);
		dirt_2 = iid_2.getImage();
		
		URL url_glass = this.getClass().getResource("glass.png");
		ImageIcon iig = new ImageIcon(url_glass);
		glass = iig.getImage();
		
		URL url_cloud = this.getClass().getResource("cloud.png");
		ImageIcon iic = new ImageIcon(url_cloud);
		cloud = iic.getImage();
		
		URL url_cloud_left = this.getClass().getResource("cloud_left.png");
		ImageIcon iic_l = new ImageIcon(url_cloud_left);
		cloud_left = iic_l.getImage();
		
		URL url_cloud_center = this.getClass().getResource("cloud_center.png");
		ImageIcon iic_c = new ImageIcon(url_cloud_center);
		cloud_center = iic_c.getImage();
		
		URL url_cloud_right = this.getClass().getResource("cloud_right.png");
		ImageIcon iic_r = new ImageIcon(url_cloud_right);
		cloud_right = iic_r.getImage();
		
		URL url_cloudF = this.getClass().getResource("cloudF.png");
		ImageIcon iicF = new ImageIcon(url_cloudF);
		cloudF = iicF.getImage();
		
		URL url_cloud_leftF = this.getClass().getResource("cloud_leftF.png");
		ImageIcon iic_lF = new ImageIcon(url_cloud_leftF);
		cloud_leftF = iic_lF.getImage();
		
		URL url_cloud_centerF = this.getClass().getResource("cloud_centerF.png");
		ImageIcon iic_cF = new ImageIcon(url_cloud_centerF);
		cloud_centerF = iic_cF.getImage();
		
		URL url_cloud_rightF = this.getClass().getResource("cloud_rightF.png");
		ImageIcon iic_rF = new ImageIcon(url_cloud_rightF);
		cloud_rightF = iic_rF.getImage();
		
		URL url_bricks_as = this.getClass().getResource("bricks_abovesea.png");
		ImageIcon iib_as = new ImageIcon(url_bricks_as);
		bricks_abovesea = iib_as.getImage();
		
		URL url_bricks_p = this.getClass().getResource("bricks_pipe.png");
		ImageIcon iib_p = new ImageIcon(url_bricks_p);
		bricks_pipe = iib_p.getImage();
		
		URL url_bricks_us = this.getClass().getResource("bricks_undersea.png");
		ImageIcon iib_us = new ImageIcon(url_bricks_us);
		bricks_undersea = iib_us.getImage();
		
		URL url_bricks_c = this.getClass().getResource("bricks_connect.png");
		ImageIcon iib_c = new ImageIcon(url_bricks_c);
		bricks_connect = iib_c.getImage();
		
		URL url_bricks_bs = this.getClass().getResource("bricks_besidesea.png");
		ImageIcon iib_bs = new ImageIcon(url_bricks_bs);
		bricks_besidesea = iib_bs.getImage();
		
		URL url_leaves = this.getClass().getResource("leaves.png");
		ImageIcon iil = new ImageIcon(url_leaves);
		leaves = iil.getImage();
		
		URL url_lift = this.getClass().getResource("lift.png");
		ImageIcon iili = new ImageIcon(url_lift);
		lift = iili.getImage();
	}
	
	//動作処理はこちらから
	public void run() {
		
		scene = TyariScreen.TITLE;
		GenerateNewMap.GenerateMap();
		GenerateNewMap.makeSpareMap();
		
		while (true) {
			switch(scene) {
			case TITLE:
				break;
			case INGAME:
				inGame();
				break;
			case GAMEOVER:
				gameover();
				break;
			case SYSTEM:
				break;
			case DESCRIPTION:
				break;
			}
		}	
	}
	
	//タイトルかゲームオーバーからボタン押したときに起動。すべて初期状態へ
	public void initGame() {
		
		sound.startBGM();
		
		gameoverAnimationTimer = 0;
		wavePictureTimer = 0;
		mapNameTimer = 0;
		
		rightPushing = false;
		leftPushing = false;
		downPushing = false;
		
		Bicycle.resetBicycleStatus();
		CalculateAllScore.resetAllCounter();
		FlyingFloor.resetFlyingTimer();
		
	}
	
	//ゲーム中に定期的に実施したい処理はこちら
	public static void inGame() {
		
		sound.checkBGMinPlaying();
		
		bicycle.update();
		
		FallingFloor.fallingTimer();
		FlyingFloor.flyingTimer();
		CalculateAllScore.addTotalX();
		
		moveWavePicture();
		mapNameTimer++;
		try {
			Thread.sleep(5); //初期値20
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void moveWavePicture() {
		
		if(wavePictureTimer == WAVETIMER_MAX) {
			wavePictureTimer = 0;
		} else {
			wavePictureTimer++;
		}
	}
	
	public static void gameover() {
		
		sound.stopBGM();
		
		if(gameoverAnimationTimer > GAMEOVERANIMATIONTIMER_MAX) {
			
		} else {
			gameoverAnimationTimer++;
		}
		
		int t = (int)GAMEOVERANIMATIONTIMER_MAX * 1/16;
		int u = gameoverAnimationTimer;
		if((u == t*3) || (u == t*8) || (u == t*7) || (u == t*9) || (u == t*11)) {
			adjustSound(true,"gotext");
		}
		
		try {
			Thread.sleep(5); //初期値20
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//描画はこちら
	public void paintComponent(Graphics g) {
		this.setLayout(null);
		switch(scene) {
		case TITLE:
			TitleScreenSettings(g);
			break;
		case INGAME:
			InGameScreenSettings(g);
			break;
		case GAMEOVER:
			GameoverScreenSettings(g);
			break;
		case SYSTEM:
			SystemScreenSettings(g);
			break;
		case DESCRIPTION:
			DescriptionScreenSettings(g);
			break;
		}
	}
	
	public void TitleScreenSettings(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.GREEN);
		
		Font gamenameFont = new Font("SansSerif", Font.PLAIN, 40);
		FontMetrics gamenameMetrics = g.getFontMetrics(gamenameFont);
		String gamename = "チャリ走もどき";
		g.setFont(gamenameFont);
		g.drawString(gamename, (WIDTH - gamenameMetrics.stringWidth(gamename))/2, HEIGHT / 3);
		
		Font pressSpaceFont = new Font("SansSerif", Font.PLAIN, 20);
		FontMetrics pressSpaceMetrics = g.getFontMetrics(pressSpaceFont);
		String pressSpace = "スペースキーを押して始めよう";
		g.setFont(pressSpaceFont);
		g.drawString(pressSpace, (WIDTH - pressSpaceMetrics.stringWidth(pressSpace))/2, HEIGHT / 2);
		
		JButton fromTitleToIngame = new JButton("ゲームスタート");
		fromTitleToIngame.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
		fromTitleToIngame.setContentAreaFilled(false);
		final int goIngame_WIDTH = (WIDTH * 2)/ 3, goIngame_HEIGHT = HEIGHT / 12;
		this.add(fromTitleToIngame);
		fromTitleToIngame.setMargin(new Insets(0, 0, 0, 0));
		fromTitleToIngame.setBounds((WIDTH - goIngame_WIDTH) / 2, (HEIGHT * 7) / 10, goIngame_WIDTH, goIngame_HEIGHT);
		fromTitleToIngame.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				initGame();
				GenerateNewMap.initMapType();
				GenerateNewMap.GenerateMap();
				scene = TyariScreen.INGAME;
				removeAll();
			}
		});
		
		JButton fromTitleToDescription = new JButton("ゲーム説明");
		fromTitleToDescription.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
		fromTitleToDescription.setContentAreaFilled(false);
		final int goDescription_WIDTH = (WIDTH * 2)/ 3, goDescription_HEIGHT = HEIGHT / 12;
		this.add(fromTitleToDescription);
		fromTitleToDescription.setMargin(new Insets(0, 0, 0, 0));
		fromTitleToDescription.setBounds((WIDTH - goDescription_WIDTH) / 2, (HEIGHT * 8) / 10, goDescription_WIDTH, goDescription_HEIGHT);
		fromTitleToDescription.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				scene = TyariScreen.DESCRIPTION;
				removeAll();
			}
		});
		
		repaint();
	}
	
	public void InGameScreenSettings(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		offsetX = WIDTH / 2 - bicycle.getBicycleX() + Death.shakeScreenDegree_X;
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, WIDTH - mapWIDTH);
		
		offsetY = HEIGHT / 2 - bicycle.getBicycleY() + Death.shakeScreenDegree_Y;
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, HEIGHT - mapHEIGHT);
		
		objectDraw(g, offsetX, offsetY);
		bicycle.bicycleDraw(g, offsetX, offsetY);
		
		Font scoreFont = new Font("SansSerif", Font.PLAIN, 11);
		g.setFont(scoreFont);
		g.setColor(Color.green);
		g.drawString(String.valueOf(CalculateAllScore.getTotalScore()), bicycle.getBicycleX() + offsetX, bicycle.getBicycleY() + offsetY);
		
		
		Font velocityFont = new Font("SansSerif", Font.BOLD, 50);
		g.setFont(velocityFont);
		g.setColor(Bicycle.velocityX > 0 ? Color.cyan : Color.magenta);
		g.drawString(String.valueOf((int)(Bicycle.velocityX / 1.5 * 100)) + "%", 0, 50);
		
		Font mapTypeFont = new Font("SansSerif", Font.ITALIC, 50);
		g.setFont(mapTypeFont);
		g.setColor(Color.gray);
		FontMetrics mapNameMetrics = getFontMetrics(mapTypeFont); 
		if(mapNameTimer < mapNameTimer_MAX) g.drawString(GenerateNewMap.mapTypeName, (WIDTH - mapNameMetrics.stringWidth(GenerateNewMap.mapTypeName))/2, HEIGHT/2);
		
		g.setColor(Color.green);
		if(bicycle.onGround) {
			if(downPushing) {
				g.drawString("BRAKE", 0, 100);
			} else {
				g.drawString(leftPushing ? (rightPushing ? "KEEP" : "←") : (rightPushing ? "→" : "") , 0, 100);
			}
		}
		
		repaint();
	}
	
	public void GameoverScreenSettings(Graphics g) {
		
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Font gameoverFont = new Font("Helvetica", Font.BOLD, 50);
		FontMetrics gameoverMetrics = g.getFontMetrics(gameoverFont); 
		Font scoreFont = new Font("Helvetica", Font.PLAIN, 20);
		FontMetrics scoreMetrics = g.getFontMetrics(scoreFont);
		
		g.setFont(gameoverFont);
		g.setColor(Color.green);
		String gameover = "ゲームオーバー";
		g.drawString(gameover, (WIDTH - gameoverMetrics.stringWidth(gameover)) / 2, (HEIGHT * 3) / 10);
		g.setFont(scoreFont);
		g.setColor(Color.white);
		String str_score = "あなたの進んだ距離は " + String.valueOf(CalculateAllScore.getTotalScore());
		if(gameoverAnimationTimer > GAMEOVERANIMATIONTIMER_MAX * 3/16) g.drawString(str_score, (WIDTH - scoreMetrics.stringWidth(str_score)) / 2 - WIDTH / 6, (HEIGHT * 4) / 10 + scoreFont.getSize() * 0);
		String str_scoreDJ = "－ " + "ダブルジャンプ回数 " + String.valueOf(CalculateAllScore.getTotalDoubleJump() + " ×10");
		if(gameoverAnimationTimer > GAMEOVERANIMATIONTIMER_MAX * 5/16) g.drawString(str_scoreDJ, (WIDTH - scoreMetrics.stringWidth(str_scoreDJ)) / 2 - WIDTH / 6, (HEIGHT * 4) / 10 + scoreFont.getSize() * 2);
		String str_scoreB = "－ " + "ブレーキ回数 " + String.valueOf(CalculateAllScore.getTotalBreak() + " ×10");
		if(gameoverAnimationTimer > GAMEOVERANIMATIONTIMER_MAX * 7/16) g.drawString(str_scoreB, (WIDTH - scoreMetrics.stringWidth(str_scoreB)) / 2 - WIDTH / 6, (HEIGHT * 4) / 10 + scoreFont.getSize() * 4);
		String str_scoreTM = "＋ " + "通過マップ数 " + String.valueOf(CalculateAllScore.getTotalThroughMap() + " ×100");
		if(gameoverAnimationTimer > GAMEOVERANIMATIONTIMER_MAX * 9/16) g.drawString(str_scoreTM, (WIDTH - scoreMetrics.stringWidth(str_scoreTM)) / 2 - WIDTH / 6, (HEIGHT * 4) / 10 + scoreFont.getSize() * 6);
		String str_scoreALL = "＝ " + String.valueOf(CalculateAllScore.getAllScore());
		if(gameoverAnimationTimer > GAMEOVERANIMATIONTIMER_MAX * 11/16) g.drawString(str_scoreALL, (WIDTH - scoreMetrics.stringWidth(str_scoreTM)) / 2 + (WIDTH * 3) / 6, (HEIGHT * 4) / 10 + scoreFont.getSize() * 3);
		
		JButton fromGameoverToIngame = new JButton("もう一度プレイ");
		fromGameoverToIngame.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
		fromGameoverToIngame.setContentAreaFilled(false);
		final int goIngame_WIDTH = (WIDTH * 2)/ 3, goIngame_HEIGHT = HEIGHT / 12;
		if(gameoverAnimationTimer > GAMEOVERANIMATIONTIMER_MAX * 13/16) this.add(fromGameoverToIngame);
		fromGameoverToIngame.setMargin(new Insets(0, 0, 0, 0));
		fromGameoverToIngame.setBounds((WIDTH - goIngame_WIDTH) / 2, (HEIGHT * 7) / 10, goIngame_WIDTH, goIngame_HEIGHT);
		fromGameoverToIngame.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				initGame();
				GenerateNewMap.regenerateFromMapCopy();
				scene = TyariScreen.INGAME;
				removeAll();
			}
		});
		
		JButton makeNewMap = new JButton("new");
		makeNewMap.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 15));
		makeNewMap.setContentAreaFilled(false);
		final int makeNewMap_WIDTH = (WIDTH * 1)/ 16, makeNewMap_HEIGHT = makeNewMap_WIDTH;
		if(gameoverAnimationTimer > GAMEOVERANIMATIONTIMER_MAX * 15/16) this.add(makeNewMap);
		makeNewMap.setMargin(new Insets(0, 0, 0, 0));
		makeNewMap.setBounds((WIDTH + goIngame_WIDTH) / 2, (HEIGHT * 7) / 10, makeNewMap_WIDTH, makeNewMap_HEIGHT);
		makeNewMap.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				initGame();
				GenerateNewMap.initMapType();
				GenerateNewMap.GenerateMap();
				scene = TyariScreen.INGAME;
				removeAll();
			}
		});
		
		JButton fromGameoverToTitle = new JButton("タイトルへ戻る");
		fromGameoverToTitle.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
		fromGameoverToTitle.setContentAreaFilled(false);
		final int goTitle_WIDTH = (WIDTH * 2)/ 3, goTitle_HEIGHT = HEIGHT / 12;
		if(gameoverAnimationTimer > GAMEOVERANIMATIONTIMER_MAX * 15/16) this.add(fromGameoverToTitle);
		fromGameoverToTitle.setMargin(new Insets(0, 0, 0, 0));
		fromGameoverToTitle.setBounds((WIDTH - goTitle_WIDTH) / 2, (HEIGHT * 8) / 10, goTitle_WIDTH, goTitle_HEIGHT);
		fromGameoverToTitle.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				scene = TyariScreen.TITLE;
				removeAll();
			}
		});
		
		repaint();
	}
	
	public void SystemScreenSettings(Graphics g) {
		
		repaint();
	}
	
	public void DescriptionScreenSettings(Graphics g) {
		int explanationX = 240;
		int explanationY = 50;
		int margin = 40;
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.green);
		g.drawString("ゲームの説明になります", explanationX, explanationY);
		g.drawString("海に落ちる等によってゲームオーバーになるまで右方向に進み続け、", explanationX / 3, explanationY * 2);
		g.drawString("進んだ横方向の距離を可能な限り高めるゲームとなります。", explanationX / 3, explanationY * 2 + margin * 1);
		g.drawString("操作方法につきましては、", explanationX / 3, explanationY * 2 + margin * 2);
		g.drawString("■→矢印キーを押すと自転車を右へ前進します。", explanationX / 3, explanationY * 2 + margin * 3);
		g.drawString("■↑矢印キーを押すとジャンプします。空中で押すことで、もう一度ジャンプが可能です。", explanationX / 3, explanationY * 2 + margin * 4);
		g.drawString("■←矢印キーを押すと自転車を左へ前進します。", explanationX / 3, explanationY * 2 + margin * 5);
		g.drawString("■↓矢印キーを押すと急減速が可能です。←→方向キーと同時に押すことは可能です。", explanationX / 3, explanationY * 2 + margin * 6);
		g.drawString("マップは自動生成されます。基本的にランダムですが、", explanationX / 3, explanationY * 2 + margin * 7);
		g.drawString("稀に固定された組み合わせが生成される事もあります。", explanationX / 3, explanationY * 2 + margin * 8);
		
		JButton fromDescriptionToTitle = new JButton("戻る");
		fromDescriptionToTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
		fromDescriptionToTitle.setContentAreaFilled(false);
		final int goTitle_WIDTH = 50, goTitle_HEIGHT = 50;
		this.add(fromDescriptionToTitle);
		fromDescriptionToTitle.setMargin(new Insets(0, 0, 0, 0));
		fromDescriptionToTitle.setBounds(0, 0, goTitle_WIDTH, goTitle_HEIGHT);
		fromDescriptionToTitle.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				scene = TyariScreen.TITLE;
				removeAll();
			}
		});
		
		repaint();
	}
	
	//追加したマップのオブジェクト等もここで描画します
	public void objectDraw(Graphics g, int offsetX, int offsetY) {
		
		firstTileX = pixelsToTiles(-offsetX);
		lastTileX = firstTileX + pixelsToTiles(WIDTH) + 1;
		lastTileX = Math.min(lastTileX, COL);
		
		firstTileY = pixelsToTiles(-offsetY);
		lastTileY = firstTileY + pixelsToTiles(HEIGHT) + 1;
		lastTileY = Math.min(lastTileY, ROW);
		
		
		for (int i = firstTileY; i < lastTileY; i++) {
			for (int j = firstTileX; j < lastTileX; j++) {
				switch (Map.map[i][j]) {
				case 1://レンガ。画面下の柱や海の下の床、自転車が走る床の下
					g.drawImage(bricks_abovesea, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
					
					//y-1を参照するが、1の位置的にyの配列範囲外を参照しない
					if(Map.map[i - 1][j] == 5) {
						g.drawImage(bricks_undersea, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
					}
					
					//xの配列範囲外を参照してしまう可能性があるパーツ
					if(j != 0 && j != COL-1) {
						
						//さらにyの配列範囲外を参照する可能性があるため分離
						if(i != ROW-1) {
							
							if(Map.map[i][j - 1] == 5 && Map.map[i][j + 1] == 5) {
								g.drawImage(bricks_besidesea, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
							}
							
							if(Map.map[i][j - 1] == 5 && Map.map[i][j + 1] == 5 && Map.map[i + 1][j - 1] == 1 && Map.map[i + 1][j + 1] == 1) {
								g.drawImage(bricks_pipe, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
							}
						}
						
						//xの配列範囲外を参照したとき、エラーが起きるため分離
						if(Map.map[i - 1][j - 1] == 5 && Map.map[i - 1][j + 1] == 5  && Map.map[i - 1][j] == 1 && Map.map[i][j - 1] == 1 && Map.map[i][j + 1] == 1) {
							g.drawImage(bricks_connect, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
						}
					}
						
					break;
				case 2://自転車が走る床。ガラスと葉っぱ
					g.drawImage(glass, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
					if(Map.map[i-1][j] == 2) g.drawImage(dirt_2, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
					if(j != 0 && j != COL-1) {
						if((Map.map[i][j - 1] != 2 && Map.map[i][j + 1] != 2) || (Map.map[i-1][j] == 3)) {
							g.drawImage(leaves, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
						}
					}
					break;
				case 3://処理用。四方向に隣接するマスの描画を操作する。
					g.setColor(Color.black);
					break;
				case 4://動く床リフト
					g.drawImage(lift,tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
					break;
				case 5://海とその波
					if(Map.map[i - 1][j] == 0 || Map.map[i - 1][j] == 7 || Map.map[i - 1][j] == 8 || Map.map[i - 1][j] == 9) {
						g.drawImage(wavePictureTimer > (int)WAVETIMER_MAX / 3 ? (wavePictureTimer > (int)(WAVETIMER_MAX * 2 / 3) ? wave_last : wave_next) : wave, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
					} else {
						g.setColor(Color.blue);
						g.fillRect(tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, TILE_SIZE, TILE_SIZE);
					}
					break;
				case 6://処理用。マップの再生成と移動。マップ左端と右端にあり処理が異なる。
					g.setColor(Color.black);
					g.fillRect(tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, TILE_SIZE, TILE_SIZE);
					break;
				case 7://当たり判定が"無い"柱。
					g.setColor(Color.darkGray);
					g.fillRect(tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, TILE_SIZE, TILE_SIZE);
					break;
				case 8://落下床。落下する前の状態
					g.drawImage(cloud, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
					if((Map.map[i][j-1] == 8 || Map.map[i][j-1] == 9) && (Map.map[i][j+1] == 8 || Map.map[i][j+1] == 9)) {
						g.drawImage(cloud_center, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
					} else {
						if((Map.map[i][j-1] != 8 && Map.map[i][j-1] != 9) && (Map.map[i][j+1] == 8 || Map.map[i][j+1] == 9)) {
							g.drawImage(cloud_left, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
						}
						if((Map.map[i][j-1] == 8 || Map.map[i][j-1] == 9) && (Map.map[i][j+1] != 8 && Map.map[i][j+1] != 9)) {
							g.drawImage(cloud_right, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
						}
					}
					break;
				case 9://落下床。落下中
					g.drawImage(cloudF, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
					if((Map.map[i][j-1] == 8 || Map.map[i][j-1] == 9) && (Map.map[i][j+1] == 8 ||Map.map[i][j+1] == 9)) {
						g.drawImage(cloud_centerF, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
					} else {
						if((Map.map[i][j-1] != 8 && Map.map[i][j-1] != 9) && (Map.map[i][j+1] == 8 || Map.map[i][j+1] == 9)) {
							g.drawImage(cloud_leftF, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
						}
						if((Map.map[i][j-1] == 8 || Map.map[i][j-1] == 9) && (Map.map[i][j+1] != 8 && Map.map[i][j+1] != 9)) {
							g.drawImage(cloud_rightF, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
						}	
					}
					break;
				}
			}
		}
	}
	
	public static int pixelsToTiles(double pixels) {
		return (int)Math.floor(pixels / TILE_SIZE);
	}
	
	public static int tilesToPixels(int tiles) {
		return tiles * TILE_SIZE;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		switch(scene) {
		case TITLE:
			
			if (key == KeyEvent.VK_SPACE) {
				initGame();
				scene = TyariScreen.INGAME;
			}
			removeAll();//ボタン削除用
			
			break;
		case INGAME:
			
			if (key == KeyEvent.VK_SPACE) scene = TyariScreen.GAMEOVER;
			
			if (key == KeyEvent.VK_UP) {
				if(jumpCoolTimeCounter == 0) bicycle.jump();
				jumpCoolTimeCounter++;
			}
			
			if (key == KeyEvent.VK_DOWN) {
				downPushing = true;
			}
			
			if (key == KeyEvent.VK_LEFT) {
				leftPushing = true;
			}
			
			if (key == KeyEvent.VK_RIGHT) {
				rightPushing = true;
			}
			
			if (key == KeyEvent.VK_SHIFT) {
				sound.pauseBGM();
				scene = TyariScreen.SYSTEM;
			}
			
			break;
		case GAMEOVER:
			
			if (key == KeyEvent.VK_SPACE) {
				scene = TyariScreen.TITLE;
				GenerateNewMap.regenerateFromMapCopy();
			}
			removeAll();
			
			break;
		case SYSTEM:
			
			if (key == KeyEvent.VK_SHIFT) {
				sound.resumeBGM();
				scene = TyariScreen.INGAME;
			}
			repaint();
			
			break;
		case DESCRIPTION:
			break;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_UP) {
			jumpCoolTimeCounter = 0;
		}
		if (key == KeyEvent.VK_DOWN) {
			downPushing = false;
			if(scene == TyariScreen.INGAME) CalculateAllScore.addTotalBrake();;
		}
		if (key == KeyEvent.VK_LEFT) {
			leftPushing = false;
		}
		if (key == KeyEvent.VK_RIGHT) {
			rightPushing = false;
		}
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		switch(scene) {
		case TITLE:
			title();
			break;
		case INGAME:
			inGame();
			break;
		case GAMEOVER:
			break;
		case SYSTEM:
			break;
		}
		*/
	}
	
	public static void adjustSound(boolean startSE, String SEkey) {
		
		if(startSE) {
			sound.startSE(SEkey);
		} else {
			sound.stopSE();
		}
	}
}