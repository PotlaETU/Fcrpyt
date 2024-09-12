# FCrypt

Fcrypt is a CLI tool that aims to simplify the encryption of files.
It uses the AES algorithm to encrypt and decrypt files and Spring Shell to provide a CLI interface.

## List of commands

- `enc [filePath]` : Encrypt a file
- `dec [filePath]` : Decrypt a file

## Installation

```bash
mvn clean package
java -jar target/fcrypt-1.0.0.jar
```

Docker: 
```bash
docker build -t fcrypt .
```

or

```bash
docker pull anthorld/fcrypt
```

and then run the container to access directly the CLI:
```bash
docker run -it -v /path/to/your/files:/files fcrypt 
```

## Warnings

- The encryption key is stored in the `secret.key` file in the root directory of the project.
- The encryption key is generated randomly and is 256 bits long.
- The encryption key is not stored in the Docker image, so it will be generated each time the container is started.