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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FaceRecognition extends JFrame {
    private final JTextField txtFile;
    private BufferedImage editedImage = null;
    private BufferedImage originalImage = null;
    private final JPanel pnlImage = new JPanel() {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawPicture(g);
        }
    };

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FaceRecognition frame = new FaceRecognition();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public FaceRecognition() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 664, 643);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTextField textField = new JTextField();
        textField.setColumns(10);
        textField.setBounds(-56, -230, 348, 22);
        contentPane.add(textField);

        JButton button = new JButton("Choose");
        button.addActionListener(e -> loadImage());
        button.setFont(new Font("Tahoma", Font.PLAIN, 18));
        button.setBounds(372, 8, 138, 25);
        contentPane.add(button);

        txtFile = new JTextField();
        txtFile.setColumns(10);
        txtFile.setBounds(12, 11, 348, 22);
        contentPane.add(txtFile);

        pnlImage.setBounds(12, 46, 500, 500);
        contentPane.add(pnlImage);

        JButton btnDetect = new JButton("Detect");
        btnDetect.addActionListener(arg0 -> detect());
        btnDetect.setBounds(524, 50, 97, 25);
        contentPane.add(btnDetect);

        JButton btnSaveImage = new JButton("Save Picture");
        btnSaveImage.addActionListener(e -> saveImage());
        btnSaveImage.setBounds(524, 10, 97, 25);
        contentPane.add(btnSaveImage);
    }

    /**
     * Detect the faces
     */
    private void detect() {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int r = 0, g = 0, b = 0;
        Color[][] rgb = new Color[width][height];
        editedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g_bi = editedImage.createGraphics();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int ro = 0, gr = 0, bl = 0, pxlColor;
                pxlColor = originalImage.getRGB(x + (int) (Math.random()),
                        y + (int) (Math.random()));
                Color c = new Color(pxlColor);
                bl += c.getBlue();
                gr += c.getGreen();
                ro += c.getRed();
                if (Math.abs(r - ro) > 5 && Math.abs(g - gr) > 5
                        && Math.abs(b - bl) > 5) {
                    rgb[x][y] = Color.BLACK;
                } else {
                    rgb[x][y] = Color.WHITE;
                }
                r = ro;
                b = bl;
                g = gr;
                g_bi.setColor(rgb[x][y]);
                g_bi.fillRect(x, y, 1, 1);
            }
        }
        pnlImage.repaint();
    }

    /**
     * Save the created/edited image to local directory as a .png
     */
    protected void saveImage() {
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
     * Load image either from path or file chooser
     */
    protected void loadImage() {
        boolean errorWhileLoading = false;
        if (txtFile.getText().equals("")) {
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
                pnlImage.repaint(); // repaint panel
            } catch (IOException e) {
                pnlImage.setVisible(false);
                System.out.println("Error while reading the file (" + e + ")");
                JOptionPane.showMessageDialog(null, "File could not be read!", "Error", JOptionPane.PLAIN_MESSAGE);
            }
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
            g.drawImage(editedImage, 0, 0, width, height, this);
        }
    }
}
