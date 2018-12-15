/*
 Version du programme: v2
 */



#include <IRremote.h>  // Bibliothèque utilisé pour le capteur infrarouge.

#define emetteur_infra 3  // Emeteur infrarouge en D3.
#define capteur_presence 2  // Récepteur Infrarouge en D2.

IRrecv irrecv(emetteur_infra);  // Déclaration de l'émetteur infrarouge, celui qui va envoyer le signal analogique.
IRsend irsend;  // Activation du mode receveur pour le capteur receveur infrarouge.         

void setup()
{
  pinMode(capteur_presence, INPUT);  // Déclaration du récepteur en entrée digital.
  irsend.enableIROut(38);  // Configuration du signal envoyé sur 38k hertz.
  irsend.mark(0);
}

void loop() {

  // Commande moteur ouverture //
  if(digitalRead(capteur_presence))
  {
    Serial.println("Presence Obstacle");
  }
  else
  {
    Serial.println("Pas d'Obstacle");
  }
}

