package banking;
import files.FilePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import java.io.*;
import org.xml.sax.SAXException;

public class User{
    String id;
    String name;
    String address;
    String age;
    String credit;

    public User(String name,String address,String age){
        this.name=name;
        this.address=address;
        this.age=age;
    }

    public void apply(){
        if(this.credit.equals("true")){
            try{
                String filepath = FilePath.userInfo;
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(filepath);

                Node bank = doc.getFirstChild();

                Node user = doc.getElementsByTagName("user").item(0);
                NamedNodeMap attr = user.getAttributes();
                Node nodeAttr = attr.getNamedItem("id");
                nodeAttr.setTextContent("0002");
                
                NodeList list = user.getChildNodes();
                for (int i = 0; i < list.getLength(); i++){
                    Node node = list.item(i);
                    if ("juniorAccount".equals(node.getNodeName())) {
                        NamedNodeMap jacc_attr = node.getAttributes();
                        Node jacc_attr_id = jacc_attr.getNamedItem("accNo");
                        jacc_attr_id.setTextContent("0002j");
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
    }

    public String credit(){
        return this.credit="true";
    }
    
    public static void main(String[] arg) {
    	User user=new User();
    	user.apply();
    }
}