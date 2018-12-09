/*
 Programme créer par Simon, projet Garage automatisé (Capteur Infrarouge)
 Version du programme: v1
 
 */



#include <IRremote.h>  // Bibliothèque utilisé pour le capteur infrarouge.

#define PIN_IR 3  // Emeteur infrarouge en D3.
#define PIN_DETECT 2  // Récepteur Infrarouge en D2.
#define PIN_STATUS 7  // LED en D7.

IRrecv irrecv(PIN_IR);  // Déclaration de l'émetteur infrarouge, celui qui va envoyer le signal analogique.
IRsend irsend;  // Activation du mode receveur pour le capteur receveur infrarouge.         

void setup()
{
  pinMode(PIN_DETECT, INPUT);  // Déclaration du récepteur en entrée digital.
  pinMode(PIN_STATUS, OUTPUT);  // Déclaration de la LED en sortie digital.
  irsend.enableIROut(38);  // Configuration du signal envoyé sur 38k hertz.
  irsend.mark(0);
}

void loop() {
  digitalWrite(PIN_STATUS, !digitalRead(PIN_DETECT));  // LED = Allumé SI PIN_DETECT = 1
}

