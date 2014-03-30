package pex3;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Home extends Applet{
	public class LayoutStructure extends JFrame{
		private JTabbedPane tabbedPane;
		//change to Search search since search is a JPanel
		private JPanel search;
		private JPanel movieQueue;
		private JPanel actors;
		
		public LayoutStructure(){
			setTitle("Movie Queue [like Netflixs]");
			setSize(300,200);
			setBackground(Color.gray);
			
			JPanel topPanel = new JPanel();
			topPanel.setLayout(new BorderLayout());
			getContentPane().add(topPanel);
			
			createSearchPage();
			createMovieQueuePage();
			createActorsPage();
			
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Search", search);
			tabbedPane.addTab("Movie Queue", movieQueue);
			tabbedPane.addTab("Actors", actors);
			topPanel.add(tabbedPane, BorderLayout.CENTER);
		}
		
		public void createSearchPage(){
			search = new Search();
		}
		
		public void createMovieQueuePage(){
			movieQueue = new MovieQueue();
		}
		
		public void createActorsPage(){
			actors = new ActorView();
		}
		
	}
	
	public void init(){
		LayoutStructure layout = new LayoutStructure();
		layout.setVisible(true);
	}
}
