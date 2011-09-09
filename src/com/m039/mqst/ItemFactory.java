package com.m039.mqst;

import com.m039.mqst.items.InstantUssd;
import com.m039.mqst.items.InstantSms;
import com.m039.mqst.items.InstantItem;

import org.xml.sax.helpers.DefaultHandler;
import java.util.List;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import android.util.Log;
import org.xml.sax.Attributes;
import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import android.content.res.AssetFileDescriptor;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is a singleton class.
 *
 * In this class static methods use IO (via DOM, writeonly)
 * operations.
 *
 * Not static methods use IO (via SAX, readonly).
 *
 * Created: Tue Aug 30 21:13:00 2011
 *
 * @author <a href="mailto:flam44@gmail.com">Mozgin Dmitry</a>
 * @version 1.0
 */
public class ItemFactory {
    private final static String TAG             = "ItemFactory";
    private final static String TEMPLATES_FILE  = "ItemFactory";

    private static ItemFactory mFactory = null;

    /**
     * This class parse the xml file and fill the mItems appropriate.
     *
     */
    private class MyHandler extends DefaultHandler {
        @Override
        public void                         startElement(String uri,
                                                         String name,
                                                         String qName,
                                                         Attributes atts) {
            InstantItem item = null;

            if (name.equals("item")) {
                try {
                    String type = atts.getValue("type");

                    if (type.equals("sms")) {
                        item = new InstantSms(atts.getValue("help"),
                                              atts.getValue("address"),
                                              atts.getValue("text"),
                                              Boolean.valueOf(atts.getValue("warning")));

                        mItems.add(item);
                    }

                    if (type.equals("ussd")) {
                        item = new InstantUssd(atts.getValue("help"),
                                               atts.getValue("text"));

                        mItems.add(item);
                    }

                    if (atts.getValue("selected").equals("true")) {
                        mSelectedItem = item;
                    }

                } catch (NullPointerException e) {
                    // failed if getValue() doesn't find a attribute
                    Log.e(TAG, "startElement");
                }
            }
        }

    }

    private final MyHandler mHandler = new MyHandler();
    private final List<InstantItem> mItems      = new ArrayList<InstantItem>();
    private InstantItem mSelectedItem;

    public static ItemFactory           getFactory() {
        return mFactory;
    }

    /**
     * Use this function to intialize and return mFactory reference
     */
    public static ItemFactory           getFactory(Context context) {
        if (mFactory == null) {
            mFactory = ItemFactory.parse(context);
        }

        return mFactory;
    }

    public static void                  saveFactory(Context context) {
        try {
            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuilder = dfactory.newDocumentBuilder();

            Document doc = dbuilder.newDocument();

            Element templates = doc.createElement("templates");

            for (InstantItem item: ItemFactory.getFactory().getItems()) {
                Element el = item.createElement(doc);
                templates.appendChild(el);
            }

            OutputStream os = context.openFileOutput(TEMPLATES_FILE, Context.MODE_PRIVATE);
            os.write(getStringFromNode(templates).getBytes());
            os.close();

        } catch (Exception e) {
            Log.e(TAG, "addItem");
        }       
    }

    /**
     * Check if return value is null !
     */
    public static ItemFactory           parse(Context context) {
        assetsToInternalStorage(context);

        ItemFactory ifactory = new ItemFactory();

        try {
            InputStream is = context.openFileInput(TEMPLATES_FILE);

            ifactory.parse(is);

            is.close();

        } catch (Exception e) {
            Log.e(TAG, "parse");
        }

        return ifactory;
    }

    private static void                 assetsToInternalStorage(Context context) {
        // check if the file already in the internal storage

        String[] files  = context.fileList();

        for (String f: files) {
            if (f.equals(TEMPLATES_FILE)) {
                return;
            }
        }

        // read the file from assets

        AssetManager am = context.getAssets();

        try {
            InputStream is          = am.open("sms_templates.xml");
            FileOutputStream fos    = context.openFileOutput(TEMPLATES_FILE, Context.MODE_PRIVATE);
            byte[] bytes            = new byte[1024];

            while (is.read(bytes) != -1) {
                fos.write(bytes);
            }

            is.close();
            fos.close();

        } catch (Exception e) {
            Log.e(TAG, "assetsToInternalStorage");
        }
    }

    public void                         parse(InputStream is) {
        SAXParserFactory factory    = SAXParserFactory.newInstance();
        SAXParser parser;

        try {
            mItems.clear();     // reset items

            parser = factory.newSAXParser();
            parser.parse(is, mHandler);

        } catch (Exception e) {
            Log.e(TAG, "parse");
        }
    }

    public void                         addItem(InstantItem item) {
        mItems.add(item);
    }

    public void                         removeItem(int position) {
        mItems.remove(position);
    }

    public void                         setItem(int position, InstantItem item) {
        mItems.set(position, item);
    }

    public InstantItem                  getItem(int position) {
        return mItems.get(position);
    }

    // debugging purpose

    public static void                  addItem(Context context, InstantItem item) {
        try {
            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbuilder = dfactory.newDocumentBuilder();

            InputStream is = context.openFileInput(TEMPLATES_FILE);
            Document doc = dbuilder.parse(is);
            is.close();

            Node templates = doc.getElementsByTagName("templates").item(0);

            Element el = item.createElement(doc);

            templates.appendChild(el);

            OutputStream os = context.openFileOutput(TEMPLATES_FILE, Context.MODE_PRIVATE);
            os.write(getStringFromNode(templates).getBytes());
            os.close();

        } catch (Exception e) {
            Log.e(TAG, "addItem");
        }
    }

    // taken from SO

    private static String               getStringFromNode(Node root) throws IOException {

        StringBuilder result = new StringBuilder();

        if (root.getNodeType() == 3)
            result.append(root.getNodeValue());
        else {
            if (root.getNodeType() != 9) {
                StringBuffer attrs = new StringBuffer();
                for (int k = 0; k < root.getAttributes().getLength(); ++k) {
                    attrs.append(" ")
                        .append(root.getAttributes().item(k).getNodeName())
                        .append("=\"")
                        .append(root.getAttributes().item(k).getNodeValue())
                        .append("\" ");
                }
                result.append("<").append(root.getNodeName()).append(" ")
                        .append(attrs).append(">");
            } else {
                result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            }

            NodeList nodes = root.getChildNodes();
            for (int i = 0, j = nodes.getLength(); i < j; i++) {
                Node node = nodes.item(i);
                result.append(getStringFromNode(node));
            }

            if (root.getNodeType() != 9) {
                result.append("</").append(root.getNodeName()).append(">");
            }
        }
        return result.toString();
    }


    /**
     * Check return for null
     */
    public InstantItem                  getSelectedItem() {
        return mSelectedItem;
    }

    public List<InstantItem>            getItems() {
        return mItems;
    }
}
