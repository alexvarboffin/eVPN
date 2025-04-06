package com.walhalla.lib

import com.walhalla.lib.pojo.VCountry
import java.util.ArrayList
import java.util.HashMap

class CountryPresenter {
    //at aut
    private val countryMap =
        HashMap<String, String>()
    val smms: MutableList<VCountry> = ArrayList()

    var longCodes: Array<String> = arrayOf(
        "austria",
        "finland",
        "france",
        "germany",
        "italy",
        "kazakhstan",
        "netherlands",
        "poland",
        "russia",
        "usa",
        "sweden",
        "spain",
        "norway",
        "turkey",
        "switzerland",
        "lithuania",
        "latvia",
        "japan",
        "greatbritain", "belgium", "canada", "denmark",
        "estonia", "greece", "hongkong"
    )

    var countryCodes: Array<String> = arrayOf(
        "AT",
        "FI",  // Finland
        "FR",  // France
        "DE",  // Germany
        "IT",  // Italy
        "KZ",  // Kazakhstan
        "NL",  // Netherlands
        "PL",  // Poland
        "RU",  // Russia
        "US",  // United States of America
        "SE",  //sweden
        "ES",  //spain
        "NO",  //norway
        "TR",  //turkey
        "CH",  //switzerland
        "LT",  //lithuania
        "LV",  //latvia
        "JP",  //japan
        "UK",  //greatbritain
        "BE",  //belgium
        "CA",  //canada
        "DK",  //denmark


        "EE",  // estonia1 @@
        "GR",  //greece1 @@
        "HK" //hongkong1
    )

    init {
        for (i in longCodes.indices) {
            countryMap.put(longCodes[i], countryCodes[i])
            smms.add(
                VCountry(
                    capitalizeCountry(longCodes[i]),
                    countryCodes[i]
                )
            )
        }
    }


    fun recognition(part0: String): VCountry {
        var countryShort = ""
        var countryLong0 = ""

        for ((key, value) in countryMap) {
            if (part0.startsWith(key)) {
                countryShort = value
                countryLong0 = capitalizeCountry(key)
                return VCountry(countryLong0, countryShort)
            }
        }
        return VCountry(part0, part0) //not found
    }

    companion object {
        private fun capitalizeCountry(str: String): String {
            if ("usa".equals(str, ignoreCase = true)) {
                return "USA"
            }

            val arr: CharArray = str.toCharArray()
            var capitalizeNext = true
            val phrase = StringBuilder()
            for (c in arr) {
                if (capitalizeNext && Character.isLetter(c)) {
                    phrase.append(c.uppercaseChar())
                    capitalizeNext = false
                    continue
                } else if (Character.isWhitespace(c)) {
                    capitalizeNext = true
                }
                phrase.append(c)
            }
            return phrase.toString()
        }
    }
}
