# s.Status
[![badge](https://img.shields.io/github/license/scolastico-dev/s.Status)](https://github.com/scolastico-dev/s.Status/blob/main/LICENSE)
[![badge](https://img.shields.io/github/languages/code-size/scolastico-dev/s.Status)](#)
[![badge](https://img.shields.io/github/issues/scolastico-dev/s.Status)](https://github.com/scolastico-dev/s.Status/issues)
[![badge](https://img.shields.io/github/v/tag/scolastico-dev/s.Status?label=version)](https://github.com/scolastico-dev/s.Status/releases)
[![badge](https://github.com/scolastico-dev/s.Status/actions/workflows/main.yml/badge.svg)](https://github.com/scolastico-dev/s.Status/actions)
[![View Demo](https://img.shields.io/badge/-View%20Demo-green)](https://status.scolasti.co/)
[![Documentation](https://img.shields.io/badge/-Documentation-green)](https://docs.scolasti.co/s.Status/main/)

## Small Notice
For all who think this repo is dead: A new rework in kotlin instead of java and a new ui in nuxt.js/vue is on the way! Checkout the [kotlin-upgrade](https://github.com/scolastico-dev/s.Status/tree/kotlin-upgrade) branch for all new updates! Release is planed for ~~01.05.2022 or earlier~~ mid summer (sorry 4 that but im also bussy with other projects)!

## Introduction
s.Status is a simple solution for monitoring pingable services such as database servers or http servers and providing a live status page. With the built in plugin system it is easy to add some functionalities like notification services.

## Pre-Release Notice
Hey, first of all it's cool that you're visiting the project before it's release. You're in luck, we're already in the prerelease phase with a functional beta version. You are welcome to use it and in general the most important functions should already work. But of course, as with all new applications, there may be bugs. It would be great if you either leave the "reportErrorsOnline" on in the settings, as this is how we find out about errors. Don't worry, your data is kept safe with us, we only use it for error analysis and rectification. If you want to support us even more, you can also create a github issue and explain to us in detail what you did that led to the error. If you want to know more about the privacy of the error report, you can view our privacy policy (GDPR) [here](https://go.scolasti.co/privacy). Apart from that, please excuse that some functions such as planned maintenance and plugin support are not yet implemented, but we will do it soon. It can also happen that our config changes several times during the prerelease phase, please always pay attention to the release notes because it explains, among other things, what to consider when updating.

## Installation & Usage
Download from the latest release the `s.Status-jar-with-dependencies.jar` and save it in a new folder where you want to execute the application. After that simply start the jar with the `java -jar s.Status-jar-with-dependencies.jar` command. After the first start you will be asked to edit the generated configuration.

You can also use the headless console client if you write arguments after the start command. For a list of all commands you can use the `help` argument. The command for this would then look like this: `java -jar s.Status-jar-with-dependencies.jar help`

## Config

To simplify the setup, here is an example of a working config:
*(Please note that we have written comments which are marked with a '#' at the beginning of a line. JSON does not support comments so if you copy something from here do not copy comments, otherwise the config cannot be read.)*

```json
{
  # HTTP webserver port
  "port": 42001,
  
  # Folder location for web templates
  "twigFolderPath": "web/default-web-files/twig/",
  "staticFolderPath": "web/default-web-files/web/",
  
  # Database config
  "databaseConfig": {
    # database type can be: SQL, MYSQL, MARIADB, SQLITE, POSTGRES, ORACLE and H2.
    "databaseType": "SQLITE",
    "host": "database.sqlite",
    "port": 3306,
    "database": "database",
    "username": "username",
    "password": "P4$$word",
    "debug": false
  }
  
  # Report errors privat to us.
  # Would be nice if you let it on true! <3
  "reportErrorsOnline": true,
  
  # Debug stuff...
  "debug": false,
  "enableShutDownHook": true,
  
  # Log errors into error.* files.
  # Realy usefull if you run it in a screen session.
  "enableErrorLogFile": true,
  
  # Will show log in console can also enabled/disabled via commands.
  "showWebServerLog": true,
  
  # Update interval for website.
  "liveUpdateInterval": 30,
  
  # Timeout for status checks.
  "timeoutMillis": 10000,
  
  # After the time the data will be deleted from the database
  "keepResultsForDays": 7,
  "keepArchiveForDays": 30,
  
  # If a server is in the status 'Seems to be online again[...]' (code 5)
  # it will be changed to 'Fixed issue' (code 6) after the defined period of time.
  "needsToBeAliveForMinutesBeforeAutoConfirmation": 60,
  
  # Run deletion of old database entrys ever x minutes.
  "runCleanUpEveryMinutes": 5,
  
  # NON FUNCTIONAL CURRENTLY!
  "allowGetApiUse": "ALLOW_WITH_API_TOKEN",
  "allowPublicPage": "ALLOW",
  
  # Set the colors for the charts.
  # ResponseTimeColor is in 'greater than' mode.
  # UptimeColor is in 'less than' mode.
  "responseTimeColor": {
    "0": "#18db4c",
    "500": "#f5ef42",
    "1000": "#e30e0e"
  },
  "uptimeColor": {
    "98": "#e30e0e",
    "100": "#18db4c",
    "90": "#e30e0e"
  },
  
  # Page buttons for about, privacy and other pages.
  # The id should also get an entry in language.json
  # under the 'additionalLanguageData' entry. The id
  # needs to be in both files the same. This is so you
  # can also translate the page buttons.
  "pageButtons": {
    "about": "https://example.com/"
  },
  
  # The status checks with two functional examples.
  "statusChecks": [
    {
      # The host can be a domain or ip adress.
      "host": "https://google.com/",
      
      # Type can be: HTTP, HTTPS, PING
      # PING doesnt makes a normal ping it trys only to reach the server.
      # This behaviour is planned to changd in future.
      "type": "HTTPS",
      
      # The name shown on the status page.
      "name": "Google",
      
      # NON FUNCTIONAL CURRENTLY!
      "hideHostOnPublicPage": true,
      
      # The id used in the database and internal stuff.
      # Needs to be diffrent on every status check!
      "id": "google",
      
      # Run the check every x seconds.
      "checkEverySeconds": 60
    },
    
    # And a second example.
    {
      "host": "8.8.8.8",
      "type": "PING",
      "name": "Google DNS server (entire not service)",
      "hideHostOnPublicPage": true,
      "id": "google-dns",
      "checkEverySeconds": 60
    }
  ]
}
```

## License
This project is licensed under the **Mozilla Public License Version 2.0**. Read more [here](https://www.mozilla.org/en-US/MPL/2.0/).

Loading animation from [loading.io](https://loading.io/). Other used images may by [undraw.co](https://undraw.co).

## Liability
I or any other contributor is not responsible for any errors or mistakes that may appear! Use at your own risk.

## Other Projects
Check out our other projects: [On Github](https://github.com/scolastico-dev/) or [on my Website](https://scolasti.co/).
