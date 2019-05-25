# Eresearch Author Matcher Service #


### Description

The purpose of this service is to accept two names of authors, name: firstname + initials + surname
and do a similarity comparison to these two, using different algorithms such as levenshtein, simonwhite, etc.

The response is the similarity comparison of these two names.
This service makes heavy usage of this library: https://github.com/Simmetrics/simmetrics

Our service uses the following algorithms:
* SIMON_WHITE
* LEVENSHTEIN
* JARO
* JARO_WINKLER
* COSINE_SIMILARITY
* EUCLIDEAN_DISTANCE
* BLOCK_DISTANCE
* DAMERAU_LEVENSHTEIN
* DICE
* GENERALIZED_JACCARD
* IDENTITY
* JACCARD
* LONGEST_COMMON_SUBSEQUENCE
* LONGEST_COMMON_SUBSTRING
* MONGE_ELKAN
* NEEDLEMAN_WUNCH
* OVERLAP_COEFFICIENT
* Q_GRAMS_DISTANCE
* SMITH_WATERMAN
* SMITH_WATERMAN_GOTOH

### Integration Tests

* Execute: `mvn clean verify`


### Create Docker Image
* Execute: `mvn clean install -DskipITs=true`
* Execute: `docker build -t chriniko/eresearch-author-matcher:1.0 .` in order to build docker image.

* Fast: `mvn clean install -DskipITs=true && docker build -t chriniko/eresearch-author-matcher:1.0 .`


### How to run service (not dockerized)
* Execute: `docker-compose up`

* Two options:
    * Execute: 
        * `mvn clean install -DskipITs=true`
        * `java -jar -Dspring.profiles.active=dev target/eresearch-author-matcher-1.0-boot.jar`
                
    * Execute:
        * `mvn spring-boot:run -Dspring.profiles.active=dev`

* (Optional) When you finish: `docker-compose down`


### How to run service (dockerized)
* Uncomment the section in `docker-compose.yml` file for service: `eresearch-author-matcher:`

* Execute: `mvn clean install -DskipITs=true`

* Execute: `docker-compose build`

* Execute: `docker-compose up`

* (Optional) When you finish: `docker-compose down`


### Example Request

```json
{
	"first-author-name":{
		"firstname":"Dimitris",
		"initials":"",
		"surname":"Piliotis"
	},
	"second-author-name":{
		"firstname":"D.",
		"initials":"",
		"surname":"Piliotis"
	}
}

```



### Example Response

```json
{
  "operation-result": true,
  "process-finished-date": 1487182289.869,
  "comparison-results": {
    "DAMERAU_LEVENSHTEIN": {
      "comparison-result": 0.5625,
      "comparison-result-floor": 0.56,
      "comparison-result-ceil": 0.57
    },
    "COSINE_SIMILARITY": {
      "comparison-result": 0,
      "comparison-result-floor": 0,
      "comparison-result-ceil": 0
    },
    "EUCLIDEAN_DISTANCE": {
      "comparison-result": 0,
      "comparison-result-floor": 0,
      "comparison-result-ceil": 0
    },
    "MONGE_ELKAN": {
      "comparison-result": 0.800000011920929,
      "comparison-result-floor": 0.8,
      "comparison-result-ceil": 0.81
    },
    "LONGEST_COMMON_SUBSTRING": {
      "comparison-result": 0.5,
      "comparison-result-floor": 0.5,
      "comparison-result-ceil": 0.5
    },
    "SMITH_WATERMAN": {
      "comparison-result": 0.800000011920929,
      "comparison-result-floor": 0.8,
      "comparison-result-ceil": 0.81
    },
    "OVERLAP_COEFFICIENT": {
      "comparison-result": 0,
      "comparison-result-floor": 0,
      "comparison-result-ceil": 0
    },
    "JARO": {
      "comparison-result": 0.7097222208976746,
      "comparison-result-floor": 0.7,
      "comparison-result-ceil": 0.71
    },
    "NEEDLEMAN_WUNCH": {
      "comparison-result": 0.75,
      "comparison-result-floor": 0.75,
      "comparison-result-ceil": 0.75
    },
    "BLOCK_DISTANCE": {
      "comparison-result": 0,
      "comparison-result-floor": 0,
      "comparison-result-ceil": 0
    },
    "LEVENSHTEIN": {
      "comparison-result": 0.5625,
      "comparison-result-floor": 0.56,
      "comparison-result-ceil": 0.57
    },
    "GENERALIZED_JACCARD": {
      "comparison-result": 0,
      "comparison-result-floor": 0,
      "comparison-result-ceil": 0
    },
    "JACCARD": {
      "comparison-result": 0,
      "comparison-result-floor": 0,
      "comparison-result-ceil": 0
    },
    "IDENTITY": {
      "comparison-result": 0,
      "comparison-result-floor": 0,
      "comparison-result-ceil": 0
    },
    "LONGEST_COMMON_SUBSEQUENCE": {
      "comparison-result": 0.5625,
      "comparison-result-floor": 0.56,
      "comparison-result-ceil": 0.57
    },
    "JARO_WINKLER": {
      "comparison-result": 0.7387499809265137,
      "comparison-result-floor": 0.73,
      "comparison-result-ceil": 0.74
    },
    "SMITH_WATERMAN_GOTOH": {
      "comparison-result": 0.800000011920929,
      "comparison-result-floor": 0.8,
      "comparison-result-ceil": 0.81
    },
    "DICE": {
      "comparison-result": 0,
      "comparison-result-floor": 0,
      "comparison-result-ceil": 0
    },
    "Q_GRAMS_DISTANCE": {
      "comparison-result": 0.6000000238418579,
      "comparison-result-floor": 0.6,
      "comparison-result-ceil": 0.61
    },
    "SIMON_WHITE": {
      "comparison-result": 0.5833333134651184,
      "comparison-result-floor": 0.58,
      "comparison-result-ceil": 0.59
    }
  },
  "comparison-input": {
    "first-author-name": {
      "firstname": "Dimitris",
      "initials": "",
      "surname": "Piliotis"
    },
    "second-author-name": {
      "firstname": "D.",
      "initials": "",
      "surname": "Piliotis"
    }
  }
}

```

