package EnginePrime;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class Component {
    int LifeStep = -1;
    int RenderPrior = 1;
    // 수신 이벤트 등록
    public Map<String, Consumer<Message>> CustomEvent = new HashMap<>();
    public ArrayList<Message> MessagePool = new ArrayList<>();
    public Entity Obj = null;

    public void ProcMessage() {
        if (LifeStep < 2) {
            return;
        }
        for (Message m : MessagePool) {
            Consumer<Message> f = CustomEvent.get(m.obj.get("Func"));
            if (f != null) {
                f.accept(m);
            }
        }
        MessagePool.clear();
    }

    public void SetRenderPrior(int p) {
        RenderPrior = p;
    }

    public void Awake() {
    };

    public void Start() {
    };

    public void Update() {
    };

    public void Render() {
    };
}
