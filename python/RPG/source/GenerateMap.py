import json
import random

def generateMap(WIDTH, HEIGHT):
    with open("../settings/map.json", "r", encoding="utf-8") as f:
        map = json.load(f)
        
    ground_map_buffer = []
    for y in range(HEIGHT):
        row = []
        for x in range(WIDTH):
            # デフォルトは草
            tile = 1
            # 縦横の道路
            if x % 20 == 0 or y % 20 == 0:
                tile = 3
            # ランダムな岩
            elif random.random() < 0.02:
                tile = 17
            # ランダムな池
            elif random.random() < 0.01:
                tile = 5
            row.append(tile)
        ground_map_buffer.append(row)
        
    ground_map = []
    for y in range(HEIGHT):
        row = []
        for x in range(WIDTH):
            tile = 1
            row.append(tile)
        ground_map.append(row)
    
    neighbors = [(-1, 0), (1, 0), (0, 0), (0, -1), (0, 1)]
        
    for i in range(1, 6):
        for y in range(1, HEIGHT - 1):
            for x in range(1, WIDTH - 1):
                if (ground_map_buffer[y][x] == 5):
                    if random.random() < 0.7:
                        for d in neighbors:
                            ground_map[y + d[1]][x + d[0]] = 5
                if (ground_map_buffer[y][x] == 17):
                    if random.random() < 0.95:
                        for d in neighbors:
                            ground_map[y + d[1]][x + d[0]] = 17
        copyMap(ground_map, ground_map_buffer, WIDTH, HEIGHT)

    for y in range(HEIGHT):
        for x in range(WIDTH):
            # 縦横の道路
            if x % 20 == 0 or y % 20 == 0:
                ground_map[y][x] = 3

    ground_map[25][39] = 9
    
    map["maps"]["map3"] = {
        "name": "plain_generated",
        "width": WIDTH,
        "height": HEIGHT,
        "warps": [
            {
                "x": 39,
                "y": 25,
                "to_map": "map1",
                "to_x": 13,
                "to_y": 1
            }
        ],
        "map": ground_map
    }
    
    # 保存
    with open("../settings/map.json", "w", encoding="utf-8") as f:
        json.dump(
            map,
            f,
            ensure_ascii=False,
            indent=4
        )

def copyMap(fromMap, toMap, WIDTH, HEIGHT):
    for y in range(HEIGHT):
            for x in range(WIDTH):
                toMap[y][x] = fromMap[y][x]