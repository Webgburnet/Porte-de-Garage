#include <SoftwareSerial.h>

#define RX 17 //Grove bluetooth //Broche A4
#define TX 18 // Grove bluetooth //Broche A5

SoftwareSerial mySerial(RX,TX);

char bluetooth;
int commande_bluetooth;

void setup()
{
    Serial.begin(9600);
    mySerial.begin(9600);
}

void loop() 
{
    // Ordre de marche bluetooth //
    Serial.print("\t");
    Serial.print("commande bluetooth :");
    Serial.print(commande_bluetooth);
    Serial.print("\t");
    if (mySerial.available()>0)
    {
      bluetooth = mySerial.read() ;
      if (bluetooth == 'A') //Ouverture
      {
        commande_bluetooth = 10;
      }
      else if (bluetooth == 'D') // Fermeture
      {
        commande_bluetooth = 20;
      }
      else if (bluetooth == 'Z') // Stop
      {
        commande_bluetooth = 50;
      }
    }

    // Commande moteur ouverture //
    if(commande_bluetooth == 10)
    {
      Serial.println("Commande moteur ouverture");
    }

    // Commande moteur fermeture //
    else if(commande_bluetooth == 20)
    {
       Serial.println("Commande moteur fermeture");
    }
    
    // arrêt des moteur //
    else if(commande_bluetooth == 30)
    {
       Serial.println("Commande moteur stop");
    }
    else
    {
       digitalWrite(commande_moteur_horaire,LOW);
       digitalWrite(commande_moteur_horaire2,LOW);
       digitalWrite(commande_moteur_antihoraire,LOW);
       digitalWrite(commande_moteur_antihoraire2,LOW);
    }
}

