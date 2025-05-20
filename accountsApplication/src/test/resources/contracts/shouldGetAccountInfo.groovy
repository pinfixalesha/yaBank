package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/account/get?currency=USD&login=user1'
    }
    response {
        status 200
        body([
                accountNumber: "40215USD1234567890123",
                currency: "USD",
                balance: 100.0
        ])
        headers {
            contentType(applicationJson())
        }
    }
}