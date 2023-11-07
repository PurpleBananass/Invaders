package GamePrime;

import EnginePrime.Component;
import EnginePrime.InputManager;
import EnginePrime.RenderManager;

import java.awt.event.KeyEvent;
import java.awt.Color;
import EnginePrime.EngineTimer;

public class test extends Component{


    float x = 0;
    float y = 400;

    public void Update(){

        if(InputManager.getInstance().isKeyPressed(KeyEvent.VK_A)){
            x -=100 *EngineTimer.getInstance().GetElapsedSeconds();

        }
        if(InputManager.getInstance().isKeyPressed(KeyEvent.VK_D)){
            x +=100*EngineTimer.getInstance().GetElapsedSeconds();
        }

        if(InputManager.getInstance().isKeyPressed(KeyEvent.VK_W)){
            y -=100*EngineTimer.getInstance().GetElapsedSeconds();
        }
        if(InputManager.getInstance().isKeyPressed(KeyEvent.VK_S)){
            y +=100*EngineTimer.getInstance().GetElapsedSeconds();
        }


    }
    public void Render(){
        RenderManager.getInstance().GetCurrentGraphic().setColor(Color.red);
        RenderManager.getInstance().GetCurrentGraphic().drawRect(Math.round(x),Math.round(y), 300, 200);

    }

}
