package banking;
import files.FilePath;
import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import java.io.*;

import org.xml.sax.SAXException;

public class Main{

    public static void main(String[] arg){
        Main main=new Main();
        System.out.println("[INFO] ---------------------");
        System.out.println("[INFO] Banking System");
        System.out.println("[MENU] 1. apply an account");
        System.out.println("[MENU] 2. open the account");
        System.out.println("[MENU] 3. close the account");
        System.out.println("[MENU] 4. deposit");
        System.out.println("[MENU] 5. withdraw");
        System.out.println("[MENU] 6. suspend");
        System.out.println("[MENU] 7. mannual update");
        System.out.println("[INFO] ---------------------");
        String[] args0=main.getArgs();
        switch(args0[0]){
            case "1":
                System.out.println("[INFO] Format: name address age");
                String[] args1=main.getArgs();
                User user=new User(args1[0],args1[1],args1[2]);
                user.apply();
                System.out.println("[INFO] Name:"+args1[0]+" Address:"+args1[1]+" Age:"+args1[2]);
                System.out.println("[INFO] system has create a new user "+user.id);
                break;
            case "2":
                System.out.println("[INFO] Format: your userid, the type");
                System.out.println("[INFO] 1. current 2. junior 3. saver");
                String[] args2=main.getArgs();
                User user2=main.search_user(args2[0]);
                Bank bank2=new Bank();
                switch(args2[1]){
                    case "1": 
                        bank2.openAccount(user2, 1);
                        CurrentAccount cacc2=new CurrentAccount(args2[0]);
                        System.out.println("[INFO] accNo: "+args2[0]+" balance: "+cacc2.balance+" overdraftlimit: "+cacc2.overdraftLimit);
                        break;
                    case "2": 
                        bank2.openAccount(user2, 2);
                        JuniorAccount jacc2=new JuniorAccount(args2[0]);
                        System.out.println("[INFO] accNo: "+args2[0]+" balance: "+jacc2.balance);
                        break;
                    case "3": 
                        bank2.openAccount(user2, 3);
                        SaverAccount sacc2=new SaverAccount(args2[0]);
                        System.out.println("[INFO] accNo: "+args2[0]+" balance: "+sacc2.balance);
                        break;
                }
            case "3":
                System.out.println("[INFO] Format: your accid");
                String[] args3=main.getArgs();
                Bank bank3=new Bank();
                bank3.closeAccount(args3[0]);
                System.out.println("[INFO] accNo: "+args3[0]+" has been deleted.");
                break;
            case "4":
                System.out.println("[INFO] Format: your accid, the amount, type, date(if saverAccount)");
                System.out.println("[INFO] 1. case 2. cheque");
                String[] args4=main.getArgs();
                User user4=main.search_user(args4[0]);
                Bank bank4=new Bank();
                switch(judgeType(args4[0])){
                    case 1:
                        bank4.openAccount(user4, 1);
                        CurrentAccount cacc4=new CurrentAccount(args4[0]);
                        cacc4.deposit(Double.parseDouble(args4[1]), Integer.parseInt(args4[2]));
                        System.out.println("[INFO] accNo: "+cacc4.accNo+" balance: "+cacc4.balance+" overdraftlimit: "+cacc4.overdraftLimit);
                        break;
                    case 2:
                        bank4.openAccount(user4, 2);
                        JuniorAccount jacc4=new JuniorAccount(args4[0]);
                        jacc4.deposit(Double.parseDouble(args4[1]), Integer.parseInt(args4[2]));
                        System.out.println("[INFO] accNo: "+jacc4.accNo+" balance: "+jacc4.balance);
                        break;
                    case 3: 
                        bank4.openAccount(user4, 3);
                        SaverAccount sacc4=new SaverAccount(args4[0]);
                        sacc4.deposit(Double.parseDouble(args4[1]), Integer.parseInt(args4[2]),args4[3]);
                        System.out.println("[INFO] accNo: "+sacc4.accNo+" balance: "+sacc4.balance);
                        break;
                }
            case "5":
                System.out.println("[INFO] Format: your accid, the amount)");
                String[] args5=main.getArgs();
                User user5=main.search_user(args5[0]);
                Bank bank5=new Bank();
                switch(judgeType(args5[0])){
                    case 1:
                        bank5.openAccount(user5, 1);
                        CurrentAccount cacc5=new CurrentAccount(args5[0]);
                        cacc5.withdraw(Double.parseDouble(args5[1]));
                        System.out.println("[INFO] accNo: "+cacc5.accNo+" balance: "+cacc5.balance+" overdraftlimit: "+cacc5.overdraftLimit);
                        break;
                    case 2:
                        bank5.openAccount(user5, 2);
                        JuniorAccount jacc5=new JuniorAccount(args5[0]);
                        jacc5.withdraw(Double.parseDouble(args5[1]));
                        System.out.println("[INFO] accNo: "+jacc5.accNo+" balance: "+jacc5.balance);
                        break;
                    case 3: 
                        bank5.openAccount(user5, 3);
                        SaverAccount sacc5=new SaverAccount(args5[0]);
                        sacc5.withdraw(Double.parseDouble(args5[1]));
                        System.out.println("[INFO] accNo: "+sacc5.accNo+" balance: "+sacc5.balance);
                        break;
                }
            case "6":
                System.out.println("[INFO] Format: your accNo");
                String[] args6=main.getArgs();
                Bank bank6=new Bank();
                bank6.suspend(args6[0]);
                System.out.println("[INFO] account suspended");
                break;
            case "7":
                System.out.println("[INFO] Has updated");
                Bank bank7=new Bank();
                bank7.autobot();
                break;
        }   
    }

    public String[] getArgs(){
        Scanner scan = new Scanner(System.in);
        String str = "";
        String[] args= {"","","","","","",""};
        System.out.print("Input:");
        if(scan.hasNextLine()==true){
            str += scan.nextLine();
            args= str.split(" ");
        }

        return args;
    }

    public User search_user(String userId) {
    	User reuser=new User();
		try{
			File file = new File(FilePath.userInfo);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			NodeList list=doc.getElementsByTagName("user");
			
			for(int i=0;i<list.getLength();i++){
				Element user=(Element)list.item(i);
				if(user.getAttribute("id").equals(userId)){
                    reuser.name=user.getAttribute("name");
                    reuser.address=user.getAttribute("address");
                    reuser.age=user.getAttribute("age");
					reuser.credit=user.getAttribute("credit");
					reuser.id=userId;
				}
				
			}
            return reuser;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reuser;
	}

    public static int judgeType (String accNo){
		int re = 0;
		if(accNo.charAt(4)=='c') re = 1;
		else if(accNo.charAt(4)=='j') re = 2;
		else if(accNo.charAt(4)=='s') re = 3;

		return re;
	}
}