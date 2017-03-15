import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Menu extends Game{
		
	protected Game ob;
	
	
	public Menu(){
		
		JButton start = new JButton("Start");
		JButton help = new JButton("How to Play!");
		ButtonGroup group = new ButtonGroup();
		JRadioButton d1 = new JRadioButton("Difficulity : easy");
		JRadioButton d2 = new JRadioButton("Difficulity : intermedia");
		JRadioButton d3 = new JRadioButton("Difficulity : difficult as hell");
		group.add(d1);
		group.add(d2);
		group.add(d3);
		d1.addItemListener(new Handler(5));
		d2.addItemListener(new Handler(7));
		d3.addItemListener(new Handler(10));
		
		JFrame jf = new JFrame();
		JPanel jp = new JPanel();
		JTextField jt = new JTextField("Welcome! ",50);
		ob = new Game();
		start.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				jf.setVisible(false);
				ob.getJFrame().setVisible(true);
				ob.start();			
			}
			
		});
		
		help.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				jt.setText("Click Left Mouse Button to jump, or you can use up key to jump! ");
			}
			
		});
				
		jp.add(start);
		jp.add(help);
		jp.add(jt);
		jp.add(d1);
		jp.add(d2);
		jp.add(d3);
		
		jf.add(jp);
		jf.setTitle("Flapy Bird(lemondoglol)");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(800, 400);
		jf.setVisible(true);
			
	}
	
	private class Handler implements ItemListener{
		int diff;
		public Handler (int i){
			diff = i;
		}
		
		@Override
		public void itemStateChanged(ItemEvent e) {	
			ob.setDifficulity(diff);
		}
	}
	
	public static void main(String args[]){
		Menu ob = new Menu();
		
	}

	
	
}
