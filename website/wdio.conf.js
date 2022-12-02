import {exec} from 'child_process'

let childProcess = null
export const config = {
  runner: 'local',
  specs: [
    './test/**/*.js',
  ],
  maxInstances: 10,
  capabilities: [{
    maxInstances: 5,
    browserName: 'chrome',
    acceptInsecureCerts: true,
  }],
  logLevel: 'info',
  bail: 0,
  baseUrl: 'http://localhost:3000/',
  waitforTimeout: 10000,
  connectionRetryTimeout: 120000,
  connectionRetryCount: 3,
  services: ['docker'],
  framework: 'mocha',
  reporters: ['spec'],
  mochaOpts: {
    ui: 'bdd',
    timeout: 60000,
  },
  onPrepare: function(config, capabilities) {
    childProcess = exec('java -jar')
  },
  onComplete: function(exitCode, config, capabilities, results) {
    if (childProcess) childProcess.kill()
  },
}
