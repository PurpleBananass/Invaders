package EnginePrime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventSystem {

    private static EventSystem instance = null;

    private static Map<UUID, Entity> EntityPool = new HashMap<>();

    private EventSystem(){};

    public static EventSystem getInstance() {
        if (instance == null) {
            instance = new EventSystem();
        }
        return instance;
    }

    public static Entity Initiate(){
        Entity e = new Entity();
        EntityPool.put(e.uuid,e);
        return e;
    }

    public static void DestroyAll(){
        EntityPool.clear();
    }


    public static Entity Destroy(Entity e){
        EntityPool.remove(e.uuid);
        return e;
    }

    public static Entity FindEntity(UUID uuid)
    {
        return EntityPool.get(uuid);
    }
    public static void Update() {
        for (Entity entity : EntityPool.values()) {
            for (Component c : entity.ComponentPool.values()) {
                switch (c.LifeStep) {
                    case 0:
                        c.Awake();
                        c.LifeStep +=1;
                        break;
                    case 1:
                        c.Start();
                        c.LifeStep +=1;
                        break;
                    case 2:
                        c.Update();
                        break;
                    default:
                        break;
                }
                c.Update();
            }
        }
    }
    public static void RenderEntity()
    {
        for (Entity entity : EntityPool.values()) {
            for (Component c : entity.ComponentPool.values()) {
                c.Render();
            }
        }
    }
}
