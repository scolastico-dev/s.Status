# s.Status | This projekt isnt ready yet... Please come back later! <3
[![badge](https://img.shields.io/github/license/scolastico-dev/s.Status)](https://github.com/scolastico/s.Status/blob/main/LICENSE)
[![badge](https://img.shields.io/github/languages/code-size/scolastico-dev/s.Status)](#)
[![badge](https://img.shields.io/github/issues/scolastico-dev/s.Status)](https://github.com/scolastico/s.Status/issues)
[![badge](https://img.shields.io/github/v/tag/scolastico-dev/s.Status?label=version)](https://github.com/scolastico/s.Status/releases)
[![badge](https://github.com/scolastico-dev/s.Status/actions/workflows/main.yml/badge.svg)](https://github.com/scolastico/s.Status/actions)
[![View Demo](https://img.shields.io/badge/-View%20Demo-green)](https://status.scolasti.co/)

## Introduction
s.Status is a simple solution for monitoring pingable services such as database servers or http servers and providing a live status page. With the built in plugin system it is easy to add some functionalities like notification services.

## Installation & Usage
Download from the latest release the `s.Status-jar-with-dependencies.jar` and save it in a new folder where you want to execute the application. After that simply start the jar with the `java -jar s.Status-jar-with-dependencies.jar` command. After the first start you will be asked to edit the generated configuration.

You can also use the headless console client if you write arguments after the start command. For a list of all commands you can use the `help` argument. The command for this would then look like this: `java -jar s.Status-jar-with-dependencies.jar help`

## Config

To simplify the setup, here is an example of a working config:
*(Please note that we have written comments which are marked with a '#' at the beginning of a line. JSON does not support comments so if you copy something from here do not copy comments, otherwise the config cannot be read.)*

```json
{
  "port": 42001,
  "pageName": "Status Page",
  "mysqlUser": "status_page",
  "mysqlPass": "P4$$w0rd",
  "mysqlHost": "127.0.0.1",
  "mysqlPort": 3306,
  "mysqlDB": "status_page",
  "sqliteFile": "database.sqlite",
  "enableSqlite": true,
  "reportErrorsOnline": true,
  "debug": true,
  "enableErrorLogFile": false,
  "enableMailSubscriptions": false,
  "enablePublicMailSubscriptions": false,
  "enablePublicOneTimeMailSubscriptions": true,
  "sendMailEveryTicks": 20,
  "mailUnsubscribeSalt": "TVYwQ0fU9y52YkwPlCbCjg0g5tPki50sp79HvBP9JisjOxuXwE6lLcC4iNMMpxEc",
  "enableGoogleRecaptchaV3": false,
  "recaptchaSiteKey": "public-key",
  "recaptchaPrivateKey": "private-key",
  "discordWebhookUrl": "webhook url",
  "timeoutMillis": 10000,
  "keepResultsForDays": 7,
  "keepArchiveForDays": 30,
  "needsToBeAliveForMinutesBeforeAutoConfirmation": 60,
  "allowGetApiUse": "ALLOW_WITH_API_TOKEN",
  "allowPublicPage": "ALLOW",
  "enabledDiscordWebhookIds": [
    "google"
  ],
  "pageButtons": [
    {
      "url": "https://example.com/",
      "text": "About"
    }
  ],
  "statusChecks": [
    {
      "host": "google.com",
      "port": 443,
      "type": "HTTPS",
      "name": "Google",
      "hideHostOnPublicPage": true,
      "id": "google",
      "checkEverySeconds": 60
    },
    {
      "host": "8.8.8.8",
      "port": 0,
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
