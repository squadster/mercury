# Mercury

[![License](https://img.shields.io/github/license/squadster/mercury.svg)](https://github.com/squadster/mercury/blob/master/LICENSE)
[![Build Status](https://travis-ci.org/squadster/mercury.svg?branch=master)](https://travis-ci.org/squadster/mercury)
[![codecov.io Code Coverage](https://img.shields.io/codecov/c/github/squadster/mercury.svg)](https://codecov.io/github/squadster/mercury?branch=master)

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
4) Configure vk tokens in tokens.properties in src/main/java/resources
5) Run application

```bash
./gradlew bootRun
```

## Deployment

We use GitHub packages for deployment.

Check out [Dockerfile](Dockerfile) and [this repo](https://github.com/squadster/squadster-deployment) for more information.

For the whole application check out [this repo](https://github.com/squadster/squadster-deployment).

## Contributing

Your ideas and wishes are welcome via [issues](https://github.com/squadster/mercury/issues) and [pull requets](https://github.com/squadster/mercury/pulls).

Check [contributing guidelines](CONTRIBUTING.md) for more info.
