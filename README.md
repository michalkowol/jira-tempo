# JIRA Tempo Client

[![Build Status](https://travis-ci.com/michalkowol/jira-tempo.svg?branch=master)](https://travis-ci.com/michalkowol/jira-tempo)

## Build

### Default

```
./gradlew
```

### Build

```
./gradle build
```

### Run

```
./gradle run
```

### Continuous build

```
./gradle run -t
```

or

```
./gradle run --continuous
```

### FatJar

```
./gradle fatJar
java -jar build/libs/${NAME}-assembly-${VERSION}.jar
```

### Heroku

```
heroku login
heroku create
git push heroku master
heroku logs -t
```
