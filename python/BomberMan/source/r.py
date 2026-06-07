import sys
import pygame
import random
import math
import copy
from enum import Enum, auto

#################### 初期設定 ####################
# 初期化
pygame.init()
WIDTH, HEIGHT = 640, 480
# フレームサイズ
screen = pygame.display.set_mode((WIDTH, HEIGHT))
# 上の名前
pygame.display.set_caption("BomberMan")
# チック処理
clock = pygame.time.Clock()

# 定数
TILE = 32
CHARA_SHEET_ROWS = 8
CHARA_SHEET_COLS = 15
CHARA_H = TILE * 4  # 1キャラの高さ
CHARA_W = TILE * 3  # 1キャラの幅

# 読み込み
sprite_path = "../images/character.png"
sprite_sheet = pygame.image.load(sprite_path).convert_alpha()

# 対象キャラの index
index = 1
start_x = (index * CHARA_W) % (CHARA_SHEET_COLS * TILE)
start_y = ((index * CHARA_W) // (CHARA_SHEET_COLS * TILE)) * CHARA_H

# 右向きの行（0:up,1:right,2:down,3:left）
right_row = 1
left_row = 3

# 列数 (3コマアニメ)
for col in range(3):
    # 右向きのフレームを切り出し
    frame_rect = pygame.Rect(start_x + col * TILE, start_y + right_row * TILE, TILE, TILE)
    frame = sprite_sheet.subsurface(frame_rect)
    
    # 左向きに反転
    flipped_frame = pygame.transform.flip(frame, True, False)
    
    # 左向き行にコピー
    sprite_sheet.blit(flipped_frame, (start_x + col * TILE, start_y + left_row * TILE))

# 上書き保存
pygame.image.save(sprite_sheet, sprite_path)
print("Left animation frames updated.")
