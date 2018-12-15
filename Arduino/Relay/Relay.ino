#define fdchaut 4 //Grove 
#define fdcbas 5 //Grove
#define BP_ouverture 7 //Grove button
#define BP_fermeture 8 //Grove button
#define commande_moteur_horaire 9 //Grove relay
#define commande_moteur_horaire2 10 //Grove relay
#define commande_moteur_antihoraire 11 //Grove relay
#define commande_moteur_antihoraire2 12 //Grove relay

void setup()
{
    pinMode(fdchaut, INPUT);
    pinMode(fdcbas, INPUT);
    
    pinMode(BP_fermeture, INPUT);
    pinMode(BP_ouverture, INPUT);

    pinMode(commande_moteur_horaire, OUTPUT);
    pinMode(commande_moteur_horaire2, OUTPUT);
    pinMode(commande_moteur_antihoraire, OUTPUT);
    pinMode(commande_moteur_antihoraire2, OUTPUT);

}

void loop() 
{
    if(digitalRead(fdchaut) || digitalRead(fdcbas)) // Arrêt de l'ordre de marche bluetooth si fin de course //
    {
      
    }
    // Commande moteur ouverture //
    if(digitalRead(BP_ouverture) && !digitalRead(fdchaut))
    {
      digitalWrite(commande_moteur_horaire,HIGH);
      digitalWrite(commande_moteur_horaire2,HIGH);
      digitalWrite(commande_moteur_antihoraire,LOW);
      digitalWrite(commande_moteur_antihoraire2,LOW);
    }

    // Commande moteur fermeture //
    else if(digitalRead(BP_fermeture) && !digitalRead(fdcbas))
    {
       digitalWrite(commande_moteur_horaire,LOW);
       digitalWrite(commande_moteur_horaire2,LOW);
       digitalWrite(commande_moteur_antihoraire,HIGH);
       digitalWrite(commande_moteur_antihoraire2,HIGH);
    }
    
    // arrêt des moteur //
    else
    {
       digitalWrite(commande_moteur_horaire,LOW);
       digitalWrite(commande_moteur_horaire2,LOW);
       digitalWrite(commande_moteur_antihoraire,LOW);
       digitalWrite(commande_moteur_antihoraire2,LOW);
    }

}

