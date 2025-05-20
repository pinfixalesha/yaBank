package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/user/create'
        headers {
            contentType(applicationJson())
        }
        body([
                login: "user1",
                password: "password123",
                fio: "Иван Иванов",
                role: "USER",
                email: "user1@example.com",
                dateOfBirth: "1990-01-01"
        ])
    }
    response {
        status 201
        body([
                statusMessage: "Create User OK",
                statusCode: "0"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}