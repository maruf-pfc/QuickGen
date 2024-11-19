const { execSync } = require('child_process');
const path = require('path');

console.log('Creating .deb package for QuickGen...');

const iconPath = path.join('quickgen', 'opt', 'quickgen', 'icon.png');

try {
  execSync(`
    jpackage --input . \
    --main-jar QuickGen.jar \
    --name QuickGen \
    --type deb \
    --linux-shortcut \
    --icon "${iconPath}" \
    --app-version 1.0 \
    --input quickgen/opt/quickgen \
    --dest .
  `, { stdio: 'inherit' });

  console.log('.deb package created successfully');
} catch (error) {
  console.error('Error creating .deb package:', error.message);
  process.exit(1);
}