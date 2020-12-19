/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author fuhrmj
 */
public class Fenster extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    BufferedImage bi = null;
    BufferedImage bildOriginal = null; // Originalbild, falls bi schon
				       // bearbeitet wurde
    BufferedImage bispeicher = null;
    BufferedImage letztesBild = null;
    int aktuellehoehe = 0, aktuellebreite = 0, xStart = 0, yStart = 0,
	    xEnde = 0, yEnde = 0, xEnde2 = 0, yEnde2 = 0, xStart2 = 0,
	    yStart2 = 0, groesse = 0;
    boolean radieren = false;
    boolean start = false;
    boolean gestartet = false;
    boolean bildspeichern = false;
    String auswahl;
    int[] f = new int[3];

    public Fenster() {
	initComponents();
	jPanel2.setVisible(false);
	jSlider2.setVisible(false);
	jButton1.setVisible(false);
    }

    private void initComponents() {

	jTextField1 = new javax.swing.JTextField();
	jButton2 = new javax.swing.JButton();
	jPanel1 = new javax.swing.JPanel() {
	    private static final long serialVersionUID = 1L;

	    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		zeichneBild(g);
	    }
	};
	jLabel1 = new javax.swing.JLabel();
	jLabel4 = new javax.swing.JLabel();
	jCheckBox1 = new javax.swing.JCheckBox();
	jComboBox1 = new javax.swing.JComboBox();
	jButton1 = new javax.swing.JButton();
	jPanel2 = new javax.swing.JPanel();
	jSlider2 = new javax.swing.JSlider();
	jButton3 = new javax.swing.JButton();
	jLabel5 = new javax.swing.JLabel();
	jButton4 = new javax.swing.JButton();
	jCheckBox2 = new javax.swing.JCheckBox();

	setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

	jTextField1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

	jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	jButton2.setText("Wählen");
	jButton2.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		jButton2ActionPerformed(evt);
	    }
	});

	jPanel1.setPreferredSize(new java.awt.Dimension(492, 492));
	jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
	    public void mouseClicked(java.awt.event.MouseEvent evt) {
		jPanel1MouseClicked(evt);
	    }

	    public void mouseReleased(java.awt.event.MouseEvent evt) {
		jPanel1MouseReleased(evt);
	    }

	    public void mousePressed(java.awt.event.MouseEvent evt) {
		jPanel1MousePressed(evt);
	    }
	});
	jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
	    public void mouseDragged(java.awt.event.MouseEvent evt) {
		jPanel1MouseDragged(evt);
	    }
	});

	javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
		jPanel1);
	jPanel1.setLayout(jPanel1Layout);
	jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
		javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0,
		Short.MAX_VALUE));
	jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
		javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0,
		Short.MAX_VALUE));

	jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	jLabel1.setText("Datei:");

	jCheckBox1.setText("Radierer");
	jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		jCheckBox1ActionPerformed(evt);
	    }
	});

	jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
		"Filterauswahl", "SW", "Unscharf", "Invertieren", "Grau",
		"Warm", "Punkt", }));
	jComboBox1.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		jComboBox1ActionPerformed(evt);
	    }
	});

	jButton1.setText("Fertig");
	jButton1.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		jButton1ActionPerformed(evt);
	    }
	});

	jPanel2.setBorder(javax.swing.BorderFactory
		.createLineBorder(new java.awt.Color(0, 0, 0)));

	javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
		jPanel2);
	jPanel2.setLayout(jPanel2Layout);
	jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(
		javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100,
		Short.MAX_VALUE));
	jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(
		javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100,
		Short.MAX_VALUE));

	jSlider2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
	    public void mouseDragged(java.awt.event.MouseEvent evt) {
		jSlider2MouseDragged(evt);
	    }
	});

	jButton3.setText("Bild speichern");
	jButton3.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		jButton3ActionPerformed(evt);
	    }
	});

	jButton4.setText("Filter auf ganzes Bild anwenden");
	jButton4.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		jButton4ActionPerformed(evt);
	    }
	});

	jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		jCheckBox2ActionPerformed(evt);
	    }
	});

	javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
		getContentPane());
	getContentPane().setLayout(layout);
	layout.setHorizontalGroup(layout
		.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		.addGroup(
			layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(
					layout.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
							layout.createSequentialGroup()
								.addComponent(
									jLabel1)
								.addGap(18, 18,
									18)
								.addComponent(
									jTextField1,
									javax.swing.GroupLayout.PREFERRED_SIZE,
									316,
									javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18,
									18)
								.addComponent(
									jButton2,
									javax.swing.GroupLayout.PREFERRED_SIZE,
									186,
									javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
									javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(
									jButton3)
								.addPreferredGap(
									javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(
									jCheckBox2)
								.addPreferredGap(
									javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(
									jLabel4,
									javax.swing.GroupLayout.PREFERRED_SIZE,
									37,
									javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(
							layout.createSequentialGroup()
								.addComponent(
									jPanel1,
									javax.swing.GroupLayout.DEFAULT_SIZE,
									537,
									Short.MAX_VALUE)
								.addGap(18, 18,
									18)
								.addGroup(
									layout.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(
											layout.createSequentialGroup()
												.addComponent(
													jComboBox1,
													javax.swing.GroupLayout.PREFERRED_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.PREFERRED_SIZE)
												.addContainerGap(
													javax.swing.GroupLayout.DEFAULT_SIZE,
													Short.MAX_VALUE))
										.addGroup(
											layout.createSequentialGroup()
												.addGroup(
													layout.createParallelGroup(
														javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
															jCheckBox1)
														.addComponent(
															jButton1)
														.addComponent(
															jPanel2,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
															jButton4)
														.addComponent(
															jSlider2,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															141,
															javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
															jLabel5,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															141,
															javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGap(0,
													0,
													Short.MAX_VALUE)))))));
	layout.setVerticalGroup(layout
		.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		.addGroup(
			javax.swing.GroupLayout.Alignment.TRAILING,
			layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(
					layout.createParallelGroup(
						javax.swing.GroupLayout.Alignment.TRAILING)
						.addGroup(
							layout.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(
									jLabel4,
									javax.swing.GroupLayout.Alignment.TRAILING,
									javax.swing.GroupLayout.PREFERRED_SIZE,
									16,
									javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(
									layout.createParallelGroup(
										javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(
											jLabel1)
										.addComponent(
											jTextField1,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(
											jButton2)
										.addComponent(
											jButton3)))
						.addComponent(jCheckBox2))
				.addGap(18, 18, 18)
				.addGroup(
					layout.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
							layout.createSequentialGroup()
								.addComponent(
									jComboBox1,
									javax.swing.GroupLayout.PREFERRED_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE,
									javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18,
									18)
								.addComponent(
									jCheckBox1)
								.addGap(18, 18,
									18)
								.addComponent(
									jButton1)
								.addGap(18, 18,
									18)
								.addComponent(
									jPanel2,
									javax.swing.GroupLayout.PREFERRED_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE,
									javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(92, 92,
									92)
								.addComponent(
									jButton4)
								.addGap(37, 37,
									37)
								.addComponent(
									jLabel5,
									javax.swing.GroupLayout.PREFERRED_SIZE,
									13,
									javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
									javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(
									jSlider2,
									javax.swing.GroupLayout.PREFERRED_SIZE,
									javax.swing.GroupLayout.DEFAULT_SIZE,
									javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(0,
									74,
									Short.MAX_VALUE))
						.addComponent(
							jPanel1,
							javax.swing.GroupLayout.DEFAULT_SIZE,
							495, Short.MAX_VALUE))
				.addContainerGap()));

	pack();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// Hole Bild
	// Hole Bild
	boolean bildfehler = false;
	if ("".equals(jTextField1.getText())) { // Wenn Textfeld leer
	    try {
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(null);
		@SuppressWarnings("unused")
		int zahl = fc.showOpenDialog(this);
		File f = fc.getSelectedFile();
		jTextField1.setText(f.getAbsolutePath());
	    } catch (Exception e) {
		System.out.println("Es wurde keine Datei ausgewählt!");
		JOptionPane.showMessageDialog(null,
			"Es wurde nichts ausgewählt!", "Fehler",
			JOptionPane.PLAIN_MESSAGE);
		bildfehler = true;
	    }
	}
	String pfad = jTextField1.getText();
	if (pfad != null && !bildfehler) {
	    try {
		bi = ImageIO.read(new File(pfad));
		bildOriginal = bi; // damit das Original noch da ist
		bispeicher = bi;
		letztesBild = bi;
		jPanel1.repaint();
	    } catch (Exception e) {
		System.out.println("Fehler aufgetreten beim Lesen der Datei");
		JOptionPane.showMessageDialog(null,
			"Die Datei konnte nicht gelesen werden!", "Fehler",
			JOptionPane.PLAIN_MESSAGE);
	    }
	}
    }// GEN-LAST:event_jButton2ActionPerformed

    private void jPanel1MouseReleased(java.awt.event.MouseEvent evt) {// Maus losgelassen
	// Wenn Maus losgelassen
	start = false;
	if (bildspeichern == true) {
	    bispeicher = bi;
	}
    }// GEN-LAST:event_jPanel1MouseReleased

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {// Radierer
	// Radieren-Checkbutton gedrückt
	radieren = !radieren; // Radieren An/Aus
	jLabel5.setText("Radiergrösse:");
	jLabel5.setVisible(radieren);
	jSlider2.setVisible(radieren);
    }// GEN-LAST:event_jCheckBox1ActionPerformed

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {// Maus gezogen auf Panel
	int anzahlx = bi.getWidth();
	int anzahly = bi.getHeight();
	float faktora = (float) aktuellebreite / anzahlx;
	float faktorb = (float) aktuellehoehe / anzahly;
	int x = (int) (evt.getX() / faktora);
	int y = (int) (evt.getY() / faktorb);
	if (radieren == true) {
	    groesse = jSlider2.getValue() / 5; // Groesse des Radierers
	    Graphics2D g_bi = bi.createGraphics();
	    Color[][] rgb = new Color[anzahlx][anzahly];
	    for (int i = 0; i < groesse; i++) {
		for (int j = 0; j < groesse; j++) {
		    int co = bildOriginal.getRGB(x - i + (groesse / 2), y - j
			    + (groesse / 2));
		    Color c = new Color(co);
		    int b = c.getBlue();
		    int g = c.getGreen();
		    int r = c.getRed();
		    rgb[x - i + (groesse / 2)][y - j + (groesse / 2)] = new Color(
			    r, g, b);
		    g_bi.setColor(rgb[x - i + (groesse / 2)][y - j
			    + (groesse / 2)]);
		    g_bi.fillRect(x - i + (groesse / 2), y - j + (groesse / 2),
			    1, 1);
		}
	    }
	    jPanel1.repaint();
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
    }// GEN-LAST:event_jPanel1MouseDragged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {// Auswahl in Combobox
	// Hole Auswahl
	auswahl = (String) jComboBox1.getSelectedItem();
	if (auswahl == "SW") {
	    jLabel5.setVisible(true);
	    jLabel5.setText("SW -Stärke:");
	    jSlider2.setVisible(true);
	} else if (auswahl == "Unscharf" || auswahl == "Warm"
		|| auswahl == "Punkt") {
	    jLabel5.setVisible(true);
	    jLabel5.setText("Filterstärke:");
	    jSlider2.setVisible(true);
	    if (auswahl == "Punkt") {
		jButton1.setVisible(true);
	    }
	} else {
	    jLabel5.setVisible(false);
	    jSlider2.setVisible(false);
	}
    }// GEN-LAST:event_jComboBox1ActionPerformed

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {// Speichere letzte Veränderung
	letztesBild = bi;
    }

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {// Mausklick
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
	    // Punkt Vorschau
	    jPanel2.setVisible(true);
	    jPanel2.setBackground(new Color(f[0], f[1], f[2]));

	}
    }// GEN-LAST:event_jPanel1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// Punkt-Auswahl "Fertig"
	if (auswahl == "Punkt") {
	    gestartet = !gestartet;
	    if (bildspeichern == true) {
		bispeicher = bi;
	    }
	    sw();
	}
    }// GEN-LAST:event_jButton1ActionPerformed

    private void jSlider2MouseDragged(java.awt.event.MouseEvent evt) {// Wenn Individual-Slider verstellt
	if (gestartet == false && radieren == false) { // Wenn etwas ausgewählt
	    sw();
	} else if (gestartet == true && radieren == false) {
	    sw();
	}
    }// GEN-LAST:event_jSlider2MouseDragged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// Speichere Bild
	// Bild speichern
	if (bildOriginal != null) {
	    try {
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(null);
		@SuppressWarnings("unused")
		int zahl = fc.showSaveDialog(this);
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

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// Effekt auf ganzes Bild anwenden
	// Effekt auf ganzes Fenster anwenden
	if (bi != null) {
	    xStart = 0;
	    yStart = 0;
	    xEnde = bi.getWidth();
	    yEnde = bi.getHeight();
	    sw();
	}
	//bi = letztesBild;
	//jPanel1.repaint();
    }

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {// Bildspeichern ?
	bildspeichern = !bildspeichern;
    }

    public void sw() {
	if (bildOriginal != null) {
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
		    int wert = jSlider2.getValue();
		    if (auswahl == "Punkt" && ro <= wert + f[0]
			    && ro >= f[0] - wert && gr <= wert + f[1]
			    && gr >= f[1] - wert && bl <= wert + f[2]
			    && bl >= f[2] - wert) {
			// rgb[xwert][ywert] = new Color(f[0], f[1], f[2]);
			rgb[xwert][ywert] = new Color(ro, gr, bl);
		    } else if ((auswahl == "SW" && xwert >= xStart
			    && ywert >= yStart && xwert <= xEnde && ywert <= yEnde)
			    || auswahl == "Punkt") {
			r = (bl + gr + ro) / 3;
			if (r > wert) {
			    rgb[xwert][ywert] = new Color(255, 255, 255);
			} else {
			    rgb[xwert][ywert] = new Color(0, 0, 0);
			}
		    } else if (auswahl == "Unscharf" && xwert >= xStart
			    && ywert >= yStart && xwert <= xEnde
			    && ywert <= yEnde) {
			wert = wert/10 +1;
			int dr = 0;
			int dg = 0;
			int db = 0;
			int co2 = 0;
			for (int i = 0; i < wert; i++) {
			    for (int j = 0; j < wert; j++) {
				if(xwert+i<anzahlx && ywert+j<anzahly){ //Wenn nicht außerhalb des Bereichs
				if (bildspeichern == true) {
				    co2 = bispeicher.getRGB(xwert + i, ywert
					    + j);
				} else {
				    co2 = bildOriginal.getRGB(xwert + i, ywert
					    + j);
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
			rgb[xwert][ywert] = new Color(dr, dg, db);
		    } else if (auswahl == "Invertieren" && xwert >= xStart
			    && ywert >= yStart && xwert <= xEnde
			    && ywert <= yEnde) {
			bl = 255 - c.getBlue();
			gr = 255 - c.getGreen();
			ro = 255 - c.getRed();
			rgb[xwert][ywert] = new Color(ro, gr, bl);
		    } else if (auswahl == "Grau" && xwert >= xStart
			    && ywert >= yStart && xwert <= xEnde
			    && ywert <= yEnde) {
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
		    } else {
			rgb[xwert][ywert] = new Color(ro, gr, bl);
		    }
		    g_bi.setColor(rgb[xwert][ywert]);
		    g_bi.fillRect(xwert, ywert, 1, 1);
		}
	    }
	    jPanel1.repaint(); // zeichne Bild
	}
    }

    public void zeichneBild(Graphics g) {
	// Zeichne das Bild
	if (bi != null) {
	    int w = jPanel1.getWidth();
	    int hoeheneu = bi.getHeight() * w / bi.getWidth();
	    aktuellehoehe = hoeheneu;
	    aktuellebreite = w;
	    g.drawImage(bi, 0, 0, w, hoeheneu, this);
	}
    }

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String args[]) {
	try {
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
		    .getInstalledLookAndFeels()) {
		if ("Nimbus".equals(info.getName())) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
	    }
	} catch (ClassNotFoundException ex) {
	    java.util.logging.Logger.getLogger(Fenster.class.getName()).log(
		    java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(Fenster.class.getName()).log(
		    java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(Fenster.class.getName()).log(
		    java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(Fenster.class.getName()).log(
		    java.util.logging.Level.SEVERE, null, ex);
	}
	// </editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		new Fenster().setVisible(true);
	    }
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
