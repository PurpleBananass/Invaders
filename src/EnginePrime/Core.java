package EnginePrime;

public final class Core {
    public static void main(final String[] args) {
        EventSystem em = EventSystem.getInstance();
        GameManager gm = GameManager.getInstance();
        gm.frame = new Frame();
        InputManager im = InputManager.getInstance();
        gm.frame.addKeyListener(im);
        gm.Initialize();
        while (gm.running) {
            gm.InstanceChanged = false;
            em.ProcMessage();
            gm.PreUpdate();
            em.ProcMessage();
            EventSystem.getInstance().Update();
            em.ProcMessage();
            gm.LateUpdate();
            em.ProcMessage();
            gm.PreRender();
            em.ProcMessage();
            em.RenderEntity();
            em.ProcMessage();
            gm.LateRender();
            em.ProcMessage();
            gm.frame.Render();
        }
        System.exit(0);
    }

    private Core() {
    }
}
