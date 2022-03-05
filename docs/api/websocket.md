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
    "passedTime": 2500 // In ms
}
```

## Server -> Client

### Question
Informs the client about a new question.
```
{
    "question": {...}, // See Question model
    "questionNumber": 19 // Indexed from 0
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
