# Websocket API

## Client -> Server

### Single-player game start request
Sent by the client as a first message after connecting.
```
{
    "username": ""
}
```

### Questions answer
Submits the answer to the server.
```
{
    "answerInt": 3,
    "answerFloat": 3.1,
    "time": 2500 // In ms
}
```

## Server -> Client

### Question
Informs the client about a new question.
```
{
    "type": "mc"|"estimation"|"comparison",
    "activities": [
        {
            "text": "",
            "imageUrl": ""
        },
        ...
    ],
    "correctAnswerInt": 3,
    "correctAnswerFloat": 3.1,
    "questionNumber": 20
}
```
### Correct answer
Informs the client about the question result.
```
{
    "questionScore": 66,
    "totalScore": 666
}
```
