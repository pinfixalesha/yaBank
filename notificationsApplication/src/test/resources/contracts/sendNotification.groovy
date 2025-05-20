package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description 'Should send notification'
    request {
        method 'POST'
        url '/notifications'
        headers {
            contentType(applicationJson())
        }
        body(
                email: "test@example.com",
                message: "Hello!",
                application: "App"
        )
    }
    response {
        status 201
        body(
                notificationId: 1,
                statusMessage: "Notification send OK",
                statusCode: "0"
        )
        headers {
            contentType(applicationJson())
        }
    }
}