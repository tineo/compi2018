import comp.Ventana;
import org.apache.commons.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        //System.out.println("Hello World!");


        String input = "hello I'm a java dev" +
                "no job experience needed" +
                "senior software engineer" +
                "java job available for senior software engineer";

        String fixedInput = input.replaceAll("(java|job|senior)", "<b>$1</b>");
        System.out.println(fixedInput);


        String types = "int|float|double|long";
        String nodeValue = "int a =  13 + b ; ";

        ArrayList<String> regexs = new ArrayList<>();


        //regexs.add("\\s*(int|float|double|long) \\w{1}\\s*=\\s*\\d*\\s*;\\s*"); //<type> <id>=<value>;
        regexs.add("\\s*(int|float|double|long) \\w{1}(\\s*|\\s*=\\s*\\d*\\s*);\\s*"); //<type> <id>( |=<value>);
        regexs.add("\\s*\\w{1}\\s*=\\s*(\\d*|\\{w{1}})\\s*;\\s*"); //<id> = <value>|<id>;
        regexs.add("\\s*(\\w{1}|(int|float|double|long) \\w{1})\\s*=\\s*(\\w{1}|\\d+)\\s*(\\+|\\-|\\*|\\/)\\s*(\\w{1}|\\d+)\\s*;\\s*");

        Ventana v = new Ventana();

        v.btnElegir.addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt", "text");
            fileChooser.setFileFilter(filter);
            int seleccion = fileChooser.showOpenDialog(v);

            if (seleccion == JFileChooser.APPROVE_OPTION)
            {
                File fichero = fileChooser.getSelectedFile();

                System.out.println(fichero);

                v.txtOrigen.setText(fichero.getAbsolutePath());

                HashMap<String, String> mapa = new HashMap<>();

                try {

                    try(BufferedReader br = new BufferedReader(new FileReader(fichero))) {
                        boolean cumple = false;
                        for(String line; (line = br.readLine()) != null; ) {
                            //System.out.println(line);


                            for (String regex : regexs) {
                                //System.out.println(regex);
                                cumple = Pattern.matches(regex, line);
                                if(cumple) break;
                            }

                            if(!cumple){
                                System.out.println(line);
                            }else{

                                if(Pattern.matches(regexs.get(0), line)){
                                    System.out.println("line: "+line);

                                    Pattern p = Pattern.compile("(int|float|double|long) (\\w{1})(\\s*|\\s*=\\s*\\d*\\s*);");
                                    Matcher m = p.matcher(line);
                                    while(m.find()) {
                                        System.out.println(m.group(1));
                                        System.out.println(m.group(2));
                                        mapa.put(m.group(1), m.group(2));

                                    }


                                }


                            }


                        }



                    }

                     //(<id>|<type> <id>)=(<value>|<id>)<op>(<value>|<id>);







                    /*String fname = FilenameUtils.getBaseName(fichero.getAbsolutePath())+"_nuevo."+FilenameUtils.getExtension(fichero.getAbsolutePath());
                    String faname = FilenameUtils.concat(fichero.getParent(),fname);

                    File nuevofichero = new File(faname);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(nuevofichero, true));
                    String[] words = nodeValue.split(" ");
                    for (String word: words) {
                        writer.write(word);
                        writer.newLine();
                    }
                    writer.close();

                    v.txtSalida.setText(faname);*/
                }catch (Exception ex){
                    ex.printStackTrace();
                }


            }

        });

        v.setVisible(true);
    }
}
