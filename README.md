# Asynchronous vs. synchronous comparing
The current Spring Boot application is example of positive performance effect due to  writing non-blocking code based ot CompletableFuture in contrast with traditional synchronous code.

Controller contains two paths which accepts POST-request with multipart files inside its body:
* /sync
* /async

Accepted files have .CVS structure and represents data about some film. In case of using asynchronous mode every file inside request's body will be handled on single thread.
In another case all files will be handled by a single thread consequentially.

To add some complexity  in both cases application resolves additional information about first 50 film directors from MOCK REST service (check application.properties that contains links).

This process consumes additional time and machine resources and makes difference between asynchronous and synchronous computation more visible.

In response application sends JSON object that contains elapsed time by selected method in milliseconds and success status.

Examples:
1. Async mode via Postman:
![alt text](https://github.com/kssadomtsev/completable-future-demo/blob/master/src/main/resources/data/async.JPG?raw=true)

2. Sync mode via Postman:
![alt text](https://github.com/kssadomtsev/completable-future-demo/blob/master/src/main/resources/data/sync.JPG?raw=true)
