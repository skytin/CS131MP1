package Disassembler;
import java.io.*;
import java.util.*;
public class Try2 {

	public static void main(String[] args)   {
		System.out.println("Enter filename: ");
		Scanner x = new Scanner(System.in);
		String fileName = x.nextLine();
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
            outputStream.println("");
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
            	}
            	
            	if(openCurly[0] != 0 && closeCurly[0] == 0){
            		if(token1.contains("char")){
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
            				outputStream.println("	"+ token1.substring(4, token1.indexOf("=", 4)) +" db "+ token1.substring(token1.indexOf("=")+1, token1.length()-1));
            			}else {
            				if(token1.contains("[")){
            					outputStream.println("	"+ token1.substring(4, token1.indexOf("[")) +" db "+ token1.substring(token1.indexOf("[")+1, token1.indexOf("]")) +" dup (?)");
            				}else {
            					outputStream.println("	"+ token1.substring(4, token1.length()-1) +" db ?");
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
            }
            
            for(int i = 0;i < 10;i++){
            	openCurly[i] = 0;
            	closeCurly[i] = 0;
            }
            
            outputStream.println(".stack 100h");
            outputStream.println(".code");
            outputStream.println("	main proc");
            outputStream.println("");
            outputStream.println("	mov ax,@data");				
            outputStream.println("	mov ds,ax");
            outputStream.println("");
            
            counter = 0;
            messCount = 0;
            StringTokenizer token2 = new StringTokenizer(b.toString(), "\n"); 
            while(token2.hasMoreTokens()){
            	String token3 = token2.nextToken();
            	if(token3.contains("main")){
            		openCurly[0] = 1;
            		counter++;
            	}else if(token3.contains("if")){
            		openCurly[counter] = 2;
            		counter++;
            	}else if(token3.contains("for")){
            		openCurly[counter] = 3;
            		counter++;
            	}else if(token3.contains("while")){
            		openCurly[counter] = 4;
            		counter++;
            	}else if(token3.contains("}")){
            		int count = 0;
            		int num = 0;
            		for(int i = 0;i < 10;i++){
            			if((openCurly[i] != 0) && (closeCurly[i] == 0)){
            				count = i;
            				num = openCurly[i];
            			}
            		}
            		closeCurly[count] = num;
            	}
            	
            	if((openCurly[0] != 0) && (closeCurly[0] == 0)){
            		if(token3.contains("if")){
            			int a = token3.indexOf("(")+1;
            			int c = token3.indexOf("=");
            			int d = token3.indexOf("=", c);
            			int e = token3.indexOf(")");
            			outputStream.println("	cmp "+ token3.substring(a, c) +", "+ token3.substring(d+2, e));
            			outputStream.println("	je label"+ counter);
            			outputStream.println("	jne next"+ counter);
            			outputStream.println("		label"+ counter +":");
            		}else if(token3.contains("}")){
            			outputStream.println("	next"+ counter +":");
            		}else if (token3.contains("cout")){
            			outputStream.println("		lea dx, message"+ messCount);
            			outputStream.println("		mov ah, 09h");
            			outputStream.println("		int 21h");
            			outputStream.println();
            			messCount++;
            		}else if(token3.contains("=")){
            			if((token3.contains("int")) || (token3.contains("char")) || (token3.contains("string")) || (token3.contains("if"))){
            				continue;
            			}else {
            				String lol = token3.replaceAll("\t", "");
            				outputStream.println("		mov "+ lol.substring(0, lol.indexOf("=")) +", "+ lol.substring(lol.indexOf("=")+1, lol.length()-1));
            			}
            		}
            	}
            }
            
            outputStream.println("	mov ax,4c00h");
            outputStream.println("	int 21h");
            outputStream.println("");
            outputStream.println("	main endp");
            outputStream.println("	end main");

            outputStream.close();
            
            for(int i = 0;i < 10;i++){
            	System.out.println(openCurly[i] +" : "+ array[i] +" : "+ closeCurly[i]);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("The file "+ fileName +" could not be found");
            System.out.println("or could not be opened.");
        }
        catch (IOException e) {
            System.out.println("Error reading from "+ fileName);
        }
    }
}
