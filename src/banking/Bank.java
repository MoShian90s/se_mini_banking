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
						break;
					}else if(type==2){
						if(Integer.parseInt(user.age)>=16){
							Element cAcc = doc.createElement("currentAccount");
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
							break;
						}else{
							System.out.println(":( dennied for age condition, sorry");
						}							
					}else if(type==3){
						Element cAcc = doc.createElement("currentAccount");
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

	public void closeAccount(String userId, int type) {
		try{
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			
			NodeList list= doc.getElementsByTagName("user");
			
			for(int i=0;i<list.getLength();i++){
				Element aimed_user =(Element)list.item(i);
				
				if(aimed_user.getAttribute("id").equals(userId)){
					if(type==1){
						NodeList closedAcc_list = aimed_user.getElementsByTagName("currentAccount");
						for(int k=0;k<closedAcc_list.getLength();k++){
							Node closedAcc = closedAcc_list.item(k);
							aimed_user.removeChild(closedAcc);
						}
					}
				}
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

	public void closeAccount(String AccId){
		try{
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			NodeList list= doc.getElementsByTagName("currentAccount");

			for(int i=0;i<list.getLength();i++){
				if(list.item(i).getNodeType()==Node.ELEMENT_NODE){
					Element closedAcc = (Element) list.item(i);
					if(closedAcc.getAttribute("accNo").equals(AccId)){
						closedAcc.getParentNode().removeChild(closedAcc);
					}
				}
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

	public void suspend(String accNo){
		try{
			String filepath = FilePath.userInfo;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			NodeList list= doc.getElementsByTagName(judgeType(accNo));

			for(int i=0;i<list.getLength();i++){
				if(list.item(i).getNodeType()==Node.ELEMENT_NODE){
					Element Acc = (Element) list.item(i);
					if(Acc.getAttribute("accNo").equals(accNo)){
						Acc.setAttribute("state", "false");
					}
				}
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

	public void update() {
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
					System.out.print("currentAccountID: " + check.getAttribute("accNo") + "\na message has been sent.");
					break;
				}
			}
			if(count==0)System.out.println("No account is overdraft");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}

	public String judgeType (String accNo){
		String re = null;
		if(accNo.charAt(4)=='c') re = "currentAccount";
		else if(accNo.charAt(4)=='j') re = "juniorAccount";
		else if(accNo.charAt(4)=='s') re = "saverAccount";

		return re;
	}
}
