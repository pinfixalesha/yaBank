package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/account/add'
        headers {
            contentType(applicationJson())
        }
        body([
                currency: "USD",
                login: "user1"
        ])
    }
    response {
        status 201
        body([
                statusMessage: "Create Account User OK",
                statusCode: "0"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}