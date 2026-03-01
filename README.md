# GhostShare

GhostShare is a high-performance, peer-to-peer file transfer utility designed for secure and stealthy operation across restricted network environments. The application utilizes Cloudflare tunneling for NAT traversal and integrates with the Windows System Tray for a zero-footprint user interface.

## Technical Specifications

- Runtime Environment: Java Development Kit (JDK) 25
- Web Framework: Javalin (Lightweight JVM Web Framework)
- Tunneling Architecture: Cloudflared (Integrated binary for automated tunnel provisioning)
- User Interface: Java AWT SystemTray (Headless operation)
- Build System: Apache Maven
- Distribution Format: Native Windows Executable (via jpackage)

## Key Functionalities

- Automated Tunneling: Dynamically establishes a public endpoint for local services, bypassing firewalls and ISP-level port blocking.
- Silent Background Execution: Operates without a console window or standard GUI, utilizing system tray notifications for status updates.
- Persistent Deployment: Implements a self-copying mechanism to the Windows Startup directory to ensure availability post-reboot.
- Runtime Bundling: Packaged with a stripped-down Java Runtime Environment (JRE) for standalone portability.

## Development and Build Process

### Prerequisites
- JDK 25 or higher
- Apache Maven 3.9+
- Windows 10/11 Environment (for jpackage execution)

### 1. Compilation
Generate the shaded artifact containing all dependencies:

mvn clean package

### 2. Packaging (Native Binary)
Execute the following command to generate the portable application image. Replace the JDK path if your installation directory differs.

"C:\Program Files\Java\jdk-25\bin\jpackage" --type app-image --name GhostShare --input target --main-jar GhostShare-1.0.0.jar --main-class com.ashu.ghostshare.Main --dest OutputEXE --icon src/main/resources/assets/icon.ico

## Directory Structure
- src/main/java: Core application logic and service handlers.
- src/main/resources: Static assets and icon resources.
- pom.xml: Dependency management and build configurations.

## Operational Note
Upon execution, GhostShare establishes a secure tunnel and copies the executable to %AppData%\Microsoft\Windows\Start Menu\Programs\Startup. This ensures the protocol initializes automatically on system login.

## Disclaimer
This project is intended for authorized file-sharing and educational purposes. The developers assume no liability for unauthorized use of the tunneling or persistence features.
