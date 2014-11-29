package Disassembler;
import java.io.*;
import java.util.*;

import javax.swing.*;
public class Try2 {

	public static boolean isNumber(String num){
		try{
			int number = Integer.parseInt(num);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public static void main(String[] args)   {
		String fileName = JOptionPane.showInputDialog("Enter filename: ");
		String fileName1 = null;
		int messCount = 0;
		int[] array = new int[10];
		int[] openCurly = new int[10];					// 1 - inside main; 2 - inside if; 3 - inside loop
		int[] closeCurly = new int[10];					// 1 - end main; 2 - end if; 3 - end loop
		int counter = 0;
		closeCurly[0] = 0;
	    PrintWriter outputStream = null;
        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(fileName));
            fileName = fileName.substring(0, fileName.length()-4);
            fileName1 = fileName + ".asm";
            outputStream = new PrintWriter(new FileOutputStream(fileName1));
            String line = inputStream.readLine();
            StringBuilder b = new StringBuilder();
            
            outputStream.println("title "+ fileName);
            outputStream.println(";Christine Joyce O. Arzadon");
            outputStream.println(";CS 131");
            outputStream.println(";2012-13554");
            outputStream.println();
            outputStream.println(".model small");
            outputStream.println(".data");
            
            while (line != null) {
            	b.append(line);
            	b.append("\n");
            	line = inputStream.readLine();
            }
            inputStream.close();
            
            // loop para sa declaration ng variables
            StringTokenizer token = new StringTokenizer(b.toString(), "\n");
            while(token.hasMoreTokens()){
            	String token1 = token.nextToken();
            	if(token1.contains("main")){
            		openCurly[0] = 1;
            		counter++;
            	}else if(token1.contains("if")){
            		openCurly[counter] = 2;
            		counter++;
            	}else if(token1.contains("for")){
            		openCurly[counter] = 3;
            		counter++;
            	}else if(token1.contains("while")){
            		openCurly[counter] = 4;
            		counter++;
            	}else if(token1.contains("}")){
            		int count = 0;
            		int num = 0;
            		for(int i = 0;i < 10;i++){
            			if((openCurly[i] != 0) && (closeCurly[i] == 0)){
            				count = i;
            				num = openCurly[i];
            			}
            		}
            		closeCurly[count] = num;
            	}else if(token1.contains("char")){
            		if(token1.contains("=")){
            			outputStream.println("	"+ token1.substring(5, token1.indexOf("=", 5)) +" db "+ token1.substring(token1.indexOf("=")+1, token1.length()-1));
            		}else {
            			if(token1.contains("[")){
            				outputStream.println("	"+ token1.substring(5, token1.indexOf("[")) +" db "+ token1.substring(token1.indexOf("[")+1, token1.indexOf("]")) +" dup (?)");
            			}else {
            				outputStream.println("	"+ token1.substring(5, token1.length()-1) +" db ?");
            			}
            			System.out.println(token1);
            		}
            	}else if(token1.contains("int")){
            		if(token1.contains("=")){
            			System.out.println(token1);
            			if(token1.contains("[")){
            				if(!token1.substring(token1.indexOf("{")+1, token1.indexOf("}")).contains(",")){
            					outputStream.println("	"+ token1.substring(4, token1.indexOf("[")) +" db "+ token1.substring(token1.indexOf("[")+1, token1.indexOf("]")) +" dup ("+ token1.substring(token1.indexOf("{")+1, token1.indexOf("}")) +")");
            				}else {
            					outputStream.println("	"+ token1.substring(4, token1.indexOf("=", 4)) +" db "+ token1.substring(token1.indexOf("{")+1, token1.indexOf("}")));
            				}
            			}else {
            				outputStream.println("	"+ token1.substring(4, token1.indexOf("=", 4)) +" db "+ token1.substring(token1.indexOf("=")+1, token1.length()-1));
            			}
            		}else {
            			if(token1.contains("[")){
            				outputStream.println("	"+ token1.substring(4, token1.indexOf("[")) +" db "+ token1.substring(token1.indexOf("[")+1, token1.indexOf("]")) +" dup (?)");
            			}else {
            				outputStream.println("	"+ token1.substring(4, token1.length()-1) +" db ?");
            			}
            			System.out.println(token1);
            		}	
            	}else if(token1.contains("float")){
            		if(token1.contains("=")){
            			System.out.println(token1);
            			token1 = token1.replaceAll(" ", "");
            			if(token1.contains("[")){
            				if(!token1.substring(token1.indexOf("{")+1, token1.indexOf("}")).contains(",")){
            					outputStream.println("	"+ token1.substring(7, token1.indexOf("[")) +" db "+ token1.substring(token1.indexOf("[")+1, token1.indexOf("]")) +" dup ("+ token1.substring(token1.indexOf("{")+1, token1.indexOf("}")) +")");
            				}else {
            					outputStream.println("	"+ token1.substring(7, token1.indexOf("=", 7)) +" db "+ token1.substring(token1.indexOf("{")+1, token1.indexOf("}")));
            				}
            			}else{
            				outputStream.println("	"+ token1.substring(7, token1.indexOf("=")) +" db "+ token1.substring(token1.indexOf("=")+1, token1.length()-1));
            			}
            		}else {
            			if(token1.contains("[")){
            				outputStream.println("	"+ token1.substring(7, token1.indexOf("[")) +" db "+ token1.substring(token1.indexOf("[")+1, token1.indexOf("]")) +" dup (?)");
            			}else{
            				outputStream.println("	"+ token1.substring(7, token1.length()-1) +" db ?");
            			}
            			System.out.println(token1);
            		}
            	}else if(token1.contains("string")){
            		if(token1.contains("=")){
            			System.out.println(token1);
            			outputStream.println("	"+ token1.substring(7, token1.indexOf("=", 7)) +" db "+ token1.substring(token1.indexOf("=")+1, token1.length()-1));
            		}else {
            			if(token1.contains("[")){
            				outputStream.println("	"+ token1.substring(7, token1.indexOf("[")) +" db "+ token1.substring(token1.indexOf("[")+1, token1.indexOf("]")) +" dup (?)");
            			}else {
            				outputStream.println("	"+ token1.substring(7, token1.length()-1) +" db ?");
            			}
            			System.out.println(token1);
            		}	
            	}else if(token1.contains("cout")){
               		int num = 0;
               		for(int i = 0;i < 10;i++){
               			if((openCurly[i] != 0) && (closeCurly[i] == 0)){
               				num = openCurly[i];
               			}
               		}
            		if(token1.contains("\"")){
            			outputStream.println("	message"+ messCount +" db "+ token1.substring(token1.indexOf("\"", 0), token1.lastIndexOf("\"")+1) +",'$'");
            			array[messCount] = num;
            			messCount++;
            		}
            	}
            }
            
            for(int i = 0;i < 10;i++){
            	openCurly[i] = 0;
            	closeCurly[i] = 0;
            }
            
            outputStream.println(".stack 100h");
            outputStream.println(".code");
            outputStream.println("	main proc");
            outputStream.println();
            outputStream.println("	mov ax,@data");				
            outputStream.println("	mov ds,ax");
            outputStream.println();
            
            counter = 0;
            messCount = 0;
            StringTokenizer token2 = new StringTokenizer(b.toString(), "\n");
            String condition = null;
            String forCondition = null;
            String ment = null;			// increment or decrement lol
            int whileCounter = 0;
            int forCounter = 0;
            int ifCounter = 0;
            while(token2.hasMoreTokens()){
            	String token3 = token2.nextToken();
            	if(token3.contains("main")){
            		openCurly[0] = 1;
            		counter++;
            	}else if(token3.contains("if")){
            		openCurly[counter] = 2;
            		counter++;
            		int a = token3.indexOf("(")+1;
        			int c = token3.indexOf("=");
        			int d = token3.indexOf("=", c);
        			int e = token3.indexOf(")");
        			ifCounter++;
        			outputStream.println("	cmp "+ token3.substring(a, c).replaceAll(" ", "") +", "+ token3.substring(d+2, e).replaceAll(" ", ""));
        			outputStream.println("	je label"+ ifCounter);
        			outputStream.println("	jne next"+ ifCounter);
        			outputStream.println("	label"+ ifCounter +":");
            	}else if(token3.contains("for")){
            		openCurly[counter] = 3;
            		counter++;
            		forCounter++;
            		String init = token3.substring(token3.indexOf("(")+1, token3.indexOf(";")).replaceAll(" ", "");
            		outputStream.println("	mov "+ init.substring(0, init.indexOf("=")) +", "+ init.substring(init.indexOf("=")+1, init.length()));
            		outputStream.println("	for"+ forCounter +":");
            		forCondition = token3.substring(token3.indexOf(";")+1, token3.lastIndexOf(";")).replaceAll(" ", "");
            		ment = token3.substring(token3.lastIndexOf(";")+1, token3.lastIndexOf(")"));
            	}else if(token3.contains("while")){
            		openCurly[counter] = 4;
            		counter++;
            		whileCounter++;
            		outputStream.println("	while"+ whileCounter +":");
            		outputStream.println();
            		condition = token3.substring(token3.indexOf("(")+1, token3.indexOf(")")).replaceAll(" ", "");
            	}else if((openCurly[0] != 0) && (closeCurly[0] == 0)){
            		if(token3.contains("}")){
                		int count = 0;
                		int num = 0;
                		for(int i = 0;i < 10;i++){
                			if((openCurly[i] != 0) && (closeCurly[i] == 0)){
                				count = i;
                				num = openCurly[i];
                			}
                		}
                		closeCurly[count] = num;
                		if(num == 4){
                			if(condition.contains("!=")){
                				outputStream.println("		cmp "+ condition.substring(0, condition.indexOf("!")) +", "+ condition.substring(condition.indexOf("=")+1, condition.length()));
                				outputStream.println("	jne while"+ whileCounter);
                				outputStream.println();
                			}else if(condition.contains("==")){
                				outputStream.println("		cmp "+ condition.substring(0, condition.indexOf("=")) +", "+ condition.substring(condition.indexOf("=")+1, condition.length()));
                				outputStream.println("	je while"+ whileCounter);
                				outputStream.println();
                			}
                		}else if(num == 2){
                			outputStream.println("	next"+ ifCounter +":");
                		}else if(num == 3){
                			if(ment.contains("+")){
                				outputStream.println("		inc "+ ment.substring(0, ment.indexOf("+")).replaceAll(" ", ""));
                			}else if(ment.contains("-")){
                				outputStream.println("		dec "+ ment.substring(0, ment.indexOf("-")).replaceAll(" ", ""));
                			}
                			if(forCondition.contains("<=")){
                				outputStream.println("		cmp "+ forCondition.substring(0, forCondition.indexOf("<")) +", "+ forCondition.substring(forCondition.indexOf("=")+1, forCondition.length()));
                				outputStream.println("	jle for"+ forCounter);
                				outputStream.println();
                			}else if(forCondition.contains(">=")){
                				outputStream.println("		cmp "+ forCondition.substring(0, forCondition.indexOf(">")) +", "+ forCondition.substring(forCondition.indexOf("=")+1, forCondition.length()));
                				outputStream.println("	jge for"+ forCounter);
                				outputStream.println();
                			}else if(forCondition.contains("<")){
                				outputStream.println("		cmp "+ forCondition.substring(0, forCondition.indexOf("<")) +", "+ forCondition.substring(forCondition.indexOf("<")+1, forCondition.length()));
                				outputStream.println("	jl for"+ forCounter);
                				outputStream.println();
                			}else if(forCondition.contains(">")){
                				outputStream.println("		cmp "+ forCondition.substring(0, forCondition.indexOf(">")) +", "+ forCondition.substring(forCondition.indexOf(">")+1, forCondition.length()));
                				outputStream.println("	jle for"+ forCounter);
                				outputStream.println();
                			}
                		}
                	}else if (token3.contains("cout")){
            			outputStream.println("		lea dx, message"+ messCount);
            			outputStream.println("		mov ah, 09h");
            			outputStream.println("		int 21h");
            			outputStream.println();
            			messCount++;
            		}else if(token3.contains("=")){
            			if((token3.contains("int")) || (token3.contains("char")) || (token3.contains("string")) || (token3.contains("if")) || (token3.contains("while")) || (token3.contains("for"))){
            				continue;
            			}else if(token3.contains("+")){
            				token3 = token3.replaceAll(" ", "");
            				String result = token3.substring(0, token3.indexOf("=")).replaceAll(" ", "");
            				result = result.replaceAll("\t", "");
            				int z = token3.indexOf("=")+1;
            				int y = token3.indexOf("+");
            				String add1 = token3.substring(z, y).replaceAll(" ", "");
            				String add2 = token3.substring(token3.indexOf("+")+1, token3.length()-1).replaceAll(" ", "");;
            				if(isNumber(add1)){
            					outputStream.println("	mov al, "+ add1 );
            					outputStream.println("	mov bl, "+ add2 );
            					outputStream.println("	add al, bl");
            					outputStream.println("	mov "+ result +", al");
            				}else if((isNumber(add2)) && (result.equals(add1))){
            					outputStream.println("	add "+ add1 +", "+ add2);
            				}else if((!isNumber(add2)) && (result.equals(add1))){
            					outputStream.println("	mov al, "+ add2);
            					outputStream.println("	add "+ add1 +", al");
            				}else if((isNumber(add2)) && (!result.equals(add1))){
            					outputStream.println("	add "+ add1 +", "+ add2);
            					outputStream.println("	mov "+ result +", "+ add1);
            					outputStream.println("	sub "+ add1 +", "+ add2);
            				}else if((!isNumber(add2)) && (!result.equals(add1))){
            					outputStream.println("	mov al, "+ add2);
            					outputStream.println("	add "+ add1 +", al");
            					outputStream.println("	mov "+ result +", "+ add1);
            					outputStream.println("	sub "+ add1 +", al");
            				}
            				
            			}else{
            				String lol = token3.replaceAll("\t", "");
            				lol = lol.replaceAll(" ", "");
            				outputStream.println("		mov "+ lol.substring(0, lol.indexOf("=")) +", "+ lol.substring(lol.indexOf("=")+1, lol.length()-1));
            			}
            		}else if(token3.contains("cin")){
            			outputStream.println("		mov ah, 01h");
            			outputStream.println("		int 21h");
            			outputStream.println("		mov "+ token3.substring(token3.lastIndexOf(" ")+1, token3.length()-1) +", al");
            			outputStream.println();
            		}else if(token3.contains("++")){
            			outputStream.println("	inc "+ token3.substring(0, token3.indexOf("+")).replaceAll(" ", "").replaceAll("\t", ""));
            		}else if(token3.contains("--")){
            			outputStream.println("	dec "+ token3.substring(0, token3.indexOf("+")).replaceAll(" ", "").replaceAll("\t", ""));
            		}
            	}
            }
            
            outputStream.println("	mov ax,4c00h");
            outputStream.println("	int 21h");
            outputStream.println();
            outputStream.println("	main endp");
            outputStream.println("	end main");

            outputStream.close();
            
            for(int i = 0;i < 10;i++){
            	System.out.println(openCurly[i] +" : "+ array[i] +" : "+ closeCurly[i]);
            }
            System.out.println(forCondition);
            System.out.println(condition);
            System.out.println(ment);
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "The file "+ fileName +" could not be found or could not be opened.");
        }
        catch (IOException e) {
        	JOptionPane.showMessageDialog(null,"Error reading from "+ fileName);
        }
    }
}
