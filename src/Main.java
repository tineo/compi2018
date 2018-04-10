import comp.Ventana;
import org.apache.commons.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
public class Main {

    public static void main(String[] args) {
        //System.out.println("Hello World!");
        Ventana v = new Ventana();

        v.btnElegir.addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
            fileChooser.setFileFilter(filter);
            int seleccion = fileChooser.showOpenDialog(v);

            if (seleccion == JFileChooser.APPROVE_OPTION)
            {
                File fichero = fileChooser.getSelectedFile();

                System.out.println(fichero);

                v.txtOrigen.setText(fichero.getAbsolutePath());


                try {
                    String nodeValue = "i am mostafa";

                    String fname = FilenameUtils.getBaseName(fichero.getAbsolutePath())+"_nuevo."+FilenameUtils.getExtension(fichero.getAbsolutePath());
                    String faname = FilenameUtils.concat(fichero.getParent(),fname);

                    File nuevofichero = new File(faname);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(nuevofichero, true));
                    String[] words = nodeValue.split(" ");
                    for (String word: words) {
                        writer.write(word);
                        writer.newLine();
                    }
                    writer.close();

                    v.txtSalida.setText(faname);
                }catch (Exception ex){
                    ex.printStackTrace();
                }


            }

        });

        v.setVisible(true);
    }
}
