# PCAD-progetto
## Multithreaded Cluster per Analisi di Testo

L'obiettivo del progetto è quello di istanziare il template Java di middleware client-server distribuito discusso a lezione per integrare e processare dati provenienti da diverse sorgenti eterogenee. 

I sorgenti del template sono a disposizione su aulaweb.


Il template prevede le seguenti componenti:

- Un gateway RMI in grado di ricevere dati inviati da applicazioni client con GUI Swing o JavaFX attraverso il protocollo RMI.
- Un gateway TCP in grado di ricevere dati da App Android attraverso il protocollo TCP e di ridirezionarli al gateway RMI attraverso metodi remoti.
- Una App Android per inviare dati al gateway TCP.

Partendo da tale template il progetto consiste nell'implementare un algoritmo distribuito per processing di dati testuali (es query inviate ad un motore di ricerca) inviati da un'insieme di client georeferenziati (es i messaggi includo testo e indicazione della posizione (zona di Genova) del device). 

Ogni client Java (App) invia messaggi di testo al gateway RMI (TCP) attraverso un metodo remoto (socket TCP).
Il gateway RMI, attraverso un pool di workerthread  (una sorta di cluster multithreaded locale), analizza il testo ricevuto dai vari client eseguendo i seguenti passi:

- estrazione delle singole parole dal testo (eliminando elementi di punteggiatura e normalizzando il testo usando solo lettere minuscole, ecc)

- aggiornamento di una struttura dati condivisa in memoria tra tutti i client  contenente la frequenza per aria geografica di ogni singola parola estratta dalle varie sorgenti.

Il server inoltre deve fornire un metodo remoto (utilizzabile da client o da una console del server) per visualizzare le 3 parole più frequenti in una determinata zona estratte dall'analisi di tutti i messaggi ricevuti dal server (GUI o testuale).

Per la memorizzazione delle frequenze usate una struttura dati concorrente usando come indice esterno la zona e come indice interno la parola stessa.
(es  albaro: [dibris: 5, dima: 6], foce: [fiera: 2])


