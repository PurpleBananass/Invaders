package EnginePrime;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Entity{
    UUID uuid = UUID.randomUUID();
    public Map<String, Component> ComponentPool = new HashMap<>();

    public <T extends Component> T AddComponent(Class<T> type) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        
        T c= type.getDeclaredConstructor().newInstance();
        c.Obj = this;
        c.LifeStep = 0;
        ComponentPool.put(c.getClass().getName(), c);
        return c;
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
