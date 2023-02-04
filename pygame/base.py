import pygame
from pygame.math import Vector2

class Actor(pygame.sprite.Sprite):
    def __init__(self):
        pygame.sprite.Sprite.__init__(self)
        self.velocity = Vector2(0, 0)
        self.acceleration = Vector2(0, 0)

    def collide(self, actor):
        pass