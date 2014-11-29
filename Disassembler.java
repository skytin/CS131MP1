package Disassembler;

import java.io.*;
import java.util.*;

import javax.swing.*;

public class Try1 {
	
	public static boolean isNumber(String num){
		try{
			int number = Integer.parseInt(num);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	static ArrayList<ArrayList<String>> variables = new ArrayList<ArrayList<String>>();
	
	public static void main(String[] args)   {
		String fileName = JOptionPane.showInputDialog("Enter filename: ");
		String fileName1 = null;
		int varDone = 0;
		int codeStart = 0;
		int[] ax = new int[2];						//registers
		int[] bx = new int[2];
		int[] cx = new int[2];		
		int[] dx = new int[2];
		PrintWriter outputStream = null;
		try{
			BufferedReader inputStream = new BufferedReader(new FileReader(fileName));
            fileName = fileName.substring(0, fileName.length()-4);
            fileName1 = fileName + ".cpp";
            outputStream = new PrintWriter(new FileOutputStream(fileName1));
            String line = inputStream.readLine();
            StringBuilder b = new StringBuilder();
            while (line != null) {
            	b.append(line);
            	b.append("\n");
            	line = inputStream.readLine();
            }
            inputStream.close();
            
            outputStream.println("# include <iostream>");
            outputStream.println("# include <iomanip>");
            outputStream.println("# include <cstdlib>");
            outputStream.println("# include <string>");
            outputStream.println();
            outputStream.println("using namespace std;");
            outputStream.println();
            outputStream.println("main(){");

            StringTokenizer token = new StringTokenizer(b.toString(), "\n");
            variables.add(new ArrayList<String>());
            variables.add(new ArrayList<String>());
            variables.add(new ArrayList<String>());
            while(token.hasMoreTokens()){
            	String token1 = token.nextToken();
            	String value = null;
            	if(token1.contains(".data")){
            		varDone = 1;
            	}else if(token1.contains(".code")){
            		codeStart = 1;
            	}else if(token1.contains(".stack")){
            		varDone = 2;
            	}else if(varDone == 1){
                	String varName = token1.substring(0, token1.indexOf(" ")).replaceAll("\t", "").replaceAll(" ", "");
            		if((token1.contains("\""))){
            			value = token1.substring(token1.indexOf("\""), token1.lastIndexOf("\"")+1);
                        variables.get(0).add(varName);
                        variables.get(1).add(value);
                        variables.get(2).add(value);
                	}else if((token1.contains("'"))){
                		value = token1.substring(token1.indexOf("'"), token1.lastIndexOf("'")+1).replaceAll(" ", "");
                		variables.get(0).add(varName);
                        variables.get(1).add(value);
                        variables.get(2).add(value);
                	}else if(!token1.contains("?")){
                		value = token1.substring(token1.lastIndexOf(" "), token1.length()).replaceAll(" ", "");
                		variables.get(0).add(varName);
                        variables.get(1).add(value);
                        variables.get(2).add(value);
                	}else if(token1.contains("?")){
                		value = "?";
                		variables.get(0).add(varName);
                        variables.get(1).add(value);
                        variables.get(2).add(value);
                	}
            		System.out.println(varName +" = "+ value);
            	}
            }
            // variable declarations DONE!!!
            
            StringTokenizer token2 = new StringTokenizer(b.toString(), "\n");
            while(token2.hasMoreTokens()){
            	String token3 = token2.nextToken();
            	String value = null;
            	if(token3.contains(".data")){
            		varDone = 1;
            	}else if(token3.contains(".code")){
            		codeStart = 1;
            	}else if(varDone == 1){
                	for(int i = 0;(i < variables.get(0).size()) && (token3.contains("mov"));i++){
                		String var = token3.substring(token3.indexOf(" ")+1, token3.indexOf(",")).replaceAll(" ", "");
                		if(variables.get(0).get(i).equalsIgnoreCase(var)){
                			value = token3.substring(token3.indexOf(",")+1, token3.length());
                			if(!value.contains("\"")){
                				value = token3.substring(token3.indexOf(",")+1, token3.length()).replaceAll(" ", "");
                			}
                			variables.get(2).set(i, value);
                		}
                	}
            	}
            }
            
            for(int i = 0;i < variables.get(0).size();i++){
            	String varName = variables.get(0).get(i);
        		String value = variables.get(2).get(i);
        		String initVal = variables.get(1).get(i);
            	if((isNumber(value)) && (!initVal.equals("?"))){
            		outputStream.println("int "+ varName +" = "+ value +";");
            	}else if((isNumber(value)) && (initVal.equals("?"))){
            		outputStream.println("int "+ varName +";");
            	}else if(initVal.contains("'")){
            		outputStream.println("char "+ varName +" = "+ initVal +";");
            	}
            }
            
            outputStream.close();
            for(int i = 0;i < variables.get(0).size();i++){
            	System.out.println(i +" : "+ variables.get(0).get(i) +" = "+ variables.get(1).get(i));
            	System.out.println(i +" : "+ variables.get(0).get(i) +" = "+ variables.get(2).get(i));
            }
		}
		catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "The file "+ fileName +" could not be found or could not be opened.");
        }
        catch (IOException e) {
        	JOptionPane.showMessageDialog(null,"Error reading from "+ fileName);
        }
	}
}
