import pygame
from pygame.math import Vector2
from colors import *
from base import Actor

WIDTH = 360
HEIGHT = 480
FPS = 30

PLAYER_ACC = 3
PLAYER_JUMP_VELOCITY = -30
PLAYER_FRICTION = 0.80
PLAYER_GRAV = 2
PLAYER_MAX_SPEED_X = 10
PLAYER_MAX_SPEED_Y = 40

class Mano(Actor):
    def __init__(self):
        Actor.__init__(self)
        self.image = pygame.Surface((50, 50))
        self.image.fill(GREEN)
        self.rect = self.image.get_rect()

    def jump(self):
        self.acceleration.y = PLAYER_JUMP_VELOCITY

    def walk(self, dir):
        self.acceleration.x = dir * PLAYER_ACC

    def update(self):

        self.velocity.y += PLAYER_GRAV
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

class Floor(Actor):
    def __init__(self):
        Actor.__init__(self)
        self.image = pygame.Surface((WIDTH, 10))
        self.image.fill(RED)
        self.rect = self.image.get_rect()
        self.rect.bottom = HEIGHT

class Platform(Actor):
    def __init__(self):
        Actor.__init__(self)
        self.image = pygame.Surface((80, 120))
        self.image.fill(BLUE)
        self.rect = self.image.get_rect()
        self.rect.left = 250
        self.rect.top = HEIGHT - 150

pygame.init()
pygame.mixer.init()
screen = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("Platform")
clock = pygame.time.Clock()

all_sprites = pygame.sprite.Group()
obstacles = pygame.sprite.Group()

mano = Mano()
mano.rect.centerx = WIDTH / 2
mano.rect.bottom = HEIGHT - 10
floor = Floor()
platform = Platform()

all_sprites.add(mano)
all_sprites.add(floor)
all_sprites.add(platform)
obstacles.add(floor)
obstacles.add(platform)

running = True
while running:
    clock.tick(FPS)
    
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        
        if event.type == pygame.KEYDOWN:
            if event.key == pygame.K_SPACE:
                mano.jump()

    keystate = pygame.key.get_pressed()
    if keystate[pygame.K_LEFT]:
        mano.walk(-1)
    if keystate[pygame.K_RIGHT]:
        mano.walk(1)

    all_sprites.update()

    collisions = pygame.sprite.spritecollide(mano, obstacles, False)
    for c in collisions:
        mano.collide(c)
        c.collide(mano)

    screen.fill(BLACK)
    all_sprites.draw(screen)
    pygame.display.flip()

pygame.quit()
