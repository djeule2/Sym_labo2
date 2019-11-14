/*
Olivier & Matthieu
Compression Activity
 */

package util;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import model.Person;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import java.util.List;

import java.lang.reflect.Method;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)

public class Serializer {
    private static String TAG = Serializer.class.getSimpleName();

    /*
     * la methode serializeXml() prend en paramètre un Oject de type Person et retourne un String au format xml
     * dans l'élaboration de cette methode nous avons procédé de manière mécanique suivant les
     * exigences de la dtd.
     * Dans un premier temp on définit un document,  ensuite les noeud(Element) du document
     * dans notre exemple on a pris comme premier élément nom de la class person puis comme sous element
     * les attributs de la class person (name, firsname, gender). on associe le noeud parent à  celui de l'enfant
     * avec la methode addContent().
     **/

    public String serializeXml(Person persons) {

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
    /*
     * cette methode recupère un Oject de type personne et retourne un string sur le format json (clé: valeur)
     * tous les getteurs de la class person seront associés à  nos valeurs. les clés  associé  à  ces valeurs,
     * seront les sous chaines ayant pour préfixe get à  partir de l'indixe 3.
     * exemple "getname" correspondra à  la clé "name"
     *
     * NB: cette algorithme à  été inspirer de la correction de la serie d'exercice 3 du cour.
     * **/

    public String serializeJson(Person persons) {
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


    /*
     * le methode deserialiseXml prend en paramètre un String dans le format xml et retourne un Objet
     * de type person
     * dans cette methode on converti le parametre string à  un objet Document .
     * par la suite on reccupère les elements du document donc les valeur correspondra au atttribut
     * de notre objet person
     * */

    public static Person deserilizeXml(String xml) {
        Person person = new Person();

        Document document = getDocumentFromString(xml);
        Element classElement = document.getRootElement();
        List<Element> personList = classElement.getChildren();
        for (Element personn : personList) {
            if(personn.getName().equals("person")) {
                person.setName(personn.getChild("name").getText());
                person.setFirstname(personn.getChild("firstname").getText());
                person.setGender(personn.getChild("gender").getText());
                person.setNumberPhone(Integer.parseInt(personn.getChild("phone").getText()));
            }

        }
        return person;
    }

    /*
     * la methode générique deserialiseJson() prend en paramètre un string au format json
     * et retourne un objet .
     * dans cette methode on cherche dans le nouvau objet à  construire tous les methode ayant pour prefixe "set"
     * on extrait des nom de ces methodes leurs sous chaines à  partir de l'indice 3
     * c-a-d sans le préfice "set". on verifi si la sous chaine trouvé est contenu dans la chaine d'entrée.
     *  si c'est le cas, reccupère ensuite la valeur pour constrouire le nouveau objet
     *
     * NB: cette algorithme est inspirer de la correction de serie3 des exercices du cour
     * */

    public static <T extends Serializable> T deserializeJson(Class<T> clazz, String json) {
        try {
            JSONObject data = new JSONObject(json);
            T o = clazz.newInstance();
            for(Method m : clazz.getMethods()) {
                if(m.getName().startsWith("set")) {
                    //we found a setter
                    try {
                        String variableName = m.getName().substring(3);
                        if(data.has(variableName)) {
                            Object value = data.get(variableName);
                            Class[] parametersType = m.getParameterTypes();
                            if(parametersType == null || parametersType.length != 1) {
                                Log.w(TAG, "not a valid setter: " + m.getName());
                                continue;
                            }

                            if(parametersType[0].equals(String.class)) {
                                m.invoke(o, (String) value);
                            }
                            else if (parametersType[0].equals(Integer.class)) {
                                m.invoke(o, (Integer) value);
                            }
                            else {
                                Log.e(TAG, "Unsupported type to deserialize: " + parametersType[0]);
                            }
                        }
                    } catch(ReflectiveOperationException e) {
                        Log.w(TAG, "Exception while serializing", e);
                    } catch(JSONException e) {
                        Log.w(TAG, "Exception while converting element to json", e);
                    }
                }
            }
            return o;

        } catch(JSONException e) {
            Log.w(TAG, "Exception while parsing json", e);
            return null;
        } catch(ReflectiveOperationException e) {
            Log.w(TAG, "Exception using reflexion", e);
            return null;
        }
    }


    /*
     * la methode getDocumentFomString()  prend en paramètre un string et nous retourne un document
     * */
    public static Document getDocumentFromString(String string)  {
        String encoding = "UTF-8";
        if (string == null) {
            throw new IllegalArgumentException("string may not be null");
        }

        byte[] byteArray = null;
        try {
            byteArray = string.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try {
            document = builder.build(byteArrayInputStream);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return document;
    }
}
