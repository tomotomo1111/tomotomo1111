import sys
import pygame
import random
import math
import copy
from enum import Enum, auto
import json
from GenerateMap import generateMap

#################### 初期設定 ####################
# 初期化
pygame.init()
generateMap(100, 100)
with open("../settings/settings.json", "r", encoding="utf-8") as f1:
    settings = json.load(f1)
with open("../settings/map.json", "r", encoding="utf-8") as f2:
    map = json.load(f2)

WIDTH, HEIGHT = settings["WIDTH"], settings["HEIGHT"]
# フレームサイズ
screen = pygame.display.set_mode((WIDTH, HEIGHT))
# 上の名前
pygame.display.set_caption("BomberMan")
# チック処理
clock = pygame.time.Clock()


# 色 (R, G, B)
WHITE  = (255, 255, 255)
BLUE   = ( 70, 130, 180)
BLACK  = (  0,   0,   0)
GRAY   = (128, 128, 128)
ORANGE = (255, 165,   0)
RED    = (255,   0,   0)
GREEN  = (  0, 255,   0)

# フォント
name_font = pygame.font.Font(None, 20)
bar_font = pygame.font.Font(None, 32)
title_font = pygame.font.Font(None, 40)

# タイルサイズ
TILE = 32
#################################################


################### 列挙型 ########################
# ゲーム画面管理列挙型
class Gamemode(Enum):
    Start = auto()
    InGame = auto()
    Pause = auto()
    EndGame = auto()
gamemode = Gamemode.Start

# 方向列挙型
class Directions(Enum):
    Up = 0
    Right = auto()
    Down = auto()
    Left = auto()
##################################################


###################### 画像読み込み ###########################
# other.bmp を読み込み
other_sprite_sheet = pygame.image.load("../images/other.png").convert_alpha()
OTHER_SHEET_ROWS = 4
OTHER_SHEET_COLS = 10

# character.bmp を読み込み
chara_sprite_sheet = pygame.image.load("../images/character.png").convert_alpha()

# スプライトシートの行数・列数
CHARA_SHEET_ROWS = 8
CHARA_SHEET_COLS = 15

# キャラ1人の高さ・幅（4×32, 3×32）
CHARA_H = TILE * 4
CHARA_W = TILE * 3

def load_other(index):
    """
    index: キャラクター番号（0〜34）
    １壁アイテム = 1タイル
    """

    # (例) index=0 → 左上から 0番目のキャラ
    #     index=1 → 32*3=96px 右へずれた位置
    x = (index * TILE) % (OTHER_SHEET_COLS * TILE)
    y = ((index * TILE) // (OTHER_SHEET_COLS * TILE)) * TILE
    image = other_sprite_sheet.subsurface(pygame.Rect(
        x, y, TILE, TILE
    ))
    return image

def load_character_anim(index):
    """
    index: キャラクター番号（0〜）
    １キャラ = 縦4 * 横3 の計12コマ
    """

    # キャラクターは横方向に並んでいると仮定
    # (例) index=0 → 左上から 0番目のキャラ
    #     index=1 → 32*3=96px 右へずれた位置
    start_x = (index * CHARA_W) % (CHARA_SHEET_COLS * TILE)
    start_y = ((index * CHARA_W) // (CHARA_SHEET_COLS * TILE)) * CHARA_H

    animations = {
        "up": [],
        "right": [],
        "down": [],
        "left": []
    }

    directions = ["up", "right", "down", "left"]

    for dir_index, dir_name in enumerate(directions):
        for frame in range(3):  # 3 コマ歩行アニメ
            x = start_x + frame * TILE
            y = start_y + dir_index * TILE

            frame_img = chara_sprite_sheet.subsurface(pygame.Rect(
                x, y, TILE, TILE
            ))

            animations[dir_name].append(frame_img)

    return animations
#############################################################


####################### バトルフィールド ####################
cmap = map["maps"]["map2"]
# マップの実際処理する用コピー
map_copy = copy.deepcopy(cmap["map"])
# マップサイズ
MAP_WIDTH_TILE = cmap["width"]
MAP_HEIGHT_TILE= cmap["height"]
MAP_WIDTH = MAP_WIDTH_TILE * TILE
MAP_HEIGHT = MAP_HEIGHT_TILE * TILE

tile_list = [load_other(i) for i in range(0, 35)]
###########################################################


#################### 便利関数 ######################
# 座標をマップのタイルインデックスに変更する関数
def xy2tilef(value):
    return int(value / TILE)
def xy2tilec(value):
    return math.ceil(value / TILE)
def tile2xy(*t):
    return (t[0] * TILE, t[1] * TILE)
# タイル間距離関数
def tiledist(a, b):
    return abs(a[0] - b[0]) + abs(a[1] - b[1])
def get_passed_gametime():
    return pygame.time.get_ticks() - gamestarttime - total_pause_time
def change_map(mapname):
    global cmap
    global map_copy
    global MAP_WIDTH_TILE
    global MAP_HEIGHT_TILE
    global MAP_WIDTH
    global MAP_HEIGHT

    cmap = map["maps"][mapname]
    map_copy = copy.deepcopy(cmap["map"])
    MAP_WIDTH_TILE = cmap["width"]
    MAP_HEIGHT_TILE = cmap["height"]
    MAP_WIDTH = MAP_WIDTH_TILE * TILE
    MAP_HEIGHT = MAP_HEIGHT_TILE * TILE
    
###################################################


#################### 初期抽選 #####################
# 湧く位置を抽選する
spawn_points = [(1, 1)]
# 再利用するようにコピー
spawn_points_copy = copy.deepcopy(spawn_points)

chara_anim_index_list = [i for i in range(0, 10)]
chara_anim_index_list_copy = copy.deepcopy(chara_anim_index_list)


chara_name_list = [
                    "A", "B", "C", "D", "E", "F",
                    "G", "H", "I", "J", "K", "L"
]
###################################################


#################### 初期化 #######################
def game_init():
    global map
    global map_copy
    global spawn_points_copy
    global chara_anim_index_list_copy
    global players_counter_remain
    global player
    global gamemode
    global gamestarttime
    global total_pause_time
    player = None
    change_map("map1")
    spawn_points_copy = copy.deepcopy(spawn_points)
    chara_anim_index_list_copy = copy.deepcopy(chara_anim_index_list)
    players_counter_remain = len(spawn_points_copy)
    player = Player(False)
    gamemode = Gamemode.Start
    gamestarttime = pygame.time.get_ticks()
    total_pause_time = 0
###################################################


# プレイヤー型
class Player:
    def __init__(self, isMoving = False):
        # ランダムに初期位置を選ぶ
        px, py = random.choice(spawn_points_copy)
        spawn_points_copy.remove((px, py)) # ダブり禁止
        # ランダムにキャラクターアニメーションを選ぶ
        chara_anim_index = random.choice(chara_anim_index_list_copy)
        chara_anim_index_list_copy.remove(chara_anim_index) # ダブり禁止
        self.chara_anim = load_character_anim(chara_anim_index)
        self.chara_name = chara_name_list[chara_anim_index]
        # キャラアニメ管理
        self.chara_anim_cycle_time = settings["CHARAANIMCYCLETIME"]
        self.chara_anim_change_time = self.chara_anim_cycle_time // 3
        self.direction = Directions.Down
        self.simultaneous_directions = set()
        # タイル→画面座標へ
        self.x = px * TILE
        self.y = py * TILE
        self.tile_x = px
        self.tile_y = py
        # 動いているかどうか
        self.isMoving = isMoving
        self.enableMove_time = pygame.time.get_ticks()
        self.enableMove_to_time = settings["ENABLEMOVETOTIME"]
        # 残りHP
        self.life = settings["FIRSTLIFE"]
        self.isDeath = False
        # グラデーション
        self.gradation_radius = settings["GRADATIONRADIUS"]
        self.gradations = settings["GRADATIONS"]
        # ワープ
        self.last_warp = (0, 0)
        
    def moveWithDirection(self, delta):
        now = pygame.time.get_ticks()
        if now > self.enableMove_time:
            self.enableMove = True
            dir = player.direction
            if dir == Directions.Up:
                self.move(0, -delta)
            elif dir == Directions.Left:
                self.move(-delta, 0)
            elif dir == Directions.Right:
                self.move(delta, 0)
            elif dir == Directions.Down:
                self.move(0, delta)
    
    def move(self, dx, dy):
        global cmap
        if self.isDeath:
            return

        if not self.enableMove or not self.isMoving:
            return

        dest_x = self.x + dx
        dest_y = self.y + dy

        # プレイヤー矩形
        player_margin_size_x = 10
        player_margin_size_y = 4
        left   = dest_x + player_margin_size_x
        top    = dest_y + player_margin_size_y
        right  = dest_x + TILE - player_margin_size_x - 1
        bottom = dest_y + TILE - player_margin_size_y - 1

        from_tile_x = xy2tilef(left)
        to_tile_x   = xy2tilef(right)

        from_tile_y = xy2tilef(top)
        to_tile_y   = xy2tilef(bottom)

        # 衝突判定
        for y in range(from_tile_y, to_tile_y + 1):
            for x in range(from_tile_x, to_tile_x + 1):

                # マップ外
                if not (0 <= x < MAP_WIDTH_TILE and 0 <= y < MAP_HEIGHT_TILE):
                    return

                tile = map_copy[y][x]

                # 通行不可
                if tile in [5, 10, 17]:
                    return
                
                if tile == 9:
                    if self.last_warp == (x, y):
                        continue
                    
                    for warp in cmap["warps"]:
                        if (x, y) == (warp["x"], warp["y"]):

                            self.last_warp = (
                                warp["to_x"],
                                warp["to_y"]
                            )

                            change_map(warp["to_map"])

                            self.x, self.y = tile2xy(
                                warp["to_x"],
                                warp["to_y"]
                            )

                            self.tile_x = warp["to_x"]
                            self.tile_y = warp["to_y"]

                            return

        # 衝突なし
        self.x = dest_x
        self.y = dest_y

        self.tile_x = xy2tilef(
            self.x + TILE // 2
        )

        self.tile_y = xy2tilef(
            self.y + TILE // 2
        )

        current_tile = (self.tile_x, self.tile_y)
        if self.last_warp is not None:
            if current_tile != self.last_warp:
                self.last_warp = None
        
        if self.enableMove:
            self.enableMove = False
            self.enableMove_time = (
                pygame.time.get_ticks()
                + self.enableMove_to_time
            )
                
    # 死亡を定義、
    def death(self):
        global gamemode, players_counter_remain, player
        self.isDeath = True
        self.x = -1
        self.y = -1
        players_counter_remain -= 1
        if players_counter_remain <= 1:
            gamemode = Gamemode.EndGame
        if self.enableOperate:
            player = None
    

    # 自機を書く
    def draw(self, offsetX, offsetY):
        if not self.isDeath:

            # 方向文字列
            dir_str = {
                Directions.Up:    "up",
                Directions.Right: "right",
                Directions.Down:  "down",
                Directions.Left:  "left"
            }[self.direction]

            # フレーム番号計算
            now = pygame.time.get_ticks()
            frame = (now // self.chara_anim_change_time) % len(self.chara_anim[dir_str])
            # アニメ描画
            screen.blit(self.chara_anim[dir_str][frame], (self.x + offsetX, self.y + offsetY))
            # キャラ名前描画
            name_text = name_font.render(self.chara_name, True, GREEN)
            screen.blit(name_text, (self.x + offsetX, self.y + offsetY))


def draw():
    global cmap
    # 背景
    screen.fill(BLACK)
    # マップ描画
    offsetX = WIDTH // 2 - (player.x + TILE // 2)
    offsetX = min(offsetX, 0)
    offsetX = max(offsetX, WIDTH - MAP_WIDTH)
    offsetY = HEIGHT // 2 - (player.y + TILE // 2)
    offsetY = min(offsetY, 0)
    offsetY = max(offsetY, HEIGHT - MAP_HEIGHT)
    draw_map(offsetX, offsetY)
    
    # プレイヤー描画(生存)
    if player:
        player.draw(offsetX, offsetY)
    
    # 洞窟のみ明るさグラデーション
    if (cmap["name"] == "cave"):
        draw_dark_gradation_in_cave(offsetX, offsetY)
        
    # タイマー及びバー描画
    if gamemode == Gamemode.InGame:
        draw_timer()

def draw_map(offsetX, offsetY):
    firstTileX = xy2tilec(-offsetX) - 1
    firstTileX = max(firstTileX, 0)
    lastTileX = firstTileX + xy2tilec(WIDTH) + 1
    lastTileX = min(lastTileX, MAP_WIDTH_TILE)
    
    firstTileY = xy2tilec(-offsetY) - 1
    firstTileY = max(firstTileY, 0)
    lastTileY = firstTileY + xy2tilec(HEIGHT) + 1
    lastTileY = min(lastTileY, MAP_HEIGHT_TILE)
    
    for i in range(firstTileY, lastTileY):
        for j in range(firstTileX, lastTileX):
            screen_x = j * TILE
            screen_y = i * TILE
            screen.blit(tile_list[map_copy[i][j]], (screen_x + offsetX, screen_y + offsetY))

def draw_dark_gradation_in_cave(offsetX, offsetY):
    # 暗闇レイヤー作成
        darkness = pygame.Surface((WIDTH, HEIGHT), pygame.SRCALPHA)
        # 全体を半透明の黒で塗る
        darkness.fill((0, 0, 0, 250))
        # プレイヤー中心座標
        center_x = player.x + offsetX + TILE // 2
        center_y = player.y + offsetY + TILE // 2
        gradation = min(player.gradations, player.gradation_radius)
        grade = player.gradation_radius // gradation
        for r in range(player.gradation_radius, 0, -grade):
            alpha = int(255 * (r / player.gradation_radius))
            pygame.draw.circle(
                darkness,
                (0, 0, 0, alpha),
                (center_x, center_y),
                r
        )
        # 画面に重ねる
        screen.blit(darkness, (0, 0))

def draw_dark_overlay():
    overlay = pygame.Surface((WIDTH, HEIGHT))
    overlay.set_alpha(190)  # 透明度：0〜255（120くらいが軽く暗い）
    overlay.fill((40, 40, 40))  # 薄いグレー
    screen.blit(overlay, (0, 0))

def draw_end_message():
    msg = title_font.render("Press SPACE to Restart", True, WHITE)
    screen.blit(msg, ((WIDTH - msg.get_width()) // 2,
                      (HEIGHT - msg.get_height()) // 2))
def draw_timer():
    time_str = str(int(get_passed_gametime()/1000)) + "s"
    text = bar_font.render(time_str, True, GREEN)
    screen.blit(text, (1 * TILE, (1 * TILE - bar_font.size(time_str)[1]) // 2))

running = True
pause_end_time = pause_start_time = total_pause_time = 0
while running:
    # ゲーム開始画面
    if gamemode == Gamemode.Start:
        # 背景
        screen.fill(BLACK)
        # タイトルと説明画面
        text = title_font.render("RPG", True, WHITE)
        screen.blit(text, ((WIDTH - title_font.size("RPG")[0]) // 2, 100 / 480 * HEIGHT))
        info = title_font.render("Press SPACE to Start", True, WHITE)
        screen.blit(info, ((WIDTH - title_font.size("Press SPACE to start")[0]) // 2, 200 / 480 * HEIGHT))
        # 押されたキーの中にスペースがあれば
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.KEYDOWN and event.key == pygame.K_SPACE:
                game_init()
                gamemode = Gamemode.InGame
        # 描画更新
        pygame.display.flip()
        clock.tick(60)  # 60FPS

    # ゲーム中
    if gamemode == Gamemode.InGame:
        # キー入力イベントを処理
        for event in pygame.event.get():
            # ×で終了する
            if event.type == pygame.QUIT:
                running = False
            # キー操作
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    pause_start_time = pygame.time.get_ticks()
                    gamemode = Gamemode.Pause
                if player:
                    player.isMoving = True
                    player.enableMove = True
                    if event.key == pygame.K_w:
                        if len(player.simultaneous_directions) < 2:
                            player.direction = Directions.Up
                            player.simultaneous_directions.add(player.direction)
                    elif event.key == pygame.K_a:
                        if len(player.simultaneous_directions) < 2:
                            player.direction = Directions.Left
                            player.simultaneous_directions.add(player.direction)
                    elif event.key == pygame.K_s:
                        if len(player.simultaneous_directions) < 2:
                            player.direction = Directions.Down
                            player.simultaneous_directions.add(player.direction)
                    elif event.key == pygame.K_d:
                        if len(player.simultaneous_directions) < 2:
                            player.direction = Directions.Right
                            player.simultaneous_directions.add(player.direction)
            elif event.type == pygame.KEYUP:
                if player:
                    if event.key == pygame.K_w:
                        player.simultaneous_directions.discard(Directions.Up)
                        if len(player.simultaneous_directions) == 1:
                            player.direction = next(iter(player.simultaneous_directions))
                    elif event.key == pygame.K_a:
                        player.simultaneous_directions.discard(Directions.Left)
                        if len(player.simultaneous_directions) == 1:
                            player.direction = next(iter(player.simultaneous_directions))
                    elif event.key == pygame.K_s:
                        player.simultaneous_directions.discard(Directions.Down)
                        if len(player.simultaneous_directions) == 1:
                            player.direction = next(iter(player.simultaneous_directions))
                    elif event.key == pygame.K_d:
                        player.simultaneous_directions.discard(Directions.Right)
                        if len(player.simultaneous_directions) == 1:
                            player.direction = next(iter(player.simultaneous_directions))
                    
                    player.isMoving = bool(player.simultaneous_directions)

        player.moveWithDirection(TILE/float(13))
        draw()
        # 描画更新
        pygame.display.flip()
        clock.tick(60)  # 60FPS
    if gamemode == Gamemode.Pause:
        # キー入力イベントを処理
        for event in pygame.event.get():
            # ×で終了する
            if event.type == pygame.QUIT:
                running = False
            # キー操作
            elif event.type == pygame.KEYDOWN:
                # 初期化しもう一度
                if event.key == pygame.K_SPACE:
                    game_init()
                    gamemode = Gamemode.InGame
                elif event.key == pygame.K_ESCAPE:
                    pause_end_time = pygame.time.get_ticks()
                    pause_time = pause_end_time - pause_start_time
                    total_pause_time += pause_time
                    gamemode = Gamemode.InGame
        draw()
        draw_dark_overlay() # 暗くする
        # 描画更新
        pygame.display.flip()
        clock.tick(60)  # 60FPS
    # escキーで中断したか勝者決定時の画面、
    if gamemode == Gamemode.EndGame:
        # キー入力イベントを処理
        for event in pygame.event.get():
            # ×で終了する
            if event.type == pygame.QUIT:
                running = False
            # キー操作
            elif event.type == pygame.KEYDOWN:
                # 初期化しもう一度
                if event.key == pygame.K_SPACE:
                    game_init()
                    gamemode = Gamemode.InGame
        draw()
        draw_dark_overlay() # 暗くする
        draw_end_message()
        # 描画更新
        pygame.display.flip()
        clock.tick(60)  # 60FPS

pygame.quit()
sys.exit()