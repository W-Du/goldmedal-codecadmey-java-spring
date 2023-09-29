package com.codecademy.goldmedal.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.codecademy.goldmedal.model.GoldMedal;

public interface GoldMedalRepository extends CrudRepository<GoldMedal, Long> {
    List<GoldMedal> findByCountryByOrderByYearAsc(String country);
    List<GoldMedal> findByCountryByOrderByYearDesc(String country);

    List<GoldMedal> findByCountryByOrderByCityAsc(String country);
    List<GoldMedal> findByCountryByOrderByCityDesc(String country);

    List<GoldMedal> findByCountryByOrderBySeasonAsc(String country);
    List<GoldMedal> findByCountryByOrderBySeasonDesc(String country);
    List<GoldMedal> findByCountryByOrderByNameAsc(String country);
    List<GoldMedal> findByCountryByOrderByNameDesc(String country);

    List<GoldMedal> findByCountryByOrderByEventAsc(String country);
    List<GoldMedal> findByCountryByOrderByEventDesc(String country);

    List<GoldMedal> findByCountry(String country);
    List<GoldMedal> findByCountryAndEventByOrderByYearAsc(String country, String event);
    List<GoldMedal> findByEvent(String event);
    List<GoldMedal> findByGender(String gender);
}
