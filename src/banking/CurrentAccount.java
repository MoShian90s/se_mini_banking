
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
/**
 * currentAccount extends from the BankAccount
 * @author yifan
 * @version 1.0
 */
public class CurrentAccount extends BankAccount {
	double overdraftLimit;
	/**
	 * constructor of Current Account
	 * @param accNo account id
	 */
	public CurrentAccount(String accNo) {
		try{
			int count=0;
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
						count++;
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
			if(count==0){
				System.out.println("[ERROR] No such accNo");
			}
		}catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (SAXException sae) {
			sae.printStackTrace();
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	/**
	 * the overriden method
	 * @param amount money amount
	 */
	protected void balance_modifier(double amount){
		try{
			String update=String.valueOf(this.balance + amount);
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
										this.balance=Double.parseDouble(update);
										System.out.println("[INFO] accNo: "+this.accNo+" balance: "+this.balance+" overdraftlimit: "+this.overdraftLimit);
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
	 * the check() to check the validation of transaction
	 * @param amount money amount
	 */
	protected boolean check(double amount) {
		boolean allowed = false;
		if (this.balance - amount >= -overdraftLimit) {
			allowed = true;
		} else {
			System.out.println("[ERROR] Withdraw " + amount
					+ " unsuccessfull. Do not have enough available funds.");
		}
		return allowed;
	}
}