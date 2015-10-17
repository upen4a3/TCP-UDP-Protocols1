package com.practice.applet;

import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UDPLoanPaymentCalServer extends JFrame{

    // Text area for displaying contents
    private JTextArea jta = new JTextArea();

    // The byte array for sending and receiving datagram packets
    private byte[] buf = new byte[1024];

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new UDPLoanPaymentCalServer();
    }

    public UDPLoanPaymentCalServer(){

        // Place text area on the frame
        setLayout(new BorderLayout());
        add(new JScrollPane(jta), BorderLayout.CENTER);

        setTitle("UDPLoanServer");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // It is necessary to show the frame here!

        try {
            // Create a server socket
            DatagramSocket socket = new DatagramSocket(8000);
            jta.append("Server started at " + new Date() + '\n');

            // Create a packet for receiving data
            DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
            // Create a packet for sending data
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length);

            while (true) {
                // Initialize buffer for each iteration
                Arrays.fill(buf, (byte)0);

                socket.receive(receivePacket);
                String[] packetsRecieved =new String(receivePacket.getData()).trim().split(",");
                 
             
                double interestRate = Double.parseDouble(packetsRecieved[0]);
                double initLoanAmount=Double.parseDouble(packetsRecieved[1]);
                int term = Integer.parseInt(packetsRecieved[2]);
                
                double monthlyPayment = calculateMontlyPayment(initLoanAmount,interestRate,term);
                
                double totalPayment = calculateTotalPayment(initLoanAmount,interestRate,term);

                

                // Format and Display input values received from user and Output values sending to user
                jta.append("The client host name is " +receivePacket.getAddress().getHostName() +
                        "and port number is " + receivePacket.getPort() + "\n");
                jta.append("Interest rate recieved from client is " + interestRate+"%"+ "\n");
                jta.append("Loan amount recieved from client is "+"$"+initLoanAmount+"\n");
                if(term > 1){ 
                    jta.append("Term recieved from client is " + term+" "+"Years"+"\n");
                }else{
                    jta.append("Term recieved from client is " + term+" "+"Year"+"\n");
                }
                jta.append("Total Payment  is " +"$"+totalPayment+"\n");
                jta.append("Monthly Payment  is "+"$"+monthlyPayment+"\n");

                // Send values to the client in a packet
                sendPacket.setAddress(receivePacket.getAddress());
                sendPacket.setPort(receivePacket.getPort());
                String resultPackets = String.valueOf(monthlyPayment)+","+String.valueOf(totalPayment);
                sendPacket.setData(resultPackets.getBytes());
                socket.send(sendPacket);
            }
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static double calculateMontlyPayment(double loanAmount,double rateOfInterest, int term) {

   	 double monthlyInterestRate = rateOfInterest / 1200;
        double monthlyPayment = loanAmount * monthlyInterestRate / (1 -
                (Math.pow(1 / (1 + monthlyInterestRate), term * 12)));

       return monthlyPayment;
   }
   
   public static double calculateTotalPayment(double loanAmount,double rateOfInterest, int term) {
   	 double totalPayment = calculateMontlyPayment(loanAmount,rateOfInterest,term) * term * 12;
        return totalPayment; 
   }
}
