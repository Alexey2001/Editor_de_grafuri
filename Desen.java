package editor_grafuri;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serial;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public class Desen extends JPanel implements MouseListener, MouseMotionListener {

    @Serial
    private static final long serialVersionUID = 1L;
    private String action;
    private String information;
    private final Graf graph;
    private boolean selectedEdge;
    private String currentVertex;

    public Desen() { //constructor

        this.graph = new Graf();
        this.selectedEdge = false;
        this.currentVertex = "";
        this.setAction("Varf");
        this.setDoubleBuffered(true);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }


    public void mouseClicked(MouseEvent e) { //evenimentul care se intampla cand facem click
        switch (action) {
            case "Varf":
                String vertexKey;
                vertexKey = JOptionPane.showInputDialog("Adauga un nou varf");
                // varfurile nu pot avea cheia nula
                if (vertexKey != null && !vertexKey.equals("")) {
                    // varfurile cu aceeasi cheie exista deja
                    if (graph.addVertex(vertexKey, e.getX(), e.getY())) {
                        JOptionPane.showMessageDialog(null, "Varful " + vertexKey + " exista deja!", "Eroare!", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case "Conectare": //optiunea de conectare
                String selectedVertexKey = this.searchByCoordinates(e.getX(), e.getY());
                if (selectedVertexKey != null) {
                    // selectati 2 varfuri pentru o muchie
                    if (!selectedEdge) {
                        this.currentVertex = selectedVertexKey;
                        this.selectedEdge = true;
                    } else {
                        this.repaint();
                        String edgeKey = JOptionPane.showInputDialog("Adauga o noua linie.");
                        if (edgeKey != null) {
                            this.graph.addEdge(this.currentVertex, selectedVertexKey, edgeKey, e.getX(), e.getY());
                        }
                        this.resetVerticesColors();
                        this.currentVertex = "";
                        this.selectedEdge = false;
                    }
                }
                break;
            default:
                if ("Trage".equals(action)) {
                } 
                else {
                    if ("Sterge".equals(action)) {
                        String deletedVertexKey = this.searchByCoordinates(e.getX(), e.getY());
                        if (deletedVertexKey != null) {
                            this.repaint();
                            int input = JOptionPane.showConfirmDialog(null, "Doriti sa stergeti varful " + deletedVertexKey + "?");
                            if (input == 0) {
                                this.graph.deleteVertex(deletedVertexKey);
                            } 
                            else {
                                this.resetVerticesColors();
                            }
                        }
                    }
                }
                break;
        }
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {// partea de desenare

        super.paint(g);
        Graphics2D dosD = (Graphics2D) g;

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        dosD.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        this.drawVertices(g);

        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.setColor(BLACK);
        g.drawString(this.action, 10, 25);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString(information, 10, 40);
    }

    //afisati informatiile in functie de actiunea selectata si actualizati actiunea curenta
    public void setAction(String action) {
        this.action = action;
        if ("Varf".equals(action)) {
            this.resetVerticesColors();
            this.information = "Apasati pe ecran pentru a adauga varfuri.";
        } else if ("Conectare".equals(action)) {
            this.resetVerticesColors();
            this.information = "Apasati pe varfuri pentru a le conecta.";
        } else if ("Trage".equals(action)) {
            this.information = "Apasati pe un varf si trageti-l pe ecran.";
        } else if ("Sterge".equals(action)) {
            this.information = "Apasati pe un varf pentru a-l sterge.";
        }
        this.repaint();
    }

    public void mouseReleased(MouseEvent event) { //actiunea de tragere a grafului/ noduri
        if (this.action.equals("Trage")) {
            this.resetVerticesColors();
            this.currentVertex = "";
        }
    }

    // cautati varful dupa coordonate si actualizati-le
    public void mouseDragged(MouseEvent event) {

        if (this.action.equals("Trage")) {
            if (this.currentVertex.equals("")) {
                String foundVertexKey = this.searchByCoordinates(event.getX(), event.getY());
                if (foundVertexKey == null) {
                    return;
                }
                this.currentVertex = foundVertexKey;
            }
            this.updateCoordinates(this.currentVertex, event.getX(), event.getY());
            this.repaint();
        }
    }

    public void drawVertices(Graphics graphics) { //desenarea propriu zisa a varfurilor

        Vertex vertex = this.graph.getHead();
        if (vertex == null) {
            return;
        }
        this.drawEdges(graphics);
        while (vertex != null) {
            graphics.setColor(vertex.getColor());
            graphics.fillOval(vertex.getxCoord(), vertex.getyCoord(), vertex.getzCoord(), vertex.getzCoord());
            graphics.setColor(BLACK);
            graphics.drawString(vertex.getKey(), vertex.getxCoord(), vertex.getyCoord() - 10);
            vertex = vertex.getNextVertex();
        }
    }

    private void drawEdges(Graphics graphics) { //desenarea muchiilor

        Vertex head = this.graph.getHead();
        if (head == null) {
            return;
        }
        Vertex vertex;
        graphics.setColor(BLACK);
        while (head != null) {
            vertex = head.getAdjacentVertex();
            while (vertex != null && graph.getVertexMap().containsKey(vertex.getKey())) {
                graphics.drawString(vertex.getEdgeKey(), this.getAverageDistance(head.getxCoord(), vertex.getxCoord()), this.getAverageDistance(head.getyCoord(), vertex.getyCoord()));
                graphics.drawLine(vertex.getxCoord(), vertex.getyCoord(), head.getxCoord() + (this.graph.getSIZE() / 2), head.getyCoord() + (this.graph.getSIZE() / 2));
                vertex = vertex.getNextVertex();
            }
            head = head.getNextVertex();
        }
    }

    // cauta un varf dupa coordonatele sale si returneaza cheia acestuia daca este gasita
    public String searchByCoordinates(int xCoordinate, int yCoordinate) {
        Vertex head = this.graph.getHead();
        if (head == null) {
            return null;
        }
        if (xCoordinate > head.getxCoord() && xCoordinate < head.getxCoord() + this.graph.getSIZE() && yCoordinate > head.getyCoord() && yCoordinate < head.getyCoord() + this.graph.getSIZE()) {
            head.setColor(Color.RED);
            return head.getKey();
        } else {
            head = head.getNextVertex();
        }
        while (head != null) {
            if (xCoordinate > head.getxCoord() && xCoordinate < head.getxCoord() + this.graph.getSIZE() && yCoordinate > head.getyCoord() && yCoordinate < head.getyCoord() + this.graph.getSIZE()) {
                head.setColor(Color.RED);
                return head.getKey();
            } else {
                head = head.getNextVertex();
            }
        }
        return null;
    }

    public void resetVerticesColors() { //reseteaza culoarea nodurilor

        Vertex head = this.graph.getHead();
        if (head == null) {
            return;
        }
        while (head != null) {
            head.setColor(new Color(0xd9820f));
            head = head.getNextVertex();
        }
    }

    public void resetColor(String key) { //resetarea culorii
        if (this.graph.getVertexMap().containsKey(key)) {
            Vertex node = this.graph.getVertexMap().get(key);
            node.setColor(new Color(0xd9820f));
            this.repaint();
        }
    }

    public void updateCoordinates(String key, int xCoordinate, int yCoordinate) { //modificarea coordonatelor grafului
        Vertex head = this.graph.getHead();
        while (head != null) {
            if (head.getKey().equals(key)) {
                head.setX(xCoordinate - this.graph.getSIZE() / 2);
                head.setY(yCoordinate - this.graph.getSIZE() / 2);
            }
            this.updateVertexCoordinates(head.getAdjacentVertex(), key, xCoordinate, yCoordinate);
            head = head.getNextVertex();
        }
    }

    public void updateVertexCoordinates(Vertex vertex, String key, int  xCoordinate, int yCoordinate) { //modificarea coordonatelor nodului
        while (vertex != null) {
            if (vertex.getKey().equals(key)) {
                vertex.setX( xCoordinate);
                vertex.setY(yCoordinate);
            }
            vertex = vertex.getNextVertex();
        }
    }

    public int getAverageDistance(int xPoint, int yPoint) { //distanta medie dintre 2 noduri
        return (xPoint + ((xPoint - yPoint) / 2) * -1);
    }

    public void mousePressed(MouseEvent e) { //generate de compilator
        // TODO document why this method is empty
    }

    public void mouseMoved(MouseEvent e) { //generate de compilator
        // TODO document why this method is empty
    }

    public void mouseEntered(MouseEvent e) { //generate de compilator
        // TODO document why this method is empty
    }

    public void mouseExited(MouseEvent e) { //generate de compilator
        // TODO document why this method is empty
    }


}
