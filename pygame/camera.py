import pygame
from pygame.math import Vector2
from base import Actor
from colors import *

TILE_SIZE = 32
TILES_X = 20
TILES_Y = 20
CAMERA_WIDTH = TILES_X * 20
CAMERA_HEIGHT = TILES_Y * 20

WIDTH = CAMERA_WIDTH
HEIGHT = CAMERA_HEIGHT
FPS = 30

PLAYER_ACC = 3
PLAYER_FRICTION = 0.4
PLAYER_MAX_SPEED_X = 10
PLAYER_MAX_SPEED_Y = 10

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
        cw, ch = camera_size
        ww, wh = world_size
        self.rect = pygame.Rect(0, 0, cw, ch)
        self.ww = ww
        self.wh = wh

    def reposition(self, center):
        self.rect.center = center

        if self.rect.left < 0:
            self.rect.left = 0
        elif self.rect.right > self.ww:
            self.rect.right = self.ww

        if self.rect.top < 0:
            self.rect.top = 0
        elif self.rect.bottom > self.wh:
            self.rect.bottom = self.wh

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
        self.player = Mano()
        self.player.rect.center = (100, 100)
        self.camera = Camera(
            (CAMERA_WIDTH, CAMERA_HEIGHT),
            (max(map(lambda r: len(r), level))*TILE_SIZE, len(level)*TILE_SIZE))
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

    def update(self):
        pass

    def collide(self, actor):
        pass

class Mano(Actor):
    def __init__(self):
        Actor.__init__(self)
        self.image = pygame.Surface((TILE_SIZE, TILE_SIZE))
        self.image.fill(GREEN)
        self.rect = self.image.get_rect()

    def walk(self, dirx, diry):
        self.acceleration.x = dirx * PLAYER_ACC
        self.acceleration.y = diry * PLAYER_ACC

    def update(self):

        self.velocity.y *= PLAYER_FRICTION
        self.velocity.x *= PLAYER_FRICTION

        self.velocity += self.acceleration
        self.acceleration = Vector2(0, 0)

        if self.velocity.x < -PLAYER_MAX_SPEED_X:
            self.velocity.x = -PLAYER_MAX_SPEED_X
        elif self.velocity.x > PLAYER_MAX_SPEED_X:
            self.velocity.x = PLAYER_MAX_SPEED_X
        elif abs(self.velocity.x) < 1: 
            self.velocity.x = 0

        if self.velocity.y < -PLAYER_MAX_SPEED_Y:
            self.velocity.y = -PLAYER_MAX_SPEED_Y
        elif self.velocity.y > PLAYER_MAX_SPEED_Y:
            self.velocity.y = PLAYER_MAX_SPEED_Y
        elif abs(self.velocity.y) < 1: 
            self.velocity.y = 0
        
        self.rect.center += self.velocity

    def collide(self, actor):
        # self speed relative to actor's speed
        dir = self.velocity - actor.velocity

        # top
        if (dir.y < 0 and 
            self.rect.top < actor.rect.bottom and
            self.rect.bottom > actor.rect.bottom):
                self.velocity.y = 0
                self.rect.top = actor.rect.bottom

        # bottom
        elif (dir.y > 0 and 
            self.rect.bottom > actor.rect.top and
            self.rect.top < actor.rect.top):
                self.velocity.y = 0
                self.rect.bottom = actor.rect.top

        # left
        elif (dir.x < 0 and 
            self.rect.left < actor.rect.right and
            self.rect.right > actor.rect.right):
                self.velocity.x = 0
                self.rect.left = actor.rect.right

        # right
        elif (dir.x > 0 and 
            self.rect.right > actor.rect.left and
            self.rect.left < actor.rect.left):
                self.velocity.x = 0
                self.rect.right = actor.rect.left

# initialize pygame and create window
pygame.init()
pygame.mixer.init()
screen = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("Camera")
clock = pygame.time.Clock()

world = World()

# Game loop
running = True
while running:
    # keep loop running at the right speed
    clock.tick(FPS)
    # Process input (events)
    for event in pygame.event.get():
        # check for closing window
        if event.type == pygame.QUIT:
            running = False
        
    keystate = pygame.key.get_pressed()
    if keystate[pygame.K_LEFT]:
        world.player.walk(-1, 0)
    if keystate[pygame.K_RIGHT]:
        world.player.walk(1, 0)
    if keystate[pygame.K_UP]:
        world.player.walk(0, -1)
    if keystate[pygame.K_DOWN]:
        world.player.walk(0, 1)

    world.update()
    world.collision()

    screen.fill(BLACK)
    world.draw(screen)
    pygame.display.flip()

pygame.quit()
