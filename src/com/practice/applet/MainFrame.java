package com.practice.applet;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame{


    private static final long serialVersionUID = 1L;

    protected JTextField interestJtf;

    protected JTextField amountJtf;

    protected JTextField loanTermJtf;

    protected JButton submit = new JButton("Submit");;

    protected JTextArea jta=new JTextArea(10,10);

    public void getFrame(){
        
        interestJtf = new JTextField(5);
        amountJtf = new JTextField(5);
        loanTermJtf = new JTextField(5);

        JPanel gui = new JPanel(new BorderLayout(5,5));
        gui.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(gui);

        JPanel labels = new JPanel(new GridLayout(0,1));
        JPanel controls = new JPanel(new GridLayout(0,1));
        gui.add(labels, BorderLayout.WEST);
        gui.add(controls, BorderLayout.CENTER);

        // Adding labels and text fields to panel
        labels.add(new JLabel("Annual Interest Rate : "));
        controls.add(interestJtf);
        interestJtf.setHorizontalAlignment(JTextField.RIGHT);
        labels.add(new JLabel("Loan Amount : "));
        controls.add(amountJtf);
        amountJtf.setHorizontalAlignment(JTextField.RIGHT);
        labels.add(new JLabel("Number Of Years : "));
        controls.add(loanTermJtf);
        loanTermJtf.setHorizontalAlignment(JTextField.RIGHT);

        // Submit Button Panel
        gui.add(submit, BorderLayout.EAST);
        
        // Output text area panel
        JPanel opPanel = new JPanel(new GridLayout(0,1));
        gui.add(opPanel, BorderLayout.SOUTH);
        opPanel.add(new JScrollPane(jta), BorderLayout.CENTER);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Loan Calculator");
        pack();
        setSize(600,300);
        setVisible(true);
    }
}