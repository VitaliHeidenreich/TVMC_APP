//This example code is in the Public Domain (or CC0 licensed, at your option.)
//By Evandro Copercini - 2018
//
//This example creates a bridge between Serial and Classical Bluetooth (SPP)
//and also demonstrate that SerialBT have the same functionalities of a normal Serial

#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;
static String comBuilder = "";
int state;

void setup() {
  Serial.begin(115200);
  SerialBT.begin("Wordclock_123"); //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");
  pinMode(2, OUTPUT);
  digitalWrite(2, HIGH);
  state = 1;
}



void loop() {
  if (Serial.available()) {
    SerialBT.write(Serial.read());
  }
  if (SerialBT.available()) {
      
      char tempIn = SerialBT.read();
      
      comBuilder = comBuilder + tempIn;
      
      if( tempIn == '$' ){
            Serial.print( "Found: " + comBuilder );
            
            //do something
            if( comBuilder.equals("X++A$")  )
            {
                  if(state == 0 )
                  {
                        state = 1;
                        digitalWrite(2, HIGH);
                        SerialBT.println("Antwort vom ESP32: LED IS ON");
                  }
                  else
                  {
                        state = 0;
                        digitalWrite(2, LOW);
                         SerialBT.println("Antwort vom ESP32: LED IS OFF");
                  }
            }
            comBuilder = "";
      }
  }
  delay(20);
}
