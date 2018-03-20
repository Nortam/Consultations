package documents;

import check.system.CurrentSystem;
import data.сollections.DataWeek;
import javafx.collections.ObservableList;
import org.apache.poi.xwpf.usermodel.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Document {

    public void createNewDocument(String path, String filename, String type) throws Exception  {
        XWPFDocument document = new XWPFDocument();
        FileOutputStream out = null;
        if (CurrentSystem.isUnix()) {
            out = new FileOutputStream( new File(path + "/" + filename + "." + type));
        } else {
            out = new FileOutputStream( new File(path + "\\" + filename + "." + type));
        }
        document.write(out);
        out.close();
    }

    public String chooseFileDirectory() {
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a directory to save");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            return fileToSave.getAbsolutePath();
        }
        return null;
    }

    public String getCurrentDirectory() {
        return new JFileChooser().getCurrentDirectory().toString();
    }

    public boolean openFile(String path, String filename, String type) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(new File(path + "\\" + filename + "." + type));
            return true;
        }
        return false;
    }

    public String createFileName() {
        return ("Report_" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy.MM.dd_(HH-mm)")));
    }

    public void writeDataToFile(ObservableList<DataWeek> dataWeek, String path, String filename, String type) {
        XWPFDocument document = new XWPFDocument();
        FileOutputStream out = null;
        try {
            if (CurrentSystem.isUnix()) {
                out = new FileOutputStream(new File(path + "/" + filename + "." + type));
            } else {
                out = new FileOutputStream(new File(path + "\\" + filename + "." + type));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XWPFTable table = document.createTable();
        int countRows = 7;
        for (int i = 0; i < 8; i++) {
            table.getRow(0).addNewTableCell();
        }
        for (int i = 0; i < countRows-1; i++) {
            table.createRow();
        }
        for (int i = 0; i < countRows; i++) {
            table.getRow(i).getCell(0).setText(dataWeek.get(i).getWd() != null ? dataWeek.get(i).getWd() : "");
            table.getRow(i).getCell(0).setColor("d2d4db");
            table.getRow(i).getCell(1).setText(dataWeek.get(i).getDate() != null ? dataWeek.get(i).getDate().toString() : "");
            table.getRow(i).getCell(1).setColor(table.getRow(i).getCell(0).getColor());
            table.getRow(i).getCell(2).setText(dataWeek.get(i).getTime() != null ? dataWeek.get(i).getTime().toString() : "");
            table.getRow(i).getCell(3).setText(dataWeek.get(i).getType() != null ? dataWeek.get(i).getType() : "");
            table.getRow(i).getCell(4).setText(dataWeek.get(i).getOtm() != null ? dataWeek.get(i).getOtm() : "");
            table.getRow(i).getCell(5).setText(dataWeek.get(i).getPerson() != null ? dataWeek.get(i).getPerson() : "");
            table.getRow(i).getCell(6).setText(dataWeek.get(i).getLocation() != null ? dataWeek.get(i).getLocation() : "");
            table.getRow(i).getCell(7).setText(dataWeek.get(i).getPlace() != null ? dataWeek.get(i).getPlace() : "");
            table.getRow(i).getCell(8).setText(dataWeek.get(i).getConsultant() != null ? dataWeek.get(i).getConsultant() : "");
        }
        try {
            document.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
