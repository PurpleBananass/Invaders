package EnginePrime;
import java.util.HashMap;
import java.util.Map;

public class Entity{
    public boolean isAlve = true;
    public String name;
    public String tag;
    public Map<String, Component> ComponentPool = new HashMap<>();


    public <T extends Component> T AddComponent(Class<T> type , int lifestep){
        try {
            T c= type.getDeclaredConstructor().newInstance();
            c.Obj = this;
            ComponentPool.put(c.getClass().getName(), c);

            if(lifestep > 0){
                c.Awake();
                c.LifeStep = 1;
            }
            if(lifestep > 1){
                c.Start();
                c.LifeStep = 2;
            }
            return c;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T extends Component> T AddComponent(Class<T> type){
        try {
            T c= type.getDeclaredConstructor().newInstance();
            c.Obj = this;
            c.LifeStep = 0;
            ComponentPool.put(c.getClass().getName(), c);
            return c;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public <T extends Component> T GetComponent(Class<T> type){
        Component c = ComponentPool.get(type.getName());
        return type.cast(c);
    }
    
    public <T extends Component> T RemoveComponent(Class<T> type) {
        T c = GetComponent(type);
        c.Obj = null;
        c.LifeStep = -1;
        ComponentPool.remove(type.getName());
        return c;
    }
}
