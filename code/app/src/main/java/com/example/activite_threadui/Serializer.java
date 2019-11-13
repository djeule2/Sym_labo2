package com.example.activite_threadui;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONObject;
import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Locale;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

public class Serializer {
    private static String TAG = Serializer.class.getSimpleName();

    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
    private Person persons;
    Serializer (Person persons){
        this.persons = persons;
    }

    protected String serializeXml()
    {

        Element racine = new Element("directory");
        DocType docType = new DocType("directory",
                "http://sym.iict.ch/directory.dtd");
        Document document = new Document(racine, docType);
        Element person = new Element("person");
        racine.addContent(person);
        person.addContent(new Element("name").setText(persons.getName()))
                .addContent(new Element("firstname").setText(persons.getFirstname()))
                .addContent(new Element("gender").setText(persons.getGender()))
                .addContent(new Element("phone").setAttribute("type", "mobile").setText(String.valueOf(persons.getNumberPhone())));

        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            String xmlString = outputter.outputString(document);
           // outputter.output(document, new FileOutputStream("person.xml"));
        return xmlString;
    }




    public String serializeJson() {
        if(persons== null) return null;

        JSONObject json = new JSONObject();

        Class c = persons.getClass();
        for(Method m : c.getMethods()) {
            if(m.getName().startsWith("get")) {
                //we found a getter
                try {
                    String variableName = m.getName().substring(3);
                    Object value = m.invoke(persons);

                    if(value instanceof String) {
                        json.accumulate(variableName, value);
                    }
                    else if (value instanceof Date) {
                        json.accumulate(variableName, DATETIME_FORMAT.format((Date) value));
                    }
                    else if (value instanceof Class) {
                        json.accumulate(variableName, ((Class) value).getCanonicalName());
                    }
                    else {
                        Log.e(TAG, "Unsupported type to serialize: " + persons.getClass());
                    }
                }  catch(JSONException e) {
                    Log.w(TAG, "Exception while converting element to json", e);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            return json.toString(1);
        } catch(JSONException e) {
            Log.w(TAG, "Exception while converting to json", e);
            return null;
        }
    }
}
