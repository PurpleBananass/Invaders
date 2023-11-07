package EnginePrime;

public final class Core {
    public static void main(final String[] args) {
        GameManager gm = GameManager.getInstance();
        gm.frame = new Frame();
        gm.frame.SetSize(300, 300);
        InputManager im = InputManager.getInstance();
        gm.frame.addKeyListener(im);
        gm.Initialize();
        while (gm.running) {
            EngineTimer.getInstance().Update();
            im.UpdateKeyState();
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
