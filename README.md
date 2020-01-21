![Build Status](https://travis-ci.org/squadster/mercury.svg?branch=master)
# Mercury
It's application for receiving and handling messages from student from university military departments.
<br>
This service has possibilities to send notification students via social networks and messengers like vkontakte using REST API.

## Installation guides
1) Clone project from GitHub
```bash
git clone https://github.com/squadster/mercury.git
```
2) Install [JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) (use Java 11)
3) Configure database in application.properties in src/main/java/resources
4) Configure vkontakte tokens in tokens.properties in src/main/java/resources
5) Run application
```bash
./gradlew bootRun
```

### Deployment

Soon...

For the whole application check out [this repo](https://github.com/squadster/squadster-deployment).

## Contributing

Your ideas and wishes are welcome via [issues](https://github.com/squadster/mercury/issues) and [pull requets](https://github.com/squadster/mercury/pulls).

Check [contributing guidelines](CONTRIBUTING.md) for more info.

## License

[CC BY-NC-SA](https://creativecommons.org/licenses/by-nc-sa/4.0)

* You cannot use this for commercial purposes.
* If you make your own application based on this you must somehow link this repo on your pages.
* You are not allowed to change license terms

Check out the [LICENCE](LICENSE.md) file
