package zagurskiy.fit.bstu.todolist;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import lombok.SneakyThrows;

public class XMLHelper {

    private static final String FILE_NAME = "tasks.xml";

    public static void writeXML(Context context, List<Task> tasks) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("Tasks");
            doc.appendChild(root);

            for (Task t : tasks) {
                Element Details = doc.createElement("Task");
                root.appendChild(Details);

                Element description = doc.createElement("Description");
                description.appendChild(doc.createTextNode(t.getDescription()));
                Details.appendChild(description);

                Element category = doc.createElement("Category");
                category.appendChild(doc.createTextNode(t.getCategory()));
                Details.appendChild(category);

                Element date = doc.createElement("Date");
                date.appendChild(doc.createTextNode(t.getDate().toString()));
                Details.appendChild(date);

                Element done = doc.createElement("Done");
                done.appendChild(doc.createTextNode(Boolean.valueOf(t.isDone()).toString()));
                Details.appendChild(done);
            }

            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer tran = tranFactory.newTransformer();
            DOMSource src = new DOMSource(doc);

            File file = new File(context.getFilesDir(), FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);

            StreamResult result = new StreamResult(fos);
            tran.transform(src, result);

            fos.close();
        } catch (Exception e) {
        }
    }

    @SneakyThrows
    public static List<Task> readXML(Context context) {
        if (!isExist(context))
            return new ArrayList<>();

        Task currentTask = null;
        boolean isEntry = false;
        String textValue = "";
        ArrayList<Task> tasks = new ArrayList<>();
        XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
        xppf.setNamespaceAware(true);// включаем поддержку namespace (по умолчанию выключена)
        XmlPullParser xpp = xppf.newPullParser(); //  // создаем парсер

        File file = new File(context.getFilesDir(), FILE_NAME);
        FileInputStream fis = new FileInputStream(file);

        xpp.setInput(fis, null);
        int eventType = xpp.getEventType(); //Позиция внутри документа

        while (eventType != XmlPullParser.END_DOCUMENT) { //открой файл и все вспомнишь)
            String tagName = xpp.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("Task".equalsIgnoreCase(tagName)) {
                        isEntry = true;
                        currentTask = new Task();
                    }
                    break;
                case XmlPullParser.TEXT:
                    textValue = xpp.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if (isEntry) {
                        if ("Task".equalsIgnoreCase(tagName)) {
                            tasks.add(currentTask);
                            isEntry = false;
                        } else if ("Description".equalsIgnoreCase(tagName))
                            currentTask.setDescription(textValue);
                        else if ("Category".equalsIgnoreCase(tagName))
                            currentTask.setCategory(textValue);
                        else if ("Date".equalsIgnoreCase(tagName))
                            currentTask.setDate(LocalDate.parse(textValue));
                        else if ("Done".equalsIgnoreCase(tagName))
                            currentTask.setDone(Boolean.valueOf(textValue));
                    }
                    break;
                default:
            }
            eventType = xpp.next();//Переход на новую позицию в документе
        }
        return tasks;
    }

    public static void deleteTask(Context context, Task taskToDel) {
        List<Task> tasks = XMLHelper.readXML(context);
        tasks.remove(taskToDel);
        XMLHelper.writeXML(context, tasks);
    }

    @SneakyThrows
    public static ArrayList<Task> getTaskByCategory(Context context, String category) {
        if (!isExist(context))
            return new ArrayList<>();

        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(context.getFilesDir(), FILE_NAME);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.parse(file);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        XPathExpression xpeDescription = xpath.compile("/Tasks/Task[Category='" + category + "']/Description/text()");
        XPathExpression xpeDate = xpath.compile("/Tasks/Task[Category='" + category + "']/Date/text()");
        XPathExpression xpeDone = xpath.compile("/Tasks/Task[Category='" + category + "']/Done/text()");
        NodeList nodesDescription = (NodeList) xpeDescription.evaluate(doc, XPathConstants.NODESET);
        NodeList nodesDate = (NodeList) xpeDate.evaluate(doc, XPathConstants.NODESET);
        NodeList nodesDone = (NodeList) xpeDone.evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < nodesDescription.getLength(); i++) {
            Task t = new Task();
            t.setDescription(nodesDescription.item(i).getNodeValue());
            t.setCategory(category);
            t.setDone(Boolean.valueOf(nodesDone.item(i).getNodeValue()));
            t.setDate(LocalDate.parse(nodesDate.item(i).getNodeValue()));
            tasks.add(t);
        }
        return tasks;
    }


    private static boolean isExist(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        return file.exists();
    }
}
