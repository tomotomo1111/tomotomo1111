
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
    texture = pygame.image.load("wall4.jpg").convert()
except:
    texture = pygame.Surface((256,256))
    for y in range(256):
        for x in range(256):
            c = 180 if ((x//16+y//16)%2) else 80
            texture.set_at((x,y),(c,c,c))

camera_pos = np.array([0.0,0.0,0.0])
camera_rot_x = 0.0
camera_rot_y = 0.0

def rot_x(a):
    c,s=np.cos(a),np.sin(a)
    return np.array([[1,0,0],[0,c,-s],[0,s,c]])

def rot_y(a):
    c,s=np.cos(a),np.sin(a)
    return np.array([[c,0,s],[0,1,0],[-s,0,c]])

def project(v):
    z = CAMERA_Z + v[2]
    if z <= 1:
        return None
    scale = FOV / z
    return np.array([v[0]*scale + CW2, v[1]*scale + CH2, v[2]])

class Cube:
    def __init__(self,x,y,z,w,h,d,is_light=False):
        self.x,self.y,self.z=x,y,z
        self.w,self.h,self.d=w/2,h/2,d/2
        self.is_light=is_light
        self.faces=[
            [0,1,3,2],[4,5,7,6],[0,2,6,4],
            [1,5,7,3],[0,4,5,1],[2,3,7,6]
        ]
        self.face_brightness=[0]*6
        self.setup()

    def setup(self):
        x,y,z=self.x,self.y,self.z
        w,h,d=self.w,self.h,self.d
        self.vertices=np.array([
            [-w+x,-h+y,-d+z],[ w+x,-h+y,-d+z],
            [-w+x, h+y,-d+z],[ w+x, h+y,-d+z],
            [-w+x,-h+y, d+z],[ w+x,-h+y, d+z],
            [-w+x, h+y, d+z],[ w+x, h+y, d+z]
        ],dtype=float)

    def lighting(self,light):
        for i,f in enumerate(self.faces):
            c=self.vertices[f].mean(axis=0)
            dist=np.linalg.norm(c-light)
            self.face_brightness[i]=min(0.8,(dist*dist)/(450*450))

class Sphere:
    def __init__(self,x,y,z,r,segments=20):
        self.tris=[]
        verts=[]
        for i in range(segments+1):
            th=i*np.pi/segments
            for j in range(segments+1):
                ph=j*2*np.pi/segments
                verts.append([
                    r*np.sin(th)*np.cos(ph)+x,
                    r*np.sin(th)*np.sin(ph)+y,
                    r*np.cos(th)+z
                ])
        self.vertices=np.array(verts,dtype=float)
        row=segments+1
        for i in range(segments):
            for j in range(segments):
                a=i*row+j
                b=a+1
                c=a+row
                d=c+1
                self.tris.append((a,b,c))
                self.tris.append((b,d,c))
        self.brightness=[1]*len(self.tris)

    def lighting(self,light):
        for i,t in enumerate(self.tris):
            c=self.vertices[list(t)].mean(axis=0)
            dist=np.linalg.norm(c-light)
            self.brightness[i]=min(0.8,(dist*dist)/(250*250))

def draw_textured_quad(surface, tex, pts):
    pygame.draw.polygon(surface,(255,255,255),[(p[0],p[1]) for p in pts],1)

cubes=[
    Cube(0,100,0,250,100,400),
    Cube(0,0,50,250,100,300),
    Cube(0,-100,100,250,100,200),
    Cube(0,-200,0,50,50,50,True)
]

sphere=Sphere(0,-300,50,100)

running=True
font=pygame.font.SysFont(None,20)

while running:
    dt=clock.tick(60)

    for e in pygame.event.get():
        if e.type==pygame.QUIT:
            running=False

    k=pygame.key.get_pressed()

    if k[pygame.K_w]: camera_rot_x -= 0.02
    if k[pygame.K_s]: camera_rot_x += 0.02
    if k[pygame.K_a]: camera_rot_y -= 0.02
    if k[pygame.K_d]: camera_rot_y += 0.02

    if k[pygame.K_LEFT]:
        camera_pos[0] -= math.cos(camera_rot_y)*4
        camera_pos[2] += math.sin(camera_rot_y)*4

    if k[pygame.K_RIGHT]:
        camera_pos[0] += math.cos(camera_rot_y)*4
        camera_pos[2] -= math.sin(camera_rot_y)*4

    screen.fill((0,0,0))

    t=pygame.time.get_ticks()*0.002
    light_cube=cubes[3]
    light_cube.x=np.cos(t)*200
    light_cube.z=np.sin(t)*200
    light_cube.setup()

    light=np.array([light_cube.x,light_cube.y,light_cube.z])

    queue=[]

    R=rot_x(-camera_rot_x) @ rot_y(-camera_rot_y)

    for ci,c in enumerate(cubes):
        if not c.is_light:
            c.lighting(light)

        transformed=[]
        for v in c.vertices:
            vv=(v-camera_pos) @ R.T
            p=project(vv)
            transformed.append(p)

        for fi,f in enumerate(c.faces):
            pts=[transformed[i] for i in f]
            if any(p is None for p in pts):
                continue

            z=sum(p[2] for p in pts)/4
            queue.append(("cube",z,pts,c,fi))

    sphere.lighting(light)

    transformed=[]
    for v in sphere.vertices:
        vv=(v-camera_pos) @ R.T
        transformed.append(project(vv))

    for ti,tr in enumerate(sphere.tris):
        pts=[transformed[i] for i in tr]
        if any(p is None for p in pts):
            continue
        z=sum(p[2] for p in pts)/3
        queue.append(("sphere",z,pts,sphere,ti))

    queue.sort(key=lambda q:q[1],reverse=True)

    for typ,_,pts,obj,idx in queue:
        if typ=="cube":
            if obj.is_light:
                pygame.draw.polygon(screen,(255,255,0),[(p[0],p[1]) for p in pts])
            else:
                draw_textured_quad(screen,texture,pts)
                b=int(255*obj.face_brightness[idx])
                shade=pygame.Surface((WIDTH,HEIGHT),pygame.SRCALPHA)
                pygame.draw.polygon(shade,(0,0,0,b),[(p[0],p[1]) for p in pts])
                screen.blit(shade,(0,0))
        else:
            b=max(0,min(255,int(255*(1-obj.brightness[idx]))))
            pygame.draw.polygon(screen,(b,b,b),[(p[0],p[1]) for p in pts])

    fps=font.render(str(int(clock.get_fps())),True,(255,255,255))
    screen.blit(fps,(20,20))
    pygame.display.flip()

pygame.quit()
