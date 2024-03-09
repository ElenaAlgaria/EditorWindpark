package windparks.view

import java.awt.Cursor
import java.util.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 *  The composable MasterDetail shows the toolbar at the top, the editor on the right and the explorer on the left.
 *
 *  It contains a column with the toolbar and a row with two cards the editor and explorer.
 *
 *  @param toolbar composable function for the top
 *  @param explorer composable function for the left side
 *  @param editor composable function for the right side
 */

@Composable
fun MasterDetail(toolbar:  @Composable () -> Unit = {},
                 explorer: @Composable () -> Unit,
                 editor:   @Composable () -> Unit){
    val padding = 20.dp
    val elevation = 2.dp

    Column {
        TopAppBar(backgroundColor = Color.LightGray){
            toolbar()
        }

        Row(modifier = Modifier.fillMaxSize()
            .padding(padding)){

            // see https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#Card(kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.foundation.BorderStroke,androidx.compose.ui.unit.Dp,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function0)
            Card(elevation = elevation,
                 modifier  =  Modifier.weight(0.4f)
                     .fillMaxSize()){
                explorer()
            }

            Spacer(Modifier.width(padding))

            Card(elevation = elevation,
                 modifier  =  Modifier.weight(1.0f)
                     .fillMaxSize()){
                editor()
            }
        }
    }
}

/**
 * The composable GenericExplorer contains a box nad a lazyColumn to display all the windpark cards.
 * Also, the list is scrollable.
 *
 * @param T generic type for the element which gets displayed
 * @param data a list with all elements
 * @param key a unique Int key for all the elements
 * @param scrollState a state for the scrolling of the list
 * @param onShow a function that gets called to show the images of the elements
 * @param listItem a composable function with the content of a list item
 */
@Composable
fun<T> GenericExplorer(data: List<T>,
                       key: (T) -> Int,
                       scrollState: LazyListState,
                       onShow : (T) -> Unit,
                       listItem: @Composable (T) -> Unit) {
    Box(contentAlignment = Alignment.Center,
        modifier         = Modifier.padding(5.dp)) {
        LazyColumn(state    = scrollState,
                   modifier = Modifier.padding(end = 12.dp).fillMaxHeight()) {
            items(items = data,
                  key   = { key(it) }) {

                LaunchedEffect(key(it)){
                    onShow(it)
                }

                listItem(it)
                Divider()
            }

        }
        VerticalScrollbar(adapter = ScrollbarAdapter(scrollState), Modifier.align(Alignment.TopEnd))
    }

}

/**
 * The composable GenericEditor either shows a image if no windpark is selected or show the windparkeditor.
 * The editor has header card with the most important information on it and an editorform where you can edit
 * the information.
 *
 * @param T generic type for the element which gets displayed
 * @param selectText a text that informs the user that no windpark is selected
 * @param headerContent a composable function with the content for the most important information about the windpark
 * @param formContent a composable function to edit the information about a windpark
 */

@Composable
fun<T> GenericEditor(item: T?,
                     selectText: String, backgroundImage: ImageBitmap,
                     headerContent: @Composable RowScope.(T) -> Unit,
                     formContent: @Composable ColumnScope.(T) -> Unit) {
    if (null == item) {
        NothingSelected(text            = selectText,
                        backgroundImage = backgroundImage)
    } else {
        Column(Modifier.fillMaxSize()
                       .padding(10.dp)) {
            Card(backgroundColor = Color(0xFFEEEEEE)){
                Row(modifier = Modifier.padding(10.dp)
                                       .height(IntrinsicSize.Max)
                                       .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    headerContent(item)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(Modifier.weight(1.0f)){
                val scrollState = rememberScrollState()

                Column(Modifier.verticalScroll(scrollState)
                               .align(Alignment.TopStart)
                               .padding(start = 10.dp, end = 22.dp)) {
                    formContent(item)
                }

                VerticalScrollbar(adapter = ScrollbarAdapter(scrollState), modifier = Modifier.align(Alignment.CenterEnd))
            }
        }
    }
}

/**
 * The composable NothingSelected is called when in the explorer no windpark is selected. It shows a text
 * and a placeholder image.
 *
 * @param text a text with an information that nothing is selected
 * @param backgroundImage the placeholder image
 */
@Composable
fun NothingSelected(text: String, backgroundImage: ImageBitmap){
    Box(modifier         = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        Text(text     = text,
             color    = Color.Gray,
             fontSize = 32.sp)

        Image(bitmap             = backgroundImage,
              contentDescription = "Default windpark image",
              contentScale       = ContentScale.Crop,
              alpha              = 0.15f,
              modifier           = Modifier.fillMaxSize()
             )
    }
}

/**
 * The composable TwoColumnRow is for the FormContent in the editor. It contains a row with a placeholder for the
 * left and the right side. Also, it has a space between them.
 *
 * @param left this composable function is for the left column
 * @param right this composable function is for the right column
 */
@Composable
fun TwoColumnRow(left:  @Composable (Modifier) -> Unit, right:  @Composable (Modifier) -> Unit){
    Row(modifier = Modifier.fillMaxWidth()){
        left(Modifier.weight(1.0f))
        Spacer(modifier = Modifier.width(60.dp))
        right(Modifier.weight(1.0f))
    }
}

/**
 * The composable FormTextField displays a text field in the editor to edit an information about a windpark. It takes
 * another composable FormField to display a label on the left side and an OutlinedTextField on the right side.
 *
 * @param label a label for the text field
 * @param labelWidth a width for the label
 * @param modifier a modifier to modify the FormField
 * @param value the value of the text field
 * @param onValueChange when the value gets changed this function gets triggert
 */
@Composable
fun FormTextField(label: String, labelWidth: Dp = 100.dp, modifier: Modifier = Modifier, value: String, onValueChange : (String) -> Unit){
    FormField(label      = label,
              labelWidth = labelWidth,
              modifier   = modifier,
              control    = { modifier -> OutlinedTextField(value         = value,
                                                           onValueChange = { onValueChange(it) },
                                                           singleLine    = true,
                                                           modifier      = modifier) })
}


/**
 * This composable FormField
 */
@Composable
fun FormField(label: String, labelWidth: Dp = 100.dp, modifier: Modifier = Modifier, control: @Composable (modifier: Modifier) -> Unit){
    Row(modifier          = modifier.padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically){

        Text(text     = label,
             modifier = Modifier.width(labelWidth))

        control(Modifier.weight(1.0f))
    }
}

/**
 * To format numbers the swiss way
 */
val CH = Locale("de", "CH")

/**
* this is an Extension Function for any kind of Number.
* see https://kotlinlang.org/docs/extensions.html
*------------------------------------------------------
* This function formats swiss numbers with markers.
 *
 * @param pattern a string for how to display numbers
 * @param nullFormat if the number is null
 * @return return the formatted number
*
*/
fun Number?.format(pattern: String, nullFormat: String = ""): String {
    return if (null == this) nullFormat else pattern.format(CH, this)
}

/**
 *  A function that makes a format for null Strings
 *
 *  @param nullFormat for when the String is null
 *  @return returns the formatted string
 */
fun String?.format(nullFormat: String = "") = this ?: nullFormat

/**
 * this function changes the cursor from arrow to hand
 */
fun Modifier.handCursor() = cursor(Cursor.HAND_CURSOR)

/**
 *  A function to change the cursor design
 *
 *  @param cursorId An Integer ID for the cursor
 *  @return modified cursor
 */
fun Modifier.cursor(cursorId: Int) : Modifier = pointerHoverIcon(PointerIcon(Cursor(cursorId)))

