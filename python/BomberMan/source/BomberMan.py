import sys
import pygame
import random
import math
import copy
from enum import Enum, auto
import json

#################### 初期設定 ####################
# 初期化
pygame.init()
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

######################## アイテム ###########################
class ItemType(Enum):
    BombCountUp = auto()
    ExplosionRangeUp = auto()
    MaxExplosionRangeUp = auto()

class Item:
    def __init__(self, x, y, type):
        self.x = x
        self.y = y
        self.type = type
        self.tile_x = xy2tilef(x)
        self.tile_y = xy2tilef(y)
        self.image = self.load_item_image(type)
        self.picked = False

    def load_item_image(self, type):
        # 適当な番号でスプライトを設定
        if type == ItemType.BombCountUp:
            return load_other(21)
        elif type == ItemType.ExplosionRangeUp:
            return load_other(26)
        elif type == ItemType.MaxExplosionRangeUp:
            return load_other(28)

    def draw(self, surface):
        if not self.picked:
            surface.blit(self.image, (self.x, self.y))
    
    def drop_item(tx, ty):
        if random.random() < 0.8:
            return

        r = random.random()
        if r < settings["PROBBOMBCOUNTUP"]:
            type = ItemType.BombCountUp
        elif r < settings["PROBBOMBCOUNTUP"] + settings["PROBEXPLOSIONRANGEUP"]:
            type = ItemType.ExplosionRangeUp
        elif r < settings["PROBBOMBCOUNTUP"] + settings["PROBEXPLOSIONRANGEUP"] + settings["PROBMAXEXPLOSIONRANGEUP"]:
            type = ItemType.MaxExplosionRangeUp
        else:
            pass
        
        items.append(Item(tx, ty, type))
items = []
############################################################

####################### バトルフィールド ####################
# ボンバーマン戦場、0壁、1壊せる壁、2無し、
map = map["map"]
# マップの実際処理する用コピー
map_copy = copy.deepcopy(map)
# マップサイズ
MAP_WIDTH = len(map_copy[0])
MAP_HEIGHT= len(map_copy)
unbrakable_wall_image = load_other(17)
breakable_wall_image = load_other(14)
empty_wall_image = load_other(8)
wall_list = [unbrakable_wall_image, breakable_wall_image, empty_wall_image]
###########################################################


#################### 便利関数 ######################
# 座標をマップのタイルインデックスに変更する関数
def xy2tilef(value):
    return int(value / TILE)
def xy2tilec(value):
    return math.ceil(value / TILE)
# タイル間距離関数
def tiledist(a, b):
    return abs(a[0] - b[0]) + abs(a[1] - b[1])
def all_bombs():
    bombs = []
    for b in bombers:
        if b.bombs:
            bombs.extend(b.bombs)
    return bombs
def get_passed_gametime():
    return pygame.time.get_ticks() - gamestarttime - total_pause_time
###################################################


#################### 初期抽選 #####################
# 湧く位置を抽選する
spawn_points = []
for y in range(MAP_HEIGHT):
    for x in range(MAP_WIDTH):
        if map[y][x] != 2:
            continue
        # 最初の 1 個は無条件採用
        if len(spawn_points) == 0:
            spawn_points.append((x, y))
            continue
        # すでにあるスポーン位置と距離4以上なら採用
        enable_add = True
        for sx, sy in spawn_points:
            if (tiledist((x, y), (sx, sy))) <= 4:
                enable_add = False
                break
        if enable_add:
            if len(spawn_points) < 10:
                spawn_points.append((x, y))
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
    global map_copy
    global spawn_points_copy
    global chara_anim_index_list_copy
    global bombers_counter_remain
    global computers
    global player
    global bombers
    global gamemode
    global gamestarttime
    global total_pause_time
    computers = []
    player = None
    map_copy = copy.deepcopy(map)
    spawn_points_copy = copy.deepcopy(spawn_points)
    chara_anim_index_list_copy = copy.deepcopy(chara_anim_index_list)
    computers.clear()
    items.clear()
    bombers_counter_remain = len(spawn_points_copy)
    for _ in range(bombers_counter_remain - 1):
        computers.append(Bomber(False))
    player = Bomber(True)
    bombers = [player] + computers
    gamemode = Gamemode.Start
    gamestarttime = pygame.time.get_ticks()
    total_pause_time = 0
###################################################


# ボマー型
class Bomber:
    def __init__(self, enableOperate = False):
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
        # タイル→画面座標へ
        self.x = px * TILE
        self.y = py * TILE
        self.tile_x = px
        self.tile_y = py
        # 自機かどうか
        self.enableOperate = enableOperate 
        # ボム起き済か
        # 設置可能ボムの最大数
        self.max_bombs = settings["FIRSTMAXBOMBS"]
        # 爆発距離
        self.bomb_power = settings["FIRSTBOMBPOWER"]
        # 設置済みボムリスト
        self.bombs = []
        self.bomb_explosion_to_time = settings["BOMBEXPLOSIONTOTIME"]
        # 無敵かどうか
        self.invincible = False
        self.invincible_time = pygame.time.get_ticks()
        self.invincible_expire_time = settings["INVINCIBLEEXPIRETIME"]
        # AI移動管理
        self.enableMove = True
        self.enableMove_time = pygame.time.get_ticks()
        self.enableMove_to_time = settings["ENABLEMOVETOTIME"]
        self.target = self
        # 残りHP
        self.life = settings["FIRSTLIFE"]
        self.isDeath = False
        self.radius = 12
    
    def move(self, dx, dy):
        if self.isDeath:
            return
        if not self.enableOperate and not self.enableMove:
            return
        
        # 方向を逆正接関数で把握する -pi から pi
        rad = math.atan2(dy, dx)
        if math.pi/4 <= rad < 3*math.pi/4:
            self.direction = Directions.Down
        elif -math.pi/4 <= rad < math.pi/4:
            self.direction = Directions.Right
        elif -3*math.pi/4 <= rad < -math.pi/4:
            self.direction = Directions.Up
        else:
            self.direction = Directions.Left

        dest_x = self.x + dx
        dest_y = self.y + dy

        # 進行先タイル
        tile_x = xy2tilef(dest_x)
        tile_y = xy2tilef(dest_y)

        # マップ衝突
        if not (0 <= tile_x < MAP_WIDTH and 0 <= tile_y < MAP_HEIGHT):
            return
        if map_copy[tile_y][tile_x] != 2:
            if not self.enableOperate and map_copy[tile_y][tile_x] == 1:
                self.setBomb()
            return

        # 爆弾衝突
        bombs = all_bombs()
        if any(bomb.tile_x == tile_x and bomb.tile_y == tile_y for bomb in bombs):
            return
        
        # プレイヤー / コンピュータ本体の衝突
        for b in bombers:
            if b.tile_x == tile_x and b.tile_y == tile_y:
                return

        # 衝突無し → 移動
        self.x = dest_x
        self.y = dest_y
        self.tile_x = tile_x
        self.tile_y = tile_y

        # 移動後アイテム取得判定を行う
        self.check_item_pickup()

        # CPUの移動クールタイム
        if not self.enableOperate:
            self.find_nearest_alive_target()
            self.enableMove = False
            self.enableMove_time = pygame.time.get_ticks() + self.enableMove_to_time

    # 一番近い敵を探す
    def find_nearest_alive_target(self):
        min_dist = 10000
        target = self
        for b in bombers:
            if b is self or b.isDeath:
                continue
            d = tiledist((self.tile_x, self.tile_y), (b.tile_x, b.tile_y))
            if 0 < d < min_dist:
                min_dist = d
                target = b
        if self.target is not target:
            self.target = target
            print(self.chara_name, " -> ", self.target.chara_name)

    # 爆弾設置、爆発する時間と威力を設定したい
    def setBomb(self):
        if not self.isDeath:
            # 設置可能ボム数チェック
            bombs = all_bombs()
            if any(b.tile_x == self.tile_x and b.tile_y == self.tile_y for b in bombs):
                return
            if len(self.bombs) >= self.max_bombs:
                return
            new_bomb = Bomb(self.x, self.y, self.bomb_explosion_to_time, 
                            self.bomb_power)
            self.bombs.append(new_bomb)
    
    # 被弾関数、爆弾やキック(未定)など汎用的に呼ぶ
    def damage(self):
        if not self.isDeath and not self.invincible:
            self.life -= 1
            # 死ぬ
            if self.life <= 0:
                self.death()
                return  
            self.invincible = True
            self.invincible_time = pygame.time.get_ticks() + self.invincible_expire_time

    def ai_update(self):
        """
        AIの更新。target_x, target_y は自分以外の敵座標
        """
        if self.isDeath: return

        # ターゲットがいないか死亡時に再度ターゲットを探索
        if self.target is None or self.target.isDeath or self.target == self:
            self.find_nearest_alive_target()
        
        bombs = all_bombs()
        if not bombs: # 爆弾が無ければ、5%ランダム移動95%ターゲットに移動
            mode = random.random()
            if mode <= 0.05:
                self.move_random_except_back()
            else:
                self.goto_target(bombs, True)
        else : # ボムがあれば
            # 危険なタイルの全体を確認する
            all_danger_tiles = []
            for bomb in bombs:
                all_danger_tiles.extend(bomb.find_dangerous_area())
            
            # 周囲に危険なタイルが無いか確認する
            min_danger_tile_distance = 100
            for danger_tile in all_danger_tiles:
                tmp = tiledist((self.tile_x, self.tile_y), danger_tile)
                if min_danger_tile_distance > tmp:
                    min_danger_tile_distance = tmp

            # 周囲に危険なタイルが無ければ20%ランダム移動20%ターゲットに移動60%アイテムに移動
            # 60%抽選してアイテムがないならターゲットに移動
            if min_danger_tile_distance >= 3:
                mode = random.random()
                if mode <= 0.2:
                    self.move_random_except_back()
                elif mode <= 0.4:
                    self.goto_target(bombs, True)
                else:
                    if items: # アイテムがあれば取得可能経路があるか確かめる
                        item_tiles = []
                        for it in items:
                            item_tiles.append((it.tile_x, it.tile_y))
                        start = (self.tile_x, self.tile_y)
                        path = self.astar(start, item_tiles, map_copy, bombs)
                        if path: # アイテムが画面内にあって、取得可能で安全ならば移動する
                            # print(self.chara_name, path, "-> item")
                            self.guide(path)
                        else: # アイテムが画面内にあるけど取得可能経路がないならターゲットに移動する
                            self.goto_target(bombs, True)
                    else: # アイテムが画面内にないならターゲットに移動する
                        self.goto_target(bombs, True)
                    
            else : # 危険なタイルがある場合は回避行動     
                safe_tiles = [(x, y) for y in range(MAP_HEIGHT) for x in range(MAP_WIDTH)
                                if map_copy[y][x] == 2 and (x,y) not in all_danger_tiles]

                if not safe_tiles:
                    return  # 逃げ道なし
                
                # A*でそこへ向かう
                start = (self.tile_x, self.tile_y)
                path = self.astar(start, safe_tiles, map_copy, bombs)
                if path:
                    # print(self.chara_name, path)
                    self.guide(path)
                else:
                    now_gametime = get_passed_gametime()
                    # 逃げながら連続で置く確率は、ゲーム秒数の十分の一％となる
                    if random.random() < (0.001 * (now_gametime // 1000)):
                        self.setBomb()
                        return
                    # 人数が半分以下になれば逃げ置き確定する
                    if bombers_counter_remain <= (bombers_counter_remain // 2):
                        self.setBomb()

    # ランダム移動、来た道は戻らない
    def move_random_except_back(self):
        dir = random.randint(0, 4)
        if dir == 0 and self.direction is not Directions.Up:
            self.move(0, TILE)
        if dir == 1 and self.direction is not Directions.Right:
            self.move(-TILE, 0)
        if dir == 2 and self.direction is not Directions.Down:
            self.move(0, -TILE)
        if dir == 3 and self.direction is not Directions.Left:
            self.move(TILE, 0)
    
    # 経路配列を貰って経路があれば一歩移動する。接近後の距離を返す
    def guide(self, path):
        if path:
            nx, ny = path[0]
            dx = nx * TILE - self.x
            dy = ny * TILE - self.y
            self.move(dx, dy)
            return
    
    # AI がターゲットに一歩接近する関数
    def goto_target(self, bombs, setbombondestination=False):
        px = self.tile_x
        py = self.tile_y
        tx = self.target.tile_x
        ty = self.target.tile_y

        # A*でそこへ向かう
        start = (px, py)
        goal = [(tx, ty)]
        path = self.astar(start, goal, map_copy, bombs)
        self.guide(path)
        
        # 着いてターゲットが近いならボムを設置するか
        if setbombondestination:
            dist = tiledist((px, py), (tx, ty))
            if dist <= 1:
                self.setBomb()
    
    # A*法の経路探索、ボムによるマイナス重み付けあり
    def astar(self, start, goals, map_data, bombs):
        import heapq
        open_set = []
        heapq.heappush(open_set, (0, start))
        came_from = {}
        g_score = {start: 0}

        def heuristic(a, b):
            # タイル距離
            # norm2x = (a[0] - b[0]) ** 2 + (a[1] - b[1]) ** 2
            # return norm2x
            return tiledist(a, b)

        while open_set:
            _, current = heapq.heappop(open_set)
            if current in goals:
                path = []
                while current in came_from:
                    path.append(current)
                    current = came_from[current]
                path.reverse()
                return path

            for dx, dy in [(0,1),(1,0),(0,-1),(-1,0)]:
                neighbor = (current[0]+dx, current[1]+dy)
                x, y = neighbor
                if not (0 <= x < MAP_WIDTH and 0 <= y < MAP_HEIGHT):
                    continue

                # 移動コスト設定
                if map_data[y][x] == 0: # 壊せない壁
                    continue # 非常に高いコストにして通らないように
                if map_data[y][x] == 1: # 壊せる壁
                    continue                 
                else: # 空きタイル
                    cost = 1

                # 移動先がボンバーマンだったら避ける
                if any(b.tile_x == x and b.tile_y == y for b in bombers):
                    continue

                # 爆弾のタイルも避ける
                if any(b.tile_x == x and b.tile_y == y for b in bombs):
                    continue

                tentative_g = g_score[current] + cost
                if neighbor not in g_score or tentative_g < g_score[neighbor]:
                    g_score[neighbor] = tentative_g
                    f_score = tentative_g + min(heuristic(neighbor, g) for g in goals)
                    heapq.heappush(open_set, (f_score, neighbor))
                    came_from[neighbor] = current

        return []  # 移動可能な経路なし
                
    # 死亡を定義、
    def death(self):
        global gamemode, bombers_counter_remain, player
        self.isDeath = True
        self.x = -1
        self.y = -1
        bombers_counter_remain -= 1
        if bombers_counter_remain <= 1:
            gamemode = Gamemode.EndGame
        bombers.remove(self)
        if self.enableOperate:
            player = None
        else:
            computers.remove(self)
        
    
    # コルーチン管理
    def coroutineCheck(self):
        # 爆弾タイマー管理
        now = pygame.time.get_ticks()
        for bomb in self.bombs[:]:
            # まだ爆発していない → 爆弾描画
            if now < bomb.explosion_time:
                bomb.draw(screen)
            # 爆発する時刻になった
            else:
                # 爆発描画
                bomb.explosion(screen)
                # 爆風は短時間だけ表示（ここでは200ms）
                if now > bomb.explosion_time + settings["BOMBEXPLOSIONDURATION"]:
                    self.bombs.remove(bomb)

        # ダメージタイマー管理
        if self.invincible:
            # 無敵が切れた
            if now > self.invincible_time:
                self.invincible = False
        
        # コンピュータ移動管理
        if not self.enableOperate:
            if now > self.enableMove_time:
                self.enableMove = True

    def check_item_pickup(self):
        for item in items:
            if not item.picked:
                # 接触判定（中心距離で判定）
                if abs(self.x - item.x) < TILE and abs(self.y - item.y) < TILE:
                    item.picked = True
                    # 効果を付与
                    if item.type == ItemType.BombCountUp:
                        self.max_bombs += 1
                    elif item.type == ItemType.ExplosionRangeUp:
                        self.bomb_power += 1
                    elif item.type == ItemType.MaxExplosionRangeUp:
                        self.bomb_power += 5
                    items.remove(item)


    # 自機を書く
    def draw(self, surface):
        if not self.isDeath:
            if self.invincible and pygame.time.get_ticks() % 200 < 100:
                return  # 点滅（描かないフレーム）

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
            surface.blit(self.chara_anim[dir_str][frame], (self.x, self.y))
            # キャラ名前描画
            name_text = name_font.render(self.chara_name, True, GREEN)
            screen.blit(name_text, (self.x, self.y))

# 爆弾型
class Bomb:
    def __init__(self, x, y, bomb_explosion_to_time, bomb_power):
        self.x = x
        self.y = y
        self.tile_x = xy2tilef(x)
        self.tile_y = xy2tilef(y)
        self.explosion_time = pygame.time.get_ticks() + bomb_explosion_to_time
        self.bomb_explosion_to_time = bomb_explosion_to_time
        self.generated_time = pygame.time.get_ticks()  # 設置時間(ms)
        self.first_check_explode = True
        self.yield_tiles = [(self.tile_x, self.tile_y)]
        self.bomb_anim = [load_other(34), load_other(33), load_other(32), load_other(31)]
        self.bomb_power = bomb_power

    def draw(self, surface):
        # フレーム番号計算
        now = pygame.time.get_ticks()
        frame = ((now - self.generated_time) // (self.bomb_explosion_to_time // len(self.bomb_anim))) % len(self.bomb_anim)
        # アニメ描画
        surface.blit(self.bomb_anim[frame], (self.x, self.y))

    # 爆弾の爆風関数
    def explosion(self, surface):
        # 爆風色
        color = (255, 200, 50)

        # 爆心地
        bx, by = self.tile_x, self.tile_y  
        

        # 上下左右（1マス）と中心
        if self.first_check_explode:
            self.yield_tiles = self.find_dangerous_area()
            self.first_check_explode = False
                    
        
        for (tx, ty) in self.yield_tiles:
            # 描画
            pygame.draw.rect(surface, color,
                (tx*TILE, ty*TILE, TILE, TILE))
            # 壊せる壁 → アイテム生成判定
            if map_copy[ty][tx] == 1:
                Item.drop_item(tx * TILE, ty * TILE)
                map_copy[ty][tx] = 2
            # ダメージ判定
            if map_copy[ty][tx] == 2:
                for b in bombers:
                    bx = b.tile_x
                    by = b.tile_y
                    if bx == tx and by == ty:
                        b.damage()
    
    # ボム爆発領域を確認する関数
    def find_dangerous_area(self):
        bx, by = self.tile_x, self.tile_y
        dangerous_area = [(bx, by)]
        for dx, dy in [(1,0),(-1,0),(0,1),(0,-1)]:
            for i in range(1, self.bomb_power+1):
                nx = bx + dx*i
                ny = by + dy*i
                if map_copy[ny][nx] == 0:
                    break
                dangerous_area.append((nx, ny))
                if map_copy[ny][nx] == 1:
                    break
                bombs = all_bombs()
                bomb_collision_frag = False
                for bomb in bombs:
                    if bomb.tile_x == nx and bomb.tile_y == ny:
                        bomb_collision_frag = True
                if bomb_collision_frag:
                    break
        return dangerous_area


def draw():
    # 背景
    screen.fill(BLACK)
    # マップ描画
    for y in range(MAP_HEIGHT):
        for x in range(MAP_WIDTH):
            screen_x = x * TILE
            screen_y = y * TILE
            screen.blit(wall_list[map_copy[y][x]], (screen_x, screen_y))
    
    # アイテム描画
    for item in items:
        item.draw(screen)
    
    if gamemode == Gamemode.InGame:
        # 爆弾描画
        # 爆弾が存在するならタイマー確認
        if player:
            player.coroutineCheck()
        if computers:
            for computer in computers:
                computer.coroutineCheck()

    
    # プレイヤー描画(生存)
    if player:
        player.draw(screen)
    # コンピュータ描画
    if computers:
        for computer in computers:
            if gamemode == Gamemode.InGame:
                computer.ai_update()
            computer.draw(screen)

    # タイマー及びバー描画
    if gamemode == Gamemode.InGame:
        time_str = str(int(get_passed_gametime()/1000)) + "s"
        text = bar_font.render(time_str, True, GREEN)
        screen.blit(text, (1 * TILE, (1 * TILE - bar_font.size(time_str)[1]) // 2))

def draw_dark_overlay():
    overlay = pygame.Surface((WIDTH, HEIGHT))
    overlay.set_alpha(190)  # 透明度：0〜255（120くらいが軽く暗い）
    overlay.fill((40, 40, 40))  # 薄いグレー
    screen.blit(overlay, (0, 0))

def draw_end_message():
    msg = title_font.render("Press SPACE to Restart", True, WHITE)
    screen.blit(msg, ((WIDTH - msg.get_width()) // 2,
                      (HEIGHT - msg.get_height()) // 2))

running = True
pause_end_time = pause_start_time = total_pause_time = 0
while running:
    # ゲーム開始画面
    if gamemode == Gamemode.Start:
        # 背景
        screen.fill(BLACK)
        # タイトルと説明画面
        text = title_font.render("BomberMan", True, WHITE)
        screen.blit(text, ((WIDTH - title_font.size("BomberMan")[0]) // 2, 100))
        info = title_font.render("Press SPACE to Start", True, WHITE)
        screen.blit(info, ((WIDTH - title_font.size("Press SPACE to start")[0]) // 2, 200))
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
                    if event.key == pygame.K_SPACE:
                        player.setBomb()
                    if event.key == pygame.K_w:
                        player.move(0, -TILE)
                    if event.key == pygame.K_a:
                        player.move(-TILE, 0)
                    if event.key == pygame.K_s:
                        player.move(0, TILE)
                    if event.key == pygame.K_d:
                        player.move(TILE, 0)
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
                if event.key == pygame.K_ESCAPE:
                    pause_end_time = pygame.time.get_ticks()
                    pause_time = pause_end_time - pause_start_time
                    for b in all_bombs():
                        b.generated_time += pause_time
                        b.explosion_time += pause_time
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