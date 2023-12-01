package entity;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;

import java.awt.*;

public class LaserBeam extends Entity {
    private Cooldown animationCoolDown;

    private int count;

    private State state;

    public static enum State {
        Warning,
        Attack,
        End
    }

    public LaserBeam(final int positionX, final int positionY) {
        super(positionX, positionY, 10 * 2, 225 * 2, Color.red);
        spriteType = DrawManager.SpriteType.LaserBeam;
        animationCoolDown = Core.getCooldown(100);
        state = State.Warning;
        count = 0;
    }

    public void update() {
        if (animationCoolDown.checkFinished()) {
            animationCoolDown.reset();
            if (state == State.Warning) {
                if (count < 10) {
                    if (getColor() == Color.red)
                        changeColor(Color.black);
                    else
                        changeColor(Color.red);
                    count++;
                }
                else {
                    state = State.Attack;
                    count = 0;
                    changeColor(new Color(255, 255, 255, 255));
                }
            }
            else if (state == State.Attack) {
                if (count < 5) {
                    int alpha = getColor().getAlpha();
                    alpha -= count * 50;
                    if (alpha < 0)
                        alpha = 0;
                    changeColor(new Color(255, 255, 255, alpha));
                    count++;
                }
                else
                    state = State.End;
            }
        }
    }

    public State getState() { return state; }
}
