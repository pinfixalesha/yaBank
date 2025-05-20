package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/blocker'
        headers {
            contentType(applicationJson())
        }
        body([
                currency: "XXX",
                login: "user123",
                action: "deposit",
                amount: 100.0
        ])
    }
    response {
        status 200
        body([
                statusCode: "0",
                statusMessage: "Operation access"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}

