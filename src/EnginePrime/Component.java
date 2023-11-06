package EnginePrime;

public abstract class Component {
    int LifeStep = -1;
    public Entity Obj = null;
    public void Awake(){};
    public void Start(){};
    public void Update(){};
    public void Ondestroy(){};
    public void Render(){};
}

