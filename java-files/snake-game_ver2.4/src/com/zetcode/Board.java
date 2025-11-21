package com.zetcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;

import com.zetcode.EnumSnakeScreen.SnakeScreen;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class Board extends JPanel implements ActionListener {
	
	private final int B_WIDTH  = 300;
	private final int B_HEIGHT = 300;
	private final int DOT_SIZE = 10;
	private final int ALL_DOTS = (B_WIDTH / DOT_SIZE) * (B_HEIGHT/ DOT_SIZE);
	private final int RAND_POSX = B_WIDTH / DOT_SIZE;
	private final int RAND_POSY = B_HEIGHT / DOT_SIZE;
	private final int DELAY = 33;
	private final int invStandard = 5;
	
	private final int x[] = new int[ALL_DOTS];
	private final int y[] = new int[ALL_DOTS];
	
	private int dots;
	private int apple_x;
	private int apple_y;
	private int candidateApple_x;
	private int candidateApple_y;
	private int enemy_x;
	private int enemy_y;
	private int firstDots;
	private int totalPoints;
	private int getAppleCounter;
	private int points;
	private int instant_delay; //仕様を練り直すため停止中
	private int three_Consecutive;
	private int invincibleTimeCounter;
	private int enemyMovetime;
	private int distance;
	private int directionNum;
	private int turnDirectionIntervalCounter;
	private int checkPressFirstCounter;
	private int increaseBallsPutCounter;
	private double stopTimeSec;
	private double bgmVolume = 0.5;
	private double seVolume = 0.5;
	private int keepBgmValue = 50;
	private int keepSeValue = 50;
	
	private boolean leftDirection;
	private boolean rightDirection;
	private boolean upDirection;
	private boolean downDirection;
	
	static SnakeScreen screen;
	
	private boolean invincible;
	private boolean timeStop;
	private boolean leftPushing;
	private boolean rightPushing;
	private boolean firstPush;
	private boolean increaseBall;
	
	private Timer timer;
	private Image ball;
	private Image apple;
	private Image head;
	private Image toxicapple;
	private Image timestar;
	
	BasicPlayer p = new BasicPlayer();
	BasicPlayer q = new BasicPlayer();
	
	public Board() {
		
		initBoard();
	}
	
	private void initBoard() {
		addKeyListener(new TAdapter());
		setBackground(Color.black);
		setFocusable(true);
		
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		loadImages();
		screen = SnakeScreen.TITLE;
		}
	
	private void loadImages() {
		
		//実行可能jarにexportしてもファイルをjarの中から探しだし絶対パスを返すことで、png画像を参照できるようにする
		
		URL url1 = this.getClass().getResource("dot.png");
		
		URL url2 = this.getClass().getResource("apple.png");
		
		URL url3 = this.getClass().getResource("head.png");
		
		URL url4 = this.getClass().getResource("toxicapple.png");
		
		URL url5 = this.getClass().getResource("timestar1.png");
		
		ImageIcon iid = new ImageIcon(url1);
		ball = iid.getImage();
		
		ImageIcon iia = new ImageIcon(url2);
		apple = iia.getImage();
		
		ImageIcon iih = new ImageIcon(url3);
		head = iih.getImage();
		
		ImageIcon iio = new ImageIcon(url4);
		toxicapple = iio.getImage();
		
		ImageIcon iip = new ImageIcon(url5);
		timestar = iip.getImage();
		
	}
	
	//initGame()はinitSnake()とinitEnemy()、initOthers()で必要な変数を初期化している
	
	private void initGame() {
		startBGM();
		timer = new Timer(DELAY, this);
		timer.start();
		
		initSnake();
		initEnemy();
		initOthers();
		
		locateApple();
		}
	
	private void initSnake() {
		firstDots = 25;
		dots = firstDots;
		
		for (int z = 0; z < dots; z++) {
			x[z] = (int)(B_WIDTH/2) - z * DOT_SIZE;
			y[z] = (int)(B_HEIGHT/2);
		}
		
		directionNum = 1;
	}
	
	private void initEnemy() {
		
		enemy_x = 150;
		enemy_y = 150;
		enemyMovetime = 0;
		
	}
	
	private void initOthers() {
		
		screen = SnakeScreen.INGAME;
		
		invincible = false;
		increaseBall = false;
		
		totalPoints = 0;
		getAppleCounter = 0;
		instant_delay = DELAY; //instant_delayは停止中
		three_Consecutive = 0;
		invincibleTimeCounter = invStandard;
		directionNum = 1;
		increaseBallsPutCounter = 0;
		stopTimeSec = 210;
		timeStop = false;
		leftPushing = false;
		rightPushing = false;
	}
	private void startBGM() {
		try {
			int r = (int) (Math.random() * 10);
			
			bgmVolume = (double) keepBgmValue / 100;
			URL bgm = this.getClass().getResource("Foyon, Rumusen - Winter myfreemp3.vip .mp3");
			
			switch (r % 2) {
			case 0:
				URL url1 = this.getClass().getResource("Foyon, Rumusen - Winter myfreemp3.vip .mp3");
				bgm = url1;
				break;
			case 1:
				URL url2 = this.getClass().getResource("sariyukuhuyuwoutau.mp3");
				bgm = url2;
				break;
				}
			
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
	
	private void resumeBGM() {
		try {
			p.resume();
			p.setGain(bgmVolume);
		} catch(Exception e) {
			System.out.println("BGMを一時停止解除出来ませんでした");
			e.printStackTrace();
		}
	}
	
	private void stopBGM() {
		try {
			p.stop();
		} catch(Exception e) {
			System.out.println("BGMを停止出来ませんでした");
			e.printStackTrace();
		}
	}
	
	private void checkBGMinPlaying() {
		if(p.getStatus() == BasicPlayer.STOPPED) {
			startBGM();
		}
	}
	
	private void startSE(String key) {
		try {
			seVolume = (double) keepSeValue / 100;
			URL se = this.getClass().getResource("getapple.mp3");
			
			switch(key) {
			case "getapple":
				URL se1 = this.getClass().getResource("getapple.mp3");
				se = se1;
				break;
			case "timestop":
				URL se2 = this.getClass().getResource("clock.mp3");
				p.pause();
				se = se2;
				break;
			case "dead":
				URL se3 = this.getClass().getResource("death.mp3");
				se = se3;
				break;
			case "dash":
				URL se4 = this.getClass().getResource("punchdash.mp3");
				se = se4;
				break;
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
	
	private void stopSE() {
		try {
			q.stop();
		} catch (BasicPlayerException e) {
			e.printStackTrace();
			System.out.println("SEを停止出来ませんでした");
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		doDrawing(g);
	}
	
	private void doDrawing(Graphics g) {
		this.setLayout(null);
		
		switch(screen) {
		case TITLE:
			title(g);
			break;
		case INGAME:
			inGame(g);
			break;
		case GAMEOVER:
			gameOver(g);
			timer.stop();
			break;
		case SYSTEM:
			system(g);
			break;
		}
		
	}
	//設計中。まだ設定画面とゲーム時画面に移るボタンと操作説明だけ
	private void title(Graphics g) {
		
		String msg1 = "← 左旋回　　　　　　右旋回 →";
		String msg3 = "ダッシュ";
		String msg4 = "↑";
		String msg5 = "時間停止";
		String msg6 = "↓";
		
		Font small = new Font("Helvetica", Font.BOLD, 15);
		FontMetrics metr = getFontMetrics(small);
		
		g.setColor(Color.gray);
		g.setFont(small);
		
		g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2, (B_HEIGHT + small.getSize()) / 2);
		g.drawString(msg3, (B_WIDTH - metr.stringWidth(msg3)) / 2, (B_HEIGHT + small.getSize()) / 2 - small.getSize() * 2);
		g.drawString(msg4, (B_WIDTH - metr.stringWidth(msg4)) / 2, (B_HEIGHT + small.getSize()) / 2 - small.getSize() * 3);
		g.drawString(msg5, (B_WIDTH - metr.stringWidth(msg5)) / 2, (B_HEIGHT + small.getSize()) / 2 + small.getSize() * 2);
		g.drawString(msg6, (B_WIDTH - metr.stringWidth(msg6)) / 2, (B_HEIGHT + small.getSize()) / 2 + small.getSize() * 3);
		
		
		JButton button1 = new JButton("START");
		final int button1_WIDTH = 80, button1_HEIGHT = 30;
		this.add(button1);
		button1.setMargin(new Insets(0, 0, 0, 0));
		button1.setBounds((B_WIDTH - button1_WIDTH) / 2, (B_HEIGHT - button1_HEIGHT) / 2, button1_WIDTH, button1_HEIGHT);
		button1.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				initGame();
				removeAll(); //remove(button1)だと機能しなかった
			}
		});
		
		JButton button2 = new JButton("SYSTEM");
		final int button2_WIDTH = 70, button2_HEIGHT = 20;
		this.add(button2);
		button2.setMargin(new Insets(0, 0, 0, 0));
		button2.setBounds(0, 0, button2_WIDTH, button2_HEIGHT);
		button2.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				screen = SnakeScreen.SYSTEM;
				repaint();
				removeAll();
			}
		});
	}
	//ゲーム中に必要な最低限の情報と、蛇の体及び頭の画像を張る
	private void inGame(Graphics g) {
		
		g.setColor(Color.green);
		g.drawImage(apple, apple_x, apple_y, this);
		g.drawImage(toxicapple, enemy_x, enemy_y, this);
		g.drawOval(enemy_x-5,enemy_y-5,20,20);
		
		String stringinvincible = (invincible || (dots == firstDots)) ? "invincible" : " ";
		String doublestopTimesec = String.valueOf((double)(Math.round((double)(stopTimeSec / 30) * 1000))/1000);
		String stringstopTimesec = (stopTimeSec > 0) ? doublestopTimesec + " sec" : "not available";
		
		g.drawString(stringinvincible, x[0] + 20, y[0] + 20);
		g.drawString(Integer.toString(totalPoints) + " points",210,150);
		g.drawString(stringstopTimesec,210,170);
		g.drawString("distance " + Integer.toString(distance),210,190);
		
		int randomnumX = (int) (Math.random() * 300);
		int randomnumY = (int) (Math.random() * 300);
		
		if (timeStop) {
			g.drawImage(timestar,randomnumX,randomnumY,this);
			setBackground(Color.darkGray);
		}
		
		if (!timeStop) setBackground(Color.BLACK);
		for (int z = 0; z < dots; z++) {
			g.drawImage(z == 0 ? head : ball, x[z], y[z], this);
		}
		
		Toolkit.getDefaultToolkit().sync();
		
	}
	//ゲームオーバーでステータス等を表示する
	private void gameOver(Graphics g) {
		
		stopBGM();
		
		String msg1 = "Game Over";
		String msg2 =  totalPoints + " points";
		String msg3 = getAppleCounter + " apples";
		String msg4 = "press SPACE to restart!!";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		Font large = new Font("Helvetica", Font.BOLD, 30);
		FontMetrics metr = getFontMetrics(small);
		
		g.setColor(Color.white);
		g.setFont(large);
		g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1) * 2) / 2, B_HEIGHT / 2);
		g.setFont(small);
		g.drawString(msg2, (B_WIDTH - metr.stringWidth(msg2)) / 2, B_HEIGHT / 2 + 50);
		g.setColor(Color.pink);
		g.drawString(msg3, (B_WIDTH - metr.stringWidth(msg3)) / 2, B_HEIGHT / 2 + 80);
		g.setColor(Color.gray);
		g.drawString(msg4, (B_WIDTH - metr.stringWidth(msg4)) / 2, B_HEIGHT / 2 + 100);
		
		JButton button1 = new JButton("BACK TITLE");
		this.add(button1);
		button1.setBounds((B_WIDTH - 120) / 2, 0, 120, 30);
		button1.setMargin(new Insets(0, 0, 0, 0));
		button1.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				screen = SnakeScreen.TITLE;
				repaint();
				removeAll();
			}
		});
	}
	//音量等、各種設定を設計するところ
	private void system(Graphics g) {
		
		int sliderWIDTH = 200;
		int sliderHEIGHT = 50;
		final int button1_WIDTH = 70, button1_HEIGHT = 20;
		
		JSlider slider1 = new JSlider(0, 100, keepBgmValue);
		this.add(slider1);
		slider1.setBounds((B_WIDTH - sliderWIDTH) / 2, sliderHEIGHT, sliderWIDTH, sliderHEIGHT);
		slider1.setMajorTickSpacing(10);
		slider1.setPaintTicks(true);
		slider1.setPaintTrack(false);
		slider1.setForeground(Color.GREEN);
		slider1.setBackground(Color.BLACK);
		slider1.setLabelTable(slider1.createStandardLabels(20));
		slider1.setPaintLabels(true);
		
		String msg1 = "BGM";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr1 = getFontMetrics(small);
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg1, (B_WIDTH - metr1.stringWidth(msg1)) / 2, sliderHEIGHT);
		
		JSlider slider2 = new JSlider(0, 100, keepSeValue);
		this.add(slider2);
		slider2.setBounds((B_WIDTH - sliderWIDTH) / 2, sliderHEIGHT * 2 + metr1.getHeight(), sliderWIDTH, sliderHEIGHT);
		slider2.setMajorTickSpacing(10);
		slider2.setPaintTicks(true);
		slider2.setPaintTrack(false);
		slider2.setForeground(Color.GREEN);
		slider2.setBackground(Color.BLACK);
		slider2.setLabelTable(slider2.createStandardLabels(20));
		slider2.setPaintLabels(true);
		
		String msg2 = "SE";
		FontMetrics metr2 = getFontMetrics(small);
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg2, (B_WIDTH - metr2.stringWidth(msg2)) / 2, sliderHEIGHT * 2 + metr1.getHeight());
		
		
		JButton button1 = new JButton("TITLE");
		this.add(button1);
		button1.setMargin(new Insets(0, 0, 0, 0));
		button1.setBounds(0, 0, button1_WIDTH, button1_HEIGHT);
		button1.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				
				keepBgmValue = slider1.getValue();
				keepSeValue = slider2.getValue();
				screen = SnakeScreen.TITLE;
				repaint();
				removeAll();
			}
		});
	}
	//当たり判定は円形。リンゴ取得で関係メソッドを実行
	private void checkApple() {
		
		boolean appleHitbox;
		
		appleHitbox = ((apple_x - x[0]) * (apple_x - x[0]) + (apple_y - y[0]) * (apple_y - y[0])) <= 11 * 11; //今は11(蛇の頭の直径は10)ドットに固定してる
		
		if (appleHitbox) {
			
			increaseBall = true;
			
			startSE("getapple");
			pointsCounter();
			locateApple();
			triStreak();
			statusShower();
		}
	}
	//ポイントはその取ったリンゴが生成された時点での蛇の頭との距離(ドット)
	private void pointsCounter() {
		
		getAppleCounter += 1;
		points = distance;
		totalPoints += points;
	}
	//このメソッドの中では書いていないが、ダッシュと時間停止を実行したときカウンターが初期化される。それはTAdapterで定義されている。
	private void triStreak() {
		
		three_Consecutive += 1;
		
		if(three_Consecutive >= 3) {//三回連続で上記技なしでリンゴを取得すると倍率。いずれここも設定出来るようにする。
			
			totalPoints *= 1.1;
			three_Consecutive = 0;
			
			System.out.println("TRYSTREAK! +10% BONUS!");//何かinGame()中で描画したいな
		}
	}
	//これが無いと左右矢印のキーを押しても即座に反応しなくなる。すなわち、左右矢印キーを連打して方向をこまめに変更するプレーも可能にしている。
	private void PushImmidiateReact() {
		
		if ((checkPressFirstCounter == 0) && (leftPushing || rightPushing)) {
			firstPush = true;
			checkPressFirstCounter++;
		}
		
		if (leftPushing && firstPush) {
			turnDirectionIntervalCounter = 6;
			firstPush = false;
		}
		
		if (rightPushing && firstPush) {
			turnDirectionIntervalCounter = 6;
			firstPush = false;
		}
	}
	//8方角をenum型で実装しようとしたが、インクリメントとデクリメントを用いれば方向転換処理が簡潔だと考え、intにした
	private void directionDefine() {
		
		if(leftPushing || rightPushing) {
			turnDirectionIntervalCounter++;
		}
		
		if(leftPushing && (turnDirectionIntervalCounter > 5)) {
			directionNum--;//反時計回り
			turnDirectionIntervalCounter = 0;
			if(directionNum < 0) directionNum = 7;
		}
		
		if(rightPushing && (turnDirectionIntervalCounter > 5)) {
			directionNum++;//時計回り
			turnDirectionIntervalCounter = 0;
			if(directionNum > 7) directionNum = 0;
		}
		
		
		switch (directionNum) {
		
		case 0://北東
			leftDirection = false;
			rightDirection = true;
			upDirection = true;
			downDirection = false;
			break;
			
		case 1://東
			leftDirection = false;
			rightDirection = true;
			upDirection = false;
			downDirection = false;
			break;
			
		case 2://南東
			leftDirection = false;
			rightDirection = true;
			upDirection = false;
			downDirection = true;
			break;
			
		case 3://南
			leftDirection = false;
			rightDirection = false;
			upDirection = false;
			downDirection = true;
			break;
			
		case 4://南西
			leftDirection = true;
			rightDirection = false;
			upDirection = false;
			downDirection = true;
			break;
			
		case 5://西
			leftDirection = true;
			rightDirection = false;
			upDirection = false;
			downDirection = false;
			break;
			
		case 6://北西
			leftDirection = true;
			rightDirection = false;
			upDirection = true;
			downDirection = false;
			break;
			
		case 7://北
			leftDirection = false;
			rightDirection = false;
			upDirection = true;
			downDirection = false;
			break;
		}
	}
	
	private void move() {
		
		for (int z = dots; z > 0; z--) {
			x[z] = x[(z - 1)];
			y[z] = y[(z - 1)];
		}
		
		if (leftDirection) {
			x[0] -= DOT_SIZE/5;
		}
		
		if (rightDirection) {
			x[0] += DOT_SIZE/5;
		}
		
		if (upDirection) {
			y[0] -= DOT_SIZE/5;
		}
		
		if (downDirection) {
			y[0] += DOT_SIZE/5;
		}
	}
	/*
	敵の動きを規定。4方角と距離(敵自身のサイズ10ドット未満の整数)をランダムで生成。それを一定回数のenemyMove()の実行を
	検知して処理。また、上記とは別にenemyMove()が実行されるたびに、蛇を追尾する方向に移動する。
	*/
	private void enemyMove() {
		
		int enemyDirection = (int) (Math.random() * 4);
		int e = (int) (Math.random() * 10);
		
		if (enemyMovetime >= 5) {
			
			switch (enemyDirection) {
			case 0://nisi
				enemy_x -= e;
				break;
				
			case 1://higasi
				enemy_x += e;
				break;
				
			case 2://kita
				enemy_y -= e;
				break;
				
			case 3://minami
				enemy_y += e;
				break;
			}
			
			enemyMovetime = 0;
		} else {
			
			enemyMovetime++;
		}
		
		if (x[0] > enemy_x) {
			
			enemy_x += 1 * DOT_SIZE/10;
		} else {
			
			enemy_x -= 1 * DOT_SIZE/10;
		}
		
		if (y[0] > enemy_y) {
			
			enemy_y += 1 * DOT_SIZE/10;
		} else {
			
			enemy_y -= 1 * DOT_SIZE/10;
		}
	}
	//無敵の時間を処理。
	private void invincibleCounter() {
		
		if (invincible) {
			
			if ((invincibleTimeCounter == 0)) {
				
				invincible = false;
				invincibleTimeCounter = invStandard;
				
				System.out.println("no longer invincible");
				System.out.println(" ");
				
			} else {
				
				invincibleTimeCounter--;
			}
		}
	}
	//4方向の壁と敵との当たり判定を処理。タイマーの停止も担う。
	private void checkCollision() {
		
		boolean myHitbox;
		boolean enemyHitbox;
		
		for (int z = dots; z > 0; z--) {
			
			myHitbox = ((x[z] - x[0]) * (x[z] - x[0]) + (y[z] - y[0]) * (y[z] - y[0])) <= 5 * 5;
			enemyHitbox = ((enemy_x - x[0]) * (enemy_x - x[0]) + (enemy_y - y[0]) * (enemy_y - y[0])) <= 10 * 10;
			
			if ((!invincible) && (z > firstDots) && (myHitbox || enemyHitbox)) {
				screen = SnakeScreen.GAMEOVER;
				startSE("dead");
			}
		}
		
		if (y[0] >= B_HEIGHT || y[0] < -5 || x[0] >= B_WIDTH || x[0] < -5) {
			screen = SnakeScreen.GAMEOVER;
			startSE("dead");
		}
		
		if (!(screen == SnakeScreen.INGAME)) {
			timer.stop();
		}
	}
	/*
	高速で滑らかなな蛇の移動を実現する上で、障害となっていた蛇の胴体の増加数調整。
	一つずつ配列に入れていかないと、座標がない胴体が変な場所に現れてしまう。取り敢えず、蛇が動いていないと
	配列の中身が移動しないので、永遠に胴体の座標を与えられない。したがって、while文等を用いづらい結論に達した。
	蛇が移動するたびに丁寧に胴体を1つ増やす。この関数はincreaseBallがtrueでないと実行されないようになっている。
	なので指定数胴体を増加させ終えたら、boolean型でfalseに。
	*/
	private void increaseBall() {
		
		if (increaseBallsPutCounter < 5) {
			dots += 1;
			increaseBallsPutCounter ++;
		} else {
			increaseBallsPutCounter = 0;
			increaseBall = false;
		}
	}
	/*
	generateAppleLocation()とcheckAppleLocation()の相互実行。そうして距離条件に適合するまで循環させる。
	適合すれば、途中にbreakをはさんでfor処理の後の要素の処理を無視しながら、
	locateApple()一行目に帰り候補点の座標をリンゴの座標として代入する。
	*/
	private void locateApple() {
		
		generateAppleLocation();
		apple_x = candidateApple_x;
		apple_y = candidateApple_y;
		distance = (int) Math.sqrt((apple_x - x[0]) * (apple_x - x[0]) + (apple_y - y[0]) * (apple_y - y[0]));
	}
	
	private void generateAppleLocation() {
		int r = (int) (Math.random() * RAND_POSX);
		candidateApple_x = ((r * DOT_SIZE));
		r = (int) (Math.random() * RAND_POSY);
		candidateApple_y = ((r * DOT_SIZE));
		checkAppleLocation();
	}
	
	private void checkAppleLocation() {
		
		for (int z = 1; z < dots;z++) {
			
			boolean distanceBetweenAppleAndBody = (int) Math.sqrt((candidateApple_x - x[z]) * (candidateApple_x - x[z]) + (candidateApple_y - y[z]) * (candidateApple_y - y[z])) <= 70;
			boolean distanceBetweenAppleAndEnemy = (int) Math.sqrt((enemy_x - candidateApple_x) * (enemy_x - candidateApple_x) + (enemy_y - candidateApple_y) * (enemy_y - candidateApple_y)) >= 180;
			
			if (distanceBetweenAppleAndBody || distanceBetweenAppleAndEnemy) {
				System.out.println("REROLLED APPLE LOCATION");
				generateAppleLocation();
				break;
			}
		}
	}
	
	private void statusShower() {
		
		System.out.println("Next " + distance);
		
		String str = Integer.toString(totalPoints);
		str = str + " points";
		System.out.println(str);
		
		str = Integer.toString(getAppleCounter);
		str = str + " apples";
		System.out.println(str);
		
		str = String.valueOf((double) instant_delay/1000) + " second interval(" + String.valueOf((double) DELAY/instant_delay) + ")";
		System.out.println(str);
		
		str = " ";
		System.out.println(str);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (screen == SnakeScreen.INGAME) {
			if(!timeStop) {
				checkApple();
				directionDefine();
				move();
				invincibleCounter();
				if (increaseBall) increaseBall();
				checkCollision();
				enemyMove();
				checkBGMinPlaying(); //タイマーが実行されるたびにbgmの状態を監視
			} else {
				timeStopCounter(); //時間停止処理のカウンターを時間停止中のみ実行
			}
		}
		repaint();
	}
	//時間停止中はbgmを停止
	private void timeStopCounter() {
		
		if (stopTimeSec == 0) {
			timeStop = false;
			stopSE();
			resumeBGM();
		} else {
			stopTimeSec--;
		}
		
	}
	/*
	矢印左右を押しているかどうかを判別する。また、ダッシュの処理や時間停止の一部処理のほかに、
	ゲームオーバー画面でSPACEキーを押すと即座にゲームが起動する処理も実行している。
	*/
	private class TAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_LEFT && (screen == SnakeScreen.INGAME)) {
				leftPushing = true;
				PushImmidiateReact();
			}
			
			if (key == KeyEvent.VK_RIGHT && (screen == SnakeScreen.INGAME)) {
				rightPushing = true;
				PushImmidiateReact();
			}
			
			if ((key == KeyEvent.VK_UP) && !timeStop && (screen == SnakeScreen.INGAME) && (totalPoints >= 50)) {
				
				invincible = true;
				invincibleTimeCounter = invStandard;
				
				startSE("dash");
				int i = 0;
				do {
					move();
					i++;
				}while(i < 13);
				
				totalPoints -= 50;
				three_Consecutive = 0;
				
				System.out.println("///DASH///");
				statusShower();
			}
			
			if (key == KeyEvent.VK_DOWN && (screen == SnakeScreen.INGAME)) {
				
				if (!timeStop && (totalPoints >= 50) && (stopTimeSec > 0)) {
					
					timeStop = true;
					startSE("timestop");
					totalPoints -= 50;
					three_Consecutive = 0;
					
					System.out.println("///TIMESTOP///");
					statusShower();	
				} else if(timeStop) {
					
					timeStop = false;
					stopSE();
					resumeBGM();
					invincibleTimeCounter = 30;
					invincible = true;
				}
			}
			
			if (key == KeyEvent.VK_SPACE && (screen == SnakeScreen.GAMEOVER)) {
				removeAll();
				initGame();
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_LEFT && (screen == SnakeScreen.INGAME)) {
				leftPushing = false;
				checkPressFirstCounter = 0;
				turnDirectionIntervalCounter = 0;
			}
			
			if (key == KeyEvent.VK_RIGHT && (screen == SnakeScreen.INGAME)) {
				rightPushing = false;
				checkPressFirstCounter = 0;
				turnDirectionIntervalCounter = 0;
			}
		}
	}
}
