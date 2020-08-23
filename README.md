# JIRA Tempo Client

[![Build Status](https://travis-ci.com/michalkowol/jira-tempo.svg?branch=master)](https://travis-ci.com/michalkowol/jira-tempo)
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

## Examples

### Create workflows

```
POST localhost:8081/worklog

Authorization: Basic XXX

WTAI-426    dynamic config  2016-12-29  4200    09:50   11:00   1h 10m  1.17
WTAI-433    TouK (release)  2016-12-29  10800   11:00   14:00   3h 0m   3.00
WTAI-426    dynamic config  2016-12-29  7200    14:00   16:00   2h 0m   2.00
WTAI-389    Sprint  2016-12-29  2400    16:00   16:40   0h 40m  0.67
WTAI-426    dynamic config  2016-12-29  3900    16:40   17:45   1h 5m   1.08
WTAI-681 WTAI-428   Populate ratings for TVPG   2016-12-30  12300   09:15   12:40   3h 25m  3.42
WTAI-681 WTAI-428   Populate ratings for TVPG   2017-01-02  32400   09:00   18:00   9h 0m   9.00
WTAI-426    dynamic config  2017-01-03  19800   10:30   16:00   5h 30m  5.50
WTAI-426    dynamic config  2017-01-03  1800    16:00   16:30   0h 30m  0.50
WTAI-389    Sprint  2017-01-03  1800    16:30   17:00   0h 30m  0.50
WTAI-389    Michal Ko & Co  2017-01-03  4200    17:00   18:10   1h 10m  1.17
WTAI-426 WTAI-551   dynamic config  2017-01-04  21900   12:10   18:15   6h 5m   6.08
```

### Delete workflows

```
DELETE localhost:8081/worklog

Authorization: Basic XXX

993631
993633
993635
993637
993640
9936411
```

## JIRA Tempo

Docs: http://tempo.io/doc/timesheets/api/rest/latest/#848933329

### Get worklogs

```
GET https://jira.mtvi.com/rest/tempo-timesheets/3/worklogs?username=kowolm&dateFrom=2016-01-01&dateTo=2017-01-01
```
### Create worklog

```
POST https://jira.mtvi.com/rest/tempo-timesheets/3/worklogs

Authorization: Basic XXX
Content-Type: application/json

{
"timeSpentSeconds": 300,
"dateStarted": "2017-01-11T00:00:00.000",
"author": {
"name": "kowolm"
},
"issue": {
"key": "WTAI-552"
}
}
```

```
curl -H 'Authorization: Basic XXX' -H 'Content-Type: application/json' --data-binary '{
"timeSpentSeconds": 300,
"dateStarted": "2017-01-11T00:00:00.000",
"author": {
"name": "kowolm"
},
"issue": {
"key": "WTAI-552"
}
}' 'https://jira.mtvi.com/rest/tempo-timesheets/3/worklogs'
```

### Delete

```
DELETE https://jira.mtvi.com/rest/tempo-timesheets/3/worklogs/{worklogId}

Authorization: Basic XXX
```

### Updates from spreadsheet

```
GET https://jira.mtvi.com/rest/tempo-timesheets/3/worklogs?username=kowolm&dateFrom=2016-08-26
```

```
POST https://jira.mtvi.com/rest/tempo-timesheets/3/worklogs

Authorization: Basic XXX
Content-Type: application/json

{
"timeSpentSeconds": 300,
"dateStarted": "2017-01-11T00:00:00.000",
"author": {
"name": "kowolm"
},
"issue": {
"key": "WTAI-552",
"remainingEstimateSeconds":0
}
}
```
