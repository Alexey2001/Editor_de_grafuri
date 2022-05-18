
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class View extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    Desen drawing;
    Graf graph;
    JMenuBar menuBar;
    JMenu graphMenu;
    JMenuItem edge, vertex, drag, delete;

    public View(String title) { //partea de vizualizare a optiunilor pe care le avem

        this.setTitle(title);
        this.graph = new Graf();
        this.drawing = new Desen();

        menuBar = new JMenuBar();
        edge = new JMenuItem("Adaugare linie.");
        vertex = new JMenuItem("Adaugare varf.");
        drag = new JMenuItem("Trage varfurile.");
        delete = new JMenuItem("Sterge varfurile.");

        graphMenu = new JMenu("MENIU");
        graphMenu.setLayout(new GridLayout(0, 2));
        graphMenu.add(vertex);
        graphMenu.add(edge);
        graphMenu.add(drag);
        graphMenu.add(delete);
        menuBar.add(graphMenu);

        edge.addActionListener(this);
        vertex.addActionListener(this);
        drag.addActionListener(this);
        delete.addActionListener(this);

        this.add(menuBar, BorderLayout.NORTH);

        drawing.setVisible(true);
        this.add(drawing);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        drawing.setSize(width, height);

        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize((int) (width - width / 5), (int) (height - height / 10));
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) { //metoda de actionare a alegerii
        if (e.getSource() == this.vertex) {
            drawing.setAction("Varf");
        } 
        else if (e.getSource() == this.edge) {
            drawing.setAction("Conectare");
        } 
        else if (e.getSource() == this.drag) {
            drawing.setAction("Trage");
        }
        else if (e.getSource() == this.delete) {
            drawing.setAction("Stergere");
        }
    }

}
