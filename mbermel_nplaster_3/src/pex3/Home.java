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
		private JPanel search;
		private JPanel advancedSearch;
		private JPanel movieQueue;
		private JPanel actors;
		private JPanel movies;
		
		
		public LayoutStructure(){
			setTitle("Movie Queue [like Netflixs]");
			setSize(600,600);
			setBackground(Color.gray);
			
			JPanel topPanel = new JPanel();
			topPanel.setLayout(new BorderLayout());
			getContentPane().add(topPanel);
			
			createMovieQueuePage();
			createSearchPage();
			createAdvancedSearchPage();
			createActorsPage();
			createMoviesPage();
			
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Search", search);
			tabbedPane.addTab("Advanced Search", advancedSearch);
			tabbedPane.addTab("Movie Queue", movieQueue);
			tabbedPane.addTab("Movies", movies);
			tabbedPane.addTab("Actors", actors);
			
			topPanel.add(tabbedPane, BorderLayout.CENTER);
		}
		
		public void createAdvancedSearchPage(){
			advancedSearch = new AdvancedSearch();
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
		
		public void createMoviesPage(){
			movies = new MovieView();
		}
		
	}
	
	public void init(){
		LayoutStructure layout = new LayoutStructure();
		//layout.pack();
		layout.setVisible(true);
	}
}
