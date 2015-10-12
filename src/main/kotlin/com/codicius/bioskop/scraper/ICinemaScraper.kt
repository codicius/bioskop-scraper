package com.codicius.bioskop.scraper

/**
 * Created by michel on 10/11/15.
 */

interface ICinemaScraper {
  fun getCinemas(city: City): Collection<Cinema>
  fun getCities(): Collection<City>
}