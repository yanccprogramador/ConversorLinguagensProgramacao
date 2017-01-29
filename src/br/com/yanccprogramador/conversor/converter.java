/*
 *Esta biblioteca converte um código visualg inserido em um programas, para Java ouc
 */
package br.com.yanccprogramador.conversor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.io.FileReader;


/**
 * versão 1.3
 * @author Yan Christoffer Programacoes, inc
 */
public class converter {

    private boolean ifAtivo = false;
    private int h = -1;
    private int l = -1;
    private boolean varAt = false;
    private boolean repitaAtivo = false;
    private boolean escAtivo = false;
    private boolean forAtivo = false;
    private boolean funcAt = false;
    private final ArrayList vares = new ArrayList();

    private int g = -1;
    private String linha;



    public String convertJava(String caminho,String b) {

        String arquivoConvertido = " ";
        try {
            BufferedReader leitor = new BufferedReader(new FileReader(caminho));

            while ((linha = leitor.readLine()) != null) {
                linha = linha.toLowerCase();
                linha = linha.replace("fimalgoritmo", "}"+"\n"+ " }");
                if (linha.contains("algoritmo")) {
                    linha = "";
                }
                g = linha.indexOf(":");
                if (linha.contains("var")) {
                    varAt = true;
                    if (b.contains("leia")) {
                        linha = linha.replace("var", "public class{ \n Scanner s=new Scanner(System.in)");
                    } else {
                        linha = linha.replace("var", "public class{");
                    }
                } else if (linha.contains("inicio")) {
                    varAt = false;

                }
                if (funcAt == false && linha.equals("inicio")) {
                    linha = linha.replace("inicio", "public static void main(String[] args){");
                }
                if (linha.contains("<-")) {
                    linha = linha.replace("<-", "=") + ";";
                }
                if (linha.contains(":=")) {
                    linha = linha.replace(":", "") + ";";
                }

                if (varAt == true && linha.contains(":") && linha.contains("funcao") == false) {
                    convertVarJava();

                }

                if (linha.contains("escreval") || linha.contains("escreva")) {
                    convertEscrJava();

                }

                if (linha.contains("se") && linha.contains("entao") && (linha.contains("=") || linha.contains("<") || linha.contains(">") || linha.contains("<=") || linha.contains(">="))) {
                    convertSe();

                } else if (ifAtivo == true && linha.contains("fimse")) {
                    linha = linha.replace("fimse", "}");
                } else if (ifAtivo == true && linha.contains("senao")) {
                    linha = linha.replaceAll("senao", "}else{");
                }
                if (linha.contains("para") && linha.contains("de") && linha.contains("faca")) {

                    convertFor();
                    forAtivo = true;
                } else if (forAtivo == true && linha.contains("fimpara")) {
                    linha = linha.replace("fimpara", "}");

                }
                if (linha.contains("repita")) {
                    repitaAtivo = true;
                    convertRepita();

                }
                if (linha.contains("ate") && repitaAtivo == true) {
                    linha = linha.replace("ate", "}while(");
                    linha = linha + ");";
                    if (linha.contains("e") && linha.charAt(linha.indexOf("e")) == ' ' && linha.charAt(linha.indexOf("e")) == ' ') {
                        linha = linha.replaceAll("e", "&&");
                        if (linha.contains("e") && linha.charAt(linha.indexOf("e")) == ' ' && linha.charAt(linha.indexOf("e")) == ' ') {
                            linha = linha.replaceAll("e", "&&");
                        }
                        if (linha.contains("ou") && linha.charAt(linha.indexOf("u") + 1) == ' ' && linha.charAt(linha.indexOf("o") - 1) == ' ') {
                            linha = linha.replaceAll("ou", "||");
                        }
                        if (linha.contains("nao") && linha.charAt(linha.indexOf("o") + 1) == ' ' && linha.charAt(linha.indexOf("n") - 1) == ' ') {
                            linha = linha.replaceAll("nao", "!");
                        }
                        if (linha.contains("<>")) {
                            linha = linha.replaceAll("<>", "!=");
                        }

                        if (linha.contains("verdadeiro")) {
                            linha = linha.replaceAll("verdadeiro", "true");
                        }
                        if (linha.contains("falso")) {
                            linha = linha.replaceAll("falso", "false");
                        }
                    }
                    if (linha.contains(",")) {
                        linha = linha.replace(",", "][");
                    }
                    if (linha.contains("ou") && linha.charAt(linha.indexOf("u") + 1) == ' ' && linha.charAt(linha.indexOf("o") - 1) == ' ') {
                        linha = linha.replaceAll("ou", "||");
                    }
                    if (linha.contains("nao") && linha.charAt(linha.indexOf("o") + 1) == ' ' && linha.charAt(linha.indexOf("n") - 1) == ' ') {
                        linha = linha.replaceAll("nao", "!");
                    }
                    if (linha.contains("<>")) {
                        linha = linha.replaceAll("<>", "!=");
                    }

                    if (linha.contains("verdadeiro")) {
                        linha = linha.replaceAll("verdadeiro", "true");
                    }
                    if (linha.contains("falso")) {
                        linha = linha.replaceAll("falso", "false");
                    }
                    if (linha.contains("mod")) {
                        linha = linha.replace(" mod ", " % ");
                    }
                    repitaAtivo = false;
                }
                if (linha.contains("enquanto") && linha.contains("faca")) {
                    convertEnquanto();

                } else if (linha.contains("fimenquanto")) {
                    linha = linha.replace("fimenquanto", "}");
                }
                if (linha.contains("escolha") && linha.contains("fimescolha") == false) {
                    String var = null;

                    if (linha.contains("(")) {

                        var = linha.substring(linha.indexOf("(") + 1, linha.indexOf(")", linha.indexOf("(")));
                    } else {
                        if (linha.charAt(linha.indexOf("a", linha.indexOf("h")) + 1) == ' ') {
                            var = linha.substring(linha.indexOf("a", linha.indexOf("h")) + 2, linha.length());
                        } else {
                            var = linha.substring(linha.indexOf("a", linha.indexOf("h")) + 1, linha.length());
                        }
                    }
                    if (vares.contains(var)) {
                        convertEscolha();
                        escAtivo = true;
                    }

                }
                if (linha.contains("fimescolha") && escAtivo == true) {
                    linha = linha.replace("fimescolha", "}");
                    escAtivo = false;
                } else if (escAtivo == true) {
                    if (linha.contains("caso") && linha.contains("outrocaso") == false) {
                        linha = linha.replace("caso", "case") + ":";
                    } else if (linha.contains("outrocaso")) {
                        linha = linha.replace("outrocaso", "default") + ":";

                    } else if (linha.contains("interrompa")) {
                        linha = linha.replace("interrompa", "break") + ";";
                    }
                }
                if (linha.contains("leia") && linha.contains("(")) {
                    convertLeiaJava();
                }

                if (varAt == true && linha.contains("funcao")) {
                    convertFuncJava();
                    funcAt = true;
                } else if (funcAt == true && linha.contains("inicio")) {
                    linha = linha.replace("inicio", "");
                } else if (funcAt == true && linha.contains("fimfuncao")) {
                    linha = linha.replace("fimfuncao", "}");
                    funcAt = false;
                } else if (funcAt == true) {
                    linha = linha.replace("retorne", "return");
                    linha += ";";
                }

                arquivoConvertido += "\n" + linha;

            }
            return arquivoConvertido;

        } catch (Exception erro) {
            return "Não conseguimos converter. Por favor, verifique o código.";
        }

    }

    public String convertC(String caminho) {
        String arquivoConvertido = " ";
        try {
            BufferedReader leitor = new BufferedReader(new FileReader(caminho));

            while ((linha = leitor.readLine()) != null) {
                linha = linha.toLowerCase();
                linha = linha.replace("fimalgoritmo", "system(\"pause>>NULL\"); \n }");
                if (linha.contains("algoritmo")) {
                    linha = "";
                }

                g = linha.indexOf(":");
                if (linha.contains("var")) {
                    varAt = true;

                    linha = linha.replace("var", "#include <stdio.h> \n #include<stdlib.h> \n #include<conio.h> \n int main(){");

                } else if (linha.contains("inicio")) {
                    varAt = false;

                }
                if (funcAt == false && linha.equals("inicio")) {
                    linha = linha.replace("inicio", "");
                }
                if (linha.contains("<-")) {
                    linha = linha.replace("<-", "=") + ";";
                }
                if (linha.contains(":=")) {
                    linha = linha.replace(":", "") + ";";
                }
                linha = linha.replace("fimalgoritmo", "system('pause>>NULL'); \n }");

                if (varAt == true && linha.contains(":") && linha.contains("funcao") == false) {
                    convertVarC();

                }

                if (linha.contains("escreval") || linha.contains("escreva")) {
                    convertEscrC();

                }

                if (linha.contains("se") && linha.contains("entao") && (linha.contains("=") || linha.contains("<") || linha.contains(">") || linha.contains("<=") || linha.contains(">="))) {
                    convertSe();

                } else if (ifAtivo == true && linha.contains("fimse")) {
                    linha = linha.replace("fimse", "}");
                } else if (ifAtivo == true && linha.contains("senao")) {
                    linha = linha.replaceAll("senao", "}else{");
                }
                if (linha.contains("para") && linha.contains("de") && linha.contains("faca")) {
                    convertFor();
                    forAtivo = true;
                } else if (forAtivo == true && linha.contains("fimpara")) {
                    linha = linha.replace("fimpara", "}");

                }
                if (linha.contains("repita")) {
                    repitaAtivo = true;
                    convertRepita();

                }
                if (linha.contains("ate") && repitaAtivo == true) {
                    linha = linha.replace("ate", "}while(");
                    linha = linha + ");";
                    if (linha.contains("e") && linha.charAt(linha.indexOf("e")) == ' ' && linha.charAt(linha.indexOf("e")) == ' ') {
                        linha = linha.replaceAll("e", "&&");
                        if (linha.contains("e") && linha.charAt(linha.indexOf("e")) == ' ' && linha.charAt(linha.indexOf("e")) == ' ') {
                            linha = linha.replaceAll("e", "&&");
                        }
                        if (linha.contains("ou") && linha.charAt(linha.indexOf("u") + 1) == ' ' && linha.charAt(linha.indexOf("o") - 1) == ' ') {
                            linha = linha.replaceAll("ou", "||");
                        }
                        if (linha.contains("nao") && linha.charAt(linha.indexOf("o") + 1) == ' ' && linha.charAt(linha.indexOf("n") - 1) == ' ') {
                            linha = linha.replaceAll("nao", "!");
                        }
                        if (linha.contains("<>")) {
                            linha = linha.replaceAll("<>", "!=");
                        }

                        if (linha.contains("verdadeiro")) {
                            linha = linha.replaceAll("verdadeiro", "true");
                        }
                        if (linha.contains("falso")) {
                            linha = linha.replaceAll("falso", "false");
                        }
                    }
                    if (linha.contains("ou") && linha.charAt(linha.indexOf("u") + 1) == ' ' && linha.charAt(linha.indexOf("o") - 1) == ' ') {
                        linha = linha.replaceAll("ou", "||");
                    }
                    if (linha.contains("nao") && linha.charAt(linha.indexOf("o") + 1) == ' ' && linha.charAt(linha.indexOf("n") - 1) == ' ') {
                        linha = linha.replaceAll("nao", "!");
                    }
                    if (linha.contains("<>")) {
                        linha = linha.replaceAll("<>", "!=");
                    }

                    if (linha.contains("verdadeiro")) {
                        linha = linha.replaceAll("verdadeiro", "true");
                    }
                    if (linha.contains("falso")) {
                        linha = linha.replaceAll("falso", "false");
                    }
                    if (linha.contains(",")) {
                        linha = linha.replace(",", "][");
                    }
                    if (linha.contains("mod")) {
                        linha = linha.replace(" mod ", " % ");
                    }
                    repitaAtivo = false;
                }
                if (linha.contains("enquanto") && linha.contains("faca")) {
                    convertEnquanto();

                } else if (linha.contains("fimenquanto")) {
                    linha = linha.replace("fimenquanto", "}");
                }
                if (linha.contains("escolha") && linha.contains("fimescolha") == false) {
                    String var = null;

                    if (linha.contains("(")) {

                        var = linha.substring(linha.indexOf("(") + 1, linha.indexOf(")", linha.indexOf("(")));
                    } else {
                        if (linha.charAt(linha.indexOf("a", linha.indexOf("h")) + 1) == ' ') {
                            var = linha.substring(linha.indexOf("a", linha.indexOf("h")) + 2, linha.length());
                        } else {
                            var = linha.substring(linha.indexOf("a", linha.indexOf("h")) + 1, linha.length());
                        }
                    }
                    if (vares.contains(var)) {
                        convertEscolha();
                        escAtivo = true;
                    }

                }
                if (linha.contains("fimescolha") && escAtivo == true) {
                    linha = linha.replace("fimescolha", "}");
                    escAtivo = false;
                } else if (escAtivo == true) {
                    if (linha.contains("caso") && linha.contains("outrocaso") == false) {
                        linha = linha.replace("caso", "case") + ":";
                    } else if (linha.contains("outrocaso")) {
                        linha = linha.replace("outrocaso", "default") + ":";

                    } else if (linha.contains("interrompa")) {
                        linha = linha.replace("interrompa", "break") + ";";
                    }
                }
                if (linha.contains("leia") && linha.contains("(")) {
                    convertLeiaC();
                }

                if (varAt == true && linha.contains("funcao")) {
                    convertFuncC();
                    funcAt = true;
                } else if (funcAt == true && linha.contains("inicio")) {
                    linha = linha.replace("inicio", "");
                } else if (funcAt == true && linha.contains("fimfuncao")) {
                    linha = linha.replace("fimfuncao", "}");
                    funcAt = false;
                } else if (funcAt == true) {
                    linha = linha.replace("retorne", "return");
                    linha += ";";
                }

                arquivoConvertido += "\n" + linha;

            }

            return arquivoConvertido;

        } catch (Exception erro) {

            return "Não conseguimos converter. Por favor, verifique o código.";
        }

    }

    private void convertVarJava() {

        if (linha.contains("vetor")) {
            String b, a;

            a = linha.substring(0, g);

            int c = linha.indexOf("]", linha.indexOf("["));
            int f = linha.lastIndexOf(".", linha.indexOf("]"));
            String p = linha.substring(f + 1, c);
            String v = linha.substring(0, g);
            b = linha.substring(linha.indexOf("e", c) + 2, linha.length());
            b = b.replace(" ", "");

            if (b.equals("inteiro")) {
                b = "int";
            }
            if (b.equals("real")) {
                b = "double";
            }
            if (b.equals("caractere") || b.equals("caracter")) {
                b = "String";
            }
            if (p.contains(",")) {
                p = p.replace(",", "]");
                p = p.replace("1..", "[");
            }
            linha = b + " " + v + "[]=new " + b + "[" + p + "];";
            vares.add(b);
            vares.add(v);

        } else {
            String b, a;

            a = linha.substring(0, g);

            b = linha.substring(g + 1, linha.length());
            b = b.replace(" ", "");
            if (b.equals("inteiro")) {
                b = "int";
            }
            if (b.equals("real")) {
                b = "double";
            }
            if (b.equals("caractere") || b.equals("caracter")) {
                b = "String";
            }
            linha = b + " " + a + ";";
            String vari = "";
            if (linha.contains(",")) {
                char[] d = a.toCharArray();
                for (int i = 0; i < d.length; i++) {

                    if (d[i] != ',') {
                        if (d[i] == ' ') {
                        } else {
                            vari += d[i];
                        }
                        if (i == a.length() - 1) {
                            vares.add(b);
                            vares.add(vari);

                        }
                    } else if (a.charAt(i) == ',') {
                        vares.add(b);
                        vares.add(vari);
                        vari = "";
                    }
                }

            } else {
                vares.add(b);
                vares.add(a);
            }

        }

    }

    private void convertEscrJava() {
        l = linha.indexOf("(");

        if (l != -1) {
            linha = linha.replace("escreval", "System.out.println");
            linha = linha.replace("escreva", "System.out.println");
            linha = linha.replace(",", "+");
            linha += ";";
        }
    }

    private void convertSe() {
        if (linha.contains("mod")) {
            linha = linha.replace(" mod ", " % ");
        }
        if (linha.contains("(")) {
            linha = linha.replace(" ", "");
            linha = linha.replace("se(", "if(");

            linha = linha.replace("entao", "{");
        } else {
            linha = linha.replace(" ", "");
            linha = linha.replace("se ", "if(");
            linha = linha.replace("entao", "){");
        }
        if (linha.contains("e") && linha.charAt(linha.indexOf("e") - 1) == ' ' && linha.charAt(linha.indexOf("e") + 1) == ' ') {
            linha = linha.replaceAll("e", "&&");
        }
        if (linha.contains("ou") && linha.charAt(linha.indexOf("u") + 1) == ' ' && linha.charAt(linha.indexOf("o") - 1) == ' ') {
            linha = linha.replaceAll("ou", "||");
        }
        if (linha.contains("nao") && linha.charAt(linha.indexOf("o") + 1) == ' ' && linha.charAt(linha.indexOf("n") - 1) == ' ') {
            linha = linha.replaceAll("nao", "!");
        }
        if (linha.contains("<>")) {
            linha = linha.replaceAll("<>", "!=");
        }

        if (linha.contains("verdadeiro")) {
            linha = linha.replaceAll("verdadeiro", "true");
        }
        if (linha.contains("falso")) {
            linha = linha.replaceAll("falso", "false");
        }

        ifAtivo = true;

        if (linha.contains(">=") == false && linha.contains(">=") == false && linha.contains("!=") == false) {
            linha = linha.replace("=", "==");
        }
        if (linha.contains(",")) {
            linha = linha.replace(",", "][");
        }
    }

    private void convertFor() {
        int t = linha.indexOf("a", linha.indexOf("r"));

        String b = linha.substring(t + 2, linha.indexOf("d", t) - 1);
        int j = linha.indexOf("e", linha.indexOf("d"));
        String g = linha.substring(j + 2, linha.indexOf("a", j) - 1);
        String d = linha.substring(linha.indexOf("e", linha.indexOf("d")) + 2, linha.indexOf("a", linha.indexOf("e")) - 1);
        if (b.contains(",")) {
            b = b.replace(",", "][");
        }
        if (linha.contains("passo")) {
            String a = linha.substring(linha.indexOf("e", linha.indexOf("a", linha.indexOf("e"))) + 2, linha.indexOf("p", linha.indexOf("a", linha.indexOf("e"))) - 1);
            String ce = linha.substring(linha.indexOf("o", linha.lastIndexOf("s")) + 2);
            String c = ce.substring(0, ce.indexOf("f") - 1);
            int e = Integer.parseInt(c);
            if (Integer.parseInt(c) == -1) {
                linha = "for(" + b + "=" + g + "; " + b + "<=" + a + "; " + b + "--){";
            } else if (Integer.parseInt(c) == 1) {
                linha = "for(" + b + "=" + g + "; " + b + "<=" + a + "; " + b + "++){";
            } else if (Integer.parseInt(c) > 1) {
                linha = "for(" + b + "=" + g + "; " + b + "<=" + a + "; " + b + "+" + c + "){";
            } else if (Integer.parseInt(c) < -1) {
                linha = "for(" + b + "=" + g + "; " + b + "<=" + a + "; " + b + "-" + c + "){";
            }
        } else {
            String lol = linha.substring(linha.indexOf("e", linha.indexOf("a", linha.indexOf("e"))) + 2, linha.indexOf("f", linha.indexOf("a", linha.indexOf("e"))) - 1);

            linha = "for(" + b + "=" + g + "; " + b + "<=" + lol + ";" + b + "++){";

        }

    }

    private void convertRepita() {
        linha = linha.replace("repita", "do{");
    }

    private void convertEnquanto() {
        if (linha.contains(",")) {
            linha = linha.replace(",", "][");
        }
        if (linha.contains(",")) {
            linha = linha.replace(",", "][");
        }
        if (linha.contains("(")) {
            linha = linha.replace("enquanto", "while");
            linha = linha.replace("faca", "{");
        } else {
            linha = linha.replace("enquanto", "while(");
            linha = linha.replace("faca", "){");
        }
        if (linha.contains("e") && linha.charAt(linha.indexOf("e")) == ' ' && linha.charAt(linha.indexOf("e")) == ' ') {
            linha = linha.replaceAll("e", "&&");
        }
        if (linha.contains("ou") && linha.charAt(linha.indexOf("u") + 1) == ' ' && linha.charAt(linha.indexOf("o") - 1) == ' ') {
            linha = linha.replaceAll("ou", "||");
        }
        if (linha.contains("nao") && linha.charAt(linha.indexOf("o") + 1) == ' ' && linha.charAt(linha.indexOf("n") - 1) == ' ') {
            linha = linha.replaceAll("nao", "!");
        }
        if (linha.contains("<>")) {
            linha = linha.replaceAll("<>", "!=");
        }

        if (linha.contains("verdadeiro")) {
            linha = linha.replaceAll("verdadeiro", "true");
        }
        if (linha.contains("falso")) {
            linha = linha.replaceAll("falso", "false");
        }
    }

    private void convertLeiaJava() {
        int x = linha.indexOf("(");
        int y = linha.indexOf(")", x);
        String var = linha.substring(x + 1, y);
        String vart;
        String tipo;

        if (linha.contains("[")) {
            if (linha.charAt(linha.indexOf("[", x) - 1) == ' ') {

                vart = linha.substring(x + 1, linha.indexOf("[", x) - 1);
                if (vart.contains(",")) {
                    vart = vart.replace(",", "][");
                }
            } else {
                vart = linha.substring(x + 1, linha.indexOf("[", x));
            }
            tipo = vares.get(vares.indexOf(vart) - 1).toString();

        } else {
            tipo = vares.get(vares.indexOf(var) - 1).toString();

        }
        switch (tipo) {
            case "String":
                linha = var + "=s.next();";
                break;
            case "int":
                linha = var + "=s.nextInt();";
                break;
            case "double":
                linha = var + "=s.nextDouble();";
                break;
        }

    }

    private void convertEscolha() {
        if (linha.contains("(")) {
            linha = linha.replace("escolha", "switch") + "{";
        } else {
            linha = linha.replace("escolha", "switch(") + "){";
        }
    }

    private void convertFuncJava() {
        String type = linha.substring(g + 1, linha.length());
        String nomeFunc = linha.substring(linha.indexOf("o", linha.indexOf("a")) + 2, g);
        switch (type) {
            case "inteiro":
                linha = "int " + nomeFunc + "{";
                break;
            case "real":
                linha = "double " + nomeFunc + "{";
                break;
            case "caracter":
                linha = "String " + nomeFunc + "{";
                break;
            case "caractere":
                linha = "String " + nomeFunc + "{";
                break;
        }
    }

    private void convertVarC() {

        if (linha.contains("vetor")) {
            int c = linha.indexOf("]", linha.indexOf("["));
            int f = linha.lastIndexOf(".", linha.indexOf("]"));
            String p = linha.substring(f + 1, c);
            String v = linha.substring(0, g);
            v = v.replace(" ", "");
            String b = linha.substring(linha.indexOf("e", c) + 2, linha.length());
            b = b.replace(" ", "");
            if (b.equals("inteiro")) {
                b = "int ";
            }
            if (b.equals("real")) {
                b = "float ";
            }
            if (b.equals("caractere") || b.equals("caracter")) {
                b = "char ";
            }
            v = v.replace(" ", "");
            if (p.contains(",")) {
                p = p.replace(",", "]");
                p = p.replace("1..", "[");
            }
            linha = b + v + "[" + p + "];";
            vares.add(b);
            vares.add(v);

        } else {

            String a = linha.substring(0, g);
            a = a.replace(" ", "");
            String b = linha.substring(g + 1, linha.length());
            b = b.replace(" ", "");
            if (b.equals("inteiro") || b.equals(" inteiro")) {
                b = "int";
            }
            if (b.equals("real") || b.equals(" real")) {
                b = "float";
            }
            if ((b.equals("caractere") || b.equals("caracter")) || (b.equals(" caractere") || b.equals(" caracter"))) {
                b = "char";
            }
            linha = b + " " + a + ";";
            String vari = "";
            if (linha.contains(",")) {
                char[] d = a.toCharArray();
                for (int i = 0; i < d.length; i++) {

                    if (d[i] != ',') {
                        if (d[i] == ' ') {
                        } else {
                            vari += d[i];
                        }
                        if (i == a.length() - 1) {
                            vari = vari.replace(" ", "");
                            vares.add(b);
                            vares.add(vari);

                        }
                    } else if (a.charAt(i) == ',') {
                        vari = vari.replace(" ", "");
                        vares.add(b);
                        vares.add(vari);
                        vari = "";
                    }
                }

            } else {
                a = a.replace(" ", "");
                b = b.replace(" ", "");
                vares.add(b);
                vares.add(a);

            }

        }

    }

    private void convertFuncC() {
        String type = linha.substring(g + 1, linha.length());
        String nomeFunc = linha.substring(linha.indexOf("o", linha.indexOf("a")) + 2, g);
        switch (type) {
            case "inteiro":
                linha = "int " + nomeFunc + "{";
                break;
            case "real":
                linha = "float " + nomeFunc + "{";
                break;
            case "caracter":
                linha = "char " + nomeFunc + "{";
                break;
            case "caractere":
                linha = "char " + nomeFunc + "{";
                break;
        }
    }

    private void convertLeiaC() {
        int x;
        int y;
        String as = "";
        if (linha.contains("[")) {
            x = linha.indexOf("(");
            y = linha.indexOf("[", x);
            as = linha.substring(y, linha.indexOf("]") + 1);
            if (as.contains(",")) {
                as = as.replace(",", "]");
                as = as.replace("1..", "[");
            }
            linha = linha.replace(as, "");
        } else {
            x = linha.indexOf("(");
            y = linha.indexOf(")", x);
        }

        String var = linha.substring(x + 1, y).replace(" ", "");
        String var2 = linha.substring(x + 1, linha.indexOf(")"));

        if (vares.contains(var)) {
            String type = vares.get(vares.indexOf(var) - 1).toString().replace(" ", "");
            switch (type) {
                case "int":

                    linha = "scanf(\"%d\",&" + var2 + as + ");";
                    break;
                case "char":
                    linha = "scanf(\"%c\",&" + var2 + as + ");";

                    break;
                case "float":
                    linha = "scanf(\"%f\",&" + var2 + as + ");";

                    break;
            }

        } else {
            linha = linha.replace("leia", "scanf");
        }

    }

    private void convertEscrC() {
        String as = "";
        if (linha.contains(",") == false && linha.contains("escreva") && linha.contains("escreval") == false) {
            String varia;

            if (linha.contains("[")) {
                varia = linha.substring(linha.indexOf("(") + 1, linha.indexOf("[", linha.indexOf("(")));
                varia = varia.replaceAll(" ", "");
                as = linha.substring(linha.indexOf("["), linha.indexOf("]") + 1);
                if (as.contains(",")) {
                    as = as.replace(",", "][");

                    varia = varia.replace(",", "][");

                }
                linha = linha.replace(as, "");
            } else {
                varia = linha.substring(linha.indexOf("(") + 1, linha.indexOf(")", linha.indexOf("(")));
                varia = varia.replaceAll(" ", "");
            }
            linha = linha.replace(")", " )");
            if (vares.contains(varia)) {
                String tipo = vares.get(vares.indexOf(varia) - 1).toString().replace(" ", "");

                switch (tipo) {
                    case "int":
                        linha = linha.replace(varia + " ", "\"%d\"," + varia + as);

                        break;
                    case "char":
                        linha = linha.replace(varia + " ", "\"%c\"," + varia + as);

                        break;
                    case "float":
                        linha = linha.replace(varia + " ", "\"%5.2f\"," + varia + as);

                        break;
                }
                linha = linha.replace("escreva", "printf") + ";";
            } else {

                linha = linha.replace("escreva", "printf") + ";";
            }
        } else if (linha.contains(",") == false && linha.contains("escreval")) {
            String varia;

            if (linha.contains("[")) {
                varia = linha.substring(linha.indexOf("(") + 1, linha.indexOf("[", linha.indexOf("(")));
                varia = varia.replaceAll(" ", "");
                as = linha.substring(linha.indexOf("["), linha.indexOf("]") + 1);
                if (as.contains(",")) {
                    as = as.replace(",", "][");
                    varia = varia.replace(",", "][");

                }
                linha = linha.replace(as, "");
            } else {
                varia = linha.substring(linha.indexOf("(") + 1, linha.indexOf(")", linha.indexOf("(")));
                varia = varia.replaceAll(" ", "");
            }

            linha = linha.replace(")", " )");
            if (vares.contains(varia)) {
                String tipo = vares.get(vares.indexOf(varia) - 1).toString().replace(" ", "");

                if (null != tipo) {
                    switch (tipo) {
                        case "int":
                            linha = linha.replace(varia + " ", "\"%d\"," + varia + as);
                            break;
                        case "char":
                            linha = linha.replace(varia + " ", "\"%c\"," + varia + as);
                            break;
                        case "float":
                            linha = linha.replace(varia + " ", "\"%5.2f\"," + varia + as);
                            break;
                    }
                }
                linha = linha.replace("escreval", "printf") + ";";
            } else {
                linha = linha.replace("escreval", "printf") + ";";
            }
        } else {
            String vari = "";
            linha = linha.replaceAll("\"", "");
            char li[] = new char[linha.length()];
            boolean virAt = false;
            String var = "";
            String fvar = "";
            int len = linha.length();

            li = linha.toCharArray();
            for (int i = 0; i < len; i++) {

                if (li[i] == '(' && virAt == false) {
                    virAt = true;

                } else if (li[i] != ',' && li[i] != ')' && virAt == true) {
                    var += li[i];

                }
                if ((li[i] == ',' || li[i] == ')') && virAt == true) {
                    var = var.replace(" ", "");
                    String var2 = "";
                    linha = linha.replace(",", " ,  ");
                    linha = linha.replace(")", " )");

                    if (var.contains("[")) {

                        var2 = var.substring(0, var.indexOf("["));
                        String tg = linha.substring(linha.indexOf("["), linha.indexOf("]") + 1);
                        if (as.contains(",")) {
                            tg = tg.replace(",", "]");

                            var = var.replace(",", "][");

                        }
                        linha = linha.replace(tg, "");

                    } else {
                        var2 = var;
                    }
                    if (vares.contains(var2)) {
                        String type = String.valueOf(vares.get(vares.indexOf(var2) - 1));
                        type = type.replace(" ", "");
                        switch (type) {
                            case "int":
                                if (linha.contains("escreval")) {
                                    linha = linha.replace("escreval", "printf");
                                } else {
                                    linha = linha.replace("escreva", "printf");
                                }

                                linha = linha.replaceFirst(var2 + " ", " %d ");

                                fvar += "," + var;
                                break;
                            case "char":
                                if (linha.contains("escreval")) {
                                    linha = linha.replace("escreval", "printf");
                                } else {
                                    linha = linha.replace("escreva", "printf");
                                }

                                linha = linha.replaceFirst(var2 + " ", " %c ");

                                fvar += "," + var;
                                break;
                            case "float":
                                if (linha.contains("escreval")) {
                                    linha = linha.replace("escreval", "printf");
                                } else {
                                    linha = linha.replace("escreva", "printf");
                                }

                                linha = linha.replaceFirst(var2 + " ", " %5.2f ");

                                fvar += "," + var;

                                break;
                        }
                        var = "";
                    } else {
                        if (linha.contains("escreval")) {
                            linha = linha.replace("escreval", "printf");
                        } else {
                            linha = linha.replace("escreva", "printf");
                        }
                        var = "";
                    }

                }
            }
            linha = linha.replace("(", "(\"");
            linha = linha.replace(")", "\"" + fvar + ");");

        }
    }
}
