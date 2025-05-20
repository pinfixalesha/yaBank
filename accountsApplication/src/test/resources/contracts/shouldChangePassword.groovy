package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/user/changepassword'
        headers {
            contentType(applicationJson())
        }
        body([
                login: "user1",
                password: "newPassword123"
        ])
    }
    response {
        status 200
        body([
                statusMessage: "Chenge Password User OK",
                statusCode: "0"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}