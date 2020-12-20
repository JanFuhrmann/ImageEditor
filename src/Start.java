import javax.swing.JFrame;

public class Start {

    public static void main(String[] args) {
        ImageEditor frame = new ImageEditor();
        // ImageEditorOld frame = new ImageEditorOld();
        frame.setVisible(true);
        frame.setTitle("Image editing program by Jan Fuhrmann");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

}
