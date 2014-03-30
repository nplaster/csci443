package pex3;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.TextArea;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MovieQueue extends JPanel implements ListSelectionListener{
	DefaultListModel<String> listModel;
    JList<String> list;
    JPanel parentFrame = new JPanel( new BorderLayout() );
    JPanel buttonFrame = new JPanel( new GridLayout(4, 0) );
    Button refresh = new Button("Refresh");
    Button up = new Button("Up");
    Button down = new Button("Down");
    Button remove = new Button("Remove");
    
	
	public MovieQueue(){
		listModel = new DefaultListModel<String>();
		buttonFrame.add(refresh);
		buttonFrame.add(up);
		buttonFrame.add(down);
		buttonFrame.add(remove);
		
		parentFrame.add(buttonFrame, BorderLayout.EAST);
		
		add(parentFrame);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	


}
