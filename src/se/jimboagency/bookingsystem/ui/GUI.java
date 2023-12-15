package se.jimboagency.bookingsystem.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    JButton button;
    JTextField yearText;
    JTextField weekText;

    public GUI(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x blir exit application och terminate
        this.setTitle("Statistics"); // Sätter titel på program
        this.setLayout(new FlowLayout());
        this.setResizable(false); // Hindrar resize
        this.setSize(400,400); // Definierar x och y dimesioner för this (Pixlar)
        //ImageIcon image = new ImageIcon("icon.png");
        //this.setIconImage(image.getImage()); // Ändrar ikonen av this
        this.getContentPane().setBackground(new Color(0,180,0)); // Ändra bakgrundsfärg (också 0xFFFFFF)

        button = new JButton("Sök");
        button.addActionListener(this);

        yearText = new JTextField();
        yearText.setPreferredSize(new Dimension(250,20));

        weekText = new JTextField();
        weekText.setPreferredSize(new Dimension(250,20));

        this.add(text);
        this.add(yearText);
        this.add(weekText);
        this.add(button);
        this.pack();
        this.setVisible(true); // Gör this synlig
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == button){
            System.out.println("År: " + yearText.getText());
            System.out.println("Vecka: " + weekText.getText());
        }
    }

}
