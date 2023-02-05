from pygame.math import Vector2
from base import Actor

PLAYER_ACC = 5
PLAYER_FRICTION = 0.4
PLAYER_MAX_SPEED_X = 10
PLAYER_MAX_SPEED_Y = 10

class Mano(Actor):
    def __init__(self, image):
        Actor.__init__(self)
        self.image = image
        self.rect = self.image.get_rect()

    def walk(self, dir):
        self.acceleration = dir * PLAYER_ACC
        if dir.x != 0 and dir.y != 0:
            self.acceleration *= 0.7071

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
