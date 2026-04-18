mvn quarkus:dev

https://quarkus.io/guides/security-jwt

JWT :

openssl genrsa -out rsaPrivateKey.pem 2048
openssl rsa -pubout -in rsaPrivateKey.pem -out publicKey.pem

openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out privateKey.pem

sudo apt install libfl-dev
sudo apt install zlib1g-dev

https://github.com/quarkusio/quarkus/wiki/Migration-Guide-3.23
