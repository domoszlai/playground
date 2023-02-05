import pygame
from pygame.math import Vector2

from pytmx.util_pygame import load_pygame
from pytmx import TiledTileLayer

from base import Actor
from mano_tile import Mano
from colors import *

FPS = 30
SCALE = 2

class Tile(Actor):
    def __init__(self, image):
        Actor.__init__(self)
        self.image = image
        self.rect = self.image.get_rect()

pygame.init()
pygame.mixer.init()
screen = pygame.display.set_mode((800, 600), pygame.RESIZABLE)
pygame.display.set_caption("Camera")
clock = pygame.time.Clock()

all_sprites = pygame.sprite.Group()
obstacles = pygame.sprite.Group()

tiled_map = load_pygame('dungeon/level.tmx')
world_with = tiled_map.width * tiled_map.tilewidth * SCALE
world_height = tiled_map.height * tiled_map.tileheight * SCALE

screen = pygame.display.set_mode((world_with, world_height), pygame.RESIZABLE)

for layer in tiled_map.visible_layers:
    if isinstance(layer, TiledTileLayer):
        for x, y, gid in layer:
            if tiled_map.images[gid]:
                props = tiled_map.tile_properties.get(gid) or {}
                image = tiled_map.images[gid]
                tclass = props.get("class")
                image = pygame.transform.scale(image, (tiled_map.tilewidth * SCALE, tiled_map.tileheight * SCALE))

                if tclass == "wall" or tclass == "object":
                    actor = Tile(image)
                    all_sprites.add(actor)
                    obstacles.add(actor)
                elif tclass == "player":
                    player = Mano(image)
                    actor = player
                    all_sprites.add(player)
                else:
                    actor = Tile(image)
                    all_sprites.add(actor)

                actor.rect.x = x * tiled_map.tilewidth * SCALE
                actor.rect.y = y * tiled_map.tileheight * SCALE

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
    player.walk(walkdir)

    all_sprites.update()

    collisions = pygame.sprite.spritecollide(player, obstacles, False)
    for c in collisions:
        player.collide(c)
        c.collide(player)

    screen.fill(BLACK)
    all_sprites.draw(screen)
    pygame.display.flip()

pygame.quit()