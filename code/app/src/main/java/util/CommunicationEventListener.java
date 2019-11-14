/*
Olivier & Matthieu
Interface permettant de traiter la réponse serveur
 */
package util;

import java.util.EventListener;

public interface CommunicationEventListener extends EventListener {
    boolean handleServerResponse(String response);
}
