import pygame
from pygame.math import Vector2

from base import Actor
from mano_tile import Mano
from colors import *

TILE_SIZE = 32
GRID_WIDTH = 40
GRID_HEIGHT = 40

CAMERA_WIDTH = GRID_WIDTH * 20
CAMERA_HEIGHT = GRID_HEIGHT * 20

WIDTH = CAMERA_WIDTH
HEIGHT = CAMERA_HEIGHT
FPS = 30

level = ["wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww",
         "w                                      w",
         "w      www                             w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w          www                         w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "w                                      w",
         "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww"]

class Camera():
    def __init__(self, camera_size, world_size):
        self.rect = pygame.Rect(0, 0, camera_size.x, camera_size.y)
        self.world_size = world_size

    def reposition(self, center):
        self.rect.center = center
        self.rect.left = max(self.rect.left, 0)
        self.rect.right = min(self.rect.right, self.world_size.x)
        self.rect.top = max(self.rect.top, 0)
        self.rect.bottom = min(self.rect.bottom, self.world_size.y)

    def get_translation(self):
        return self.rect.topleft

class CameraGroup(pygame.sprite.Group):
    def __init__(self, camera):
        pygame.sprite.Group.__init__(self)
        self.camera = camera

    def draw(self, surface):
        sprites = self.sprites()
        (tr_x, tr_y) = self.camera.get_translation()
        for spr in sprites:
            self.spritedict[spr] = surface.blit(spr.image, (spr.rect.x - tr_x, spr.rect.y - tr_y))
        return []

class World():
    def __init__(self):
        mano_image = pygame.Surface((TILE_SIZE, TILE_SIZE))
        mano_image.fill(GREEN)        
        self.player = Mano(mano_image)
        self.player.rect.center = (100, 100)

        self.camera = Camera(
            Vector2(CAMERA_WIDTH, CAMERA_HEIGHT),
            Vector2(max(map(lambda r: len(r), level)), len(level))*TILE_SIZE)
        self.all_sprites = CameraGroup(self.camera)
        self.all_sprites.add(self.player)
        self.obstacles = pygame.sprite.Group()

        y = 0
        for row in level:
            x = 0
            for t in row:
                if t == 'w':
                    tile = Tile(x * TILE_SIZE, y * TILE_SIZE)    
                    self.all_sprites.add(tile)
                    self.obstacles.add(tile)
                x += 1
            y += 1

    def update(self):
        self.camera.reposition(self.player.rect.center)
        self.all_sprites.update()

    def draw_grid(self, surface):
        for x in range(0, WIDTH, TILE_SIZE):
            pygame.draw.line(surface, LIGHTGREY, (x, 0), (x, HEIGHT))
        for y in range(0, HEIGHT, TILE_SIZE):
            pygame.draw.line(surface, LIGHTGREY, (0, y), (WIDTH, y))

    def draw(self, surface):
        self.draw_grid(surface)
        self.all_sprites.draw(surface)

    def collision(self):
        collisions = pygame.sprite.spritecollide(self.player, self.obstacles, False)
        for c in collisions:
            self.player.collide(c)
            c.collide(self.player)

class Tile(Actor):
    def __init__(self, x, y):
        Actor.__init__(self)
        self.image = pygame.Surface((TILE_SIZE, TILE_SIZE))
        self.image.fill(BLUE)
        self.rect = self.image.get_rect()
        self.rect.left = x
        self.rect.top = y

pygame.init()
pygame.mixer.init()
screen = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("Camera")
clock = pygame.time.Clock()

world = World()

running = True
while running:
    clock.tick(FPS)
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        
    walkdir = Vector2(0,0)
    keystate = pygame.key.get_pressed()
    if keystate[pygame.K_LEFT]:
        walkdir.x = -1
    if keystate[pygame.K_RIGHT]:
        walkdir.x = 1
    if keystate[pygame.K_UP]:
        walkdir.y = -1
    if keystate[pygame.K_DOWN]:
        walkdir.y = 1
    world.player.walk(walkdir)

    world.update()
    world.collision()

    screen.fill(BLACK)
    world.draw(screen)
    pygame.display.flip()

pygame.quit()
