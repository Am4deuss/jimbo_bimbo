package se.jimboagency.bookingsystem.ui;

import se.jimboagency.bookingsystem.logic.LogicManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener {

    private LogicManager logic;
    private JLabel year;
    private JTextField yearText;
    private JLabel week;
    private JTextField weekText;
    private JButton button;
    private JCheckBox checkBox;
    private JTextArea textArea;
    private ArrayList<String> results;

    public GUI(LogicManager logic){
        this.logic = logic;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // x blir exit application och inte terminatar program
        this.setTitle("Statistics"); // Sätter titel på program
        this.setLayout(new FlowLayout()); // Gör så saker positionerar sig bättre
        this.setResizable(false); // Hindrar resize av window
        this.setSize(200,750); // Definierar x och y dimesioner för this (Pixlar)

        year = new JLabel("År"); // Sätter text "År"
        yearText = new JTextField(); // Sätter inmatningsfält för år
        yearText.setPreferredSize(new Dimension(250,20));

        week = new JLabel("Vecka"); // Sätter text "Vecka"
        weekText = new JTextField(); // Sätter inmatningsfält för vecka
        weekText.setPreferredSize(new Dimension(250,20));


        button = new JButton("Sök"); // Skapar en sök knapp
        button.addActionListener(this); // Lägger till funktion till knappen

        checkBox = new JCheckBox(); // Legit en checkbox
        checkBox.setText("Jag är inte en robbot"); // "Jag är inte en robbot" ~ Helmer 2023 ;) plz bli inte arga på oss
        checkBox.setFocusable(false);

        textArea = new JTextArea(20, 80); // Där bokningar visas upp

        // Lägger till allt på skärmen
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
                textArea.setText(""); // Clearar sökytan för varje submit
                if(results.size() < 1){
                    textArea.setText("Hittade inga bokningar med angivet år och vecka.");
                } else {
                    for(String item : results){
                        textArea.append(item + "\n");
                    }
                }
            }
            catch (Exception e){
                textArea.setText("Fel format.");
            }
        }
    }

}
