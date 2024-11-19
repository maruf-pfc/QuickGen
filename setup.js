const { execSync } = require('child_process');
const fs = require('fs');
const path = require('path');

console.log('Setting up QuickGen project structure...');

// Create necessary directories
const dirs = [
  'quickgen/opt/quickgen',
  'quickgen/usr/share/applications'
];

dirs.forEach(dir => {
  fs.mkdirSync(dir, { recursive: true });
  console.log(`Created directory: ${dir}`);
});

// Compile Java files
console.log('Compiling Java files...');
try {
  execSync('javac ProjectGeneratorServer.java ProjectGeneratorClient.java', { stdio: 'inherit' });
  console.log('Java files compiled successfully.');
} catch (error) {
  console.error('Error compiling Java files:', error.message);
  process.exit(1);
}

// Create manifest file
fs.writeFileSync('manifest.txt', 'Main-Class: QuickGenApp\n');
console.log('Created manifest file.');

// Create JAR file
console.log('Creating JAR file...');
try {
  execSync('jar cfm QuickGen.jar manifest.txt *.class', { stdio: 'inherit' });
  console.log('JAR file created successfully.');
} catch (error) {
  console.error('Error creating JAR file:', error.message);
  process.exit(1);
}

// Copy JAR file to package directory
fs.copyFileSync('QuickGen.jar', 'quickgen/opt/quickgen/QuickGen.jar');
console.log('Copied QuickGen.jar to package directory');

// Create a simple placeholder icon
const iconPath = path.join('quickgen', 'opt', 'quickgen', 'icon.png');
const svgIcon = `<svg xmlns="http://www.w3.org/2000/svg" width="128" height="128" viewBox="0 0 128 128">
  <rect width="128" height="128" fill="#4a90e2"/>
  <text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" font-family="Arial" font-size="48" fill="#ffffff">QG</text>
</svg>`;
fs.writeFileSync(iconPath, svgIcon);
console.log('Created placeholder icon.');

console.log('QuickGen project setup complete.');