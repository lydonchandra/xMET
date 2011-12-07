/******************************************************************************
 * xMET - eXtensible Metadata Editing Tool<br>
 * <br>
 * Copyright (C) 2010-2011 - Office Of Spatial Data Management<br>
 * <br>
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.<br>
 * <br>
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br>
 * <br>
 * For a copy of the GNU General Public License, see http://www.gnu.org/licenses
 ******************************************************************************/
package xmet.tools.metadata.editor.views.custom;

//
// import java.io.File;
// import java.io.IOException;
// import javax.xml.parsers.DocumentBuilder;
// import javax.xml.parsers.DocumentBuilderFactory;
// import javax.xml.parsers.ParserConfigurationException;
//
// import org.w3c.dom.Document;
// import org.w3c.dom.Element;
// import org.w3c.dom.Node;
// import org.w3c.dom.NodeList;
// import org.xml.sax.SAXException;
//
// import xmet.client.profiles.model.ElementDeclaration;
// import xmet.client.profiles.model.Entity;
// import xmet.client.profiles.model.ModelUtils;
// import xmet.client.profiles.model.Optional;
// import xmet.client.profiles.model.Repeated;
//
// /**
// * @author Shaan
// *
// */
/**
 * UILoader.
 */
public final class UILoader {
    //
    /* private static Entity model; */
    //
    /* public static View loadUI(String fileName) throws SAXException, */
    /* IOException, ParserConfigurationException { */
    /* DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory */
    // .newInstance();
    /* DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder(); */
    /* Document doc = docBuilder.parse(new File(fileName)); */
    //
    // /*normalize text representation */
    /* doc.getDocumentElement().normalize(); */
    // /*System.out.println ("Root element of the doc is " + */
    // /*doc.getDocumentElement().getNodeName()); */
    /* NodeList nlView = doc.getElementsByTagName("View"); */
    /* Node viewNode = nlView.item(0); */
    /* Element viewElement = (Element) viewNode; */
    //
    /* NodeList pages = viewElement.getChildNodes(); */
    // /*System.out.println("Number of Pages = " + pages.getLength()); */
    //
    /* String viewName = viewElement.getAttribute("name"); */
    /* Page rootPage = new Page("root"); */
    // /*System.out.println("View Name=" + viewName); */
    /* for (int pageNum = 0; pageNum < pages.getLength(); pageNum++) { */
    /* Node pageNode = pages.item(pageNum); */
    /* if (pageNode.getNodeType() == Node.ELEMENT_NODE) { */
    /* expandPageNode(pageNode, rootPage); */
    // }
    // }
    /* View view = new View(viewName, rootPage); */
    /* return view; */
    // }
    //
    /* public static Page loadUI2(String fileName, Entity model) */
    /* throws SAXException, IOException, ParserConfigurationException { */
    /* UILoader.model = model; */
    /* DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory */
    // .newInstance();
    /* DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder(); */
    /* Document doc = docBuilder.parse(new File(fileName)); */
    //
    // /*normalize text representation */
    /* doc.getDocumentElement().normalize(); */
    // /*System.out.println ("Root element of the doc is " + */
    // /*doc.getDocumentElement().getNodeName()); */
    /* NodeList nlView = doc.getElementsByTagName("View"); */
    /* Node viewNode = nlView.item(0); */
    /* Element viewElement = (Element) viewNode; */
    //
    /* NodeList pages = viewElement.getChildNodes(); */
    // /*System.out.println("Number of Pages = " + pages.getLength()); */
    //
    /* String viewName = viewElement.getAttribute("name"); */
    /* Page rootPage = new Page("root"); */
    // /*System.out.println("View Name=" + viewName); */
    /* for (int pageNum = 0; pageNum < pages.getLength(); pageNum++) { */
    /* Node pageNode = pages.item(pageNum); */
    /* if (pageNode.getNodeType() == Node.ELEMENT_NODE) { */
    /* expandPageNode(pageNode, rootPage); */
    // }
    // }
    /* return rootPage; */
    // }
    //
    /* private static void expandPageNode(Node pageNode, Page rootPage) { */
    /* Element pageElement = (Element) pageNode; */
    /* Page page = new Page(pageElement.getAttribute("name")); */
    /* rootPage.add(page); */
    // /*System.out.println(pageElement.getNodeName() + */
    // /*pageElement.getAttribute("id")); */
    //
    /* NodeList page2 = pageElement.getElementsByTagName("Page"); */
    /* for (int i = 0; i < page2.getLength(); i++) { */
    /* Node pageNode2 = page2.item(i); */
    /* if (pageNode2.getNodeType() == Node.ELEMENT_NODE) { */
    /* expandPageNode(pageNode2, page); */
    // }
    // }
    //
    /* NodeList panel = pageElement.getElementsByTagName("Panel"); */
    //
    /* Node panelNode = panel.item(0); */
    /* if (panelNode.getNodeType() == Node.ELEMENT_NODE) { */
    /* expandPanelNode(panelNode, page); */
    // }
    // }
    //
    /* private static void expandPanelNode(Node panelNode, Page page) { */
    /* Element panelElement = (Element) panelNode; */
    //
    /* NodeList layout = panelElement.getElementsByTagName("Layout"); */
    //
    /* Node layoutNode = layout.item(0); */
    /* if (layoutNode.getNodeType() == Node.ELEMENT_NODE) { */
    /* expandLayoutNode(layoutNode, page); */
    // }
    //
    /* NodeList guiObjects = panelElement.getElementsByTagName("GUIObjects"); */
    /* Node guiObjectsNode = guiObjects.item(0); */
    /* if (guiObjectsNode.getNodeType() == Node.ELEMENT_NODE) { */
    /* Element guiObjectsElement = (Element) guiObjectsNode; */
    //
    /* guiObjects = guiObjectsElement.getChildNodes(); */
    //
    /* for (int i = 0; i < guiObjects.getLength(); i++) { */
    /* Node guiNode = guiObjects.item(i); */
    /* if (guiNode.getNodeType() == Node.ELEMENT_NODE) { */
    /* expandGUINode(guiNode, page); */
    // }
    // }
    //
    // }
    // }
    //
    /* private static void expandLayoutNode(Node layoutNode, Page page) { */
    /* String colValue = "", rowValue = ""; */
    /* Element layoutElement = (Element) layoutNode; */
    /* NodeList col = layoutElement.getElementsByTagName("Col"); */
    //
    /* Node colNode = col.item(0); */
    /* if (colNode.getNodeType() == Node.ELEMENT_NODE) { */
    /* Element colElement = (Element) colNode; */
    /* colValue = colElement.getChildNodes().item(0).getNodeValue(); */
    // /*System.out.println(colValue); */
    // }
    //
    /* NodeList row = layoutElement.getElementsByTagName("Row"); */
    //
    /* Node rowNode = row.item(0); */
    /* if (rowNode.getNodeType() == Node.ELEMENT_NODE) { */
    /* Element rowElement = (Element) rowNode; */
    /* rowValue = rowElement.getChildNodes().item(0).getNodeValue(); */
    // }
    // /*System.out.println("col=" + colValue + "\nrow=" + rowValue); */
    /* page.setColumnSpec(colValue); */
    /* page.setRowSpec(rowValue); */
    // //page.getPanel().setLayout(new FormLayout(colValue, rowValue));
    // }
    //
    /* private static void expandGUINode(Node guiNode, Page page) { */
    /* Element guiElement = (Element) guiNode; */
    // /*System.out.println(guiElement.getNodeName()); */
    /* GUIObject guiObject = new Label(); */
    //
    /* try { */
    /* guiObject = (GUIObject) UIMappings.get(guiElement.getNodeName()) */
    // .newInstance();
    /* page.getGuiObjects().add(guiObject); */
    // } catch (InstantiationException e) {
    // /*TODO Auto-generated catch block */
    /* e.printStackTrace(); */
    // } catch (IllegalAccessException e) {
    // /*TODO Auto-generated catch block */
    /* e.printStackTrace(); */
    // } catch (Exception e) {
    // /*TODO Auto-generated catch block */
    /* e.printStackTrace(); */
    // }
    //
    /* NodeList properties = guiElement.getElementsByTagName("Properties"); */
    /* Node propertyNode = properties.item(0); */
    /* if (propertyNode.getNodeType() == Node.ELEMENT_NODE) { */
    /* Element propertyElement = (Element) propertyNode; */
    //
    /* NodeList nodeList = propertyElement.getChildNodes(); */
    /* for (int i = 0; i < nodeList.getLength(); i++) { */
    /* Node node = nodeList.item(i); */
    /* if (node.getNodeType() == Node.ELEMENT_NODE) { */
    /* Element element = (Element) node; */
    // /*System.out.println(element.getNodeName() + "," + */
    // /*element.getNodeValue()); */
    /* if (element.getNodeName().equalsIgnoreCase("constraints")) { */
    /* String constraints = element.getChildNodes().item(0) */
    // .getNodeValue();
    // /*System.out.println(constraints); */
    // //builder.add(guiObject, constraints);
    /* guiObject.setConstraints(constraints); */
    // } else if (element.getNodeName().equalsIgnoreCase("text")) {
    /* String text = element.getChildNodes().item(0) */
    // .getNodeValue();
    /* if (guiObject instanceof Label) { */
    /* Label label = (Label) guiObject; */
    /* label.setValue(text); */
    // }
    // } else if (element.getNodeName().equalsIgnoreCase("entity")) {
    /* if (element.getChildNodes().getLength() > 0) { */
    /* String entityName = element.getChildNodes().item(0) */
    // .getNodeValue();
    /* if ((entityName != null) && (!entityName.isEmpty())) { */
    /* System.out.println(entityName); */
    /* String[] entities = entityName.split("\\."); */
    /* Entity entity = null; */
    /* for (int q = 0; q < entities.length; q++) { */
    /* if (q < 1) { */
    /* entity = getEntityByName(model, */
    /* entities[0]); */
    // } else {
    /* entity = getEntityByName(entity, */
    /* entities[q]); */
    // }
    /* if (entity != null) */
    /* System.out.println(entities[q]); */
    // }
    /* guiObject.setEntity(entity); */
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    //
    // /*this function should be in some place else */
    /* private static Entity getEntityByName(Entity parent, String child) { */
    /* if (parent == null) */
    /* return null; */
    /* Entity entity = null; */
    /* if (parent instanceof ElementDeclaration) { */
    /* entity = ModelUtils.asElementDeclaration(parent).getGroup() */
    // .getChildByName(child);
    // } else if (parent instanceof Optional) {
    /* ModelUtils.asOptional(parent).setTermPresent(true); */
    /* return getEntityByName(ModelUtils.asOptional(parent).getSetTerm(), */
    /* child); */
    // } else if (parent instanceof Repeated) {
    /* if (parent.asRepeated().size() > 0) { */
    /* System.out.println("repeated=" */
    // + parent.asRepeated().get(0).getName());
    /* return getEntityByName(parent.asRepeated().get(0), child); */
    // }
    // } else {
    //
    // }
    //
    /* return entity; */
    // }
    //
    // /**
    // * @param args
    // */
    /* public static void main(String[] args) throws Exception { */
    // /*TODO Auto-generated method stub */
    /* UIMappings.loadUIMappings(""); */
    /* UILoader.loadUI("src/xmet/client/resources/customviews/anzmet.xml"); */
    // }
    //
}
