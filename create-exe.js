const { execSync } = require('child_process');
const path = require('path');

console.log('Creating .exe file for QuickGen...');

try {
  // Ensure QuickGen.jar exists
  if (!fs.existsSync('QuickGen.jar')) {
    console.error('QuickGen.jar not found. Please run the setup script first.');
    process.exit(1);
  }

  // Run Launch4j
  const launch4jPath = path.join('launch4j', 'launch4j.exe');
  execSync(`${launch4jPath} launch4j-config.xml`, { stdio: 'inherit' });

  console.log('.exe file created successfully');
} catch (error) {
  console.error('Error creating .exe file:', error.message);
  process.exit(1);
}