mvn quarkus:dev

https://quarkus.io/guides/security-jwt

JWT : 

openssl genrsa -out rsaPrivateKey.pem 2048
openssl rsa -pubout -in rsaPrivateKey.pem -out publicKey.pem

openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out privateKey.pem

sudo apt install libfl-dev
sudo apt install zlib1g-dev

mvn clean install sonar:sonar -Dsonar.login=sqa_4ebec87e191f7c7b3de66ca20f9967db134a83ee -Dsonar.host.url=http://localhost:9000