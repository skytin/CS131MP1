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
		String[] ax = new String[3];						//registers
		String[] bx = new String[3];
		String[] cx = new String[3];		
		String[] dx = new String[3];
		PrintWriter outputStream = null;
		try{
			BufferedReader inputStream = new BufferedReader(new FileReader(fileName));
            fileName = fileName.substring(0, fileName.length()-4);
            fileName1 = fileName + "_Converted.cpp";
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
            outputStream.println("int main(){");

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
                	String varName = token1.substring(0, token1.indexOf("db")).replaceAll("\t", "").replaceAll(" ", "");
            		if((token1.contains("\""))){
            			value = token1.substring(token1.indexOf("\""), token1.lastIndexOf("\"")+1);
                        variables.get(0).add(varName);
                        variables.get(1).add(value);
                        variables.get(2).add(value);
                	}else if((token1.contains("'"))){
                		value = token1.substring(token1.indexOf("'"), token1.lastIndexOf("'")+1);
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
            String value = null;
            while(token2.hasMoreTokens()){
            	String token3 = token2.nextToken();
            	if(token3.contains(".data")){
            		varDone = 1;
            	}else if(token3.contains(".code")){
            		codeStart = 1;
            	}else if(varDone == 1){
            		if(token3.contains("mov") || token3.contains("add")){
            			for(int i = 0;i < variables.get(0).size();i++){
            				String var = null;
            				if(token3.contains("mov")){
            					var = token3.substring(token3.indexOf("mov"), token3.indexOf(",")).replaceAll("mov", "").replaceAll(" ", "");
            				}else if(token3.contains("add")){
            					var = token3.substring(token3.indexOf("add"), token3.indexOf(",")).replaceAll("add", "").replaceAll(" ", "");
            				}
                    		value = token3.substring(token3.indexOf(",")+1, token3.length()).replaceAll(" ", "");
                    		if(variables.get(0).get(i).equalsIgnoreCase(var)){
                    			value = token3.substring(token3.indexOf(",")+1, token3.length());
                    			if(!value.contains("\"")){
                    				value = token3.substring(token3.indexOf(",")+1, token3.length()).replaceAll(" ", "");
                    			}
                    			variables.get(2).set(i, value);
                    		}else if(var.equalsIgnoreCase("al")){
                    			ax[1] = value;
                    		}else if(var.equalsIgnoreCase("ah")){
                    			ax[0] = value;
                    		}else if(var.equalsIgnoreCase("bl")){
                    			bx[1] = value;
                    		}else if(var.equalsIgnoreCase("bh")){
                    			bx[0] = value;
                    		}else if(var.equalsIgnoreCase("cl")){
                    			cx[1] = value;
                    		}else if(var.equalsIgnoreCase("ch")){
                    			cx[0] = value;
                    		}else if(var.equalsIgnoreCase("dl")){
                    			dx[1] = value;
                    		}else if(var.equalsIgnoreCase("dh")){
                    			dx[0] = value;
                    		}else if(var.equalsIgnoreCase("ax")){
                    			if(value.contains("h")){
                    				value = value.replaceAll("h", "");
                    			}else if(value.contains("d")){
                    				value = value.replaceAll("d", "");
                    			}
                    			if(isNumber(value)){
                    				int v = Integer.parseInt(value);
                    				if(v > 256){
                    					ax[0] = (v - 256) + "";
                    					ax[1] = 256 + "";
                    				}else if(v <= 256){
                    					ax[1] = v + "";
                    				}
                    			}else{
                    				ax[2] = value;
                    			}
                    		}else if(var.equalsIgnoreCase("bx")){
                    			if(value.contains("h")){
                    				value = value.replaceAll("h", "");
                    			}else if(value.contains("d")){
                    				value = value.replaceAll("d", "");
                    			}
                    			if(isNumber(value)){
                    				int v = Integer.parseInt(value);
                    				if(v > 256){
                    					bx[0] = (v - 256) + "";
                    					bx[1] = 256 + "";
                    				}else if(v <= 256){
                    					bx[1] = v + "";
                    				}
                    			}else{
                    				bx[2] = value;
                    			}
                    		}else if(var.equalsIgnoreCase("cx")){
                    			if(value.contains("h")){
                    				value = value.replaceAll("h", "");
                    			}else if(value.contains("d")){
                    				value = value.replaceAll("d", "");
                    			}
                    			if(isNumber(value)){
                    				int v = Integer.parseInt(value);
                    				if(v > 256){
                    					cx[0] = (v - 256) + "";
                    					cx[1] = 256 + "";
                    				}else if(v <= 256){
                    					cx[1] = v + "";
                    				}
                    			}else{
                    				cx[2] = value;
                    			}
                    		}else if(var.equalsIgnoreCase("dx")){
                    			if(value.contains("h")){
                    				value = value.replaceAll("h", "");
                    			}else if(value.contains("d")){
                    				value = value.replaceAll("d", "");
                    			}
                    			if(isNumber(value)){
                    				int v = Integer.parseInt(value);
                    				if(v > 256){
                    					dx[0] = (v - 256) + "";
                    					dx[1] = 256 + "";
                    				}else if(v <= 256){
                    					dx[1] = v + "";
                    				}
                    			}else{
                    				dx[2] = value;
                    			}
                    		}
                    	}
            		}
            	}
            }
            
            for(int i = 0;i < variables.get(0).size();i++){
            	String varName = variables.get(0).get(i);
        		value = variables.get(2).get(i);
        		String initVal = variables.get(1).get(i);
            	if((isNumber(value)) && (!initVal.equals("?"))){
            		outputStream.println("int "+ varName +" = "+ value +";");
            	}else if((isNumber(value)) && (initVal.equals("?"))){
            		outputStream.println("int "+ varName +";");
            	}else if(value.contains("'")){
            		if(initVal.contains("?")){
            			outputStream.println("char "+ varName +";");
            		}else{
            			outputStream.println("char "+ varName +" = "+ initVal +";");
            		}
            	}else if(initVal.contains("'")){
            		int c = 0;
            		for(int j = 0;j < initVal.length();j++){
            			if(initVal.charAt(j) == ','){
            				c++;
            			}
            		}
                	if(c < 2){
                		outputStream.println("char "+ varName +" = "+ initVal.replaceAll("$", "").replaceAll(" ", ""));
                	}else{
                		StringTokenizer tok = new StringTokenizer(initVal, ",");
                    	String echos = "";
                    	while(tok.hasMoreTokens()){
                    		String tok1 = tok.nextToken().replaceAll("'", "").replaceAll("$", "");
                   			String to = tok1.replaceAll(" ", "");
                   			if(tok1.equals("")){
                   				
                   			}else if(!isNumber(to)){
                   				echos += tok1;
                   			}else if(isNumber(to)){
                   				int a = Integer.parseInt(to);
                   				char d = (char)a;
                   				if(a != 10){
                   					echos += d;
                   				}
                   			}
                   		}
                    	if(initVal.contains("10")){
                    		outputStream.println("string "+ varName +" = \""+ echos.replaceAll("$", "") +"\" << endl;");
                    	}else{
                    		outputStream.println("string "+ varName +" = \""+ echos.replaceAll("$", "") +"\";");
                    	}
                    	
                	}
            	}else if(!value.contains("?")){
            		if((value.contains("ah")) || (value.contains("al")) || (value.contains("bh")) || (value.contains("bl")) || (value.contains("ch")) || (value.contains("cl")) 
            		|| (value.contains("dh")) || (value.contains("dl")) || (value.contains("ax")) || (value.contains("bx")) || (value.contains("cx")) || (value.contains("dx")) ){
            			if(initVal.contains("?")){
            				outputStream.println("int "+ varName +";");
            			}else{
            				outputStream.println("int "+ varName +" = "+ initVal +";");
            			}
            		}
            	}else if(value.contains("?")){
            		outputStream.println("int "+ varName +";");
            	}
            }
            // variables done
            
            StringTokenizer token4 = new StringTokenizer(b.toString(), "\n");
            value = null;
            String leaValue = null;
            String ifComp1 = null;
            String ifComp2 = null;
            while(token4.hasMoreTokens()){
            	String token3 = token4.nextToken();
            	if(token3.contains(".data")){
            		varDone = 1;
            	}else if(token3.contains(".code")){
            		codeStart = 1;
            	}else if(varDone == 1){
            		if(token3.contains("lea")){
            			if(token3.contains(";")){
            				leaValue = token3.substring(token3.indexOf(",")+1, token3.indexOf(";")).replaceAll(" ", "");
            			}else{
            				leaValue = token3.substring(token3.indexOf(",")+1, token3.length()).replaceAll(" ", "");
            			}
            		}else if(token3.contains("int 21h")){
            			if(ax[0].contains("09h") && !ax[2].contains("4c00")){
            				outputStream.println("	cout << "+ leaValue +";");
            			}else if(ax[0].contains("02h") && !ax[2].contains("4c00")){
            				outputStream.println("	cout << "+ dx[1] +";");
            			}
            		}else if(token3.contains("inc")){
            			outputStream.println("	"+ token3.substring(token3.indexOf("inc"), token3.length()).replaceAll("inc", "") +"++");
            		}else if(token3.contains("dec")){
            			outputStream.println("	"+ token3.substring(token3.indexOf("dec"), token3.length()).replaceAll("dec", "") +"--");
            		}else if(token3.contains("mov")){
            			for(int i = 0;i < variables.get(0).size();i++){
                    		String var = token3.substring(token3.indexOf("mov"), token3.indexOf(",")).replaceAll("mov", "").replaceAll(" ", "");
                    		value = token3.substring(token3.indexOf(",")+1, token3.length()).replaceAll(" ", "");
                    		if(variables.get(0).get(i).equalsIgnoreCase(var)){
                    			if(value.contains(";")){
                    				value = token3.substring(token3.indexOf(",")+1, token3.indexOf(";")).replaceAll(" ", "");
                    			}else{
                    				value = token3.substring(token3.indexOf(",")+1, token3.length()).replaceAll(" ", "");
                    			}
                    			if(value.contains("al")){
                    				outputStream.println("	"+ var +" = "+ ax[1] +";");
                    			}else if(value.contains("ah")){
                    				outputStream.println("	"+ var +" = "+ ax[0] +";");
                    			}else if(value.contains("bl")){
                    				outputStream.println("	"+ var +" = "+ bx[1] +";");
                    			}else if(value.contains("bh")){
                    				outputStream.println("	"+ var +" = "+ bx[0] +";");
                    			}else if(value.contains("cl")){
                    				outputStream.println("	"+ var +" = "+ cx[1] +";");
                    			}else if(value.contains("ch")){
                    				outputStream.println("	"+ var +" = "+ cx[0] +";");
                    			}else if(value.contains("dl")){
                    				outputStream.println("	"+ var +" = "+ dx[1] +";");
                    			}else if(value.contains("dh")){
                    				outputStream.println("	"+ var +" = "+ dx[0] +";");
                    			}else if(value.contains("ax")){
                    				outputStream.println("	"+ var +" = "+ ax[2] +";");
                    			}else if(value.contains("bx")){
                    				outputStream.println("	"+ var +" = "+ bx[2] +";");
                    			}else if(value.contains("cx")){
                    				outputStream.println("	"+ var +" = "+ cx[2] +";");
                    			}else if(value.contains("dx")){
                    				outputStream.println("	"+ var +" = "+ dx[2] +";");
                    			}
                    			//outputStream.println(var +" = "+ value +";");
                    		}else if(var.equalsIgnoreCase("al")){
                    			ax[1] = value;
                    		}else if(var.equalsIgnoreCase("ah")){
                    			ax[0] = value;
                    		}else if(var.equalsIgnoreCase("bl")){
                    			bx[1] = value;
                    		}else if(var.equalsIgnoreCase("bh")){
                    			bx[0] = value;
                    		}else if(var.equalsIgnoreCase("cl")){
                    			cx[1] = value;
                    		}else if(var.equalsIgnoreCase("ch")){
                    			cx[0] = value;
                    		}else if(var.equalsIgnoreCase("dl")){
                    			dx[1] = value;
                    		}else if(var.equalsIgnoreCase("dh")){
                    			dx[0] = value;
                    		}else if(var.equalsIgnoreCase("ax")){
                    			if(value.contains("h")){
                    				value = value.replaceAll("h", "");
                    			}else if(value.contains("d")){
                    				value = value.replaceAll("d", "");
                    			}
                    			if(isNumber(value)){
                    				int v = Integer.parseInt(value);
                    				if(v > 256){
                    					ax[0] = (v - 256) + "";
                    					ax[1] = 256 + "";
                    				}else if(v <= 256){
                    					ax[1] = v + "";
                    				}
                    			}else{
                    				ax[2] = value;
                    			}
                    		}else if(var.equalsIgnoreCase("bx")){
                    			if(value.contains("h")){
                    				value = value.replaceAll("h", "");
                    			}else if(value.contains("d")){
                    				value = value.replaceAll("d", "");
                    			}
                    			if(isNumber(value)){
                    				int v = Integer.parseInt(value);
                    				if(v > 256){
                    					bx[0] = (v - 256) + "";
                    					bx[1] = 256 + "";
                    				}else if(v <= 256){
                    					bx[1] = v + "";
                    				}
                    			}else{
                    				bx[2] = value;
                    			}
                    		}else if(var.equalsIgnoreCase("cx")){
                    			if(value.contains("h")){
                    				value = value.replaceAll("h", "");
                    			}else if(value.contains("d")){
                    				value = value.replaceAll("d", "");
                    			}
                    			if(isNumber(value)){
                    				int v = Integer.parseInt(value);
                    				if(v > 256){
                    					cx[0] = (v - 256) + "";
                    					cx[1] = 256 + "";
                    				}else if(v <= 256){
                    					cx[1] = v + "";
                    				}
                    			}else{
                    				cx[2] = value;
                    			}
                    		}else if(var.equalsIgnoreCase("dx")){
                    			if(value.contains("h")){
                    				value = value.replaceAll("h", "");
                    			}else if(value.contains("d")){
                    				value = value.replaceAll("d", "");
                    			}
                    			if(isNumber(value)){
                    				int v = Integer.parseInt(value);
                    				if(v > 256){
                    					dx[0] = (v - 256) + "";
                    					dx[1] = 256 + "";
                    				}else if(v <= 256){
                    					dx[1] = v + "";
                    				}
                    			}else{
                    				dx[2] = value;
                    			}
                    		}
                    	}
            		}else if(token3.contains("add")){
            			String add1 = token3.substring(token3.indexOf("add"), token3.indexOf(",")).replaceAll("add", "").replaceAll(" ", "");
            			String add2 = token3.substring(token3.indexOf(",")+1, token3.length()).replaceAll(" ", "");
            			if(add1.contains("al")){
            				ax[1] += " + " + add2;
            			}else if(add1.contains("ah")){
            				ax[0] += " + " + add2;
            			}else if(add1.contains("bl")){
            				bx[1] += " + " + add2;
            			}else if(add1.contains("bh")){
            				bx[0] += " + " + add2;
            			}else if(add1.contains("cl")){
            				cx[1] += " + " + add2;
            			}else if(add1.contains("ch")){
            				cx[0] += " + " + add2;
            			}else if(add1.contains("dl")){
            				dx[1] += " + " + add2;
            			}else if(add1.contains("dh")){
            				dx[0] += " + " + add2;
            			}else if((isNumber(add2)) || (add2.contains("'"))){
            				outputStream.println("	"+ add1 +" += "+ add2 +";");
            			}
            		}else if(token3.contains("sub")){
            			String sub1 = token3.substring(token3.indexOf("sub"), token3.indexOf(",")).replaceAll("sub", "").replaceAll(" ", "");
            			String sub2 = token3.substring(token3.indexOf(",")+1, token3.length()).replaceAll(" ", "");
            			if(sub1.contains("al")){
            				ax[1] += " - " + sub2;
            			}else if(sub1.contains("ah")){
            				ax[0] += " - " + sub2;
            			}else if(sub1.contains("bl")){
            				bx[1] += " - " + sub2;
            			}else if(sub1.contains("bh")){
            				bx[0] += " - " + sub2;
            			}else if(sub1.contains("cl")){
            				cx[1] += " - " + sub2;
            			}else if(sub1.contains("ch")){
            				cx[0] += " - " + sub2;
            			}else if(sub1.contains("dl")){
            				dx[1] += " - " + sub2;
            			}else if(sub1.contains("dh")){
            				dx[0] += " - " + sub2;
            			}else if((isNumber(sub2)) || (sub2.contains("'"))){
            				outputStream.println("	"+ sub1 +" -= "+ sub2 +";");
            			}
            		}else if(token3.contains("cmp")){
            			ifComp1 = token3.substring(token3.indexOf("cmp"), token3.indexOf(",")).replaceAll("cmp", "").replaceAll(" ", "");
            			if(token3.contains(";")){
            				ifComp2 = token3.substring(token3.indexOf(",")+1, token3.indexOf(";")).replaceAll(" ", "");
            			}else{
            				ifComp2 = token3.substring(token3.indexOf(",")+1, token3.length()).replaceAll(" ", "");
            			}
            			if((ifComp1.contains("ax")) || (ifComp1.contains("bx")) || (ifComp1.contains("cx")) || (ifComp1.contains("dx")) || (ifComp1.contains("al")) || (ifComp1.contains("ah"))
            			|| (ifComp1.contains("bl")) || (ifComp1.contains("bh")) || (ifComp1.contains("cl")) || (ifComp1.contains("ch")) || (ifComp1.contains("dl")) || (ifComp1.contains("dh"))){
            				
            			}else{
            				token3 = token4.nextToken();
                			if(token3.contains("je")){
                				outputStream.println("if("+ ifComp1 +" != "+ ifComp2 +"){");
                			}else if(token3.contains("jne")){
                				outputStream.println("if("+ ifComp1 +" == "+ ifComp2 +"){");
                			}else if(token3.contains("jl")){
                				outputStream.println("if("+ ifComp1 +" >= "+ ifComp2 +"){");
                			}else if(token3.contains("jle")){
                				outputStream.println("if("+ ifComp1 +" > "+ ifComp2 +"){");
                			}else if(token3.contains("jg")){
                				outputStream.println("if("+ ifComp1 +" <= "+ ifComp2 +"){");
                			}else if(token3.contains("jge")){
                				outputStream.println("if("+ ifComp1 +" < "+ ifComp2 +"){");
                			}
            			}
            		}else if(token3.contains("jmp")){
            			outputStream.println("}");
            		}
            	}
            }
            
            
            System.out.println("ah = "+ ax[0] +" al = "+ ax[1] +" ax = "+ ax[2]);
            System.out.println("bh = "+ bx[0] +" bl = "+ bx[1] +" bx = "+ bx[2]);
            System.out.println("ch = "+ cx[0] +" cl = "+ cx[1] +" cx = "+ cx[2]);
            System.out.println("dh = "+ dx[0] +" dl = "+ dx[1] +" dx = "+ dx[2]);
            
            outputStream.println();
            outputStream.println("return 0;");
            outputStream.println("}");
            
            outputStream.close();
            for(int i = 0;i < variables.get(0).size();i++){
            	System.out.println("Initial value "+ i +" : "+ variables.get(0).get(i) +" = "+ variables.get(1).get(i));
            	System.out.println("Ending value  "+ i +" : "+ variables.get(0).get(i) +" = "+ variables.get(2).get(i));
            }
		}
		catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "The file "+ fileName +" could not be found or could not be opened.");
        }
        catch (IOException e) {
        	JOptionPane.showMessageDialog(null,"Error reading from "+ fileName);
        }
		catch (Exception e){
			JOptionPane.showMessageDialog(null, "There seems to be an error.\nPlease wait while our programmers are sleeping.");
			e.printStackTrace();
		}
	}
}
