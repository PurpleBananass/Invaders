package EnginePrime;

public final class Core {
    public static void main(final String[] args) {
        GameManager gm = GameManager.getInstance();
        gm.frame = new Frame();
        gm.frame.SetSize(448, 520);
        InputManager im = InputManager.getInstance();
        gm.frame.addKeyListener(im);
        gm.Initialize();
        while (gm.running) {
            gm.PreUpdate();
            EventSystem.getInstance().Update();
            gm.LateUpdate();
            EventSystem.getInstance().RenderEntity();
            gm.frame.Render();
        }
        
        System.exit(0);
    }
    
    private Core() {}
}
