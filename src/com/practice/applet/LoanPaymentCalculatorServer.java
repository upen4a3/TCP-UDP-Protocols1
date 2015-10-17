package com.practice.applet;

import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LoanPaymentCalculatorServer extends JFrame {

    // Initialize text area component
    private JTextArea jta=new JTextArea();

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new LoanPaymentCalculatorServer();
    }

    public LoanPaymentCalculatorServer(){

        setLayout(new BorderLayout());
        add(new JScrollPane(jta), BorderLayout.CENTER);

        setTitle("MultiThreadServer");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // It is necessary to show the frame here!

        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(8000);
            jta.append("MultiThreadServer started at " + new Date() + '\n');

            // Number a client
            int clientNo = 1;

            while (true) {
                // Listen for a new connection request
                Socket socket = serverSocket.accept();

                // Display the client number
                jta.append("Starting thread for client " + clientNo +
                        " at " + new Date() + '\n');

                // Find the client's host name, and IP address
                InetAddress inetAddress = socket.getInetAddress();
                jta.append("Client " + clientNo + "'s host name is "
                        + inetAddress.getHostName() + "\n");
                jta.append("Client " + clientNo + "'s IP Address is "
                        + inetAddress.getHostAddress() + "\n");

                // Create a new thread for the connection
                HandleAClient task = new HandleAClient(socket);

                // Start the new thread
                new Thread(task).start();

                // Increment clientNo
                clientNo++;
            }
        }
        catch(IOException ex) {
            System.err.println(ex);
        }
    }

    // Inner class
    // Define the thread class for handling new connection
    class HandleAClient implements Runnable {
        private Socket socket; // A connected socket

        /** Construct a thread */
        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        /** Run a thread */
        public void run() {
            try {
                // Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                        socket.getOutputStream());

                // Continuously serve the client
                while (true) {
                    // Receive radius from the client
                    double interestRate = inputFromClient.readDouble();
                    double initLoanAmount=inputFromClient.readDouble();
                    int financeTerm=inputFromClient.readInt();
                    
//                    Loan loan = new Loan(interestRate, financeTerm, initLoanAmount);
//                    System.out.println(loan.getMonthlyPayment());
//                    System.out.println(loan.getTotalPayment());
                    // Compute area
                    double totalPayment = calculateTotalPayment(initLoanAmount,financeTerm,interestRate);

                    double monthlyPayment = calculateMontlyPayment(initLoanAmount,financeTerm,interestRate);

                    // Send area back to the client
                    outputToClient.writeDouble(totalPayment);
                    outputToClient.writeDouble(monthlyPayment);
                    jta.append("Interest Rate received from client: " + interestRate + '\n');
                    jta.append("Initial loan amount received from client: " + initLoanAmount + '\n');
                    jta.append("Term received from client: " + financeTerm + '\n');

                    jta.append(" Total Payment is : " + totalPayment + '\n');
                    jta.append(" Monthly Payment is : " + monthlyPayment + '\n');
                }
            }
            catch(IOException e) {
                System.err.println(e);
            }
        }
    }

    public static double calculateMontlyPayment(double loanAmount, int term, double rateOfInterest) {

    	 double monthlyInterestRate = rateOfInterest / 1200;
         double monthlyPayment = loanAmount * monthlyInterestRate / (1 -
                 (Math.pow(1 / (1 + monthlyInterestRate), term * 12)));

        return monthlyPayment;
    }
    
    public static double calculateTotalPayment(double loanAmount, int term, double rateOfInterest) {
    	 double totalPayment = calculateMontlyPayment(loanAmount,term,rateOfInterest) * term * 12;
         return totalPayment; 
    }
}
