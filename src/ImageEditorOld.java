import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author JanFuhrmann
 */
public class ImageEditorOld extends JFrame {
    BufferedImage editedImage = null;
    BufferedImage originalImage = null; // Original
    BufferedImage backupImage = null;
    BufferedImage lastImg = null;
    int recentHeight = 0, recentWidth = 0, xStart = 0, yStart = 0,
            xEnd = 0, yEnd = 0, xEnd2 = 0, yEnd2 = 0, xStart2 = 0,
            yStart2 = 0, size = 0;
    boolean erase = false;
    boolean start = false;
    boolean savePicture = false;
    String selection;
    int[] rgbOfPoint = new int[3];

    public ImageEditorOld() {
        initComponents();
        pnlPreview.setVisible(false);
        slider.setVisible(false);
    }

    /**
     * This function initializes all elements (e.g. size, position, textSize)
     */
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Initialize Components
        JButton btnChooseFile = new JButton("Choose");
        JLabel lblFile = new JLabel("File: ");
        JCheckBox checkboxEraser = new JCheckBox("Eraser");
        JButton btnSaveImage = new JButton("Save Picture");
        JButton btnApply = new JButton("Apply Filter");
        JCheckBox checkboxSaveChanges = new JCheckBox("Save Mode");
        txtFile = new JTextField();
        comboBox = new JComboBox();
        pnlPreview = new JPanel();
        slider = new JSlider();
        lblSliderDescription = new JLabel();

        // Initialize Panels
        pnlImage = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawPicture(g);
            }
        };
        pnlImage.setPreferredSize(new Dimension(492, 492));
        GroupLayout jPanel1Layout = new GroupLayout(pnlImage);
        pnlImage.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
        pnlPreview.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        GroupLayout layoutPnlPreview = new GroupLayout(pnlPreview);
        pnlPreview.setLayout(layoutPnlPreview);
        layoutPnlPreview.setHorizontalGroup(layoutPnlPreview.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        layoutPnlPreview.setVerticalGroup(layoutPnlPreview.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));

        // Fonts and text size
        Font font14 = new Font("Tahoma", Font.PLAIN, 14);
        lblFile.setFont(font14);
        txtFile.setFont(font14);
        btnChooseFile.setFont(font14);

        // Listener
        btnChooseFile.addActionListener(evt -> loadImage());
        btnSaveImage.addActionListener(evt -> saveImage());
        btnApply.addActionListener(evt -> applyFilterOnWhole());
        checkboxEraser.addActionListener(evt -> {
            erase = !erase;
            lblSliderDescription.setText("Eraser size:");
            lblSliderDescription.setVisible(erase);
            slider.setVisible(erase);
        });
        checkboxSaveChanges.addActionListener(evt -> savePicture = !savePicture);
        comboBox.addActionListener(evt -> getSelection());
        pnlImage.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                ImageEditorOld.this.mouseClicked(evt);
            }

            public void mouseReleased(MouseEvent evt) {
                ImageEditorOld.this.mouseReleased();
            }

            public void mousePressed(MouseEvent evt) {
                ImageEditorOld.this.mousePressed();
            }
        });
        pnlImage.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent mouseEvent) {
                ImageEditorOld.this.mouseDragged(mouseEvent);
            }
        });
        slider.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                if (!erase) {
                    applyFilter();
                }
            }
        });

        // Other
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"Select Filter...", "Black & White", "Blurred", "Invert", "Grey", "Warm", "Point"}));

        //Layout
        GroupLayout layout = new GroupLayout(
                getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(
                                                GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                        layout.createSequentialGroup()
                                                                .addComponent(
                                                                        lblFile)
                                                                .addGap(18, 18,
                                                                        18)
                                                                .addComponent(
                                                                        txtFile,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        316,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18,
                                                                        18)
                                                                .addComponent(
                                                                        btnChooseFile,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        186,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(
                                                                        btnSaveImage)
                                                                .addPreferredGap(
                                                                        LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(
                                                                        checkboxSaveChanges))
                                                .addGroup(
                                                        layout.createSequentialGroup()
                                                                .addComponent(
                                                                        pnlImage,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        537,
                                                                        Short.MAX_VALUE)
                                                                .addGap(18, 18,
                                                                        18)
                                                                .addGroup(
                                                                        layout.createParallelGroup(
                                                                                GroupLayout.Alignment.LEADING)
                                                                                .addGroup(
                                                                                        layout.createSequentialGroup()
                                                                                                .addComponent(
                                                                                                        comboBox,
                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                .addContainerGap(
                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                        Short.MAX_VALUE))
                                                                                .addGroup(
                                                                                        layout.createSequentialGroup()
                                                                                                .addGroup(
                                                                                                        layout.createParallelGroup(
                                                                                                                GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(
                                                                                                                        checkboxEraser)
                                                                                                                .addComponent(
                                                                                                                        pnlPreview,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        btnApply)
                                                                                                                .addComponent(
                                                                                                                        slider,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        110,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        lblSliderDescription,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        110,
                                                                                                                        GroupLayout.PREFERRED_SIZE))
                                                                                                .addGap(0,
                                                                                                        0,
                                                                                                        Short.MAX_VALUE)))))));
        layout.setVerticalGroup(layout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                        GroupLayout.Alignment.TRAILING,
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(
                                                GroupLayout.Alignment.TRAILING)
                                                .addGroup(
                                                        layout.createParallelGroup(
                                                                GroupLayout.Alignment.LEADING)
                                                                .addGroup(
                                                                        layout.createParallelGroup(
                                                                                GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(
                                                                                        lblFile)
                                                                                .addComponent(
                                                                                        txtFile,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(
                                                                                        btnChooseFile)
                                                                                .addComponent(
                                                                                        btnSaveImage)))
                                                .addComponent(checkboxSaveChanges))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        layout.createParallelGroup(
                                                GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                        layout.createSequentialGroup()
                                                                .addComponent(
                                                                        comboBox,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18,
                                                                        18)
                                                                .addComponent(
                                                                        checkboxEraser)
                                                                .addGap(18, 18,
                                                                        18)
                                                                .addComponent(
                                                                        pnlPreview,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addGap(92, 92,
                                                                        92)
                                                                .addComponent(
                                                                        btnApply)
                                                                .addGap(37, 37,
                                                                        37)
                                                                .addComponent(
                                                                        lblSliderDescription,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        13,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(
                                                                        slider,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0,
                                                                        74,
                                                                        Short.MAX_VALUE))
                                                .addComponent(
                                                        pnlImage,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        500, Short.MAX_VALUE))
                                .addContainerGap()));

        pack();
    }

    /**
     * Load image either from path or file chooser
     */
    private void loadImage() {
        boolean errorWhileLoading = false;
        if ("".equals(txtFile.getText())) {
            try {
                final JFileChooser fileChooser = new JFileChooser();
                // fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                fileChooser.showOpenDialog(null);
                txtFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
            } catch (Exception e) {
                System.out.println("No File selected!");
                JOptionPane.showMessageDialog(null, "No File selected!", "Warning: No File selected", JOptionPane.PLAIN_MESSAGE);
                errorWhileLoading = true;
            }
        }
        if (!errorWhileLoading) {
            try {
                editedImage = ImageIO.read(new File(txtFile.getText()));
                originalImage = editedImage; // save original
                backupImage = editedImage; // backup image
                lastImg = editedImage; // save last image
                pnlImage.repaint();
            } catch (Exception e) {
                System.out.println("Error while reading the file (" + e + ")");
                JOptionPane.showMessageDialog(null, "File could not be read!", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * When mouse was released
     */
    private void mouseReleased() {
        start = false;
        if (savePicture) {
            backupImage = editedImage;
        }
    }

    /**
     * When mouse was dragged
     *
     * @param mouseEvent The specific Event
     */
    private void mouseDragged(MouseEvent mouseEvent) {
        int width = editedImage.getWidth();
        int height = editedImage.getHeight();
        float factorA = (float) recentWidth / width;
        float factorB = (float) recentHeight / height;
        int x = (int) (mouseEvent.getX() / factorA);
        int y = (int) (mouseEvent.getY() / factorB);
        if (erase) {
            size = slider.getValue() / 4; // eraser size
            Graphics2D g_bi = editedImage.createGraphics();
            Color[][] rgb = new Color[width][height];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    try {
                        int pxlColor = originalImage.getRGB(x - i + (size / 2), y - j
                                + (size / 2));
                        Color c = new Color(pxlColor);
                        int b = c.getBlue();
                        int g = c.getGreen();
                        int r = c.getRed();
                        rgb[x - i + (size / 2)][y - j + (size / 2)] = new Color(
                                r, g, b);
                        g_bi.setColor(rgb[x - i + (size / 2)][y - j
                                + (size / 2)]);
                        g_bi.fillRect(x - i + (size / 2), y - j + (size / 2),
                                1, 1);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        // That's okay
                    }
                }
            }
            pnlImage.repaint();
        } else if (!selection.equals("Point")) {
            // get start- and endpoint of selection
            if (!start) {
                xStart = x;
                yStart = y;
                xStart2 = x;
                yStart2 = y;
                start = true;
                if (savePicture) {
                    backupImage = editedImage;
                }
            } else {
                xEnd2 = x;
                yEnd2 = y;
                xEnd = x;
                yEnd = y;
                if (xEnd2 < xStart2) {
                    xEnd = xStart2;
                    xStart = xEnd2;
                }
                if (yEnd2 < yStart2) {
                    yEnd = yStart2;
                    yStart = yEnd2;
                }
                applyFilter();
            }
        }
    }

    /**
     * Show elements if specific items are selected
     */
    private void getSelection() {
        selection = (String) comboBox.getSelectedItem();
        slider.setVisible(selection.equals("Black & White") || selection.equals("Blurred") || selection.equals("Warm") || selection.equals("Point"));
        lblSliderDescription.setVisible(selection.equals("Black & White") || selection.equals("Blurred") || selection.equals("Warm") || selection.equals("Point"));
        if (selection.equals("Black & White")) {
            lblSliderDescription.setText("B&W strength:");
        } else if (selection.equals("Blurred") || selection.equals("Warm") || selection.equals("Point")) {
            lblSliderDescription.setText("Filter strength:");
        }
    }

    /**
     * When mouse was pressed on panel
     */
    private void mousePressed() {
        lastImg = editedImage; // save last changes
    }

    /**
     * When mouse was clicked on panel
     *
     * @param mouseEvent The specific event
     */
    private void mouseClicked(MouseEvent mouseEvent) {
        if (selection.equals("Point")) {
            float factorA = (float) recentWidth / editedImage.getWidth();
            float factorB = (float) recentHeight / editedImage.getHeight();
            int x = (int) (mouseEvent.getX() / factorA);
            int y = (int) (mouseEvent.getY() / factorB);
            int r = 0;
            int g = 0;
            int b = 0;
            int pxlColor;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (savePicture) {
                        pxlColor = backupImage.getRGB(x, y);
                    } else {
                        pxlColor = editedImage.getRGB(x, y);
                    }
                    Color c = new Color(pxlColor);
                    b += c.getBlue();
                    g += c.getGreen();
                    r += c.getRed();
                }
            }
            rgbOfPoint[0] = r / 100;
            rgbOfPoint[1] = g / 100;
            rgbOfPoint[2] = b / 100;
            pnlPreview.setVisible(true);
            pnlPreview.setBackground(new Color(rgbOfPoint[0], rgbOfPoint[1], rgbOfPoint[2]));

        }
    }

    /**
     * Save the created/edited image to local directory as a .png
     */
    private void saveImage() {
        if (editedImage != null) {
            try {
                final JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(null);
                fc.showSaveDialog(this);
                File f = fc.getSelectedFile();
                ImageIO.write(editedImage, "png", f);
                System.out.println("Image successfully saved at " + f.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "Image successfully saved!", "Successfully Saved", JOptionPane.PLAIN_MESSAGE);
            } catch (IOException e) {
                System.out.println("Error occurred while saving (" + e + ")");
                JOptionPane.showMessageDialog(null, "Image could not be saved!", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Applies the chosen filter on whole image
     */
    private void applyFilterOnWhole() {
        if (editedImage != null) {
            xStart = 0;
            yStart = 0;
            xEnd = editedImage.getWidth();
            yEnd = editedImage.getHeight();
            applyFilter();
        }
    }

    /**
     * Applies the chosen filter
     */
    public void applyFilter() {
        if (originalImage != null) {
            int red = 0, blue = 0, green = 0;
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            Color[][] rgb = new Color[width][height];
            editedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = editedImage.createGraphics();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pxlRed = 0, pxlGreen = 0, pxlBlue = 0, pxlColor;
                    if (savePicture) {
                        pxlColor = backupImage.getRGB(x + (int) (Math.random()), y + (int) (Math.random()));
                    } else {
                        pxlColor = originalImage.getRGB(x + (int) (Math.random()), y + (int) (Math.random()));
                    }
                    Color c = new Color(pxlColor);
                    pxlBlue += c.getBlue();
                    pxlGreen += c.getGreen();
                    pxlRed += c.getRed();
                    int sliderValue = slider.getValue();

                    if (selection.equals("Point") && pxlRed <= sliderValue + rgbOfPoint[0] && pxlRed >= rgbOfPoint[0] - sliderValue && pxlGreen <= sliderValue + rgbOfPoint[1] && pxlGreen >= rgbOfPoint[1] - sliderValue && pxlBlue <= sliderValue + rgbOfPoint[2] && pxlBlue >= rgbOfPoint[2] - sliderValue) {
                        rgb[x][y] = new Color(pxlRed, pxlGreen, pxlBlue);
                    } else if (selection.equals("Point")) {
                        int valueGrey = (int) (pxlRed * 0.299 + pxlGreen * 0.587 + pxlBlue * 0.114);
                        rgb[x][y] = new Color(valueGrey, valueGrey, valueGrey);
                    } else if (x >= xStart && y >= yStart && x <= xEnd && y <= yEnd) {
                        if (selection.equals("Black & White")) {
                            int valueBW = (pxlBlue + pxlGreen + pxlRed) / 3;
                            if (valueBW > sliderValue) {
                                rgb[x][y] = new Color(255, 255, 255);
                            } else {
                                rgb[x][y] = new Color(0, 0, 0);
                            }
                        } else if (selection.equals("Blurred") || selection.equals("Horror")) {
                            sliderValue /= 10 + 1;
                            int dr = 0;
                            int dg = 0;
                            int db = 0;
                            int pxlColor2 = 0;
                            for (int i = 0; i < sliderValue; i++) {
                                for (int j = 0; j < sliderValue; j++) {
                                    if (x + i < width && y + j < height) { // If not outside of range
                                        if (savePicture) {
                                            pxlColor2 = backupImage.getRGB(x + i,
                                                    y + j);
                                        } else {
                                            pxlColor2 = originalImage.getRGB(x + i,
                                                    y + j);
                                        }
                                    }
                                    Color c2 = new Color(pxlColor2);
                                    dr += c2.getRed();
                                    dg += c2.getGreen();
                                    db += c2.getBlue();
                                }
                            }
                            if (sliderValue == 0) sliderValue = 1;
                            dr /= (sliderValue * sliderValue);
                            dg /= (sliderValue * sliderValue);
                            db /= (sliderValue * sliderValue);
                            if (selection.equals("Blurred")) {
                                rgb[x][y] = new Color(dr, dg, db);
                            } else {
                                if (pxlRed - dr > 0) {
                                    pxlRed -= dr;
                                }
                                if (pxlGreen - dg > 0) {
                                    pxlGreen -= dg;
                                }
                                if (pxlBlue - db > 0) {
                                    pxlBlue -= db;
                                }
                                rgb[x][y] = new Color(pxlRed, pxlGreen, pxlBlue);
                            }
                        } else if (selection.equals("Invert")) {
                            rgb[x][y] = new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
                        } else if (selection.equals("Grey")) {
                            int valueGrey = (int) (pxlRed * 0.299 + pxlGreen * 0.587 + pxlBlue * 0.114);
                            rgb[x][y] = new Color(valueGrey, valueGrey, valueGrey);
                        } else if (selection.equals("Warm")) {
                            if (pxlRed + (sliderValue / 2) < 256) {
                                pxlRed += (sliderValue / 2);
                            }
                            if (pxlGreen + (sliderValue / 2) < 256) {
                                pxlGreen += (sliderValue / 2);
                            }
                            rgb[x][y] = new Color(pxlRed, pxlGreen, pxlBlue);
                        } else if (selection.equals("Drawing")) {
                            if (Math.abs(red - pxlRed) > 5 && Math.abs(green - pxlGreen) > 5 && Math.abs(blue - pxlBlue) > 5) {
                                rgb[x][y] = Color.BLACK;
                            } else {
                                rgb[x][y] = Color.WHITE;
                            }
                            red = pxlRed;
                            blue = pxlBlue;
                            green = pxlGreen;
                        } else {
                            // original picture
                            rgb[x][y] = new Color(pxlRed, pxlGreen, pxlBlue);
                        }
                    } else {
                        // original picture
                        rgb[x][y] = new Color(pxlRed, pxlGreen, pxlBlue);
                    }
                    graphics.setColor(rgb[x][y]);
                    graphics.fillRect(x, y, 1, 1);
                }
            }
            pnlImage.repaint(); // paint image
        }
    }

    /**
     * Draw the image every time the panel gets reloaded
     *
     * @param g The graphics to be drawn
     */
    public void drawPicture(Graphics g) {
        if (editedImage != null) {
            int width = pnlImage.getWidth();
            int height = editedImage.getHeight() * width / editedImage.getWidth();
            System.out.println(width+"-"+height);
            recentHeight = height;
            recentWidth = width;
            g.drawImage(editedImage, 0, 0, width, height, this);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager
                    .getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error: "+ex);
        }

        /* Create and display the form */
        EventQueue.invokeLater(() -> new ImageEditorOld().setVisible(true));
    }

    private JComboBox comboBox;
    private JLabel lblSliderDescription;
    private JPanel pnlImage;
    private JPanel pnlPreview;
    private JSlider slider;
    private JTextField txtFile;
}
