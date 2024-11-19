# Creating Executable Versions of QuickGen

This guide explains how to create executable versions of QuickGen for both Windows (.exe) and Linux (.deb) platforms.

## Prerequisites

- Node.js installed on your system
- Java Development Kit (JDK) 8 or higher
- npm (Node Package Manager)

## Windows Executable (.exe)

### Setup Launch4j

1. Install required Node.js package:

```bash
npm install adm-zip
```

1. Run the Launch4j setup script:

```shellscript
node setup-launch4j.js
```

3. Create the executable:

```shellscript
node create-exe.js
```

The QuickGen.exe file will be created in your project directory.

### Running the Windows Version

1. Download QuickGen.exe
2. Double-click to run the application
3. If prompted with a security warning, click "Run"

Note: Java Runtime Environment (JRE) 1.8 or later is required.

## Linux Package (.deb)

### Creating the Package

1. Set up the project structure:

```shellscript
node setup.js
```

2. Create the .deb package:

```shellscript
node create-deb.js
```

### Installing on Linux

1. Download the quickgen_1.0_amd64.deb file
2. Open a terminal in the download directory
3. Install using:

```shellscript
sudo dpkg -i quickgen_1.0_amd64.deb
```

4. Launch QuickGen either:

1. From your applications menu
1. By running `quickgen` in terminal

## Troubleshooting

### Windows

- If Java is not found, download and install JRE from Oracle's website
- Run as administrator if you encounter permission issues

### Linux

- Ensure all dependencies are installed:

```shellscript
sudo apt-get install -f
```

- Check application logs in case of startup issues:

```shellscript
journalctl -xe
```

## Additional Notes

- The Windows executable includes a bundled configuration for Java requirements
- The Linux package creates appropriate desktop entries and icons
- Both versions maintain the same functionality as the JAR version
