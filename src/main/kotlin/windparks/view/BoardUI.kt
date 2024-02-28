package windparks.view

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import windparks.model.ControllingInstitution
import windparks.model.Windpark

@Composable
fun ApplicationScope.SupervisoryBoardWindow(institution: ControllingInstitution){
    Window(title          = institution.title,
           onCloseRequest = ::exitApplication,
           state          =  rememberWindowState(width = 1000.dp,
                                                 height = 700.dp,
                                                 position = WindowPosition(Alignment.Center))){
        TheUI(institution)
    }
}

@Composable
private fun TheUI(institution: ControllingInstitution) {

    // some actions, e.g. scrolling,  must be executed in UI-scope, therefore model need to know this scope
    institution.uiScope = rememberCoroutineScope()

    MasterDetail(toolbar  = { Toolbar(institution) },
                 explorer = { Explorer(institution) },
                 editor   = { Editor(institution.windparkUnderControl) }
                )

}

@Composable
private fun Toolbar(institution: ControllingInstitution){
    Text("Toolbar")
}

@Composable
private fun Explorer(institution: ControllingInstitution){
    Text("Explorer \n(review 'GenericExplorer' in SharedComposables)")
}

@Composable
private fun Editor(windpark: Windpark?){
    Text("Editor \n(review 'GenericEditor' in SharedComposables)")
}