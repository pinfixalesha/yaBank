package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/transfer'
        headers {
            contentType(applicationJson())
        }
        body([
                senderLogin: "user1",
                receiverLogin: "user2",
                currency: "USD",
                amount: 50.0
        ])
    }
    response {
        status 200
        body([
                statusMessage: "Операция выполнена успешно",
                statusCode: "0"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}