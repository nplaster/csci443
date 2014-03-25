package pex3;

import java.applet.Applet;

import java.awt.*;
import java.awt.event.*;


public class Search extends Applet  { 
    TextField inputLine = new TextField(15); 
    Button enterButton = new Button("Enter");
    
    public Search() {
    	setSize(1000, 500);
        add(inputLine);
        inputLine.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    String s = inputLine.getText();
                    String sUp = s.toUpperCase();
                    inputLine.setText(sUp);
                }
            }
         );
        add(enterButton);
        enterButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        String s = inputLine.getText();
                        String sUp = s.toUpperCase();
                        inputLine.setText(sUp);
                    }
                }
             );
    }
}
