package windparks.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ControllingInstitutionTest {

    private lateinit var controllingInstitution: ControllingInstitution

    @BeforeEach
    fun setUp() {
        controllingInstitution = ControllingInstitution()
        controllingInstitution.uiScope = CoroutineScope(Job())
    }

    @Test
    fun allWindparks() {
        val size = controllingInstitution.allWindparks.size
        assertEquals(size, 17)
    }

    @Test
    fun windParkUnderControl() {
        val windpark = controllingInstitution.allWindparks[0]
        controllingInstitution.updateWindparkUnderControl(windpark)
        assertEquals(windpark, controllingInstitution.windparkUnderControl)
    }

    @Test
    fun createWindPark() {
        val size = controllingInstitution.allWindparks.size
        assertEquals(size, 17)
        controllingInstitution.create()
        assertEquals(size + 1, controllingInstitution.allWindparks.size)

        val newWindpark = controllingInstitution.allWindparks[17]
        assertEquals(newWindpark, controllingInstitution.windparkUnderControl)
    }

    @Test
    fun deleteWindPark() {
        val size = controllingInstitution.allWindparks.size
        assertEquals(size, 17)
        controllingInstitution.updateWindparkUnderControl(controllingInstitution.allWindparks[8])
        controllingInstitution.delete()
        assertEquals(size - 1, controllingInstitution.allWindparks.size)

        controllingInstitution.allWindparks.clear()
        controllingInstitution.delete()
        assertEquals(null, controllingInstitution.windparkUnderControl)
    }

    @Test
    fun saveWindPark() {
       val output =  controllingInstitution.save()
        assertEquals("saved", output)
    }

}
