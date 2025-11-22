import sys
import pygame


# パラメータ定義
WIDTH, HEIGHT = 300, 300
color_background = (0, 0, 255)

def pygame_init():

    pygame.init()
    screen = pygame.display.set_mode((WIDTH, HEIGHT))
    screen.fill(color_background)
    pygame.display.flip()
    
    clock = pygame.time.Clock()

    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_a:
                    pass
                
if __name__ == "__main__":
    pygame_init()