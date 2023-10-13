import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MoneyDisplayGame extends JFrame {
    private int money = 0;
    private int targets = 10;
    private JLabel moneyLabel;

    public MoneyDisplayGame() {
        setTitle("Adventure Game");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        moneyLabel = new JLabel("Money: $" + money);
        moneyLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        moneyLabel.setHorizontalAlignment(JLabel.CENTER);
        add(moneyLabel, BorderLayout.CENTER);

        JButton killTargetButton = new JButton("Kill Target");
        killTargetButton.setFont(new Font("Arial", Font.PLAIN, 20));
        killTargetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (targets > 0) {
                    money += 10;  // Simulate earning money
                    targets--;     // Decrease the number of targets
                    updateMoneyLabel();
                }
            }
        });
        add(killTargetButton, BorderLayout.SOUTH);
    }

    private void updateMoneyLabel() {
        moneyLabel.setText("Money: $" + money);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MoneyDisplayGame game = new MoneyDisplayGame();
            game.setVisible(true);
        });
    }
}