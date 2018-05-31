
package banking;
import files.FilePath;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.time.ZoneId;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import java.io.*;

import org.xml.sax.SAXException;
/**
 * the operations of Bank
 * @author yifan, yuan
 * @version 1.0
 */
public class Bank {
	/**
	 * the method for opening an new account
	 * @param user the user has been created using User.apply()
	 * @param type the type of bank account
	 * @return true age condition
	 * @return false 
	 */
	public boolean openAccount(User user,int type) {
		boolean flag=true;
		try{
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			
			NodeList list= doc.getElementsByTagName("user");

			for(int i=0;i<list.getLength();i++){
				Element aimed_user =(Element)list.item(i);
				
				if(aimed_user.getAttribute("id").equals(user.id)){
					if(type==1){
						Element cAcc = doc.createElement("currentAccount");
						cAcc.setAttribute("accNo", user.id+"c");
						cAcc.setAttribute("state","true");
						aimed_user.appendChild(doc.createTextNode("\n        "));
						aimed_user.appendChild(cAcc);

						Element cbalance = doc.createElement("balance");
						cbalance.appendChild(doc.createTextNode("0.00"));
						cAcc.appendChild(doc.createTextNode("\n            "));
						cAcc.appendChild(cbalance);

						Element coverdraft = doc.createElement("overdraft");
						coverdraft.appendChild(doc.createTextNode("500.00"));
						cAcc.appendChild(doc.createTextNode("\n            "));
						cAcc.appendChild(coverdraft);

						cAcc.appendChild(doc.createTextNode("\n        "));
						aimed_user.appendChild(doc.createTextNode("\n    "));

						System.out.println("[INFO] accNo: "+user.id+"c"+" balance: "+"0.0"+" overdraftlimit: 500.0");			
					}else if(type==2){
						if(Integer.parseInt(user.age)<=16){
							Element cAcc = doc.createElement("juniorAccount");
							cAcc.setAttribute("accNo", user.id+"j");
							cAcc.setAttribute("state","true");
							aimed_user.appendChild(doc.createTextNode("\n        "));
							aimed_user.appendChild(cAcc);

							Element cbalance = doc.createElement("balance");
							cbalance.appendChild(doc.createTextNode("0.00"));
							cAcc.appendChild(doc.createTextNode("\n            "));
							cAcc.appendChild(cbalance);

							cAcc.appendChild(doc.createTextNode("\n        "));
							aimed_user.appendChild(doc.createTextNode("\n    "));
							System.out.println("[INFO] accNo: "+user.id+"j"+" balance: "+"0.0");
							
						}else{
							flag=false;
							return flag;
						}					
					}else if(type==3){
						Element cAcc = doc.createElement("saverAccount");
						cAcc.setAttribute("accNo", user.id+"s");
						cAcc.setAttribute("state","true");
						aimed_user.appendChild(doc.createTextNode("\n        "));
						aimed_user.appendChild(cAcc);

						Element cbalance = doc.createElement("balance");
						cbalance.appendChild(doc.createTextNode("0.00"));
						cAcc.appendChild(doc.createTextNode("\n            "));
						cAcc.appendChild(cbalance);

						cAcc.appendChild(doc.createTextNode("\n        "));
						aimed_user.appendChild(doc.createTextNode("\n    "));

						System.out.println("[INFO] accNo: "+user.id+"s"+" balance: "+"0.0");
			
					}
				}
				
			}
			
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			
			transformer.transform(source, result);
			
		}
		catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
		return flag;
	}
	/**
	 * @see #closeAccount(String)
	 * close an account
	 * @param userId user id
	 * @param type type of account
	 */
	public void closeAccount(String userId, int type) {
		try{
			int count=0;
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			
			NodeList list= doc.getElementsByTagName("user");
			
			for(int i=0;i<list.getLength();i++){
				Element aimed_user =(Element)list.item(i);
				
				if(aimed_user.getAttribute("id").equals(userId)){
					if(type==1){
						NodeList closedAcc_list1 = aimed_user.getElementsByTagName("currentAccount");
						for(int k=0;k<closedAcc_list1.getLength();k++){
							Node closedAcc1 = closedAcc_list1.item(k);
							aimed_user.removeChild(closedAcc1);
						}
					}
					else if(type==2){
						NodeList closedAcc_list2 = aimed_user.getElementsByTagName("juniorAccount");
						for(int k=0;k<closedAcc_list2.getLength();k++){
							Node closedAcc2 = closedAcc_list2.item(k);
							aimed_user.removeChild(closedAcc2);
						}
					}
					else if(type==3){
						NodeList closedAcc_list3 = aimed_user.getElementsByTagName("saverAccount");
						for(int k=0;k<closedAcc_list3.getLength();k++){
							Node closedAcc3 = closedAcc_list3.item(k);
							aimed_user.removeChild(closedAcc3);
						}
					}
				}
			}
			if(count==0){
				System.out.println("[INFO] No such accNo");
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		}
		catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}
	/**
	 * close an account
	 * @see #closeAccount(String, int)
	 * @param AccId id of account
	 */
	public void closeAccount(String AccId){
		try{
			int count=0;
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			NodeList list= doc.getElementsByTagName(judgeType(AccId));

			for(int i=0;i<list.getLength();i++){
				if(list.item(i).getNodeType()==Node.ELEMENT_NODE){
					Element closedAcc = (Element) list.item(i);
					if(closedAcc.getAttribute("accNo").equals(AccId)){
						count++;
						closedAcc.getParentNode().removeChild(closedAcc);
						System.out.println("[INFO] accNo: "+AccId+" has been deleted.");
					}
				}
			}
			if(count==0){
				System.out.println("[ERROR] No such accNo");
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		}
		catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}
	/**
	 * release the power of users suspended
	 * @see #suspendUser(String)
	 * @param userId user Id
	 */
	public void releaseAcc(String userId){
		try{
			int count=0;
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			NodeList list= doc.getElementsByTagName("user");

			for(int i=0;i<list.getLength();i++){
				if(list.item(i).getNodeType()==Node.ELEMENT_NODE){
					Element Acc = (Element) list.item(i);
					if(Acc.getAttribute("id").equals(userId)){
						count++;
						Acc.setAttribute("credit", "true");
						System.out.println("[INFO] user power released");
					}
				}
			}
			if(count==0){
				System.out.println("[ERROR] No such accNo");
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		}
		catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}
	/**
	 * suspend a user
	 * @see #releaseAcc(String)
	 * @param userId user Id
	 */
	public void suspendUser(String userId){
		try{
			int count=0;
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			NodeList list= doc.getElementsByTagName("user");

			for(int i=0;i<list.getLength();i++){
				if(list.item(i).getNodeType()==Node.ELEMENT_NODE){
					Element Acc = (Element) list.item(i);
					if(Acc.getAttribute("id").equals(userId)){
						count++;
						Acc.setAttribute("credit", "false");
						System.out.println("[INFO] user power suspended");
					}
				}
			}
			if(count==0){
				System.out.println("[ERROR] No such accNo");
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		}
		catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}
	/**
	 * check the overdraft of accounts
	 * @see #cheque_update()
	 * @see #save_update()
	 */
	public void overdraft_update() {
		try{
			Double balance=0.0,overdraft=0.0,sum=0.0;
			int count=0;
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			NodeList list=doc.getElementsByTagName("currentAccount");

			for(int i=0;i<list.getLength();i++){
				Element check=(Element)list.item(i);
				NodeList check_list=check.getChildNodes();
				for(int k=0;k<check_list.getLength();k++){
					if(check_list.item(k).getNodeType()==Node.ELEMENT_NODE){
						Element check_list_node=(Element)check_list.item(k);
						if(check_list_node.getNodeName().equals("balance")) {
							balance=Double.parseDouble(check_list_node.getTextContent());}
						else {
							if(check_list_node.getNodeName().equals("overdraft")){
								overdraft=Double.parseDouble(check_list_node.getTextContent());
							}
						}
					}
				}
				sum=overdraft+balance;
				if(0>sum){
					break;
				}
			}
			if(count==0);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}
	/**
	 * the check of cheque
	 * @see #save_update()
	 * @see #overdraft_update()
	 */
	public void cheque_update(){
		try{
			String filepath = FilePath.cheque;
			double cash=0.0;
			String accNo=null;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
		
			NodeList list=doc.getElementsByTagName("cheque");

			for(int i=0;i<list.getLength();i++){
				if(list.item(i).getNodeType()==Node.ELEMENT_NODE){
					Element node=(Element)list.item(i);
					if(node.getAttribute("state").equals("false")){
						if(cheque_credit(node.getAttribute("id"))){
							cash=Double.parseDouble(node.getTextContent());
							accNo=node.getAttribute("accNo");
							if(judgeType(accNo).equals("currentAccount")){
								CurrentAccount cacc=new CurrentAccount(accNo);
								cacc.balance_modifier(cash);
							}else if(judgeType(accNo).equals("juniorAccount")) {
								JuniorAccount jacc=new JuniorAccount(accNo);
								jacc.balance_modifier(cash);
							}else if(judgeType(accNo).equals("saverAccount")){
								SaverAccount sacc=new SaverAccount(accNo);
								sacc.balance_modifier(cash);
							}
							node.setAttribute("state", "true");
						}
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
		
			transformer.transform(source, result);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}
	/**
	 * check of save
	 * @see #cheque_update()
	 * @see #overdraft_update()
	 */
	public void save_update(){
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			NodeList list=doc.getElementsByTagName("save");

			for(int i=0;i<list.getLength();i++){
				if(list.item(i).getNodeType()==Node.ELEMENT_NODE){
					Element node=(Element)list.item(i);
					Date date1 = formatter.parse(node.getAttribute("date"));
					Date date2 = new Date();
					LocalDateTime localDateTime = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					localDateTime = localDateTime.minusDays(3);
					Date letterDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
					
					if(date2.compareTo(date1)>=0){
						System.out.println("[INFO] "+node.getAttribute("accNo")+" "+node.getTextContent()+node.getAttribute("id")+" funds clear");
						SaverAccount sacc=new SaverAccount(node.getAttribute("accNo"));
						double temp=Double.parseDouble(node.getTextContent());
						
						sacc.balance_modifier(temp);
						System.out.println(sacc.toString());
						node.getParentNode().removeChild(node);
					}
					else if(date2.compareTo(letterDate)>0){
						System.out.println("[INFO] ---------------------");
						System.out.println("[INFO] a letter of "+node.getAttribute("id")+" has been sent");
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			
            transformer.transform(source, result);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		} catch (ParseException pae) {
			pae.printStackTrace();
		}
	}
	/**
	 * the credit check of an account
	 * @see #openAccount(User, int)
	 * @param accNo number of account
	 * @return true
	 */
	public boolean cheque_credit(String accNo){
		return true;
	}
	/**
	 * judge the type of account based on its account No
	 * @param accNo account id
	 * @return the type of account
	 */
	public String judgeType (String accNo){
		String re = null;
		if(accNo.charAt(4)=='c') re = "currentAccount";
		else if(accNo.charAt(4)=='j') re = "juniorAccount";
		else if(accNo.charAt(4)=='s') re = "saverAccount";

		return re;
	}
	/**
	 * an mannual bot can check the accounts using an extra thread
	 * @see #cheque_update()
	 * @see #overdraft_update()
	 * @see #save_update()
	 */
	public void manualbot(){
		save_update();
		cheque_update();
		overdraft_update();
	}
	/**
	 * an automatic bot can check the accounts using an extra thread
	 * @see #cheque_update()
	 * @see #overdraft_update()
	 * @see #save_update()
	 */
	public void autobot (){
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20); // 控制时
        calendar.set(Calendar.MINUTE, 24);    // 控制分
        calendar.set(Calendar.SECOND, 0);    // 控制秒
     
        Date time = calendar.getTime();
     
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				save_update();
				cheque_update();
				overdraft_update();
			}
		},time,1000*3600*24);
	}
	/**
	 * u can use this method to check the duplicate
	 * @param userId user Id
	 * @param type type of account
	 * @return true no duplicate account
	 * @return false duplicate account
	 */
	public boolean duplicate(String userId, int type){
		boolean re=true;
		try{
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			String para=null,accId=null;
			switch(type){
				case 1: para="currentAccount";accId=userId+"c";break;
				case 2: para="juniorAccount";accId=userId+"j";break;
				case 3: para="saverAccount";accId=userId+"s";break;
			}
			NodeList list=doc.getElementsByTagName(para);
			
			for(int i=0;i<list.getLength();i++){
				if(list.item(i).getNodeType()==Node.ELEMENT_NODE){
					Element node=(Element) list.item(i);
					
					if(node.getAttribute("accNo").equals(accId)) return re=false;
					else continue;
				}
			}
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
		return re;
	}
}
