package com.codicius.bioskop.scraper

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.*

/**
 * Created by michel on 10/11/15.
 */

class Cinema21Scraper: ICinemaScraper {
  val baseUrl = "http://www.21cineplex.com"
  val doc: Document

  init {
    doc = Jsoup.connect(baseUrl).get()
  }

  override fun getCinemas(city: City): Collection<Cinema> {
    val cityDoc = Jsoup.connect("$baseUrl/page/ajax-theater-list.php")
        .data("civy", city.id).post()
    return cityDoc.getElementsByTag("optgroup")
        .flatMap { optgroup ->
          optgroup.getElementsByTag("option").map {
            cinema ->
            Cinema(
                cinema.attr("value").split(",").first(),
                cinema.text(),
                optgroup.attr("label"))
          }
        }
  }

  override fun getCities(): Collection<City> {
    val cities: Element = doc.getElementById("mvsearch")

    val cityElements = cities.getElementsByTag("option")
        .filterNot { it.attr("value").isEmpty() }
    return cityElements.map { City(it.attr("value"), it.text()) }
  }
}

fun main(args: Array<String>) {
  val cinema21 = Cinema21Scraper()
  cinema21.getCities().forEach {
    println("${it.name}: ${it.id}")
    if (it.id == "3") {
      println(cinema21.getCinemas(it).filter {
        cinema -> cinema.group=="IMAX THEATERS"
      }.first()
      )
    }
  }

}