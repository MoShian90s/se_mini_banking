
package banking;
import files.FilePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import java.io.*;

import org.xml.sax.SAXException;
/**
 * junior class extends from the BankAccount.class
 * @author yifan, yuan
 * @version 1.0
 */
public class JuniorAccount extends BankAccount {
	/**
	 * the constructor of JuniorAccount
	 * @param accNo the account id
	 */
	public JuniorAccount(String accNo) {
		try{
			int count=0;
			this.accNo=accNo;
			
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			
			NodeList list=doc.getElementsByTagName("juniorAccount");

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

}