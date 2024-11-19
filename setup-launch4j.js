const fs = require('fs');
const path = require('path');
const https = require('https');
const { execSync } = require('child_process');
const AdmZip = require('adm-zip');

console.log('Setting up Launch4j...');

const launch4jUrl = 'https://sourceforge.net/projects/launch4j/files/launch4j-3/3.14/launch4j-3.14-win32.zip/download';
const launch4jZip = 'launch4j.zip';

// Download Launch4j
console.log('Downloading Launch4j...');
https.get(launch4jUrl, (response) => {
  const file = fs.createWriteStream(launch4jZip);
  response.pipe(file);
  file.on('finish', () => {
    file.close();
    console.log('Launch4j downloaded successfully.');
    
    // Extract Launch4j
    console.log('Extracting Launch4j...');
    const zip = new AdmZip(launch4jZip);
    zip.extractAllTo('.', true);
    console.log('Launch4j extracted successfully.');
    
    // Clean up zip file
    fs.unlinkSync(launch4jZip);
    
    console.log('Launch4j setup complete.');
  });
}).on('error', (err) => {
  console.error('Error downloading Launch4j:', err.message);
});