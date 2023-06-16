/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pathfinding;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.JComboBox;

public class ProjekPAA2023 {
    JFrame frame;
    // variabels
    private int cells = 15;
    private int delay = 130;
    private double dense = .3;
    private double density = (cells*cells)*.3;
    private int startx = -1;
    private int starty = -1;
    private int finishx = -1;
    private int finishy = -1;
    private int tool = 0;
    private int curAlg = 0;
    private int WIDTH = 850;
    private final int HEIGHT = 650;
    private final int MSIZE = 600;
    private int CSIZE = MSIZE/cells;
    private final String[] tools = {"Tambah Droid Merah","Tambah Droid Hijau","Tambah Tembok", "Hapus Tembok"}; //aray
    private boolean solving = false; //boolean

    Node[][] map;
    Algorithm Alg = new Algorithm();
    Random r = new Random();
    JLabel toolL = new JLabel("FITUR TAMBAHAN"); //label
    //button
    JButton startB = new JButton("MULAI"); //mulai sistem
    JButton stopB = new JButton("STOP");  //hentikan gerakan droid
    JButton genMapB = new JButton("ACAK PETA"); //acak map
    JButton resetB = new JButton("RESET"); //mulai dari petak kosong

    JComboBox toolBx = new JComboBox(tools);
    JPanel toolP = new JPanel();
    Map canvas;
    Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

public static void main(String[] args) {	//fungsi utama
    new ProjekPAA2023();}

    public ProjekPAA2023() {	//constructor
	clearMap();
	initialize();}
	
    public void generateMap() {	//acak map
	clearMap();	//hapus map utk mulai
	for(int i = 0; i < density; i++) {
	Node current;
            do {
		int x = r.nextInt(cells);
		int y = r.nextInt(cells);
		current = map[x][y];	//temukan random node
            } while(current.getType()==2);	//kalau udh ada tembok temukan yg lain
		current.setType(2);	//jadikan node tembok
		}
	}
	
	public void clearMap() {	//hapus map
            finishx = -1;	//reset mulai dan selesai
            finishy = -1;
            startx = -1;
            starty = -1;
            map = new Node[cells][cells];	//buat map dari node
		for(int x = 0; x < cells; x++) {
                    for(int y = 0; y < cells; y++) {
                        map[x][y] = new Node(3,x,y);	//buat node jadi kosong
			}
		}
		reset();	//reset variabel
	}
	
	public void resetMap() {	//reset peta
		for(int x = 0; x < cells; x++) {
		for(int y = 0; y < cells; y++) {
                    Node current = map[x][y];
			if(current.getType() == 4 || current.getType() == 5)	//cek node udh pernah diperiksa atau udh final
                            map[x][y] = new Node(3,x,y);	//reset jadi node kosong
			}
		}
		if(startx > -1 && starty > -1) {	//reset mulai dan berhenti
			map[startx][starty] = new Node(0,startx,starty);
			map[startx][starty].setHops(0);
		}
		if(finishx > -1 && finishy > -1)
			map[finishx][finishy] = new Node(1,finishx,finishy);
		reset();	//reset variabel
	}

	private void initialize() {	//elemen gui
            frame = new JFrame();
            frame.setVisible(true);
            frame.setResizable(false);
            frame.setSize(WIDTH,HEIGHT);
            frame.setTitle("Project PAA 2023");
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setLayout(null);
		
            toolP.setBorder(BorderFactory.createTitledBorder(loweredetched," Fitur Projek :v "));
		int space = 25;
                int buff = 75; //jarak antar kotak fitur
		
	toolP.setLayout(null);
	toolP.setBounds(10,10,210,550);
		
	startB.setBounds(30,space, 150, 50);
        startB.setBackground(Color.green);
	toolP.add(startB);
	space+=buff;
		
	stopB.setBounds(30,space,150,50);
        stopB.setBackground(Color.red);
        stopB.setForeground(Color.white);
	toolP.add(stopB);
	space+=buff;
		
	genMapB.setBounds(30,space, 150, 50);
        genMapB.setBackground(Color.cyan);
	toolP.add(genMapB);
	space+=buff;
		
	resetB.setBounds(30,space, 150, 50);
        resetB.setBackground(Color.DARK_GRAY);
        resetB.setForeground(Color.white);
	toolP.add(resetB);
	space+=buff;
				
	toolL.setBounds(30,space,150,50); //kotak fitur
	toolP.add(toolL);
	space+=40;
		
	toolBx.setBounds(30,space,150,35);
	toolP.add(toolBx);
	
        frame.getContentPane().add(toolP);	
	canvas = new Map();
	canvas.setBounds(230, 10, MSIZE+1, MSIZE+1);
        frame.getContentPane().add(canvas);
	frame.getContentPane().add(toolP);		
	canvas = new Map();
	canvas.setBounds(230, 10, MSIZE+1, MSIZE+1);
	frame.getContentPane().add(canvas);
        
	startB.addActionListener(new ActionListener() {		//ACTION LISTENERS
	@Override
		public void actionPerformed(ActionEvent e) {
		reset();
                    if((startx > -1 && starty > -1) && (finishx > -1 && finishy > -1))
			solving = true;        }
		});
		stopB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetMap();
				Update();}
		});
		genMapB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateMap();
				Update();}
		});
		resetB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearMap();
				Update();}
		});
		toolBx.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				tool = toolBx.getSelectedIndex();}
		});	
		startSearch();	} //strat state nya
	
	public void startSearch() {	
		if(solving) {
			switch(curAlg) {
				case 0:
					Alg.AStar(); //deklarasi algoritma
					break;}
		}
        JOptionPane.showMessageDialog(frame, "!! TARGET DITEMUKAN !!", "YEAY", JOptionPane.NO_OPTION); //pop up message
		pause();}
	
	public void pause() {	//stop state
		int i = 0;
		while(!solving) {
		i++;
                    if(i > 500)
                    i = 0;
                    try {
			Thread.sleep(1);} 
                    catch(Exception e) {}
		}
            startSearch();} //deklarasi mulai
	
	public void Update() {	//update elemen
            density = (cells*cells)*dense;
            CSIZE = MSIZE/cells;
            canvas.repaint();}
	
	public void reset() {	//reset
            solving = false;}
	
	public void delay() {	//lama delay
		try {
		Thread.sleep(delay);} 
                catch(Exception e) {}
	}
	
class Map extends JPanel implements MouseListener, MouseMotionListener{	//kelas map		
	public Map() {
	addMouseListener(this);
	addMouseMotionListener(this);}
		
	public void paintComponent(Graphics g) {	//REPAINT
            super.paintComponent(g);
		for(int x = 0; x < cells; x++) {	//PAINT EACH NODE IN THE GRID
                for(int y = 0; y < cells; y++) {
                    switch(map[x][y].getType()) {
			case 0:
                            g.setColor(Color.RED); //tambah droid merah
                            break;
                        case 1:
                            g.setColor(Color.GREEN); //tambah droid hijau
                            break;
			case 2:
                            g.setColor(Color.DARK_GRAY); //tambah tembok
                            break;
			case 3:
                            g.setColor(Color.LIGHT_GRAY); //baground map
                            break;
			case 4:
                            g.setColor(Color.BLUE); //seleksi jalan
                            break;
                        case 5:
                            g.setColor(Color.WHITE); //jalan
                            break;}
		g.fillRect(x*CSIZE,y*CSIZE,CSIZE,CSIZE);
		g.setColor(Color.BLUE); //grid map
		g.drawRect(x*CSIZE,y*CSIZE,CSIZE,CSIZE);}
		}
		}

	@Override
	public void mouseDragged(MouseEvent e) {
            try {
		int x = e.getX()/CSIZE;	
		int y = e.getY()/CSIZE;
		Node current = map[x][y];
                    if((tool == 2 || tool == 3) && (current.getType() != 0 && current.getType() != 1))
			current.setType(tool);
			Update();} 
                    catch(Exception z) {}
		}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
            resetMap(); //reset map ketika di klik
		try {
                    int x = e.getX()/CSIZE;	//posisi x dan y seukuran grid map
                    int y = e.getY()/CSIZE;
            Node current = map[x][y];
                switch(tool ) {
                    case 0: {	//node mulai
			if(current.getType()!=2) {	//jika bukan tembok
			if(startx > -1 && starty > -1) {	//jika mulai ad isian maka jadikan kosong
                            map[startx][starty].setType(3);
                            map[startx][starty].setHops(-1);}
			current.setHops(0);
                            startx = x;	//set x dan y
                            starty = y;
			current.setType(0);	} //set droid merah
			break;}
                    case 1: {//FINISH NODE
			if(current.getType()!=2) {	
			if(finishx > -1 && finishy > -1)	
                            map[finishx][finishy].setType(3);
                            finishx = x;	
                            finishy = y;
			current.setType(1);	}  //set node yg d klik droid hijau
			break;}
			default:
			if(current.getType() != 0 && current.getType() != 1)
                            current.setType(tool);
			break;}
		Update();} 
                catch(Exception z) {}	
		}
	@Override
	public void mouseReleased(MouseEvent e) {}
	}
	
class Algorithm {	//kelas algoritma pencarian
    public void AStar() {
	ArrayList<Node> priority = new ArrayList<Node>();
	priority.add(map[startx][starty]);
            while(solving) {
            if(priority.size() <= 0) {
		solving = false;
		break;}
            int hops = priority.get(0).getHops()+1;
	ArrayList<Node> explored = exploreNeighbors(priority.get(0),hops);
            if(explored.size() > 0) {
	priority.remove(0);
        priority.addAll(explored);
	Update();
	delay();} 
            else {
		priority.remove(0);}
		sortQue(priority);} //urutkan antrian prioritas
		}
    public ArrayList<Node> sortQue(ArrayList<Node> sort) {	
	int c = 0;
            while(c < sort.size()) {
	int sm = c;
            for(int i = c+1; i < sort.size(); i++) {
                if(sort.get(i).getEuclidDist()+sort.get(i).getHops() < sort.get(sm).getEuclidDist()+sort.get(sm).getHops())
                    sm = i;}
                if(c != sm) {
		Node temp = sort.get(c);
		sort.set(c, sort.get(sm));
		sort.set(sm, temp);}	
		c++;}
	return sort;}
		
    public ArrayList<Node> exploreNeighbors(Node current, int hops) {	//periksa tetangga
	ArrayList<Node> explored = new ArrayList<Node>();	//list node yang udh dikunjung
	for(int a = -1; a <= 1; a++) {
            for(int b = -1; b <= 1; b++) {
		int xbound = current.getX()+a;
		int ybound = current.getY()+b;
            if((xbound > -1 && xbound < cells) && (ybound > -1 && ybound < cells)) {	//pastikan node diluar grid
		Node neighbor = map[xbound][ybound];
            if((neighbor.getHops()==-1 || neighbor.getHops() > hops) && neighbor.getType()!=2) {	//cek node bukan tembok dn belum dikunjungi
		explore(neighbor, current.getX(), current.getY(), hops);	//
		explored.add(neighbor);} //tambahkan node ke list yg dikunjungi
					}}
			}
            return explored;}
		
    public void explore(Node current, int lastx, int lasty, int hops) {	//periksa node
	if(current.getType()!=0 && current.getType() != 1)	
            current.setType(4);	//jadikan sudah di kunjungi
            current.setLastNode(lastx, lasty);	
            current.setHops(hops);	//set hops dari start droid

			if(current.getType() == 1) {	//backtrack untuk dpt jalan
				backtrack(current.getLastX(), current.getLastY(),hops);}
		}
		
    public void backtrack(int lx, int ly, int hops) {	//backtrack
	while(hops > 1) {	
            Node current = map[lx][ly];
            current.setType(5);
            lx = current.getLastX();
            ly = current.getLastY();
            hops--;}
	solving = false;}
	
}
	
class Node {
    private int cellType = 0;
    private int hops;
    private int x;
    private int y;
    private int lastX;
    private int lastY;
    private double dToEnd = 0;
	
    public Node(int type, int x, int y) {	//constructor
	cellType = type;
	this.x = x;
	this.y = y;
	hops = -1;}
		
    public double getEuclidDist() {		//hitung jarak euclidis ke droid hijau
	int xdif = Math.abs(x-finishx);
	int ydif = Math.abs(y-finishy);
	dToEnd = Math.sqrt((xdif*xdif)+(ydif*ydif));
	return dToEnd;}
		
    public int getX() {return x;}		//buat methods
    public int getY() {return y;}
    public int getLastX() {return lastX;}
    public int getLastY() {return lastY;}
    public int getType() {return cellType;}
    public int getHops() {return hops;}
    public void setType(int type) {cellType = type;}		//set methods
    public void setLastNode(int x, int y) {lastX = x; lastY = y;}
    public void setHops(int hops) {this.hops = hops;}
	}
}
// 0 = start, 1 = finish, 2 = wall, 3 = empty, 4 = checked, 5 = finalpath