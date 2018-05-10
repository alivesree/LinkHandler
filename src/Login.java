
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Login extends JFrame implements ActionListener {
    // Mmbox m1;

    ResultSet rs;
    String ln, pw;
    Runtime r;
    boolean x;
    JButton b1;
    JLabel l1, l2, l3;
    JTextField t1;
    JPasswordField p1;
    JPanel pp;
    String border_type = "Line";
    AbstractBorder border = new LineBorder(Color.black, 1);

    public Login() {
        setSize(500, 400);
        move(120, 100);
        setTitle("Brokern Links Login");

        pp = new JPanel();
        l3 = new JLabel("LOGIN FOR Broken links User");
        l3.setForeground(Color.black);
        l1 = new JLabel("NAME");
        t1 = new JTextField(15);
        l2 = new JLabel("PASSWORD");
        p1 = new JPasswordField(15);
        b1 = new JButton("CONTINUE");
        l1.setBounds(95, 150, 300, 30);
        l1.setFont(new Font("convecta", Font.BOLD, 14));

        pp.setBounds(30, 120, 420, 175);

        t1.setBounds(250, 150, 170, 23);
        t1.setFont(new Font("convecta", Font.BOLD, 15));
        l2.setBounds(95, 180, 300, 30);
        l2.setFont(new Font("convecta", Font.BOLD, 14));
        p1.setBounds(250, 180, 170, 23);
        p1.setFont(new Font("convecta", Font.BOLD, 15));
        b1.setBounds(150, 235, 150, 25);
        b1.setFont(new Font("convecta", Font.BOLD, 15));
        l3.setBounds(75, 40, 420, 40);
        l3.setFont(new Font("convecta", Font.BOLD, 16));
        l1.setForeground(Color.black);
        l2.setForeground(Color.black);

        Container contain = getContentPane();
        contain.add(l1);
        contain.add(t1);
        contain.add(l2);
        contain.add(p1);
        contain.add(b1);
        contain.add(l3);
        contain.setLayout(null);

        if (border_type.equals("Line")) {
            contain.add(pp);
            pp.setBorder(border);
            repaint();
        }

        setVisible(true);
        b1.addActionListener(this);

    }//end of ouser constructor.

    public static void main(String args[]) {
        new Login();
    }

    public void check() {
// 		 Mmbox m1;
        try {

            ln = "admin";
            pw = "admin";

            if (ln == null || pw == null) {
                JOptionPane.showMessageDialog(null, "Enter username and password.");
            }
            if (ln.equals(t1.getText()) && pw.equals(p1.getText())) {

                try {

                    WebCheck wc = new WebCheck();
                    this.setVisible(false);
                    wc.setVisible(true);

                } catch (Exception eee) {
                    System.out.println("ERROR" + eee);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Wrong username and password.");
            }

        } catch (Exception ec) {
            System.out.println("" + ec);
        }
    }//end of check method. 

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {

            check();
        }//end of if loop.

    }//end of actionPerformed.
}//end of ouser class.
