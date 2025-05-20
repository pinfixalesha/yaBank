package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/cash/in'
        headers {
            contentType(applicationJson())
        }
        body([
                currency: "USD",
                login: "user1",
                amount: 100.0
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