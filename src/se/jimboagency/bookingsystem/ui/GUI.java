package se.jimboagency.bookingsystem.ui;

import se.jimboagency.bookingsystem.logic.LogicManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener {

    private LogicManager logic;
    JLabel year;
    JTextField yearText;
    JLabel week;
    JTextField weekText;
    JButton button;
    JCheckBox checkBox;

    JTextArea textArea;
    ArrayList<String> results;

    public GUI(LogicManager logic){
        this.logic = logic;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // x blir exit application och terminate
        this.setTitle("Statistics"); // Sätter titel på program
        this.setLayout(new FlowLayout());
        this.setResizable(false); // Hindrar resize
        this.setSize(200,750); // Definierar x och y dimesioner för this (Pixlar)

        year = new JLabel("År");
        yearText = new JTextField();
        yearText.setPreferredSize(new Dimension(250,20));

        week = new JLabel("Vecka");
        weekText = new JTextField();
        weekText.setPreferredSize(new Dimension(250,20));


        button = new JButton("Sök");
        button.addActionListener(this);

        checkBox = new JCheckBox();
        checkBox.setText("Jag är inte en robot");
        checkBox.setFocusable(false);

        textArea = new JTextArea(20, 80);


        this.add(year);
        this.add(yearText);
        this.add(week);
        this.add(weekText);
        this.add(button);
        this.add(checkBox);
        this.add(textArea);
        this.pack();
        this.setVisible(true); // Gör this synlig
    }

    @Override
    public void actionPerformed(ActionEvent a){
        if(a.getSource() == button){
            try{
                results = this.logic.findStats(yearText.getText(), weekText.getText(), checkBox.isSelected());
                textArea.setText("");
                if(results.size() < 1){
                    textArea.setText("Hittade inga bokningar med angivet år och vecka.");
                } else {
                    for(String item : results){
                        textArea.append(item + "\n");
                    }
                }
            }
            catch (Exception e){
                textArea.setText("Fel.");
            }
        }
    }

}
