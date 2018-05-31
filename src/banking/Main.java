package banking;
import files.FilePath;
import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
/**
 * the interaction bank use to operate the Banking System
 * @author yifan, yuan
 * @version 1.0
 */
public class Main{

    public static void main(String[] arg){
        System.out.println("[INFO] ---------------------");
        System.out.println("[INFO] Banking System");
        System.out.println("[MENU] 1. user application");
        System.out.println("[MENU] 2. open the account");
        System.out.println("[MENU] 3. close the account");
        System.out.println("[MENU] 4. deposit");
        System.out.println("[MENU] 5. withdraw");
        System.out.println("[MENU] 6. suspend");
        System.out.println("[MENU] 7. release");
        System.out.println("[MENU] 8. mannual update");
        System.out.println("[INFO] ---------------------");
        Main main=new Main();
        String[] args0=main.getArgs();
        {
            if(args0.length!=1) {
                System.out.println("[ERROR] illegal menu");
                main(null);
            }
        }
        switch(args0[0]){
            case "1":
                System.out.println("[INFO] Format: name address age");
                String[] args1=main.getArgs();
                main.checkpara(3, args1);
                User user=new User(args1[0],args1[1],args1[2]);
                user.apply();
                System.out.println("[INFO] Name:"+args1[0]+" Address:"+args1[1]+" Age:"+args1[2]);
                System.out.println("[INFO] system has create a new user "+user.id);
                main(null);
                break;
            case "2":
                System.out.println("[INFO] Format: your userid, the type");
                System.out.println("[INFO] 1. current 2. junior 3. saver");
                String[] args2=main.getArgs();
                String accNo=null;
                main.checkpara(2, args2);
                User user2=main.search_user(args2[0]);
                checkUserId(user2.id);
                Bank bank2=new Bank();
                switch(args2[1]){
                    case "1": 
                        if(bank2.duplicate(user2.id, 1)==false){
                            System.out.println("[ERROR] duplicate accounts");
                            main(null);break;
                        }
                        bank2.openAccount(user2, 1);
                        accNo=args2[0]+"c";
                        break;
                    case "2": 
                        if(bank2.duplicate(user2.id, 2)==false){
                            System.out.println("[ERROR] duplicate accounts");
                            main(null);break;
                        }
                        if(bank2.openAccount(user2, 2)==false){
                            System.out.println("[ERROR] dennied for age");
                            main(null);break;
                        }
                        accNo=args2[0]+"j";
                        break;
                    case "3": 
                        if(bank2.duplicate(user2.id, 3)==false){
                            System.out.println("[ERROR] duplicate accounts");
                            main(null);break;
                        }
                        bank2.openAccount(user2, 3);
                        accNo=args2[0]+"s";
                        break;
                    default:
                        System.out.println("[ERROR] illegal input");
                        main(null);
                }
                System.out.println("[INFO] set your private PIN");
                String[] argspin2=main.getArgs();
                main.checkpara(1, argspin2);
                BankAccount aBankAccount=new BankAccount();
                aBankAccount.accNo=accNo;
                aBankAccount.setPin(argspin2[0]);
                main(null);
                break;
            case "3":
                System.out.println("[INFO] Format: your accId, PIN");
                String[] args3=main.getArgs();
                main.checkpara(2, args3);
                checkAccId(args3[0]);
                checkPin(args3[0], args3[1]);
                Bank bank3=new Bank();
                bank3.closeAccount(args3[0]);
                main(null);
                break;
            case "4":
                System.out.println("[INFO] \nFormat: your accId, PIN");
                String[] argspin=main.getArgs();
                main.checkpara(2, argspin);
                checkAccId(argspin[0]);
                checkPin(argspin[0], argspin[1]);
                System.out.println("[INFO] Format: the amount, type, date(if saverAccount)");
                System.out.println("[INFO] 1. case 2. cheque");
                String[] args4=main.getArgs();
                switch(judgeTypeString(argspin[0])){
                    case "currentAccount":
                        main.checkpara(2, args4);
                        CurrentAccount cacc4=new CurrentAccount(argspin[0]);
                        cacc4.deposit(Double.parseDouble(args4[0]), Integer.parseInt(args4[1]));
                        break;
                    case "juniorAccount":
                        main.checkpara(2, args4);
                        JuniorAccount jacc4=new JuniorAccount(argspin[0]);
                        jacc4.deposit(Double.parseDouble(args4[0]), Integer.parseInt(args4[1]));
                        break;
                    case "saverAccount":
                        main.checkpara(3, args4);
                        SaverAccount sacc4=new SaverAccount(argspin[0]);
                        sacc4.deposit(Double.parseDouble(args4[0]), Integer.parseInt(args4[1]), args4[2]);
                        break;
                    default:
                        System.out.println("[ERROR] illegal input");
                        main(null);
                }
                main(null);
                break;
            case "5":
                System.out.println("[INFO] \nFormat: your accId, PIN");
                String[] argspin5=main.getArgs();
                main.checkpara(2, argspin5);
                checkAccId(argspin5[0]);
                checkPin(argspin5[0], argspin5[1]);
                System.out.println("[INFO] Format: the amount");
                String[] args5=main.getArgs();
                main.checkpara(2, argspin5);
                
                switch(judgeTypeString(argspin5[0])){
                    case "currentAccount":
                        
                        CurrentAccount cacc5=new CurrentAccount(argspin5[0]);
                        cacc5.withdraw(Double.parseDouble(args5[0]));
                        break;
                    case "juniorAccount":
                       
                        JuniorAccount jacc5=new JuniorAccount(argspin5[0]);
                        jacc5.withdraw(Double.parseDouble(args5[0]));
                        break;
                    case "saverAccount": 
                        
                        SaverAccount sacc5=new SaverAccount(argspin5[0]);
                        sacc5.withdraw(Double.parseDouble(args5[0]));
                        break;
                    default:
                        System.out.println("[ERROR] illegal input");
                        main(null);
                }
                main(null);
                break;
            case "6":
                System.out.println("[INFO] Format: userId");
                String[] args6=main.getArgs();
                main.checkpara(1, args6);
                Bank bank6=new Bank();
                bank6.suspendUser(args6[0]);
                main(null);
                break;
            case "7":
                System.out.println("[INFO] Format: userId");
                String[] args7=main.getArgs();
                main.checkpara(1, args7);
                Bank bank7=new Bank();
                bank7.releaseAcc(args7[0]);
                main(null);
                break;
            case "8":
                System.out.println("[INFO] Has updated");
                Bank bank8=new Bank();
                bank8.manualbot();
                main(null);
                break;
            default:
                System.out.println("[ERROR] illegal menu");
                main(null);
        }   
    }

    public void checkpara(int k,String[] args){
        if(args.length!=k){
            System.out.println("[ERROR] illegal input");
            main(null);
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
            int count=0;
			File file = new File(FilePath.userInfo);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			NodeList list=doc.getElementsByTagName("user");
			
			for(int i=0;i<list.getLength();i++){
				Element user=(Element)list.item(i);
				if(user.getAttribute("id").equals(userId)){
                    count++;
                    reuser.name=user.getAttribute("name");
                    reuser.address=user.getAttribute("address");
                    reuser.age=user.getAttribute("age");
					reuser.credit=user.getAttribute("credit");
					reuser.id=userId;
				}
            }
            if(count==0){
                System.out.println("[ERROR] No such accNo");
            }
            return reuser;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reuser;
	}
    
    public static void checkUserId(String userId){
		try{
            int count=0;
			File file = new File(FilePath.userInfo);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			NodeList list=doc.getElementsByTagName("user");
			
			for(int i=0;i<list.getLength();i++){
				Element user=(Element)list.item(i);
				if(user.getAttribute("id").equals(userId)){
                    count++;
                    if(user.getAttribute("credit").equals("false")){
                        System.out.println("[ERROR] User Authentication denied.");
                        main(null);
                    }
				}
            }
            if(count==0){
                System.out.println("[ERROR] No such accNo");
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    public static void checkAccId(String accId){
		try{
            int count=0;
            String userId=accId.substring(0,4);
			File file = new File(FilePath.userInfo);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
            
			NodeList list=doc.getElementsByTagName("user");
			
			for(int i=0;i<list.getLength();i++){
				Element user=(Element)list.item(i);
				if(user.getAttribute("id").equals(userId)){
                    count++;
                    if(user.getAttribute("credit").equals("false")){
                        System.out.println("[ERROR] User Authentication denied.");
                        main(null);
                    }
				}
            }
            if(count==0){
                System.out.println("[ERROR] No such accNo");
            }
		} catch (Exception e) {
			e.printStackTrace();
        }
    }

    public static void checkPin(String accId,String pin){
		try{
            int count=0;
            
			File file = new File(FilePath.userInfo);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
            
			NodeList list=doc.getElementsByTagName(judgeTypeString(accId));
			
			for(int i=0;i<list.getLength();i++){
				Element user=(Element)list.item(i);
				if(user.getAttribute("accNo").equals(accId)){
                    count++;
                    if(pin.equals(user.getAttribute("pin")))break;
                    else{
                        System.out.println("[ERROR] PIN denied");
                        main(null);
                    }
				}
            }
            if(count==0){
                System.out.println("[ERROR] No such accNo");
                main(null);
            }
		} catch (Exception e) {
			e.printStackTrace();
        }
    }

    public static String judgeTypeString (String accNo){
    	if(accNo.length()!=5) {
    		System.out.println("[ERROR] illegal input");
    		main(null);
    	}
		String re = null;
		if(accNo.charAt(4)=='c') re = "currentAccount";
		else if(accNo.charAt(4)=='j') re = "juniorAccount";
		else if(accNo.charAt(4)=='s') re = "saverAccount";

		return re;
	}
}