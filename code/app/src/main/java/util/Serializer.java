package util;

import android.util.Log;

import model.Person;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.Serializable;
import java.text.ParseException;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Locale;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

public class Serializer {
    private static String TAG = Serializer.class.getSimpleName();

    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
    private Person persons;

    public Serializer(Person persons) {
        this.persons = persons;
    }

    public String serializeXml() {
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
        if (persons == null) return null;

        JSONObject json = new JSONObject();

        Class c = persons.getClass();
        for (Method m : c.getMethods()) {
            if (m.getName().startsWith("get")) {
                //we found a getter
                try {
                    String variableName = m.getName().substring(3);
                    Object value = m.invoke(persons);

                    if (value instanceof String) {
                        json.accumulate(variableName, value);
                    } else if (value instanceof Integer) {
                        json.accumulate(variableName, value);

                    } else if (value instanceof Date) {
                        json.accumulate(variableName, DATETIME_FORMAT.format((Date) value));
                    } else if (value instanceof Class) {
                        json.accumulate(variableName, ((Class) value).getCanonicalName());
                    } else {
                        Log.e(TAG, "Unsupported type to serialize: " + persons.getClass());
                    }
                } catch (JSONException e) {
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
        } catch (JSONException e) {
            Log.w(TAG, "Exception while converting to json", e);
            return null;
        }
    }

    public static <T extends Serializable> T deserialize(Class<T> clazz, String json) {
        try {
            JSONObject data = new JSONObject(json);
            T o = clazz.newInstance();
            for (Method m : clazz.getMethods()) {
                if (m.getName().startsWith("set")) {
                    //we found a setter
                    try {
                        String variableName = m.getName().substring(3);
                        //  Determine if the JSONObject contains a specific key.
                        if (data.has(variableName)) {
                            // Get the value object associated with a key variableName.
                            Object value = data.get(variableName);
                            Class[] parametersType = m.getParameterTypes();
                            if (parametersType == null || parametersType.length != 1) {
                                Log.w(TAG, "not a valid setter: " + m.getName());
                                continue;
                            }
                            if (parametersType[0].equals(String.class)) {
                                m.invoke(o, (String) value);
                            }else if (parametersType[0].equals(Integer.class)){
                                m.invoke(o, (Integer) value);
                            }
                            else if (parametersType[0].equals(Date.class)) {
                                try {
                                    m.invoke(o, DATETIME_FORMAT.parse((String) value));
                                } catch (ParseException e) {
                                    Log.w(TAG, "Exception while parsing date: " + value, e);
                                }
                            } else {
                                Log.e(TAG, "Unsupported type to deserialize: " + parametersType[0]);
                            }
                        }

                    } catch (JSONException e) {
                        Log.w(TAG, "Exception while converting element to json", e);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            return o;

        } catch (JSONException e) {
            Log.w(TAG, "Exception while parsing json", e);
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
