package entity;

import engine.DrawManager;
import engine.GameState;

public class EnemyShipB extends EnemyShip {
    public EnemyShipB(final int positionX, final int positionY,
                      final DrawManager.SpriteType spriteType, final GameState gameState) {
        super(positionX, positionY, spriteType, gameState);
        super.HP = (int)(super.HP * 1);
        super.pointValue = 20;
        System.out.println(super.pointValue);
    }
}
