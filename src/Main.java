import comp.Ventana;
import org.apache.commons.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        //System.out.println("Hello World!");




        ArrayList<String> regexs = new ArrayList<>();

        //regexs.add("\\s*(int|float|double|long) \\w{1}\\s*=\\s*\\d*\\s*;\\s*"); //<type> <id>=<value>;
        regexs.add("\\s*(int|float|double|long) (\\w{1})(\\s*|\\s*=\\s*\\d*\\s*);\\s*"); //<type> <id>( |=<value>); r=2
        regexs.add("\\s*(\\w{1})\\s*=\\s*(\\d*|(\\{w{1}}))\\s*;\\s*"); //<id> = <value>|<id>;   r=1,3
        regexs.add("\\s*(\\w{1})\\s*=\\s*((\\w{1})|\\d+)\\s*(\\+|\\-|\\*|\\/)\\s*((\\w{1})|\\d+)\\s*;\\s*"); //(<id>)=(<value>|<id>)<op>(<value>|<id>); r=1,3,6
        regexs.add("\\s*(?:(int|float|double|long) (\\w{1}))\\s*=\\s*(?:(\\w{1})|\\d+)(\\s*(?:\\+|\\-|\\*|\\/)\\s*(?:(\\w{1})|\\d+))*\\s*;\\s*"); //(<type> <id>)=(<value>|<id>)(<op>(<value>|<id>))*;  r=3







        Ventana v = new Ventana();

        v.btnElegir.addActionListener(e -> {

            int i = 0;
            int f = 0;
            int d = 0;
            int l = 0;

            boolean error = false;
            HashMap<String, String> mapa = new HashMap<>();

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt", "text");
            fileChooser.setFileFilter(filter);
            int seleccion = fileChooser.showOpenDialog(v);

            if (seleccion == JFileChooser.APPROVE_OPTION)
            {
                File fichero = fileChooser.getSelectedFile();

                System.out.println(fichero);

                v.txtOrigen.setText(fichero.getAbsolutePath());



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
                                String  id = "";
                                if(Pattern.matches(regexs.get(3), line)){
                                    System.out.println("line: "+line);

                                    Pattern p = Pattern.compile(regexs.get(3));
                                    Matcher m = p.matcher(line);

                                    while(m.find()) {


                                        if (mapa.containsKey(m.group(2))) {
                                            JOptionPane.showMessageDialog(v, "Se esta redeclarando la variable "+m.group(2)+".");
                                            error = true;
                                        }

                                        switch (m.group(1)){
                                            case "int":
                                                i++;
                                                mapa.put(m.group(2), "i"+i); break;
                                            case "float":
                                                f++;
                                                mapa.put(m.group(2), "f"+f); break;
                                            case "double":
                                                d++;
                                                mapa.put(m.group(2), "d"+d);break;
                                            case "long":
                                                l++;
                                                mapa.put(m.group(2), "l"+l); break;
                                        }
                                    }

                                }
                            }
                        }

                    }


                    try(BufferedReader nbr = new BufferedReader(new FileReader(fichero))) {
                        for(String linea; (linea = nbr.readLine()) != null; ) {


                            String rgx = "\\s*(int|float|double|long).*";
                            String aux = linea;
                            String newlinea = "";
                            if(Pattern.matches(rgx, linea)){
                                List<String> lista = new ArrayList<String>();
                                String tipo = "";
                                if(linea.matches("(?i)\\s*int.*")){
                                    //System.out.println("E w int");
                                    lista = Arrays.asList(linea.split("int"));
                                    tipo = "int";
                                }else if (linea.matches("(?i)\\s*float.*")){
                                    //System.out.println("E w float");
                                    lista = Arrays.asList(linea.split("float"));
                                    tipo = "float";
                                }else if (linea.matches("(?i)\\s*double.*")){
                                    //System.out.println("E w double");
                                    lista = Arrays.asList(linea.split("double"));
                                    tipo = "double";
                                }else if (linea.matches("(?i)\\s*long.*")){
                                    //System.out.println("E w long");
                                    lista = Arrays.asList(linea.split("long"));
                                    tipo = "long";
                                }
                                String linea2 = lista.get(1);

                                for (int ix = 0; ix < linea2.length(); ix++){
                                    char c = linea2.charAt(ix);
                                    String key = Character.toString(c);
                                    //System.out.println("char: "+c);

                                    if(key.chars().allMatch(Character::isLetter)&& mapa.get(key) == null){
                                        JOptionPane.showMessageDialog(v, "Variable "+ key +" no declarada");
                                        error = true;
                                    }

                                    if(mapa.get(key) != null || (mapa.get(key) == null && mapa.containsKey(key))){
                                        //System.out.println(mapa.get(key));
                                        newlinea += mapa.get(key);
                                    }else{

                                        newlinea += key;
                                    }
                                }
                                System.out.println("size: "+lista.size());
                                newlinea = lista.get(0) +tipo+ newlinea;
                            }else{
                                for (int ix = 0; ix < linea.length(); ix++){
                                    char c = linea.charAt(ix);
                                    String key = Character.toString(c);
                                    //System.out.println("char: "+c);

                                    if(key.chars().allMatch(Character::isLetter)&& mapa.get(key) == null){
                                        JOptionPane.showMessageDialog(v, "Variable "+ key +" no declarada");
                                        error = true;
                                    }

                                    if(mapa.get(key) != null || (mapa.get(key) == null && mapa.containsKey(key))){
                                    //    System.out.println(mapa.get(key));
                                        newlinea += mapa.get(key);
                                    }else{

                                        newlinea += key;
                                    }
                                }
                            }

                            System.out.println(newlinea);

                            if(!error) {
                                long unixTime = System.currentTimeMillis() / 1000L;
                                String fname = FilenameUtils.getBaseName(fichero.getAbsolutePath()) + "_"+unixTime+"." + FilenameUtils.getExtension(fichero.getAbsolutePath());
                                String faname = FilenameUtils.concat(fichero.getParent(), fname);
                                File nuevofichero = new File(faname);
                                BufferedWriter writer = new BufferedWriter(new FileWriter(nuevofichero, true));
                                writer.write(newlinea);
                                writer.newLine();
                                writer.close();
                                v.txtSalida.setText(faname);
                            }
                        }
                    }

                    Iterator it = mapa.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        System.out.println(pair.getKey() + " => " + pair.getValue());
                        v.txtResultado.append(pair.getKey() + " => " + pair.getValue()+"\n");
                        it.remove(); // avoids a ConcurrentModificationException
                    }


                }catch (Exception ex){
                    ex.printStackTrace();
                }


            }

        });

        v.setVisible(true);
    }
}
