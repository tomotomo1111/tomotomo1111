
import pygame
import numpy as np
import math
import time

WIDTH, HEIGHT = 1080, 620
CW2, CH2 = WIDTH // 2, HEIGHT // 2
FOV = 500
CAMERA_Z = 1000

pygame.init()
screen = pygame.display.set_mode((WIDTH, HEIGHT))
clock = pygame.time.Clock()

try:
    texture = pygame.image.load("./wall4.jpg").convert()
except:
    texture = pygame.Surface((256, 256))
    for y in range(256):
        for x in range(256):
            c = 180 if ((x // 16 + y // 16) % 2) else 80
            texture.set_at((x, y), (c, c, c))

camera_pos = np.array([0.0, 0.0, 0.0])
camera_rot_x = 0.0
camera_rot_y = 0.0

def rot_x(a):
    c, s = np.cos(a), np.sin(a)
    return np.array(
        [
            [1, 0,  0],
            [0, c, -s],
            [0, s,  c]
        ]
    )

def rot_y(a):
    c, s = np.cos(a), np.sin(a)
    return np.array(
        [
            [ c, 0, s],
            [ 0, 1, 0],
            [-s, 0, c]
        ]
    )

def project(v):
    z = CAMERA_Z + v[2]
    if z <= 1:
        return None
    scale = FOV / z
    return np.array([v[0] * scale + CW2, v[1] * scale + CH2, v[2]])

class Cube:
    def __init__(self, x, y, z, w, h, d, is_light = False):
        self.x, self.y, self.z = x, y, z
        self.w, self.h, self.d = w / 2, h / 2, d / 2
        self.is_light = is_light
        self.faces = [
            [0, 1, 3, 2],[4, 5, 7, 6],[0, 2, 6, 4],
            [1, 5, 7, 3],[0, 4, 5, 1],[2, 3, 7, 6]
        ]
        self.face_brightness=[0] * 6
        self.setup()

    def setup(self):
        x, y, z= self.x, self.y, self.z
        w, h, d= self.w, self.h, self.d
        self.vertices = np.array([
            [-w + x, -h + y, -d + z],[w + x, -h + y, -d + z],
            [-w + x,  h + y, -d + z],[w + x,  h + y, -d + z],
            [-w + x, -h + y,  d + z],[w + x, -h + y,  d + z],
            [-w + x,  h + y,  d + z],[w + x,  h + y,  d + z]
        ],dtype = float)

    def lighting(self,light):
        for i, f in enumerate(self.faces):
            c = self.vertices[f].mean(axis = 0)
            dist = np.linalg.norm(c - light)
            self.face_brightness[i]=min(0.95, (dist * dist) / (450 * 450))

class Sphere:
    def __init__(self, x, y, z, r, segments = 20):
        self.tris = []
        verts = []
        for i in range(segments + 1):
            th = i * np.pi / segments
            for j in range(segments + 1):
                ph = j * 2 * np.pi / segments
                verts.append(
                    [
                        r * np.sin(th) * np.cos(ph) + x,
                        r * np.sin(th) * np.sin(ph) + y,
                        r * np.cos(th) + z
                    ]
                )
        self.vertices = np.array(verts, dtype = float)
        row = segments + 1
        for i in range(segments):
            for j in range(segments):
                a = i * row + j
                b = a + 1
                c = a + row
                d = c + 1
                self.tris.append((a, b, c))
                self.tris.append((b, d, c))
        self.tris = np.array(
            self.tris,
            dtype=np.int32
        )
        self.brightness=[1] * len(self.tris)
        
    def lighting(self, light):

        centers = self.vertices[
            self.tris
        ].mean(axis=1)

        dist = np.linalg.norm(
            centers - light,
            axis=1
        )

        self.brightness = np.minimum(
            0.2,
            (dist * dist) / (250 * 250)
        )

def draw_textured_quad(surface, tex, pts):
    
    tl,tr,br,bl = pts

    steps = int(max(
        np.hypot(bl[0]-tl[0], bl[1]-tl[1]),
        np.hypot(br[0]-tr[0], br[1]-tr[1])
    ))

    tex_h = tex.get_height()

    for i in range(steps):

        t = i / max(1, steps-1)

        tex_y = int(t * (tex_h-1))

        lx = tl[0] + (bl[0]-tl[0]) * t
        ly = tl[1] + (bl[1]-tl[1]) * t

        rx = tr[0] + (br[0]-tr[0]) * t
        ry = tr[1] + (br[1]-tr[1]) * t

        width = int(np.hypot(rx-lx, ry-ly))

        if width <= 0:
            continue

        strip = tex.subsurface(
            (0, tex_y, tex.get_width(), 1)
        )

        strip = pygame.transform.scale(
            strip,
            (width,1)
        )

        angle = math.degrees(
            math.atan2(ry-ly, rx-lx)
        )

        strip = pygame.transform.rotate(
            strip,
            -angle
        )

        rect = strip.get_rect(
            center=((lx+rx)/2,(ly+ry)/2)
        )

        surface.blit(strip, rect)
        
def draw_triangle_zbuffer(framebuffer, zbuffer, pts, color):

    p0, p1, p2 = pts

    x0, y0, z0 = p0
    x1, y1, z1 = p1
    x2, y2, z2 = p2

    min_x = max(0, int(min(x0, x1, x2)))
    max_x = min(WIDTH - 1, int(max(x0, x1, x2)))

    min_y = max(0, int(min(y0, y1, y2)))
    max_y = min(HEIGHT - 1, int(max(y0, y1, y2)))

    denom = (
        (y1 - y2) * (x0 - x2)
        +
        (x2 - x1) * (y0 - y2)
    )

    if abs(denom) < 1e-6:
        return

    xs, ys = np.meshgrid(
        np.arange(min_x, max_x + 1),
        np.arange(min_y, max_y + 1)
    )

    w0 = (
        (y1 - y2) * (xs - x2)
        +
        (x2 - x1) * (ys - y2)
    ) / denom

    w1 = (
        (y2 - y0) * (xs - x2)
        +
        (x0 - x2) * (ys - y2)
    ) / denom

    w2 = 1.0 - w0 - w1

    inside = (
        (w0 >= 0)
        &
        (w1 >= 0)
        &
        (w2 >= 0)
    )

    if not inside.any():
        return

    z = (
        w0 * z0 +
        w1 * z1 +
        w2 * z2
    )

    sub_z = zbuffer[
        min_y:max_y+1,
        min_x:max_x+1
    ]

    update = inside & (z < sub_z)

    sub_z[update] = z[update]

    framebuffer[
        min_y:max_y+1,
        min_x:max_x+1
    ][update] = color

cubes = [
    Cube( -25,-200, -25,  50,  50,  50, True),
    Cube( -50,   0, -50, 100, 400, 100),
    Cube( 350,   0, -50, 100, 400, 100),
    Cube(-450,   0, -50, 100, 400, 100),
    Cube( -50,   0, 350, 100, 400, 100),
    Cube( -50,   0,-450, 100, 400, 100)
]

sphere = Sphere( -50, -300, -50, 100)

running = True
font = pygame.font.SysFont(None,20)
framebuffer = np.zeros(
        (HEIGHT, WIDTH, 3),
        dtype = np.uint8
)

while running:
    dt = clock.tick(60)

    for e in pygame.event.get():
        if e.type == pygame.QUIT:
            running = False

    k = pygame.key.get_pressed()

    if k[pygame.K_w]: camera_rot_x -= 0.04
    if k[pygame.K_s]: camera_rot_x += 0.04
    if k[pygame.K_a]: camera_rot_y -= 0.04
    if k[pygame.K_d]: camera_rot_y += 0.04

    if k[pygame.K_LEFT]:
        camera_pos[0] -= math.cos(camera_rot_y) * 4
        camera_pos[2] += math.sin(camera_rot_y) * 4

    if k[pygame.K_RIGHT]:
        camera_pos[0] += math.cos(camera_rot_y) * 4
        camera_pos[2] -= math.sin(camera_rot_y) * 4

    framebuffer.fill(0)
    zbuffer = np.full(
        (HEIGHT, WIDTH),
        np.inf,
        dtype=np.float32
    )

    t = pygame.time.get_ticks() * 0.002
    light_cube = cubes[0]
    light_cube.x = np.cos(t) * 200 -25
    light_cube.z = np.sin(t) * 200 -25
    light_cube.setup()

    light = np.array([light_cube.x, light_cube.y, light_cube.z])

    queue = []

    R = rot_x(-camera_rot_x) @ rot_y(-camera_rot_y)

    for ci, c in enumerate(cubes):
        if not c.is_light:
            c.lighting(light)

        transformed = []
        for v in c.vertices:
            vv = (v - camera_pos) @ R.T
            p = project(vv)
            transformed.append(p)

        for fi, f in enumerate(c.faces):
            pts = [transformed[i] for i in f]
            if any(p is None for p in pts):
                continue

            a = pts[0]
            b = pts[1]
            c2 = pts[2]

            cross = (
                (b[0]-a[0])*(c2[1]-a[1])
                -
                (b[1]-a[1])*(c2[0]-a[0])
            )

            if cross < 0:
                continue

            z = sum(p[2] for p in pts) / 4
            queue.append(("cube", z, pts, c, fi))

    sphere.lighting(light)

    verts = (
        sphere.vertices
        - camera_pos
    ) @ R.T
    z = CAMERA_Z + verts[:,2]
    scale = FOV / z
    transformed = np.column_stack([
        verts[:,0] * scale + CW2,
        verts[:,1] * scale + CH2,
        verts[:,2]
    ])

    for ti, tr in enumerate(sphere.tris):
        pts = [transformed[i] for i in tr]
        if any(p is None for p in pts):
            continue
        z = sum(p[2] for p in pts) / 3
        queue.append(("sphere", z, pts, sphere, ti))

    for typ, _, pts, obj, idx in queue:
        if typ == "cube":
            color = (255,255,0)
            if obj.is_light:
                color = (255,255,0)
            else:
                brightness = int(
                    255 * (1 - obj.face_brightness[idx])
                )
                color = (
                    brightness,
                    brightness,
                    brightness
                )
            tri1 = [pts[0], pts[1], pts[2]]
            tri2 = [pts[0], pts[2], pts[3]]
            draw_triangle_zbuffer(
                framebuffer,
                zbuffer,
                tri1,
                color
            )
            draw_triangle_zbuffer(
                framebuffer,
                zbuffer,
                tri2,
                color
            )
        else:
            b = max(0, min(255, int(255 * (1 - obj.brightness[idx]))))
            draw_triangle_zbuffer(
                framebuffer,
                zbuffer,
                pts,
                (b, b, b)
            )
    
    pygame.surfarray.blit_array(
        screen,
        framebuffer.swapaxes(0, 1)
    )
    fps = font.render(str(int(clock.get_fps())), True, (255, 255, 255))
    screen.blit(fps, (20, 20))
    pygame.display.flip()

pygame.quit()
