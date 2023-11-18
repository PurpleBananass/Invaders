
package EnginePrime;
import javax.swing.JFrame;


public class Frame  extends JFrame {
	
	
	public Frame() {
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUndecorated(true);
		setVisible(true);
	}

    public void SetSize(final int width, final int height){
        super.setSize(width, height);
		setLocationRelativeTo(null);
		RenderManager.getInstance().Resize(width,height);

    }


	public void Render(){
		RenderManager.getInstance().Render(this);
	}
}
