# s.Status
[![badge](https://img.shields.io/badge/license-MPL--2.0-orange)](https://github.com/scolastico-dev/s.Status/blob/main/LICENSE)
[![badge](https://img.shields.io/github/languages/code-size/scolastico-dev/s.Status)](https://github.com/scolastico-dev/s.Status/graphs/contributors)
[![badge](https://img.shields.io/github/issues/scolastico-dev/s.Status)](https://github.com/scolastico-dev/s.Status/issues)
[![badge](https://img.shields.io/github/v/tag/scolastico-dev/s.Status?label=version)](https://github.com/scolastico-dev/s.Status/releases)
[![badge](https://github.com/scolastico-dev/s.Status/actions/workflows/main.yml/badge.svg)](https://github.com/scolastico-dev/s.Status/actions)

## Introduction
s.Status is a simple solution for monitoring pingable services such as database servers or http servers and providing a live status page. With the built in plugin system it is easy to add some functionalities like notification services.

## Pre-Release Notice
Hey, first of all it's cool that you're visiting the project before it's release. You're in luck, we're already in the prerelease phase with a functional beta version. You are welcome to use it and in general the most important functions should already work. But of course, as with all new applications, there may be bugs. It would be great if you either leave the "reportErrorsOnline" on in the settings, as this is how we find out about errors. Don't worry, your data is kept safe with us, we only use it for error analysis and rectification. If you want to support us even more, you can also create a github issue and explain to us in detail what you did that led to the error. If you want to know more about the privacy of the error report, you can view our privacy policy (GDPR) [here](https://go.scolasti.co/privacy). Apart from that, please excuse that some functions such as planned maintenance and plugin support are not yet implemented, but we will do it soon. It can also happen that our config changes several times during the prerelease phase, please always pay attention to the release notes because it explains, among other things, what to consider when updating.

### Next Steps
- [x] Upgrade to Kotlin
- [x] Rework the api
- [x] Rework the frontend
- [ ] Add support for plugins
- [ ] Add admin gui with config editor

## Installation & Usage
Because the application needs for the ping icmp package root privileges its recommended to use the docker installation which is more secure.

### Docker
**coming soon**

### Manual
Download from the latest release the `status-shadow.jar` and save it in a new folder where you want to execute the application. After that simply start the jar with the `java -jar status-shadow.jar` command. After the first start you will be asked to edit the generated configuration.

## License
This project is licensed under the **Mozilla Public License 2.0**.
### About
MPL-2.0
Permissions of this weak copyleft license are conditioned on making available source code of licensed files and modifications of those files under the same license (or in certain cases, one of the GNU licenses). Copyright and license notices must be preserved. Contributors provide an express grant of patent rights. However, a larger work using the licensed work may be distributed under different terms and without source code for files added in the larger work.
### What you can do
| Permissions                                                                                                                       | Conditions                                                                                                                                                                                                                           | Limitations                                                                                                                                                                                                                      |
|-----------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| <details><summary>🟢 Commercial use</summary>The licensed material and derivatives may be used for commercial purposes.</details> | <details><summary>🔵 Disclose source</summary>Source code must be made available when the licensed material is distributed.</details>                                                                                                | <details><summary>🔴 Liability</summary>This license includes a limitation of liability.</details>                                                                                                                               |
| <details><summary>🟢 Distribution</summary>The licensed material may be distributed.</details>                                    | <details><summary>🔵 License and copyright notice</summary>A copy of the license and copyright notice must be included with the licensed material.</details>                                                                         | <details><summary>🔴 Trademark use</summary>This license explicitly states that it does NOT grant trademark rights, even though licenses without such a statement probably do not grant any implicit trademark rights.</details> |
| <details><summary>🟢 Modification</summary>The licensed material may be modified.</details>                                       | <details><summary>🔵 Same license (file)</summary>Modifications of existing files must be released under the same license when distributing the licensed material. In some cases a similar or related license may be used.</details> | <details><summary>🔴 Warranty</summary>This license explicitly states that it does NOT provide any warranty.</details>                                                                                                           |
| <details><summary>🟢 Patent use</summary>This license provides an express grant of patent rights from contributors.</details>     |                                                                                                                                                                                                                                      |                                                                                                                                                                                                                                  |
| <details><summary>🟢 Private use</summary>The licensed material may be used and modified in private.</details>                    |                                                                                                                                                                                                                                      |                                                                                                                                                                                                                                  |
*Information provided by https://choosealicense.com/licenses/mpl-2.0/*

Read more [here](https://github.com/scolastico-dev/s.Status/blob/main/LICENSE).

## Other Projects
Check out my other projects: [On Github](https://github.com/scolastico-dev) or [on my Website](https://scolasti.co/).
