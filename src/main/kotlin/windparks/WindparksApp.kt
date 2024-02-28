package windparks

import androidx.compose.ui.window.application
import windparks.model.ControllingInstitution
import windparks.view.SupervisoryBoardWindow

fun main() {
    val model = ControllingInstitution()

    application {
        SupervisoryBoardWindow(model)
    }
}