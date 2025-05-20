package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/user/findbylogin?login=user1'
    }
    response {
        status 200
        body([
                login: "user1",
                fio: "Иван Иванов",
                email: "user1@example.com",
                role: "USER",
                dateOfBirth: "1990-01-01",
                statusCode: "0",
                statusMessage: "OK"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}