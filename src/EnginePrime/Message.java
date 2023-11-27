package EnginePrime;

import org.json.simple.JSONObject;

public class Message {
    public enum MessageType {
        ADD, Remove, Custom, // "Entity" , "Func" 지정.
    }

    public Entity e;
    public MessageType message;
    public JSONObject obj;

    public Message(Entity e, MessageType m) {
        this.e = e;
        this.message = m;
        obj = null;
    }

    public Message(Entity e, MessageType m, JSONObject obj) {
        this.e = e;
        this.message = m;
        this.obj = obj;
    }
}
