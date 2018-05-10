
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class SPro extends JFrame implements ActionListener {

    JLabel l1, l2, l3;
    JButton b1, b2;
    Container c = this.getContentPane();
    JPanel p, p1;

    String border_type = "Line";
    AbstractBorder border = new LineBorder(Color.black);

    public SPro() {

        setSize(500, 400);
        setTitle("                                              WELCOME       PAGE   ");
        p = new JPanel();
        p1 = new JPanel();
        l1 = new JLabel("WELCOME TO BROKEN LINKS HANDLER CLICK GO TO PROCEED");
        l2 = new JLabel("New Employee");
        l3 = new JLabel("Welcome to Broken links");

        b1 = new JButton("Go");
        b2 = new JButton("Go");

        c.setLayout(null);
        move(120, 100);
        p1.setBounds(40, 65, 400, 1);
        p.setBounds(65, 120, 340, 150);
        l1.setBounds(110, 140, 250, 40);

        b2.setBounds(285, 210, 50, 30);
        l3.setBounds(75, 40, 420, 40);
        l1.setFont(new Font("convecta", Font.BOLD, 16));
        l1.setForeground(Color.black);
        l2.setFont(new Font("convecta", Font.BOLD, 16));
        l2.setForeground(Color.black);
        b1.setFont(new Font("convecta", Font.BOLD, 12));
        b2.setFont(new Font("convecta", Font.BOLD, 12));
        l3.setFont(new Font("convecta", Font.BOLD, 16));
        l3.setForeground(Color.black);

        b1.setForeground(Color.black);
        b2.setForeground(Color.black);

        this.setBackground(Color.gray);
        c.add(l1);

        c.add(b2);
        if (border_type.equals("Line")) {
            c.add(p1);
            c.add(p);
            p1.setBorder(border);
            p.setBorder(border);
            repaint();
            repaint();
        }

        b1.addActionListener(this);
        b2.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == b2) {

            Login l = new Login();
            this.setVisible(false);
            l.setVisible(true);
        } else {

            setVisible(false);

        }
    }

    public static void main(String a[]) {
        new SPro();
    }//end of main method.
}
