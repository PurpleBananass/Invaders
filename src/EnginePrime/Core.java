package EnginePrime;

public final class Core {
    public static void main(final String[] args) {
        
        Frame frame = new Frame();
        frame.SetSize(1000, 800);
        InputManager im = InputManager.getInstance();
        frame.addKeyListener(im);
        GameManager gm = GameManager.getInstance();
        gm.Initialize();
        while (gm.running) {
            gm.EngineTime.Update();
            im.UpdateKeyState();
            gm.PreUpdate();
            EventSystem.getInstance().Update();
            gm.LateUpdate();
            RenderManager.Fill();
            EventSystem.getInstance().RenderEntity();
            frame.Render();
        }
        System.exit(0);
    }
    
    private Core() {}
}
