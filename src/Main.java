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
                                String  id = "";
                                if(Pattern.matches(regexs.get(3), line)){
                                    System.out.println("line: "+line);

                                    Pattern p = Pattern.compile(regexs.get(3));
                                    Matcher m = p.matcher(line);

                                    while(m.find()) {
                                        System.out.println("01: "+m.group(1));
                                        System.out.println("02: "+m.group(2));
                                        System.out.println("03: "+m.group(3));

                                        if (mapa.containsKey(m.group(2))) {
                                            JOptionPane.showMessageDialog(v, "Se esta redeclarando la variable "+m.group(2)+".");
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

                                        id = m.group(2);


                                    }

                                    String aux = line;

                                    Pattern p1 = Pattern.compile(regexs.get(3));
                                    Matcher m1 = p1.matcher(line);
                                    List<String> tokens = new LinkedList<>();
                                    while(m1.find())
                                    {
                                        String token = m1.group( 1 ); //group 0 is always the entire match
                                        System.out.println("\n tokens: \n");
                                        System.out.println("1: " + m1.group( 1 ));
                                        System.out.println("2: " + m1.group( 2 ));
                                        System.out.println("3: " + m1.group( 3 ));
                                        System.out.println("4: " + m1.group( 4 ));
                                        System.out.println("5: " + m1.group( 5 ));
                                        //System.out.println("6: "+m1.group( 6 ));
                                        //System.out.println("7: "+m1.group( 7 ));
                                        //System.out.println("8: "+m1.group( 8 ));
                                        //System.out.println("9: "+m1.group( 9 ));
                                        tokens.add(token);
                                    }
                                    //String fixedInput = aux.replaceAll(regexs.get(3), "$2 "+ mapa.get(id)+" = $4");
                                    //System.out.println(fixedInput);

                                    //String replacedString = line.replace(" a", " i0001");
                                    //System.out.println("r:  "+replacedString);
                                }
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
