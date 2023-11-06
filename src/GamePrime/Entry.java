package GamePrime;
import java.lang.reflect.InvocationTargetException;

import EnginePrime.Entity;
import EnginePrime.EventSystem;
import EnginePrime.GManager;

public class Entry implements GManager{

    public void Initialize(){

        Entity e =  EventSystem.getInstance().Initiate();
        try {
            e.AddComponent(test.class);



            
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            ex.printStackTrace(); // 또는 다른 예외 처리 동작을 수행
        }
    }

    public void PreUpdate(){};

    public void LateUpdate(){};
}

