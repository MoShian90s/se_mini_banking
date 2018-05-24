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
public class JuniorAccount extends BankAccount {
	
	public JuniorAccount(String accNo) {
		try{
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			
			NodeList list=doc.getElementsByTagName("juniorAccount");

			for(int i=0;i<list.getLength();i++){
				Element node=(Element)list.item(i);
				if(node.getAttribute("accNo").equals(accNo)){
					NodeList childs=node.getChildNodes();
					for(int k=0;k<childs.getLength();k++){
						Element child_node=(Element)childs.item(k);
						if(child_node.getTagName().equals("balance")){
							this.balance=Double.parseDouble(child_node.getTextContent());
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
		Double updated_balance=this.balance+amount;
		balance_modifier(updated_balance);
	}

	public void withdraw(double amount) {
		Double updated_balance=this.balance-amount;
		if(check(updated_balance)) balance_modifier(updated_balance);
	}

	protected void balance_modifier(double amount){
		try{
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			
			NodeList list=doc.getElementsByTagName("currentAccount");

			for(int i=0;i<list.getLength();i++){
				Element node=(Element)list.item(i);
				if(node.getAttribute("accNo").equals(accNo)){
					NodeList childs=node.getChildNodes();
					for(int k=0;k<childs.getLength();k++){
						Element child_node=(Element)childs.item(k);
						if(child_node.getTagName().equals("balance")){
							child_node.setTextContent(String.valueOf(amount));
							this.balance=amount;
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
		if (this.balance - amount >= 0) {
			allowed = true;
		} else {
			System.out.println("Withdraw " + amount
					+ " unsuccessfull. Do not have enough available funds.");
		}
		return allowed;
	}
}