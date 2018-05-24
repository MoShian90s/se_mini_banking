package banking;
import files.FilePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import java.io.*;

import org.xml.sax.SAXException;
public class CurrentAccount extends BankAccount {
	double overdraftLimit;
	
	public CurrentAccount(String accNo) {
		try{
			this.accNo=accNo;
			
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			
			NodeList list=doc.getElementsByTagName("currentAccount");

			for(int i=0;i<list.getLength();i++){
				if(list.item(i).getNodeType()==Node.ELEMENT_NODE){
					Element node=(Element)list.item(i);
					if(node.getAttribute("accNo").equals(this.accNo)){
						
						NodeList childs=node.getChildNodes();
						for(int k=0;k<childs.getLength();k++){
							
							if(childs.item(k).getNodeType()==Node.ELEMENT_NODE){
								Element child_node=(Element)childs.item(k);
								if(child_node.getTagName().equals("balance")){
									this.balance=Double.parseDouble(child_node.getTextContent());
								}
								if(child_node.getTagName().equals("overdraft")){
									this.overdraftLimit=Double.parseDouble(child_node.getTextContent());
								}
							}
						}
					}
				}
			}
		}catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (SAXException sae) {
			sae.printStackTrace();
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void deposit(double amount) {
		Double updated_balance = this.balance + amount;
		this.balance_modifier(updated_balance);
	}

	public void withdraw(double amount) {
		Double updated_balance=this.balance-amount;
		if(this.check(amount)) balance_modifier(updated_balance);
	}

	protected void balance_modifier(double amount){
		try{
			String update=String.valueOf(amount);
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			
			NodeList list=doc.getElementsByTagName("currentAccount");
			
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
										
										child_node.setTextContent(update);
										this.balance=amount;
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

	protected boolean check(double amount) {
		boolean allowed = false;
		if (this.balance - amount >= -overdraftLimit) {
			allowed = true;
		} else {
			System.out.println("Withdraw " + amount
					+ " unsuccessfull. Do not have enough available funds.");
		}
		return allowed;
	}
	
	public static void main(String[] arg){
		CurrentAccount acc=new CurrentAccount("0001c");
		acc.withdraw(1000.0);
	}
}