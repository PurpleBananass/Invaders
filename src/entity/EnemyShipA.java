package entity;

import engine.DrawManager;
import engine.GameState;

public class EnemyShipA extends EnemyShip {
    public EnemyShipA(final int positionX, final int positionY,
                      final DrawManager.SpriteType spriteType, final GameState gameState) {
        super(positionX, positionY, spriteType, gameState);
        super.HP = (int)(super.HP * 0.5);
        super.pointValue = 10;
    }
}
