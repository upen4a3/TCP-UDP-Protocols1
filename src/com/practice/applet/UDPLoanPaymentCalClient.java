package com.practice.applet;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UDPLoanPaymentCalClient extends MainFrame{


    private static final long serialVersionUID = 1L;

    // Server InetAddress
    private InetAddress address;

    private byte[] buffer=new byte[1024];

    private DatagramPacket packet;
    
   

    private DatagramPacket receivePacket;

    // Datagram socket
    private DatagramSocket socket;

    public static void main(String[] args) {
        new UDPLoanPaymentCalClient();
    }

    public UDPLoanPaymentCalClient(){
        
        submit.addActionListener(actionListener);
        getFrame();

        try {
            // get a datagram socket
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
            packet =
                    new DatagramPacket(buffer, buffer.length, address, 8000);
        
            receivePacket = new DatagramPacket(buffer, buffer.length);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    ActionListener actionListener=new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {
            try {

                // Initialize buffer for each iteration
                Arrays.fill(buffer, (byte)0);

                // Get the Values from the text field
                String interestRate = interestJtf.getText().trim();
                String initLoanAmount=amountJtf.getText().trim();
                String financeTerm=loanTermJtf.getText().trim();
                
                
                String packets = interestRate+","+initLoanAmount+","+financeTerm;

              
                packet.setData(packets.trim().getBytes());
                socket.send(packet);
                // receive values from the server in a packet
                socket.receive(receivePacket);

                String[] recievedResults =new String(receivePacket.getData()).trim().split(",");

                double monthlyPayment = Double.parseDouble(recievedResults[0]);

                double totalPayment  = Double.parseDouble(recievedResults[1]);

                // Display to the text area
                jta.append("Interest rate is " + interestRate+"%"+ "\n");
                jta.append("Loan amount is "+"$"+ initLoanAmount+"\n");
                
                // Calculated loan values received from the server
                jta.append("Calculated values received from server :");
                jta.append("Total payment value received from the server is "+"$"+totalPayment+ "\n");
                jta.append("Monthly payment value from the server is "+"$"+monthlyPayment+ "\n\n\n");
            }catch (IOException ex) {
                System.err.println(ex);
            }
        }
    };
}
