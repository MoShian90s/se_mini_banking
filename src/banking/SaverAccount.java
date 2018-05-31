
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
 * SaverAccounts extends from the class BankAccount
 * @author yifan 2015213364
 * @version 1.0
 */
public class SaverAccount extends BankAccount {
	/**
	 * the constructor of SaverAccount
	 * @param accNo the account id of saver account
	 */
	public SaverAccount(String accNo) {
		try{
			int count=0;
			this.accNo=accNo;
			
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			
			NodeList list=doc.getElementsByTagName("saverAccount");

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
	 * the method saveraccount use to deposit
	 * @param amount the money amount u operate
	 * @param type the way u deposit
	 * @param date the date u set to store the save information
	 */
	public void deposit(double amount,int type,String date) {
		try{
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			NodeList list=doc.getElementsByTagName("saverAccount");
			NodeList money_list=doc.getElementsByTagName("save");

			for(int i=0;i<list.getLength();i++){
				if(list.item(i).getNodeType()==Node.ELEMENT_NODE){
					Element node=(Element)list.item(i);
					if(node.getAttribute("accNo").equals(this.accNo)){
						Element money=doc.createElement("save");
						money.setAttribute("id", "s"+String.format("%04d", money_list.getLength()+1));
						money.setAttribute("date", date);
						money.setAttribute("accNo", this.accNo);
						node.appendChild(doc.createTextNode("    "));
						node.appendChild(money);
						node.appendChild(doc.createTextNode("\n        "));
						money.setTextContent(String.valueOf(amount));
						System.out.println("[INFO] accNo: "+this.accNo+" . A new save of "+amount+" has been deposited");
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
	

}