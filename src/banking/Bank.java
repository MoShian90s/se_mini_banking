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

public class Bank {

	public void openAccount(User user,int type) {
		try{
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			
			Node bank = doc.getFirstChild();
			NodeList list= doc.getElementsByTagName("user");

			for(int i=0;i<list.getLength();i++){
				Element aimed_user =(Element)list.item(i);
				
				if(aimed_user.getAttribute("id").equals(user.id)){
					if(type==1){
						Element cAcc = doc.createElement("currentAccount");
						cAcc.setAttribute("accNo", user.id+"c");
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
						break;
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
	}

	public void closeAccount() {

	}

	public void update() {
		
	}

	public static void main(String[] arg){
		User user=new User("Any","UK","19");
		user.apply();
		Bank bank=new Bank();
		bank.openAccount(user, 1);
	}
}
