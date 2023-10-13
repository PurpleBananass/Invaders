package entity;

import engine.DrawManager;
import engine.GameState;

public class EnemyShipC extends EnemyShip {
    public EnemyShipC(final int positionX, final int positionY,
                      final DrawManager.SpriteType spriteType, final GameState gameState) {
        super(positionX, positionY, spriteType, gameState);
        super.HP = (int)(super.HP * 2);
        super.pointValue = 30;
        System.out.println(super.pointValue);
    }
}
