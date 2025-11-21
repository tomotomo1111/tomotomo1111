package ST;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.net.URL;

import javax.swing.ImageIcon;

public class Bicycle extends Map{
	
	private static final int BICYCLE_WIDTH = 32;
	private static final int BICYCLE_HEIGHT = 32;
	private static final double JUMP_SPEED = 3.0;//3.0
	private static final double ACCELERATION = 0.007;//0.007
	private static final double GRAVITY = 0.03;//0.03
	private static final double FRICTIONFORCE = 0.005;
	private static final double VELOCITYX_MAX = 1.5;

	private static double x;
	private static double y;
	public static double velocityX;
	public static double velocityY;
	public static double hitboxX;
	public static double hitboxY;
	private static double keepVelocity = 0;
	
	private static int fallSoundTimer = 1;
	private static int liftSoundTimer = 1; 
	
	public static boolean onGround;
	private static boolean canAirJump;
	private static boolean saveJumpFromFallingWithFallingBlock;
	private static boolean transitionBetweenBeginningAndEnd;
	private static boolean isRIGHT;
	
	private Image bicycle_right;
	private Image bicycle_left;
	private Image bicycle_right_down;
	private Image bicycle_left_down;
	private Image bicycle_right_45_up;
	private Image bicycle_left_45_up;
	private Image brakefire_left;
	private Image brakefire_right;
	private Image fire_left;
	private Image fire_right;
	

	Sound sound = new Sound();
	
	public Bicycle() {
		
		loadImage();
		resetBicycleStatus();
	}
	
	private void loadImage() {
		
		URL url_bicycle_right = this.getClass().getResource("bicycle-right.png");
		ImageIcon iib_r = new ImageIcon(url_bicycle_right);
		bicycle_right = iib_r.getImage();
		
		URL url_bicycle_left = this.getClass().getResource("bicycle-left.png");
		ImageIcon iib_l = new ImageIcon(url_bicycle_left);
		bicycle_left = iib_l.getImage();
		
		URL url_bicycle_right_down = this.getClass().getResource("bicycle-right-vertical-down.png");
		ImageIcon iib_rvd = new ImageIcon(url_bicycle_right_down);
		bicycle_right_down = iib_rvd.getImage();
		
		URL url_bicycle_left_down = this.getClass().getResource("bicycle-left-vertical-down.png");
		ImageIcon iib_lvd = new ImageIcon(url_bicycle_left_down);
		bicycle_left_down = iib_lvd.getImage();
		
		URL url_bicycle_right_45_up = this.getClass().getResource("bicycle-right-45-up.png");
		ImageIcon iib_r45u = new ImageIcon(url_bicycle_right_45_up);
		bicycle_right_45_up = iib_r45u.getImage();
		
		URL url_bicycle_left_45_up = this.getClass().getResource("bicycle-left-45-up.png");
		ImageIcon iib_l45u = new ImageIcon(url_bicycle_left_45_up);
		bicycle_left_45_up = iib_l45u.getImage();
		
		URL url_brakefire_left = this.getClass().getResource("brakefire_left.png");
		ImageIcon iibf_l = new ImageIcon(url_brakefire_left);
		brakefire_left = iibf_l.getImage();
		
		URL url_brakefire_right = this.getClass().getResource("brakefire_right.png");
		ImageIcon iibf_r = new ImageIcon(url_brakefire_right);
		brakefire_right = iibf_r.getImage();
		
		URL url_fire_left = this.getClass().getResource("fire_left.png");
		ImageIcon iif_l = new ImageIcon(url_fire_left);
		fire_left = iif_l.getImage();
		
		URL url_fire_right = this.getClass().getResource("fire_right.png");
		ImageIcon iif_r = new ImageIcon(url_fire_right);
		fire_right = iif_r.getImage();
	}
	
	public static void resetBicycleStatus() {
		
		velocityX = 0;
		velocityY = 0;
		hitboxX = 0;
		hitboxY = 0;
		keepVelocity = 0;
		
		onGround = false;
		canAirJump = false;
		saveJumpFromFallingWithFallingBlock = false;
		transitionBetweenBeginningAndEnd = false;
		isRIGHT = true;
		
		returnStartPositionBicycle(true);
	}
	
	public static void returnStartPositionBicycle(boolean canYposReset) {
		x = 0;
		MainFrame.mapNameTimer = 0;
		if(canYposReset) {
			y = 416;
			
		} else {
			MainFrame.adjustSound(true,"goal");
		}
	}
	

	public void jump() {
		
		if (canAirJump && (!onGround)) {
			MainFrame.adjustSound(true,"djump");
			
			velocityY = -JUMP_SPEED;
			canAirJump = false;
			CalculateAllScore.addTotalDoubleJump();
		}
		
		if (onGround) {
			MainFrame.adjustSound(true,"jump");
			
			velocityY = -JUMP_SPEED;
			onGround = false;
			double velocityWhileJumping = 0.2;
			
			velocityX += isRIGHT ? velocityWhileJumping : -velocityWhileJumping ;
			
			if(velocityX > VELOCITYX_MAX || velocityX < -VELOCITYX_MAX) {
				velocityX = isRIGHT ? VELOCITYX_MAX : -VELOCITYX_MAX;
			}	
		}
	}
	
	public void update() {
		
		int currentTileX = (int) (getBicycleX()/32);
		int currentTileY = (int) (getBicycleY()/32);
		if(onGround && map[currentTileY+1][currentTileX] == 8) {
			map[currentTileY+1][currentTileX] = 9;
		}
		
		if(checkExistanceOfLiftInNearSpace(currentTileX,currentTileY)) {
			if(liftSoundTimer % 150 == 0) {
				MainFrame.adjustSound(true,"lift");
			}
			liftSoundTimer++;
		}
		
		FallingFloor.checkExistanceOfFallingFloor();
		if(MainFrame.downPushing && onGround) brakingToDecreaseVelocity();
		if(MainFrame.leftPushing && !MainFrame.rightPushing && !MainFrame.downPushing && onGround) leftMove();
		if(MainFrame.rightPushing && !MainFrame.leftPushing && !MainFrame.downPushing && onGround) rightMove();
		if(!MainFrame.leftPushing && !MainFrame.rightPushing && onGround) bicycleDecreaseVelocityByFrictionForce();
		if(transitionBetweenBeginningAndEnd) returnStartPosition();
		checkAndMoveNewPoint();
		if(saveJumpFromFallingWithFallingBlock) checkFallingWithFallingBlock();
	
	}
	
	private static boolean checkExistanceOfLiftInNearSpace(int x, int y) {
		int range = 8;
		if ((8 <= x && x <= 53)&&(8 <= y && y <= 11)) {
			for(int j = 0; j < 17;j++) {
				for(int i = 0; i < 17;i++) {
					if(map[y+j-8][x+i-8] == 4) return true;
				}
			}
			return false;
		} else {
			
			if(Math.min(x, 8) == x) {
				if(Math.min(y, 8) == y) {
					range = Math.min(x, y);
				} else if(Math.max(y, 11) == y){
					range = Math.min(x,19-y);
				} else {
					range = x;
				}
			} else if(Math.max(x, 53) == x){
				if(Math.min(y, 8) == y) {
					range = Math.min(60-x,y);
				} else if(Math.max(y, 11) == y){
					range = Math.min(60-x, 19-y);
				} else {
					range = 60-x;
				}
			} else {
				if(Math.min(y, 8) == y) {
					range = y;
				} else {
					range = 19-y;
				}
			}
		}
		
		for(int j = 0; j < 2*range+1;j++) {
			for(int i = 0; i < 2*range+1;i++) {
				if(map[y+j-range][x+i-range] == 4) return true;
			}
		}
		
		return false;
	}

	private void brakingToDecreaseVelocity() {
		//*3がデフォルト
		if(velocityX >= ACCELERATION * 3) {
			velocityX -= ACCELERATION * 3;
		} else if(velocityX <= -ACCELERATION * 3) {
			velocityX += ACCELERATION * 3;
		} else {
			velocityX = 0;
		}
	}
	
	private void leftMove() {
		//-1.5がデフォルト
		if(velocityX > -VELOCITYX_MAX) {
			velocityX -= ACCELERATION * 1;
		} else {
			velocityX = -VELOCITYX_MAX;
		}
	}
	
	private void rightMove() {
		
		if(velocityX < VELOCITYX_MAX) {
			velocityX += ACCELERATION * 1;
		} else {
			velocityX = VELOCITYX_MAX;
		}
	}
	
	private void bicycleDecreaseVelocityByFrictionForce() {
		
		if(velocityX > FRICTIONFORCE) {
			velocityX -= FRICTIONFORCE;
		} else if(velocityX < -FRICTIONFORCE) {
			velocityX += FRICTIONFORCE;
		} else {
			velocityX = 0;
		}
	}
	
	private void returnStartPosition() {
		
		/*
		マップ右端の6に触れてbooleanをtrueにした後、updata()が実行されると初期値に戻りマップを再生成する。
		初期値側にも6があり、そちらはbooleanをfalseにする。こうしないとマップの抽選が描写される。
		*/
		
		GenerateNewMap.GenerateMap();
		returnStartPositionBicycle(false);
		CalculateAllScore.addTotalThroughMap();
	}
	
	private void checkAndMoveNewPoint() {
		
		x = (x + MainFrame.mapWIDTH) % MainFrame.mapWIDTH;
		double newX = x + velocityX;
		hitboxX = newX;
		Point tile = getTileCollision(this, newX, y);
		if (tile == null) {
			x = newX;
			if(velocityX == 0) {
				velocityX += keepVelocity;
				keepVelocity = 0;
			}
		} else {
			if (velocityX > 0) {
				x = MainFrame.tilesToPixels(tile.x) - BICYCLE_WIDTH;
			} else if (velocityX < 0) {
				x = MainFrame.tilesToPixels(tile.x + 1);
			}
			keepVelocity = velocityX;
			velocityX = 0;
		}
		
		
		velocityY += GRAVITY;
		double newY = y + velocityY;
		hitboxY = newY;
		tile = getTileCollision(this, x, newY);
		if (tile == null) {
			y = newY;
			if(!saveJumpFromFallingWithFallingBlock)onGround = false;
		} else {
			if (velocityY > 0) {
				y = MainFrame.tilesToPixels(tile.y) - BICYCLE_HEIGHT;
				onGround = true;
				canAirJump = true;
				if(velocityY > 1.2) MainFrame.adjustSound(true,"landing");
			} else if (velocityY < 0) {
				y = MainFrame.tilesToPixels(tile.y + 1);
			}
			velocityY = 0;
		}
	}
	
	//落下床に乗り続けている間(落ちるときも)は、saveJumpFromFallingWithFallingBlockのtrueとfalseを繰り返すが、落下床から離れるとtrueが更新されなくなる。
	private void checkFallingWithFallingBlock() {
			saveJumpFromFallingWithFallingBlock = false;
			if(fallSoundTimer % 100 == 0) {
				MainFrame.adjustSound(true,"fall");
			}
			fallSoundTimer++;
	}
	
	public void bicycleDraw(Graphics g, int offsetX, int offsetY) {
		
		if(velocityX > 0) {
			isRIGHT = true;
			
		} else if(velocityX < 0) {
			isRIGHT = false;
		}
		
		double degreeTransformBicyclePictureVelocity = 1.9;
		
		if(onGround) {
			g.setColor(Color.green);
		} else {
			
			if(canAirJump) {
				g.setColor(Color.yellow);
			} else {
				g.setColor(Color.gray);
			}
		}
		
		if(isRIGHT) {
			if(velocityY > degreeTransformBicyclePictureVelocity) {
				g.drawImage(bicycle_right_down, getBicycleX() + offsetX, getBicycleY() + offsetY, null);
				g.fillRect((int) x + offsetX + (BICYCLE_WIDTH * 3) / 4, (int) y + offsetY + (BICYCLE_WIDTH * 3) / 4, BICYCLE_WIDTH / 4, BICYCLE_HEIGHT / 4);
			} else if(velocityY < -degreeTransformBicyclePictureVelocity) {
				g.drawImage(bicycle_right_45_up, getBicycleX() + offsetX, getBicycleY() + offsetY, null);
				g.fillRect((int) x + offsetX, (int) y + offsetY, BICYCLE_WIDTH / 4, BICYCLE_HEIGHT / 4);
			} else {
				g.drawImage(bicycle_right, getBicycleX() + offsetX, getBicycleY() + offsetY, null);
				g.fillRect((int) x + offsetX + (BICYCLE_WIDTH * 3) / 4, (int) y + offsetY, BICYCLE_WIDTH / 4, BICYCLE_HEIGHT / 4);
			}
		} else {
			if(velocityY > degreeTransformBicyclePictureVelocity) {
				g.drawImage(bicycle_left_down, getBicycleX() + offsetX, getBicycleY() + offsetY, null);
				g.fillRect((int) x + offsetX, (int) y + offsetY + (BICYCLE_WIDTH * 3) / 4, BICYCLE_WIDTH / 4, BICYCLE_HEIGHT / 4);
			} else if(velocityY < -degreeTransformBicyclePictureVelocity) {
				g.drawImage(bicycle_left_45_up, getBicycleX() + offsetX, getBicycleY() + offsetY, null);
				g.fillRect((int) x + offsetX + (BICYCLE_WIDTH * 3) / 4, (int) y + offsetY, BICYCLE_WIDTH / 4, BICYCLE_HEIGHT / 4);
			} else {
				g.drawImage(bicycle_left, getBicycleX() + offsetX, getBicycleY() + offsetY, null);
				g.fillRect((int) x + offsetX, (int) y + offsetY, BICYCLE_WIDTH / 4, BICYCLE_HEIGHT / 4);
			}
		}
		g.setColor(Color.white);
		g.drawRect((int)hitboxX+offsetX,(int)hitboxY+offsetY,4,4);
		if(MainFrame.downPushing && onGround && velocityX != 0) g.drawImage(isRIGHT ? brakefire_right : brakefire_left, getBicycleX() + offsetX + (isRIGHT ? -BICYCLE_WIDTH : +BICYCLE_WIDTH), getBicycleY() + offsetY, null);
		if(!MainFrame.leftPushing && !MainFrame.rightPushing && onGround && velocityX != 0) g.drawImage(isRIGHT ? fire_right : fire_left, getBicycleX() + offsetX + (isRIGHT ? -BICYCLE_WIDTH : +BICYCLE_WIDTH), getBicycleY() + offsetY, null);
		
	}

	public int getBicycleX() {
		return (int)x;
	}
	 
	public int getBicycleY() {
		return (int)y;
	}
	
	private Point getTileCollision(Bicycle player, double newX, double newY) {
		
		if(velocityX > 0) {
			newX = Math.ceil(newX);
		} else {
			newX = Math.floor(newX);
		}
		if(velocityY > 0) {
			newY = Math.ceil(newY);
		} else {
			newY = Math.floor(newY);
		}
		
		double fromX = Math.min((double)getBicycleX(), newX);
		double fromY = Math.min((double)getBicycleY(), newY);
		double toX = Math.max((double)getBicycleX(), newX);
		double toY = Math.max((double)getBicycleY(), newY);
		int fromTileX = MainFrame.pixelsToTiles(fromX);
		int fromTileY = MainFrame.pixelsToTiles(fromY);
		int toTileX = MainFrame.pixelsToTiles(toX + BICYCLE_WIDTH - 1);
		int toTileY = MainFrame.pixelsToTiles(toY + BICYCLE_HEIGHT - 1);
		
		Point tmp = null;
		
		for (int x = fromTileX; x <= toTileX; x++) {
			for (int y = fromTileY; y <= toTileY; y++) {
				
				if (x < 0 || x >= MainFrame.COL) {
					return new Point(x, y);
				}
				
				if (y < 0 || y >= MainFrame.ROW) {
					return new Point(x, y);
				}
				
				if (map[y][x] != 0 && map[y][x] != 3 && map[y][x] != 5 && map[y][x] != 6 && map[y][x] != 7) {
					tmp = new Point(x, y);
					if(velocityY <= 0 && (map[y][x] == 8 || map[y][x] == 9)) return null;
				}
				
				if (map[y][x] == 5) {
					if(velocityX >= 0 && x == fromTileX && map[y][toTileX] == 5) {
						if(MainFrame.downPushing) CalculateAllScore.addTotalBrake();
						MainFrame.scene = EnumScreen.TyariScreen.GAMEOVER;
					}
					if(velocityX < 0 && x == toTileX && map[y][fromTileX] == 5) {
						if(MainFrame.downPushing) CalculateAllScore.addTotalBrake();
						MainFrame.scene = EnumScreen.TyariScreen.GAMEOVER;
					}
				}
				
				if (map[y][x] == 6) {
					if(!(x == 0)) {
						transitionBetweenBeginningAndEnd = true;
					} else {
						transitionBetweenBeginningAndEnd = false;
					}
				}
				
				if (map[y+1][x] == 8 && onGround && velocityY == 0) {

					map[y+1][x] = 9;
				}
				
				if (map[y+1][x] == 8 || map[y+1][x] == 9) {
					saveJumpFromFallingWithFallingBlock = true;
				}
			}
		}
		
		return tmp;
	}
}