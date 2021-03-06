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
 * Description: the operations of user
 * @author yifan
 * @version 1.0
 */
public class User{
    String id;
    String name;
    String address;
    String age;
    String credit;

    public User(){}
    /**
     * the constructor of User
     * @param name the name
     * @param address the string address
     * @param age the String age
     * @see #idGenerator()
     */
    public User(String name,String address,String age){
        this.name=name;
        this.address=address;
        this.age=age;
        this.idGenerator();
    }
    /**
     * the method to apply
     * @see #credit()
     */
    public void apply(){
        if(this.credit().equals("true")){
            try{
                String filepath = FilePath.userInfo;
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(filepath);

                Node bank = doc.getFirstChild();

                Element user = doc.createElement("user");
                
                user.setAttribute("id", this.id);
                user.setAttribute("name", this.name);
                user.setAttribute("address", this.address);
                user.setAttribute("age", this.age);
                user.setAttribute("credit", this.credit);

                bank.appendChild(doc.createTextNode("    "));
                bank.appendChild(user);

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
    }
    /**
     * the credit agency
     * @return String
     */
    public String credit(){
        return this.credit="true";
    }
    /**
     * the id generator
     * @see #User(String, String, String)
     */
    public void idGenerator() {
        try{
            int k=1;
            String filepath = FilePath.userInfo;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            Node bank=doc.getFirstChild();
            NodeList list= bank.getChildNodes();

            for(int i=0;i<list.getLength();i++){
                Node node=list.item(i);
                if ("user".equals(node.getNodeName())) k++;
            }
            this.id=String.format("%04d", k);
        }
    	catch (Exception e) {
            e.printStackTrace();
        }
    }
}