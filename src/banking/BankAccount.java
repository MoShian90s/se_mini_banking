
package banking;
import files.FilePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import java.io.*;
import org.xml.sax.SAXException;
/**
 * Description: this is the super class of BankAccounts.
 * @see CurrentAccount
 * @see JuniorAccount
 * @see SaverAccount
 * @author yifan 2015213364
 * @version 1.0
 */
public class BankAccount {
	String accNo;
	double balance;
	String pin;

	public BankAccount() {
	}
	/**
	 * a method to deposit, need to be overriden in SaverAccount
	 * @see #balance_modifier(double)
	 * @see #deposit(double, int)
	 * @see #cheque_processor(double)
	 * @param amount the money u want to deposit
	 * @param type the way u deposit, cash or cheque
	 */
	public void deposit(double amount,int type) {
		if(type==1){
			this.balance_modifier(amount);
		} else if(type==2){
			cheque_processor(amount);
		}
	}
	/**
	 * a method to withdraw
	 * @see #balance_modifier(double)
	 * @see #deposit(double, int)
	 * @see #check(double)
	 * @param amount the money u wan to withdraws
	 */
	public void withdraw(double amount) {
		if(this.check(amount)) balance_modifier(-amount);
	}
	/**
	 * the tool the deposit and withdraw to modify the money in .XML
	 * @see #deposit(double, int)
	 * @see #withdraw(double)
	 * @param amount the money u deposit/withdraw
	 */
	protected void balance_modifier(double amount){
		try{
			String update=String.valueOf(this.balance + amount);
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			
			NodeList list=doc.getElementsByTagName(judgeTypeString(this.accNo));
			//System.out.println(judgeTypeString(this.accNo));
			for(int i=0;i<list.getLength();i++){
				if(list.item(i).getNodeType()==Node.ELEMENT_NODE){
					Element node=(Element)list.item(i);
					if(node.getAttribute("accNo").equals(this.accNo)){
						if(node.getAttribute("state").equals("true")){
							NodeList childs=node.getChildNodes();
							for(int k=0;k<childs.getLength();k++){
								if(childs.item(k).getNodeType()==Node.ELEMENT_NODE) {
									Element child_node=(Element)childs.item(k);
									if(child_node.getTagName().equals("balance")){
										System.out.println("test");
										System.out.println(child_node.getTextContent());
										child_node.setTextContent(update);
										System.out.println(child_node.getTextContent());
										this.balance=Double.parseDouble(update);
										System.out.println("[INFO] accNo: "+this.accNo+" balance: "+child_node.getTextContent());
									}
								}
							}
						}
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
		}
	}
	/**
	 * the method to handle the cheque in transaction
	 * @param amount the money recorded in the cheque
	 */
	public void cheque_processor(double amount){
		try{
			String filepath = FilePath.cheque;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			Element bank=(Element)doc.getFirstChild();

			Element cheque=doc.createElement("cheque");
			cheque.setAttribute("accNo", this.accNo);
			cheque.setAttribute("id", "c"+String.format("%04d", doc.getElementsByTagName("cheque").getLength()+1));
			cheque.setAttribute("state", "false");
			bank.appendChild(doc.createTextNode("    "));
			bank.appendChild(cheque);
			cheque.appendChild(doc.createTextNode(String.valueOf(amount)));
			
			
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
		}
	}
	/**
	 * the method used to check the leagcity of transacation
	 * @param amount the money:)
	 * @see #withdraw(double)
	 */
	protected boolean check(double amount) {
		boolean allowed = false;
		if (this.balance - amount >= 0) {
			allowed = true;
		} else {
			System.out.println("Withdraw " + amount
					+ " unsuccessfull. Do not have enough available funds.");
		}
		return allowed;
	}
	/**
	 * the method to print the basic information of Account
	 */
	public String toString() {
		return "Account Number: " + this.accNo + "\nBalance: " + this.balance;
	}
	/**
	 * the method to set PIN in account
	 * @param pin String transfered to set
	 */
	public void setPin(String pin){
		try{
            int count=0;
            String filepath = FilePath.userInfo;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            NodeList list=doc.getElementsByTagName(judgeTypeString(this.accNo));
			
			for(int i=0;i<list.getLength();i++){
				Element user=(Element)list.item(i);
				if(user.getAttribute("accNo").equals(this.accNo)){
                    count++;
                    user.setAttribute("pin", pin);
					this.pin=pin;
					System.out.println("[INFO] PIN has been recorded");
				}
            }
            if(count==0){
                System.out.println("[ERROR] No such accNo");
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
	}
	/**
	 * the method to judge the type of account
	 * @param accNo the account id
	 * @return String such as "currentAccount"
	 */
	public static String judgeTypeString (String accNo){
		String re = null;
		if(accNo.charAt(4)=='c') re = "currentAccount";
		else if(accNo.charAt(4)=='j') re = "juniorAccount";
		else if(accNo.charAt(4)=='s') re = "saveAccount";

		return re;
	}

	public void main(String[] args){
		CurrentAccount cacc=new CurrentAccount("0001c");
		cacc.balance_modifier(1000.0);
	}
}
