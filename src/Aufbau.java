import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSlider;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Aufbau extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane = new JPanel();
    private JTextField textField = new JTextField();
    private JSlider slider = new JSlider();
    private JLabel lblFilterstrke = new JLabel("Filterstärke:");
    private JButton btnFertig = new JButton("Fertig");
    private JPanel panel_1 = new JPanel();
    private JButton btnFilterAufGanzes = new JButton(
	    "Filter auf ganzes Bild anwenden");
    private JPanel panel = new JPanel() {
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    zeichneBild(g);
	}
    };
    private JCheckBox chckbxRadierer = new JCheckBox("Radierer");
    private JComboBox<String> comboBox = new JComboBox<String>();
    private JCheckBox chckbxSpeichern = new JCheckBox("Speichern");
    private JButton btnBildSpeichern = new JButton("Bild speichern");
    private JButton btnWhlen = new JButton("Wählen");
    private JLabel lblDatei = new JLabel("Datei: ");
    private JButton btnRckgngig = new JButton("\u25C4");
    private JButton btnFarbe = new JButton("Farbe");

    private BufferedImage bi = null;
    private BufferedImage bildOriginal = null; // Originalbild, falls bi schon
    // bearbeitet wurde
    private BufferedImage bispeicher = null;
    private ArrayList<BufferedImage> lastPic = new ArrayList<BufferedImage>();
    private ArrayList<BufferedImage> newPic = new ArrayList<BufferedImage>();
    private int aktuellehoehe = 0, aktuellebreite = 0, xStart = 0, yStart = 0,
	    xEnde = 0, yEnde = 0, xEnde2 = 0, yEnde2 = 0, xStart2 = 0,
	    yStart2 = 0, groesse = 0;
    private boolean radieren = false;
    private boolean malen = false;
    private boolean start = false;
    private boolean gestartet = false;
    private boolean bildspeichern = false;
    private String auswahl;
    private int[] f = new int[3];
    private final JButton button = new JButton("\u25BA");

    private int fx = 0;
    private int fy = 0;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    Aufbau frame = new Aufbau();
		    frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the frame.
     */
    public Aufbau() {
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 1411, 905);
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	contentPane.setLayout(null);

	lblDatei.setFont(new Font("Tahoma", Font.PLAIN, 18));
	lblDatei.setBounds(12, 13, 56, 16);
	contentPane.add(lblDatei);

	textField.setBounds(80, 12, 348, 22);
	contentPane.add(textField);
	textField.setColumns(10);
	btnWhlen.setFont(new Font("Tahoma", Font.PLAIN, 18));
	btnWhlen.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		holeBild();
	    }
	});

	btnWhlen.setBounds(440, 9, 138, 25);
	contentPane.add(btnWhlen);
	btnBildSpeichern.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		savePicture();
	    }
	});

	btnBildSpeichern.setBounds(868, 10, 127, 25);
	contentPane.add(btnBildSpeichern);
	chckbxSpeichern.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		bildspeichern = !bildspeichern;
	    }
	});
	chckbxSpeichern.setFont(new Font("Tahoma", Font.PLAIN, 14));

	chckbxSpeichern.setToolTipText("Effekte bleiben gespeichert");
	chckbxSpeichern.setBounds(1107, 9, 97, 25);
	contentPane.add(chckbxSpeichern);
	comboBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
	comboBox.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		holeAuswahl();
	    }
	});

	comboBox.setBounds(973, 61, 266, 22);
	comboBox.addItem("Filterauswahl");
	comboBox.addItem("SW");
	comboBox.addItem("Unscharf");
	comboBox.addItem("Invertieren");
	comboBox.addItem("Grau");
	comboBox.addItem("Warm");
	comboBox.addItem("Punkt");
	comboBox.addItem("Horror");
	comboBox.addItem("Zeichnung");
	contentPane.add(comboBox);
	chckbxRadierer.setFont(new Font("Tahoma", Font.PLAIN, 20));
	chckbxRadierer.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		radieren();
	    }
	});

	chckbxRadierer.setBounds(1045, 119, 127, 33);
	contentPane.add(chckbxRadierer);
	panel.addMouseMotionListener(new MouseMotionAdapter() {
	    @Override
	    public void mouseDragged(MouseEvent e) {
		mausDragged(e);
	    }
	});
	panel.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
		mausReleased(e);
	    }

	    @Override
	    public void mousePressed(MouseEvent e) {
		lastPic.add(bi);
		newPic.clear();
	    }

	    @Override
	    public void mouseClicked(MouseEvent e) {
		mausClicked(e);
	    }
	});

	panel.setBounds(22, 42, 805, 805);
	contentPane.add(panel);
	btnFilterAufGanzes.setFont(new Font("Tahoma", Font.PLAIN, 18));
	btnFilterAufGanzes.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		onWholePic();
	    }
	});

	btnFilterAufGanzes.setBounds(973, 761, 288, 59);
	contentPane.add(btnFilterAufGanzes);
	btnFertig.setFont(new Font("Tahoma", Font.PLAIN, 20));
	btnFertig.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		punktAuswahl();
	    }
	});

	btnFertig.setBounds(1045, 535, 127, 42);
	btnFertig.setVisible(false);
	contentPane.add(btnFertig);

	panel_1.setBounds(961, 194, 300, 300);
	contentPane.add(panel_1);

	lblFilterstrke.setFont(new Font("Tahoma", Font.PLAIN, 20));
	lblFilterstrke.setVisible(false);
	lblFilterstrke.setBounds(1056, 617, 154, 16);
	contentPane.add(lblFilterstrke);
	slider.addMouseMotionListener(new MouseMotionAdapter() {
	    @Override
	    public void mouseDragged(MouseEvent e) {
		sliderDragged();
	    }
	});

	slider.setBounds(973, 633, 288, 66);
	slider.setVisible(false);
	contentPane.add(slider);
	btnRckgngig.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (!newPic.contains(bi)) {
		    newPic.add(bi);
		}
		if (lastPic.size() - 1 > 0) {
		    bi = lastPic.get(lastPic.size() - 1);
		    lastPic.remove(lastPic.size() - 1);
		} else {
		    bi = bildOriginal;
		}
		panel.repaint();
	    }
	});
	btnRckgngig.setBounds(1257, 10, 56, 25);
	contentPane.add(btnRckgngig);

	button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (newPic.size() - 1 > 0) {
		    lastPic.add(newPic.get(newPic.size() - 1));
		    bi = lastPic.get(lastPic.size() - 1);
		    panel.repaint();
		    newPic.remove(newPic.size() - 1);
		} else {
		    bi = newPic.get(newPic.size() - 1);
		    panel.repaint();
		}
	    }
	});
	button.setBounds(1325, 10, 56, 25);

	contentPane.add(button);

	JCheckBox chckbxMalen = new JCheckBox("Malen");
	chckbxMalen.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		malen();
	    }
	});
	chckbxMalen.setFont(new Font("Tahoma", Font.PLAIN, 20));
	chckbxMalen.setBounds(1045, 157, 113, 25);
	contentPane.add(chckbxMalen);
	btnFarbe.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		panel_1.setVisible(true);
		panel_1.setBackground(JColorChooser.showDialog(null,
			"Farbauswahl", null));
	    }
	});

	btnFarbe.setBounds(1166, 156, 97, 25);
	btnFarbe.setVisible(false);
	contentPane.add(btnFarbe);
	
	JButton btnRahmen = new JButton("Rahmen");
	btnRahmen.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		    Rahmen r = new Rahmen();
		    r.setVisible(true);
		}
	});
	btnRahmen.setBounds(759, 11, 97, 25);
	contentPane.add(btnRahmen);

    }

    protected void onWholePic() {
	// Filter auf ganzes Bild anwenden
	if (bi != null) {
	    xStart = 0;
	    yStart = 0;
	    xEnde = bi.getWidth();
	    yEnde = bi.getHeight();
	    sw();
	}
    }

    protected void savePicture() {
	// Speichere Bild
	if (bildOriginal != null) {
	    try {
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(null);
		fc.showSaveDialog(this);
		File f = fc.getSelectedFile();
		System.out.println("Speichern als " + f.getAbsolutePath());
		ImageIO.write(bi, "png", f);
		System.out.println("Bild wurde erfolgreich gespeichert als "
			+ f.getAbsolutePath());
		JOptionPane.showMessageDialog(null,
			"Bild wurde erfolgreich gespeichert!", "Erfolgreich",
			JOptionPane.PLAIN_MESSAGE);
	    } catch (Exception ex) {
		System.out.println("Problem beim Speichern aufgetreten!");
		JOptionPane.showMessageDialog(null,
			"Problem beim Speichern aufgetreten!", "Fehler",
			JOptionPane.PLAIN_MESSAGE);
	    }
	}
    }

    protected void sliderDragged() {
	// Wenn Slider bewegt
	if (gestartet == false && radieren == false) { // Wenn etwas ausgewählt
	    sw();
	} else if (gestartet == true && radieren == false) {
	    sw();
	}
    }

    protected void punktAuswahl() {
	// Wähle Punkt aus
	if (auswahl == "Punkt") {
	    gestartet = !gestartet;
	    if (bildspeichern == true) {
		bispeicher = bi;
	    }
	    sw();
	}
    }

    protected void holeAuswahl() {
	// Hole Auswahl
	auswahl = (String) comboBox.getSelectedItem();
	if (auswahl == "SW") {
	    lblFilterstrke.setVisible(true);
	    lblFilterstrke.setText("SW -Stärke:");
	    slider.setVisible(true);
	} else if (auswahl == "Unscharf" || auswahl == "Warm"
		|| auswahl == "Punkt") {
	    lblFilterstrke.setVisible(true);
	    lblFilterstrke.setText("Filterstärke:");
	    slider.setVisible(true);
	    if (auswahl == "Punkt") {
		btnFertig.setVisible(true);
	    }
	} else {
	    lblFilterstrke.setVisible(false);
	    slider.setVisible(false);
	}
    }

    protected void mausDragged(MouseEvent evt) {
	// Maus gezogen
	int anzahlx = bi.getWidth();
	int anzahly = bi.getHeight();
	float faktora = (float) aktuellebreite / anzahlx;
	float faktorb = (float) aktuellehoehe / anzahly;
	int x = (int) (evt.getX() / faktora);
	int y = (int) (evt.getY() / faktorb);
	if (malen) {
	    Graphics2D g_bi = bi.createGraphics();
	    g_bi.setColor(panel_1.getBackground());
	    g_bi.fillRect(x, y, 1, 1);
	    panel.repaint();
	}
	if (radieren) {
	    groesse = slider.getValue() / 5; // Groesse des Radierers
	    Graphics2D g_bi = bi.createGraphics();
	    Color[][] rgb = new Color[anzahlx][anzahly];
	    for (int i = 0; i < groesse; i++) {
		for (int j = 0; j < groesse; j++) {
		    if (x - i + (groesse / 2) < anzahlx
			    && y - j + (groesse / 2) < anzahly
			    && x - i + (groesse / 2) > -1
			    && y - j + (groesse / 2) > -1) {
			int co = bildOriginal.getRGB(x - i + (groesse / 2), y
				- j + (groesse / 2));
			Color c = new Color(co);
			int b = c.getBlue();
			int g = c.getGreen();
			int r = c.getRed();
			rgb[x - i + (groesse / 2)][y - j + (groesse / 2)] = new Color(
				r, g, b);
			g_bi.setColor(rgb[x - i + (groesse / 2)][y - j
				+ (groesse / 2)]);
			g_bi.fillRect(x - i + (groesse / 2), y - j
				+ (groesse / 2), 1, 1);
		    }
		}
	    }
	    panel.repaint();
	} else if (auswahl != "Punkt") {
	    // Hole Anfangs- sowie Endpunkt der Auswahl
	    if (!start) {
		xStart = x;
		yStart = y;
		xStart2 = x;
		yStart2 = y;
		start = true;
		if (bildspeichern == true) {
		    bispeicher = bi;
		}
	    } else {
		xEnde2 = x;
		yEnde2 = y;
		xEnde = x;
		yEnde = y;
		if (xEnde2 < xStart2) {
		    xEnde = xStart2;
		    xStart = xEnde2;
		}
		if (yEnde2 < yStart2) {
		    yEnde = yStart2;
		    yStart = yEnde2;
		}
		sw();
	    }
	}
    }

    protected void mausClicked(MouseEvent evt) {
	// Maus geklickt
	if (auswahl == "Punkt") {
	    float faktora = (float) aktuellebreite / bi.getWidth();
	    float faktorb = (float) aktuellehoehe / bi.getHeight();
	    int x = (int) (evt.getX() / faktora);
	    int y = (int) (evt.getY() / faktorb);
	    int r = 0;
	    int b = 0;
	    int g = 0;
	    int co = 0;
	    for (int i = 0; i < 10; i++) {
		for (int j = 0; j < 10; j++) {
		    if (bildspeichern == true) {
			co = bispeicher.getRGB(x, y);
		    } else {
			co = bi.getRGB(x, y);
		    }
		    Color c = new Color(co);
		    b += c.getBlue();
		    g += c.getGreen();
		    r += c.getRed();
		}
	    }
	    f[0] = r / 100;
	    f[1] = g / 100;
	    f[2] = b / 100;
	    System.out.println(f[0] + "," + f[1] + "," + f[2]);
	    // Punkt Vorschau
	    panel_1.setVisible(true);
	    panel_1.setBackground(new Color(f[0], f[1], f[2]));

	}
    }

    protected void mausReleased(MouseEvent evt) {
	// Wenn Maus losgelassen
	start = false;
	if (bildspeichern == true) {
	    bispeicher = bi;
	}
    }

    protected void radieren() {
	// Radieren-Checkbutton gedrückt
	radieren = !radieren; // Radieren An/Aus
	lblFilterstrke.setText("Radiergrösse:");
	lblFilterstrke.setVisible(radieren);
	slider.setVisible(radieren);
    }

    protected void malen() {
	// Malen-Checkbutton gedrückt
	malen = !malen; // Malen An/Aus
	if (malen) {
	    JOptionPane.showMessageDialog(null,
		    "Diese Funktion befindet sich noch in der Beta-Phase!",
		    "Hinweis", JOptionPane.PLAIN_MESSAGE);
	    btnFarbe.setVisible(true);
	} else {
	    btnFarbe.setVisible(false);
	    panel_1.setVisible(false);
	}
    }

    protected void holeBild() {
	boolean bildfehler = false;
	if ("".equals(textField.getText())) { // Wenn Textfeld leer
	    try {
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(null);
		fc.showOpenDialog(null);
		File f = fc.getSelectedFile();
		textField.setText(f.getAbsolutePath());
	    } catch (Exception e) {
		System.out.println("Es wurde keine Datei ausgewählt!");
		JOptionPane.showMessageDialog(null,
			"Es wurde nichts ausgewählt!", "Fehler",
			JOptionPane.PLAIN_MESSAGE);
		bildfehler = true;
	    }
	}
	String pfad = textField.getText();
	if (pfad != null && !bildfehler) {
	    try {
		bi = ImageIO.read(new File(pfad));
		bildOriginal = bi; // damit das Original noch da ist
		bispeicher = bi;
		lastPic.clear();
		newPic.clear();
		panel.repaint();
	    } catch (Exception e) {
		System.out.println("Fehler aufgetreten beim Lesen der Datei");
		JOptionPane.showMessageDialog(null,
			"Die Datei konnte nicht gelesen werden!" + e, "Fehler",
			JOptionPane.PLAIN_MESSAGE);
	    }
	}
    }

    public void sw() {
	if (bildOriginal != null) {
	    int rot = 0, blau = 0, gruen = 0;
	    int anzahlx = bildOriginal.getWidth();
	    int anzahly = bildOriginal.getHeight();
	    Color[][] rgb = new Color[anzahlx][anzahly];
	    bi = new BufferedImage(anzahlx, anzahly,
		    BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g_bi = bi.createGraphics();
	    for (int xwert = 0; xwert < anzahlx; xwert++) {
		for (int ywert = 0; ywert < anzahly; ywert++) {
		    int ro = 0, gr = 0, bl = 0, r = 0, co = 0;
		    if (bildspeichern == true) {
			co = bispeicher.getRGB(xwert + (int) (Math.random()),
				ywert + (int) (Math.random()));
		    } else {
			co = bildOriginal.getRGB(xwert + (int) (Math.random()),
				ywert + (int) (Math.random()));
		    }
		    Color c = new Color(co);
		    bl += c.getBlue();
		    gr += c.getGreen();
		    ro += c.getRed();
		    int wert = slider.getValue();
		    if (auswahl == "Punkt" && ro <= wert + f[0]
			    && ro >= f[0] - wert && gr <= wert + f[1]
			    && gr >= f[1] - wert && bl <= wert + f[2]
			    && bl >= f[2] - wert) {
			// rgb[xwert][ywert] = new Color(f[0], f[1], f[2]);
			rgb[xwert][ywert] = new Color(ro, gr, bl);
		    } else if (auswahl == "SW" && xwert >= xStart
			    && ywert >= yStart && xwert <= xEnde
			    && ywert <= yEnde) {
			r = (bl + gr + ro) / 3;
			if (r > wert) {
			    rgb[xwert][ywert] = new Color(255, 255, 255);
			} else {
			    rgb[xwert][ywert] = new Color(0, 0, 0);
			}
		    } else if ((auswahl == "Unscharf" || auswahl == "Horror")
			    && xwert >= xStart && ywert >= yStart
			    && xwert <= xEnde && ywert <= yEnde) {
			wert = wert / 10 + 1;
			int dr = 0;
			int dg = 0;
			int db = 0;
			int co2 = 0;
			for (int i = 0; i < wert; i++) {
			    for (int j = 0; j < wert; j++) {
				if (xwert + i < anzahlx && ywert + j < anzahly) { // Wenn
										  // nicht
										  // außerhalb
										  // des
										  // Bereichs
				    if (bildspeichern == true) {
					co2 = bispeicher.getRGB(xwert + i,
						ywert + j);
				    } else {
					co2 = bildOriginal.getRGB(xwert + i,
						ywert + j);
				    }
				}
				Color c2 = new Color(co2);
				dr += c2.getRed();
				dg += c2.getGreen();
				db += c2.getBlue();
			    }
			}
			dr = (int) dr / (wert * wert);
			dg = (int) dg / (wert * wert);
			db = (int) db / (wert * wert);
			if (auswahl == "Unscharf") {
			    rgb[xwert][ywert] = new Color(dr, dg, db);
			} else {
			    if (ro - dr > 0) {
				ro -= dr;
			    }
			    if (gr - dg > 0) {
				gr -= dg;
			    }
			    if (bl - db > 0) {
				bl -= db;
			    }
			    rgb[xwert][ywert] = new Color(ro, gr, bl);
			}
		    } else if (auswahl == "Invertieren" && xwert >= xStart
			    && ywert >= yStart && xwert <= xEnde
			    && ywert <= yEnde) {
			bl = 255 - c.getBlue();
			gr = 255 - c.getGreen();
			ro = 255 - c.getRed();
			rgb[xwert][ywert] = new Color(ro, gr, bl);
		    } else if ((auswahl == "Grau" && xwert >= xStart
			    && ywert >= yStart && xwert <= xEnde && ywert <= yEnde)
			    || auswahl == "Punkt") {
			int grau = (int) (ro * 0.299 + gr * 0.587 + bl * 0.114);
			rgb[xwert][ywert] = new Color(grau, grau, grau);
		    } else if (auswahl == "Warm" && xwert >= xStart
			    && ywert >= yStart && xwert <= xEnde
			    && ywert <= yEnde) {
			if (ro + (wert / 2) < 256) {
			    ro += (wert / 2);
			}
			if (gr + (wert / 2) < 256) {
			    gr += (wert / 2);
			}
			rgb[xwert][ywert] = new Color(ro, gr, bl);
		    } else if (auswahl == "Zeichnung" && xwert >= xStart
			    && ywert >= yStart && xwert <= xEnde
			    && ywert <= yEnde) {
			if (Math.abs(rot - ro) > 5 && Math.abs(gruen - gr) > 5
				&& Math.abs(blau - bl) > 5) {
			    rgb[xwert][ywert] = Color.BLACK;
			} else {
			    rgb[xwert][ywert] = Color.WHITE;
			}
			rot = ro;
			blau = bl;
			gruen = gr;
		    } else {
			rgb[xwert][ywert] = new Color(ro, gr, bl);
		    }
		    g_bi.setColor(rgb[xwert][ywert]);
		    g_bi.fillRect(xwert, ywert, 1, 1);
		}
	    }
	    panel.repaint(); // zeichne Bild
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
