# Jammed

## Introduction
This project has been created during an Hackathon sponsored by Tim and Google.

- [Demo video](https://github.com/MoonCoders/jammed/releases/download/v1.0.0/demo_video.mp4)
- [Download Jammed Android APK](https://github.com/MoonCoders/jammed/releases/download/v1.0.0/jammed.apk)
- [Presentazione](https://github.com/MoonCoders/jammed/releases/download/v1.0.0/JAMMED.pdf)

##  Requirements

Add the following api keys in the project local.properties
```
# Google apiKey with access for **places** and **maps** API
GOOGLE_MAPS_API_KEY=AIzaxxx 
#Tim apiKey with access for **pedestrian detection** API
TIM_API_KEY=JRxxx
 ```
 
# Jammed Cloud Technical Overview

## Che cos’è Jammed Cloud

Jammed Cloud è l’infrastruttura serverless ospitata su Google Cloud che permette il funzionamento di tutti i Jammed Device, tra cui Jammed Android di cui è stata realizzata la PoC durante questa hackathon. L’infrastruttura è fortemente legata alla piattaforma Tim Digital Business Platform, di cui utilizza in questo momento il servizio di Pedestrian API, ma che in roadmap prevede anche integrazioni con SMS API per il couponing e le reservations e con le altre opportunità di integrazione offerte dalle crescenti API per le Smart Cities.

### Che cos’è un Jammed Device
E’ un qualsiasi dispositivo in grado di comunicare con Jammed Cloud tramite API, quindi ad esempio PC, Smartphone, Totem e Wearable.

### Che servizi espone Jammed Cloud?
Data la crescente preoccupazione delle persone di essere monitorate e seguite e i numerosi problemi legati alla privacy e alla riservatezza dei dati, Jammed Cloud non prevede una registrazione degli utenti, ma solo la geolocalizzazione tramite GPS per poter fornire informazioni utili riguardo alle affluenze dei punti di interesse nei dintorni. I dati comunicati dai Jammed Device a Jammed Cloud sono in forma anonima e non riconducibili a una persona fisica.

I servizi esposti sono:

- Lista dei punti di interesse intorno a una posizione espressa in latitudine, longitudine e raggio circostante
- Per ogni punto di interesse vengono esposti: titolo e indirizzo, dimensioni monitorate in metri quadrati, indicatore di affluenza in questo momento, indicazioni stradali con Google Maps, distribuzione di affluenza nelle diverse fasce orarie dei giorni settimanali, possibilità di vedere il livestream, lista di categorie e sponsor del punto di interesse (logo, headline)

### Come funziona?
Un Jammed Device avvia la propria sessione interrogando Jammed Cloud sui punti di interesse presenti nei dintorni, inviando latitudine, longitudine e raggio. 

La richiesta arriva alla CDN. Se l’informazione è presente in cache, viene immediatamente restituita al client, altrimenti viene invocata una Cloud Function. Questo permette di sostenere carichi maggiori e di ridurre i costi.

La Cloud Function individua i diversi punti di interesse presenti nell’area e poi interroga i diversi Data Provider implementati. Ciascuno fornisce un indice di affluenza in questo momento e una affluenza media. Viene calcolata una media delle affluenze segnalate da ciascun provider e restituita come risultato, che viene cachato dalla CDN con un TTL (Time-To-Live) e restituito al client.

Ciascun Data Provider, ricevuta la richiesta del punto di interesse, può rispondere con HTTP 200 OK e il risultato oppure con un HTTP 404 Not Found se il punto di interesse non è supportato.

In questa hackathon abbiamo sviluppato il Data Provider di Skyline Webcams + TIM Pedestrian APIs. Per ragioni di tempo, quello che vedrete nella demo app non rispecchia l’idea originale, che avrebbe richiesto considerevole tempo in più e integrazioni con provider esterni. Non abbiamo messo in opera alcuna componente lato Cloud, dato che non avevamo a disposizione uno spazio in cloud dedicato per l’hackathon e per noi non era sostenibile attivare delle utente solo per una competizione. Ci siamo focalizzati sul prodotto e sulla soluzione concreta, descrivendo le interazioni tra le varie componenti e attori in gioco.

#### Idea Completa
Per ogni punto di interesse sono mappate da 1 a N viste, una per ogni webcam a inquadratura fissa presente. Non sono previste inquadrature mobili.
Per ogni vista viene effettuata una mappatura dei metri quadrati coperti.

Ricevuto il punto di interesse, viene prelevato un frame dalla webcam live e inviato alle Tim Pedestrian API. Mediante le formule di calcolo dell’affluenza viene calcolato il valore di affluenza istantaneo, che viene storicizzato per i calcoli delle affluenze medie e restituito come risultato.

#### Idea realizzata
Il server è InApp con le configurazioni cablate. Abbiamo mappato 4 punti di interesse sulla mappa e per ognuno abbiamo prelevato una istantanea di un frame dalla webcam Skyline che abbiamo trovato in rete. Questa istantanea viene inviata alle TIM Pedestrian API e viene effettuato il calcolo dell’affluenza istantaneo. Essendo un server standalone InApp non avviene alcuna storicizzazione e i dati di affluenza medi che vedrete sono mockati a scopo dimostrativo.
