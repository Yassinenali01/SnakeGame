import javax.swing.JFrame;

public class SnakeFrame extends JFrame {
    
    public SnakeFrame() {
        super("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SnakePanel panel = new SnakePanel();
        add(panel);
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
    }
   


}
