package ru.yandex.practicum.yaBank.exchangeApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.yaBank.exchangeApplication.entities.Rate;

public interface RatesRepository extends JpaRepository<Rate, Long> {
}
