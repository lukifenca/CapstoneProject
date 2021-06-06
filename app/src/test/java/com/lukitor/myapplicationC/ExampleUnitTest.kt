package com.lukitor.myapplicationC

import android.util.Log
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println("Response : {\"calcium\": \"3\", \"calories\": \"333\", \"carbohydrate\": \"41.70\", \"cholesterol\": \"103\", \"fat\": \"12.34\", \"fiber\": \"1.4\", \"iron\": \"15\", \"measurement_description\": \"cup\", \"metric_serving_amount\": \"198.000\", \"metric_serving_unit\": \"g\", \"monounsaturated_fat\": \"3.776\", \"number_of_units\": \"1.000\", \"polyunsaturated_fat\": \"5.344\", \"potassium\": \"202\", \"protein\": \"12.47\", \"saturated_fat\": \"2.251\", \"serving_description\": \"1 cup\", \"serving_id\": \"19064\", \"serving_url\": \"https://www.fatsecret.com/calories-nutrition/generic/rice-fried?portionid=19064&portionamount=1.000\", \"sodium\": \"822\", \"sugar\": \"1.50\", \"vitamin_a\": \"5\", \"vitamin_c\": \"4\"}")
        assertEquals(4, 2 + 2)
    }
}