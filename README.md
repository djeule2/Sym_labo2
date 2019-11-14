# Sym_labo2
Ce laboratoire propose d'illustrer l'utilisation de protocoles de communication applicatifs, basés sur http, dans le cadre d'applications mobiles

4. Questions

4.1 Traitement des erreurs 

Si le serveur n'est pas joignable ou s'il retourne un code HTTP d'erreur, la requ�te tombe dans le vide et ne sera jamais trait�e par le serveur.
L'application ne recevra donc jamais de r�ponse.

Il faudrait rajouter � SymComManager une m�thode
	onRequestFailure(String error)
et une m�thode
	handleServerError(String error)
� l'interface CommunicationEventListener

4.2 Authentification
Une authentification dans un protocole asynchrone est possible pour autant que le serveur et l'utilisateur se connaissent d�j�.
Une solution est d'utiliser une cl� SSH, qu'il faut g�n�rer pour chaque utilisateur (appareil).

Une transmission diff�r�e est possible. On envoie les requ�tes une fois que l'authentification est valid�e.

4.3 Threads concurrents
Cel� peut poser un probl�me d'int�griter des donn�es. Il faut s'assurer que les donn�es lues et modifi�es par les threads le soient de mani�re exclusive.


4.4 Ecriture diff�r�e
Une connexion par transmission diff�r�e a l'avantage de bien s�parer les requ�tes et donc leur traitement (par le serveur par l'application).
Par contre, chaque connexion a un co�t, peut surcharger le serveur et les requ�tes peuvent mettre plus de temps � �tre trait�es.

Multiplexer les connexions implique d'avoir une protocole bien �tabli entre le serveur et l'application, que la structure des messages soient bien d�finies pour pouvoir distinguer chaque transmission. Les requ�tes peuvent �tre plus lourdes et demander plus de m�moire. L'avantage principal est qu'on utilise qu'une seule connexion, donc moins de risque de surcharge c�t� serveur, et moins de risque de connexion qui �choue.

On pourrait imaginer un mix des deux, selon le type de donn�es qu'on voudrait envoyer. Certaines donn�es se pr�tent bien � �tre regroup�es et compress�es, d'autres moins. (Voir slides sur la s�rialisation)



4.5 Transmission d'objets
a. L'avantage d'utiliser une infrastructure comme SOAP est de pouvoir valider les donn�es re�ues plus rigoureusement et de structurer les donn�es. Cela facilite ensuite leur traitement.
L'avantage d'utiliser une infrastructure de type REST/JSON est la flexibilit�. Il est plus facile d'�voluer notre environnement sur du JSON qu'avec du XML+DTD. On peut noter aussi qu'une s�rialisation en JSON est g�n�ralement plus l�g�re qu'en XML.

b. Protocol Buffer peut �tre compatible avec HTTP. En effet, HTTP se contente d'envoyer une suite de byte peu importe sa nature. Il faut, par contre, que l'application et le serveur soit d'accord sur la structure du Protocol Buffer.
L'avantage de Protocol Buffer est qu'il se pr�te bien � des descriptions d'objet (Java par exemple). Il est optimis� pour �a, les messages seront donc plus l�gers qu'avec un JSON ou XML. Moins flexible que JSON et moins portable que XML.

c. On pourrait imaginer un syst�me qui fasse des requ�tes plus pr�cises, plus proches de ce que SQL peut offrir.

4.6 Transmission compress�
JSON de 151KB avec compression niveau 9:
	2605ms
	2461ms
	2303ms
JSON de 151KB sans compression:
	12467ms
	716ms
	735ms
JSON de 3269KB avec compression niveau 9:
	75171ms
JSON de 3269KB sans compression:
	19411ms
	6471ms
	5081ms
JSON de 3269KB avec compression niveau 1:
	74506ms

On remarque que les requ�tes sont ex�cut�es plus rapidement sans compression, alors que �a devrait �tre le contraire...
Tests effectu�s sur connexion mobile, il se peut donc que la d�bit n'ait pas �t� suffisement constant pendant leurs r�alisations.

