package windparks.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WindparkTest{

    private lateinit var windpark : Windpark

    @BeforeEach
    fun setup(){
        windpark = Windpark(id     = 1000,
                            name   = "Megawind II",
                            status = "in Betrieb")
    }

    @Test
    fun testConstructor(){
        //then
        assertEquals(WindparkState.OPERATIONAL, windpark.status)
        assertEquals(0.0, windpark.totalProduction_MWH)
    }


    @Test
    fun testIfInt(){
        //given
        var value : Int? = null

        //when
        "1".ifInt { value = it }

        //then
        assertEquals(1, value)

        //when
        "+2".ifInt { value = it }

        //then
        assertEquals(2, value)

        //when
        "-2".ifInt { value = it }

        //then
        assertEquals(-2, value)

        //when
        "-2’000".ifInt { value = it }

        //then
        assertEquals(-2000, value)

        //when
        "invalid input".ifInt { value = it }

        //then
        assertEquals(-2000, value)

        //when
        "   ".ifInt { value = it }

        //then
        assertNull(value)
    }

    //todo: implement TestCase 'testIfDouble' analog zu 'testIfInt'


    @Test
    fun testUpdateProduction(){
        //when
        windpark.updateProduction2015("10")

        //then
        assertEquals(10.0, windpark.production2015_MWH)

        //when
        windpark.updateProduction2015("  2’000.0  ")

        //then
        assertEquals(2000.0, windpark.production2015_MWH)

        //when
        windpark.updateProduction2015(" invalid input doesn't change value")

        //then
        assertEquals(2000.0, windpark.production2015_MWH)

        //when
        windpark.updateProduction2015("   ")

        //then
        assertNull(windpark.production2015_MWH)
    }

    @Test
    fun testTotalProduction(){
        //when
        windpark.updateProduction2015("10")

        //then
        assertEquals(10.0, windpark.totalProduction_MWH)

        //when
        windpark.updateProduction2016("20")

        //then
        assertEquals(30.0, windpark.totalProduction_MWH)


        //when
        windpark.updateProduction2017("30")

        //then
        assertEquals(60.0, windpark.totalProduction_MWH)


        //when
        windpark.updateProduction2018("40")

        //then
        assertEquals(100.0, windpark.totalProduction_MWH)

        //when
        windpark.updateProduction2018("")

        //then
        assertEquals(60.0, windpark.totalProduction_MWH)
    }
}