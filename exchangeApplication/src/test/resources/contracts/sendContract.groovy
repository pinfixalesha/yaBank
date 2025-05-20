package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/exchange/rates'
        headers {
            contentType(applicationJson())
        }
        body([
                [
                        currency: "USD",
                        buy: 75.0,
                        sale: 76.0
                ],
                [
                        currency: "EUR",
                        buy: 85.0,
                        sale: 86.0
                ]
        ])
    }
    response {
        status 200
        body([
                statusCode: "0",
                statusMessage: "Курсы валют успешно сохранены."
        ])
        headers {
            contentType(applicationJson())
        }
    }
}

