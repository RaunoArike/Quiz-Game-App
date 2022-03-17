# Websocket API

## Client -> Server

### Single-player game start request
Sent by the client as a first message after connecting.
```
{
    "username": ""
}
```
### Join multi-player waiting room
Can either receive a waiting room state update or an error message if it was unsuccessful. 
```
{
    "username": ""
}
```

### Start multi-player game from waiting room
```
{

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
### Joker played 
```
{
    //TODO
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
    "totalScore": 666,
}
```
### Waiting room state
```
{
    "noOfPeopleInRoom": "",
}
```

### Error message
```
{
    "messageText": "",
}
```

