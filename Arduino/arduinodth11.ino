// Incluimos librería
#include <Thread.h>
#include <SoftwareSerial.h>
SoftwareSerial Blue(2, 3);// 3-RXD  ;  2-TXD
#define ANALOGCARGA 0
// Variables
int analogValor = 0;
float voltaje = 0; 

//Controlador de carga
long int data;
int carga1 = 13;
int carga2= 12;
long int password1 = 79;// solar
long int password2 = 92; // usb
long int password3 =91; //  apagas
char state = 0;

Thread thrBlink2 = Thread();
Thread thrBlink3 = Thread();

void setup() {
  Serial.begin(9600);

  //Controlador de Carga
  pinMode(carga1, OUTPUT);
  pinMode(carga2, OUTPUT); 
  digitalWrite(carga1, LOW);
  digitalWrite(carga2, LOW);
  Blue.begin(9600);

  //Bluetooth
  thrBlink2.enabled = true;
  thrBlink2.setInterval(500);
  thrBlink2.onRun(codigo_Bluetooth);
  //Carga
  thrBlink3.enabled = true;
  thrBlink3.setInterval(500);
  thrBlink3.onRun(medir_carga);
}
void blink() {
  //definir tarea
}
void loop() {
  // Esperamos 5 segundos entre medidas
  thrBlink2.run();
  thrBlink3.run();
}
void codigo_Bluetooth(){ 
   if(Blue.available()==0){
    return;
   }
 
    if(Blue.available()>0) 
    {
        data = Blue.parseInt();
 
    } 
    delay(1);
  //Serial.print(data);
 
  if (data == password1)
    {
  
      digitalWrite(carga1,HIGH);
      digitalWrite(carga2, LOW);
      Serial.println("Solar ON ");
  
    }
   
   if( data == password2)
     {
       digitalWrite(carga1,LOW);
       digitalWrite(carga2, HIGH);
       Serial.println("USB ON");
     }

   if( data == password3)
     {
       digitalWrite(carga1,LOW);
       digitalWrite(carga2, LOW);
       Serial.println("OFF All");
     }
 

  
}
void medir_carga(){  
  // Leemos valor de la entrada analógica
  analogValor = analogRead(ANALOGCARGA);
  // Obtenemos el voltaje
  voltaje = 0.0029 * analogValor;
  sigfoxSend(&voltaje,sizeof(voltaje));
  Serial.print("Voltaje: ");
  Serial.println(voltaje);
  delay(1000);
}

bool sigfoxSend(const void* data, uint8_t len) {
  uint8_t* bytes = (uint8_t*)data;
  Serial.println("AT$RC");
  Serial.print("AT$SF=");
  for(uint8_t i = len-1; i < len; --i) {
    if (bytes[i] < 16) {Serial.print("0");}
    Serial.print(bytes[i], HEX);
  }
  Serial.print('\r');
}
