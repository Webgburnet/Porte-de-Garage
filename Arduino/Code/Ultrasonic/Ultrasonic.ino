#include <Ultrasonic.h>

#define alarme_visuel 14 //Grove LED //Broche A1
#define alarme_sonore 15 //Grove Buzzer //broche A2
#define detecteur_mur_fond 16 //Grove Ultrasonic //broche A3

Ultrasonic ultrasonic2(detecteur_mur_fond);

unsigned long date_courante = 0;
int etat_alarme = 0;
long distance_mur;

void setup()
{
  
}

void loop() 
{
    distance_mur = ultrasonic2.MeasureInCentimeters();
    Serial.print("distance mur :");
    Serial.print(distance_mur);
    Serial.println(" cm");
    if(distance_mur <= 2)
    {
      digitalWrite(alarme_visuel, HIGH);
      digitalWrite(alarme_sonore, HIGH);
    }
    else if(distance_mur > 2 && distance_mur <=5)
    {
      if((millis() - date_courante) > 200)
      {
        etat_alarme = !etat_alarme;
        digitalWrite(alarme_visuel, etat_alarme);
        digitalWrite(alarme_sonore, etat_alarme);
        date_courante = millis();
      }
    }
    else if(distance_mur > 5 && distance_mur <=8) // proche du mur
    {
      if((millis() - date_courante) > 400)  // temporisation d'une seconde
      {
        etat_alarme = !etat_alarme;
        digitalWrite(alarme_visuel, etat_alarme);
        digitalWrite(alarme_sonore, etat_alarme);
        date_courante = millis();
      }
    }
    else if(distance_mur > 8 && distance_mur <=11) // proche du mur
    {
      if((millis() - date_courante) > 600)  // temporisation d'une seconde
      {
        etat_alarme = !etat_alarme;
        digitalWrite(alarme_visuel, etat_alarme);
        digitalWrite(alarme_sonore, etat_alarme);
        date_courante = millis();
      }
    }
    else if(distance_mur > 11 && distance_mur <=14) // proche du mur
    {
      if((millis() - date_courante) > 600)  // temporisation d'une seconde
      {
        etat_alarme = !etat_alarme;
        digitalWrite(alarme_visuel, etat_alarme);
        digitalWrite(alarme_sonore, etat_alarme);
        date_courante = millis();
      }
    }
    else if (distance_mur >15)
    {
      digitalWrite(alarme_visuel, LOW);
      digitalWrite(alarme_sonore, LOW);
    }
}

