# FCrypt

Fcrypt is a CLI tool that aims to simplify the encryption of files.
It uses the AES algorithm to encrypt and decrypt files and Spring Shell to provide a CLI interface.

## Installation

```bash
mvn clean package
java -jar target/fcrypt-0.0.1-SNAPSHOT.jar
```

Docker: 
```bash
docker build -t fcrypt .
```

or

```bash
docker pull anthorld/fcrypt
```