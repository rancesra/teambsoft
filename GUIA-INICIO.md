# Guía de inicio — Equipo B (Windows)

Pasos para dejar tu computador listo y empezar a trabajar desde donde está el proyecto. Todos los comandos se ejecutan en **PowerShell**, la terminal por defecto de Windows y de VS Code.

## 0. Acceso al repositorio

1. Necesitas una cuenta de GitHub.
2. Acepta la invitación de colaborador que te llegó por correo, o entra a https://github.com/rancesra/teambsoft/invitations. Sin aceptarla puedes descargar el repo, pero no subir cambios.

## 1. Instalar las herramientas

| Herramienta | Para qué |
|---|---|
| Git | Control de versiones |
| JDK 21 | Compilar y ejecutar el backend |
| VS Code | Editor |
| Docker Desktop | Correr MongoDB (y más adelante RabbitMQ, Eureka y Kong) |

No hace falta instalar Maven: el proyecto trae el Maven Wrapper (`mvnw.cmd`), que lo descarga solo.

Instala en este orden:

1. **Git:** https://git-scm.com/downloads/win
   Deja las opciones por defecto. Dos de ellas importan:
   - *Checkout Windows-style, commit Unix-style line endings*: evita problemas de finales de línea con compañeros que usan Mac.
   - *Git Credential Manager*: es lo que te pedirá iniciar sesión en GitHub la primera vez que subas cambios.

2. **JDK 21 (Temurin):** https://adoptium.net/temurin/releases/?version=21
   Descarga el `.msi` para Windows x64. En la pantalla de componentes, **"Set JAVA_HOME variable"** viene desactivado: haz clic en su ícono y elige *Will be installed on local hard drive*.

3. **VS Code:** https://code.visualstudio.com/download
   Deja marcada la opción **Add to PATH**.

4. **Docker Desktop:** https://www.docker.com/products/docker-desktop/
   Deja marcada la opción de usar **WSL 2**. Al terminar te pedirá reiniciar. Después ábrelo una vez para que termine de configurarse; si te pide actualizar WSL, acepta. Si dice que la virtualización no está activada, hay que activarla en la BIOS; busca cómo hacerlo para tu modelo de computador.

### Extensiones de VS Code

Abre Extensiones (`Ctrl+Shift+X`) e instala:

- **Extension Pack for Java**
- **Spring Boot Extension Pack**

### Verificar

Cierra VS Code y cualquier terminal abierta, y vuelve a abrirlos para que reconozcan lo que instalaste. En PowerShell:

```powershell
git --version
java -version
docker --version
```

`java -version` debe mostrar la versión **21**.

## 2. Configurar Git (una sola vez)

Usa el correo registrado en tu cuenta de GitHub, para que tus commits aparezcan a tu nombre:

```powershell
git config --global user.name "Tu Nombre"
git config --global user.email "tu-correo@ejemplo.com"
```

No tienes que iniciar sesión en GitHub ahora. La primera vez que hagas `git push` se abrirá el navegador para que autorices tu cuenta. Usa la misma cuenta con la que aceptaste la invitación.

## 3. Clonar el repositorio

*Clonar* es descargar el repositorio con todo su historial. Hazlo en una carpeta **fuera de OneDrive**: en muchos Windows, el Escritorio y Documentos se sincronizan con OneDrive, y eso bloquea archivos mientras Git o Maven trabajan. Por ejemplo, en `C:\dev`:

```powershell
mkdir C:\dev
cd C:\dev
git clone https://github.com/rancesra/teambsoft.git
cd teambsoft
code .
```

`code .` abre la carpeta del proyecto en VS Code.

## 4. Verificar que el backend compila

En VS Code abre una terminal (menú **Terminal → New Terminal**) y ejecuta:

```powershell
cd backend
.\mvnw.cmd test
```

El `.\` es obligatorio en PowerShell: significa "el archivo que está en esta carpeta".

La primera vez tarda unos minutos, porque descarga Maven y las librerías. Debe terminar con `Tests run: 1, Failures: 0` y `BUILD SUCCESS`. Los mensajes `Connection refused` de MongoDB son normales: Mongo todavía no está corriendo.

## 5. Antes de programar

- Lee el [contrato](docs/CONTRATO-CATALOGO.md). Es el acuerdo con los otros equipos y no se cambia sin consultarlo.
- Cada vez que vayas a trabajar, trae primero los cambios de tus compañeros:

```powershell
git pull
```

- **Flujo de trabajo en equipo (ramas y pull requests):** por definir.