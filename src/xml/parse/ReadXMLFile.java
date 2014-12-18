
package xml.parse;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.*;
import java.io.File;

import java.util.Scanner;
import java.util.ArrayList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ReadXMLFile {
    
   private static NodeList nList;
   private static Document doc;
   private static String[] act;
  
     
   public static String PrintX(int i){
            Node nNode = nList.item(i);
	    Element eElement = (Element) nNode;
			return ("id:  " + eElement.getElementsByTagName("id").item(0).getTextContent())+
                               ("Name: " + eElement.getElementsByTagName("Name").item(0).getTextContent())+
			       ("Category : " + eElement.getElementsByTagName("Category").item(0).getTextContent())+
			       ("Price : " + eElement.getElementsByTagName("Price").item(0).getTextContent());    
    }
    
    public static ArrayList find(String param){
         ArrayList search=new ArrayList();
         for (int temp = 0; temp < nList.getLength(); temp++){
 
             Node nNode = nList.item(temp);
             Element eElement = (Element) nNode;
             String check = eElement.getElementsByTagName("Name").item(0).getTextContent().replaceAll("^\\s","");

             if (param.equals("*"))  search.add( PrintX(temp));
             if (check.startsWith(param) && (!param.equals("/*")) ) search.add( PrintX(temp));
             } 
    return search;
    }
     
    public static void edit(String[] param) throws TransformerException{
        if (param.length != 4) {System.out.println("Wrong number of arguments!");
        }else { NodeList nodes = doc.getElementsByTagName("Dish");
	            for (int i = 0; i < nodes.getLength(); i++) {
                    Node nNode = nodes.item(i);
                    Element element = (Element) nNode;       
                      if (param[3].length()<3) {System.out.println("New name is too short");
                      } else if (element.getElementsByTagName("id").item(0).getTextContent().trim().equals(param[1])) { 
                      switch (param[2]){
                              case "Name":
                                   element.getElementsByTagName("Name").item(0).setTextContent(param[3]+"\n");
                                   break;
                              case "Category":
                                   element.getElementsByTagName("Category").item(0).setTextContent(param[3]+"\n");
                                   break;
                              case "Price":
                                   element.getElementsByTagName("Price").item(0).setTextContent(param[3]+"\n");
                                   break;
                             default: System.out.println("TagName or id is wrong");
                    }
                    }
                      filePut();
                      System.out.println(PrintX(i));}
                      }
    }
     
    public static void add(String[] param) throws Exception{
      if (param.length != 5) {System.out.println("Wrong number of arguments!");
     }else if (param[2].length() <5) {System.out.println("Name is too short!");
     }else { Element root = doc.getDocumentElement();
            System.out.println(root.getTextContent());
            Element dish=doc.createElement("Dish");
            root.appendChild(dish);
    
            Element id = doc.createElement("id");
            Element name = doc.createElement("Name");
            Element category = doc.createElement("Category");
            Element price = doc.createElement("Price");
      
            dish.appendChild(id);
            id.setTextContent(param[1]+"\n");
            
            dish.appendChild(name);
            name.setTextContent(param[2]+"\n");
            
            dish.appendChild(category); 
            category.setTextContent(param[3]+"\n");
            
            dish.appendChild(price); 
            price.setTextContent(param[4]+"\n");
   
            for (int temp = 0; temp < nList.getLength(); temp++){
            System.out.println(PrintX(temp));}
            filePut();
      }
    }
    
    public static void delete(String arg) throws TransformerException {
         NodeList nodes = doc.getElementsByTagName("Dish");
		for (int i = 0; i < nodes.getLength(); i++) 
                {    Element element = (Element) nodes.item(i);
			if (element.getElementsByTagName("id").item(0).getTextContent().trim().equals(arg))
                        {  Element root = doc.getDocumentElement();
			    root.removeChild(nodes.item(i));
		            filePut();
			}
		}
      for (int temp = 0; temp < nodes.getLength(); temp++){
      System.out.println(PrintX(temp));}
    }
    
    public static void filePut() throws TransformerException {
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		        Transformer transformer = transformerFactory.newTransformer();
		        DOMSource domSrc = new DOMSource(doc);
		        StreamResult strResult = new StreamResult(new File("Resources\\Restaurant.txt"));
		        transformer.transform(domSrc, strResult);
    }       
   
    public static void main(String argv[]) {
    try {
        
	File fXmlFile = new File("Resources\\Restaurant.txt");
    
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	doc = dBuilder.parse(fXmlFile);
        nList = doc.getElementsByTagName("Dish");
        
	for (int temp = 0; temp < nList.getLength(); temp++){
        System.out.println(PrintX(temp));}

        System.out.println("Write find <what to find> to search");
        System.out.println("Write edit <id> <Tag to edit> <New value of tag > to edit, for example: edit 2 Category First ");
        System.out.println("Write add <id> <Name> <Category><Price> to add a new element, for example: add 5 Cola Drinks 15");
        System.out.println("Write delete <id> to delete"); 
        System.out.println("Write Exit to exit");
        
         while(true){
              Scanner scan=new Scanner(System.in);
              String action=scan.nextLine();
              act=action.split(" ");
        
              if (action.equals("Exit")) break;
                  switch (act[0]) {	
				case "find":
					System.out.println(find(act[1]));
					break;
				case "edit":
					edit(act);
					break;
				case "add":
					add(act);
					break;
				case "delete":
					delete(act[1]);
					break;
                                    }  
            }
    } catch(ArrayIndexOutOfBoundsException e){
             System.out.println("Wrong command type");
    }catch (Exception e) {
             System.out.println("Error has occured "+e);
    }
  }
}