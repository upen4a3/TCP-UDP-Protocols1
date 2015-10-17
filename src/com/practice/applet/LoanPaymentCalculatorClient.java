package com.practice.applet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;


public class LoanPaymentCalculatorClient extends MainFrame implements Serializable{


    private static final long serialVersionUID = 1L;

    // Server InetAddress
    private InetAddress address;

    // IO streams. 
    private DataOutputStream outStreamToServer;
    private DataInputStream inStreamFromServer;

    public LoanPaymentCalculatorClient(){

        submit.addActionListener(actionListener);
        getFrame();

        try {
            // Create a socket to connect to the server
            address = InetAddress.getByName("localhost");
            Socket socket = new Socket(address, 8000);

            // Create an input stream to receive data from the server
            inStreamFromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            outStreamToServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            jta.append(ex.toString() + '\n');
        }
    }

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                // Get the radius from the text field
                double interestRate = Double.parseDouble(interestJtf.getText().trim());
                double initLoanAmount=Double.parseDouble(amountJtf.getText().trim());
                int financeTerm=Integer.valueOf(loanTermJtf.getText().trim());
                // Send the radius to the server
                outStreamToServer.writeDouble(interestRate);
                outStreamToServer.writeDouble(initLoanAmount);
                outStreamToServer.writeInt(financeTerm);
                outStreamToServer.flush();

                // Get Values from the server
                double totalPayment = inStreamFromServer.readDouble();
                double monthlyPayment=inStreamFromServer.readDouble();

                // Display to the text area
                jta.append("Input Values are " + interestRate + "\n"+ initLoanAmount + "\n"+ financeTerm + "\n");
                jta.append("Total Payment received from the server is "+ totalPayment + '\n');
                jta.append("Monthly Payment received from the server is "+ monthlyPayment + '\n');
            }
            catch (IOException ex) {
                System.err.println(ex);
            }finally{
                try{
                    if(inStreamFromServer != null){
                        inStreamFromServer.close();
                    }

                    if(outStreamToServer != null){
                        outStreamToServer.close();
                    }

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    };

    public static void main(String[] args) {
        new LoanPaymentCalculatorClient();
    }
}
