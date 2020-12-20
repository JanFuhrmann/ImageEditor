import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Gesichterkennung extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private BufferedImage bi = null;
    private BufferedImage bi2 = null;
    private BufferedImage bildOriginal = null;
    private JPanel panel = new JPanel() {
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    zeichneBild(g);
	}
    };
    private int aktuellehoehe = 0;
    private int aktuellebreite = 0;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(() -> {
	try {
		Gesichterkennung frame = new Gesichterkennung();
		frame.setVisible(true);
	} catch (Exception e) {
		e.printStackTrace();
	}
	});
    }

    /**
     * Create the frame.
     */
    public Gesichterkennung() {
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 664, 643);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	contentPane.setLayout(null);

	textField = new JTextField();
	textField.setColumns(10);
	textField.setBounds(-56, -230, 348, 22);
	contentPane.add(textField);

	JButton button = new JButton("W\u00E4hlen");
	button.setFont(new Font("Tahoma", Font.PLAIN, 18));
	button.setBounds(304, -233, 138, 25);
	contentPane.add(button);

	textField_1 = new JTextField();
	textField_1.setColumns(10);
	textField_1.setBounds(12, 11, 348, 22);
	contentPane.add(textField_1);

	JButton button_1 = new JButton("W\u00E4hlen");
	button_1.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		holeBild();
	    }
	});
	button_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
	button_1.setBounds(372, 8, 138, 25);
	contentPane.add(button_1);

	panel.setBounds(12, 46, 500, 500);
	contentPane.add(panel);

	JButton btnErkennen = new JButton("Erkennen");
	btnErkennen.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		int anzahlx = bildOriginal.getWidth();
		int anzahly = bildOriginal.getHeight();
		int r = 0, g = 0, b = 0;
		Color[][] rgb = new Color[anzahlx][anzahly];
		bi = new BufferedImage(anzahlx, anzahly,
			BufferedImage.TYPE_INT_ARGB);
		Graphics2D g_bi = bi.createGraphics();
		for (int xwert = 0; xwert < anzahlx; xwert++) {
		    for (int ywert = 0; ywert < anzahly; ywert++) {
			int ro = 0, gr = 0, bl = 0, pxlColor = 0;
			pxlColor = bildOriginal.getRGB(xwert + (int) (Math.random()),
				ywert + (int) (Math.random()));
			Color c = new Color(pxlColor);
			bl += c.getBlue();
			gr += c.getGreen();
			ro += c.getRed();
			if (Math.abs(r - ro) > 5 && Math.abs(g - gr) > 5
				&& Math.abs(b - bl) > 5) {
			    rgb[xwert][ywert] = Color.BLACK;
			} else {
			    rgb[xwert][ywert] = Color.WHITE;
			}
			r = ro;
			b = bl;
			g = gr;
			g_bi.setColor(rgb[xwert][ywert]);
			g_bi.fillRect(xwert, ywert, 1, 1);
		    }
		}
		panel.repaint();
	    }
	});
	btnErkennen.setBounds(524, 50, 97, 25);
	contentPane.add(btnErkennen);

	JButton btnSpeichern = new JButton("Speichern");
	btnSpeichern.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// Speichere Bild
		if (bildOriginal != null) {
		    try {
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(null);
			fc.showSaveDialog(null);
			File f = fc.getSelectedFile();
			System.out.println("Speichern als "
				+ f.getAbsolutePath());
			ImageIO.write(bi, "png", f);
			System.out
				.println("Bild wurde erfolgreich gespeichert als "
					+ f.getAbsolutePath());
			JOptionPane.showMessageDialog(null,
				"Bild wurde erfolgreich gespeichert!",
				"Erfolgreich", JOptionPane.PLAIN_MESSAGE);
		    } catch (Exception ex) {
			System.out
				.println("Problem beim Speichern aufgetreten!");
			JOptionPane.showMessageDialog(null,
				"Problem beim Speichern aufgetreten!",
				"Fehler", JOptionPane.PLAIN_MESSAGE);
		    }
		}
	    }
	});
	btnSpeichern.setBounds(524, 10, 97, 25);
	contentPane.add(btnSpeichern);
    }

    protected void holeBild() {
	boolean bildfehler = false;
	if ("".equals(textField_1.getText())) { // Wenn Textfeld leer
	    try {
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(null);
		fc.showOpenDialog(null);
		File f = fc.getSelectedFile();
		textField_1.setText(f.getAbsolutePath());
	    } catch (Exception e) {
		System.out.println("Es wurde keine Datei ausgewählt!");
		JOptionPane.showMessageDialog(null,
			"Es wurde nichts ausgewählt!", "Fehler",
			JOptionPane.PLAIN_MESSAGE);
		bildfehler = true;
	    }
	}
	String pfad = textField_1.getText();
	if (pfad != null && !bildfehler) {
	    try {
		bi = ImageIO.read(new File(pfad));
		bildOriginal = bi; // damit das Original noch da ist
		panel.repaint();
	    } catch (Exception e) {
		System.out.println("Fehler aufgetreten beim Lesen der Datei");
		JOptionPane.showMessageDialog(null,
			"Die Datei konnte nicht gelesen werden!" + e, "Fehler",
			JOptionPane.PLAIN_MESSAGE);
	    }
	}
    }

    public void zeichneBild(Graphics g) {
	// Zeichne das Bild
	if (bi != null) {
	    int w = panel.getWidth();
	    int hoeheneu = bi.getHeight() * w / bi.getWidth();
	    aktuellehoehe = hoeheneu;
	    aktuellebreite = w;
	    g.drawImage(bi, 0, 0, w, hoeheneu, this);
	}
    }
}
