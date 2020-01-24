#include <SoftwareSerial.h>
SoftwareSerial Blue(2, 3);// 3-RXD  ;  2-TXD
int relay=7;
long int data;
long int arreglo[3]={92,79,91};
char state=0;

void setup() {
  // put your setup code here, to run once:
  pinMode(relay,OUTPUT);
  digitalWrite(relay,LOW);
  Serial.begin(9600);
  Blue.begin(9600);
}

void loop() {

  while(Blue.available()==0) ;
 
    if(Blue.available()>0) 
    {
        data = Blue.parseInt();
    } 
    delay(400);
    int delimitador=0;
    for(int j=0;j<3;j++)
    {
         if(data==arreglo[j])
         {
           delimitador++;
         }
    }

    if(delimitador==0)
    {
        long int i=0;
        while(i<data)
        {
          digitalWrite(relay,HIGH);
           Serial.println("Relay Accionado");
           delay(1000);

           digitalWrite(relay,LOW);
           Serial.println("Relay No Accionado");
           delay(1000);
           i+=2;
        }
    }
}
