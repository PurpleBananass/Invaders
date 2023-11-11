package entity;

import engine.DrawManager;
import engine.GameState;

public class BossShip extends EnemyShip {

    public BossShip (final int positionX, final int positionY,
                     final DrawManager.SpriteType spriteType, final GameState gameState){
        super(positionX, positionY, spriteType, gameState);
        super.HP = 0;//따로 수정;
        super.pointValue = 0; //따로수정

    }


}
