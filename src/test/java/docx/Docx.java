package docx;

import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.poi.xwpf.usermodel.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Label;
import java.awt.TextField;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class Docx {

    public static void createNewDocument(String path, String filename, String type) throws Exception  {
        XWPFDocument document = new XWPFDocument();
        FileOutputStream out = new FileOutputStream( new File(path + "\\" + filename + "." + type));
        document.write(out);
        out.close();
    }

    public static String chooseFileDirectory() {
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.showSaveDialog(null);
        return f.getSelectedFile().toString();
    }
}