package EnginePrime;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.UUID;
public class EventSystem {

    private static EventSystem instance = null;

    private static Map<String, Entity> EntityPool = new HashMap<>();


    
    private static ArrayList<Message> MessagePool = new ArrayList<>();

    private EventSystem(){};

    public static EventSystem getInstance() {
        if (instance == null) {
            instance = new EventSystem();
        }
        return instance;
    }


    public static void SendMessage(Message m){
        MessagePool.add(m);
    }
    public static Entity Initiate(String name){
        Entity e = new Entity();
        e.name = name;
        MessagePool.add(new Message(e,Message.MessageType.ADD));
        return e;
    }

    public static ArrayList<Entity> FindTagEntities(String tag){

        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity : EntityPool.values()) {
            if(entity.tag == tag && entity.isAlve){
                entities.add(entity);
            }    
        }
        return entities;
    }

    public static Entity Initiate(){
        Entity e = new Entity();
        e.name = UUID.randomUUID().toString();
        MessagePool.add(new Message(e,Message.MessageType.ADD));
        return e;
    }
    public static void DestroyAll() {
        for (Entity entity : EntityPool.values()) {
            entity.isAlve = false;
            Destroy(entity);
        }
    }

    public static Entity Destroy(Entity e){
        e.isAlve = false;
        MessagePool.add(new Message(e,Message.MessageType.Remove));
        return e;
    }

    public static Entity FindEntity(String name)
    {
        Entity e =  EntityPool.get(name);
        if( e != null && e.isAlve ){
            return EntityPool.get(name);
        }
        return null;
    }

    public static void ProcMessage(){
        for (Message m : MessagePool) {
            if(m.message == Message.MessageType.ADD){
                EntityPool.put(m.e.name, m.e);
            }
            if (m.message == Message.MessageType.Remove) {
                EntityPool.remove(m.e.name);
            }
            if (m.message == Message.MessageType.Custom) {
                Entity e = (Entity) m.obj.get("Entity");
                for (Component c : e.ComponentPool.values()) {
                    c.MessagePool.add(m);
                    c.ProcMessage();
                }
            }
        }
        MessagePool.clear();
    }

    public static void Update() {
        for (Entity entity : EntityPool.values()) {
            if(!entity.isAlve){
                continue;
            }
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
                        c.ProcMessage();
                        c.LifeStep +=1;
                        break;
                    case 3:
                        c.ProcMessage();
                        c.Update();
                        break;
                    default:
                        break;
                }
            }
        }
    }
    public static void RenderEntity()
    {
        for (int i = 0; i <3; i++){
            for (Entity entity : EntityPool.values()) {
                if(!entity.isAlve){
                    continue;
                }
                for (Component c : entity.ComponentPool.values()) {
                    if(c.LifeStep==3 && c.RenderPrior ==i){
                        c.Render();
                    }
                }
            }
        }
    }
}
