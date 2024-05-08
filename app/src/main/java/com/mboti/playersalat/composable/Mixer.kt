package com.mboti.playersalat.composable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.PlaybackParameters
import com.mboti.playersalat.preferences.MySharedPreferences
import com.mboti.playersalat.R
import com.mboti.playersalat.player
import kotlin.math.roundToInt


const val strElFatiha = "ٱلْفَاتِحَةِ"
const val strAyat = "آيات"
const val strOther = "آحرون"
const val steps = 100 // Number of steps in the SeekBar
const val divided = steps.toFloat()


@Composable
fun OpenMixer() {
    var showModal by remember { mutableStateOf(false) }

    Column {
        Row (
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            FilledTonalIconToggleButton(
                checked = showModal,
                onCheckedChange = { showModal = it },
                modifier = Modifier.size(60.dp),
            )
            {
                if (showModal) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.settings_mixer_off),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.settings_mixer),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }


        if (showModal) {
            BottomSheetMixer(
                onDismissRequest = { /* Ne rien faire ici pour empêcher la fermeture */ },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetMixer(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheetScaffold(
        scaffoldState = rememberBottomSheetScaffoldState(),
        sheetContent = { /* Contenu de votre BottomSheet*/ },
        sheetPeekHeight = 0.dp,
        modifier = modifier
    ) {

        Column(
            Modifier
                .fillMaxSize()
            //.background(Color.White)
        ) {

            // Récupérer les valeurs des SharedPréférences
            val myPref = MySharedPreferences(LocalContext.current)
            val valElFatiha = myPref.getSoundElFatiha(myPref.sharedSoundElFatiha, 60)
            val valAyat = myPref.getSoundAyat(myPref.sharedSoundAyat, 90)
            val valOthers = myPref.getSoundOther(myPref.sharedSoundOther, 10)
            val valCountdown = myPref.getCountdown(myPref.sharedSoundCountdown, true)
            val valSpeed = myPref.getSpeed(myPref.sharedSoundSpeed, 1.0F)


            Column (Modifier.padding(16.dp)){

                Text(text = "Settings", fontSize = 30.sp, fontWeight = FontWeight.W600)
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row (Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween)
                {
                    VerticalSliderSound(strElFatiha,"El-Fatiha",valElFatiha, myPref )
                    VerticalSliderSound(strAyat,"Ayat", valAyat, myPref)
                    VerticalSliderSound(strOther,"Others", valOthers, myPref)
                }

                Spacer(modifier = Modifier.height(20.dp))
                SwitchCountdown(valCountdown, myPref)
                Spacer(modifier = Modifier.height(16.dp))
                //Spacer(modifier = Modifier.height(2.dp))
                SliderSpeed(valSpeed, myPref)
            }


        }

    }
}



@Composable
fun SwitchCountdown(valCountdown: Boolean, myPref: MySharedPreferences) {
    val switchCountdownState = remember { mutableStateOf(valCountdown) }

    Row(
        Modifier
            .fillMaxWidth()
        /*.padding(start = 20.dp, end = 20.dp)*/,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Text(text = "Countdown")
        Switch(
            checked = switchCountdownState.value,
            onCheckedChange = { isChecked ->
                switchCountdownState.value = isChecked
                myPref.saveCountdown(myPref.sharedSoundCountdown, isChecked)
                Log.i("Mounir", "   ${switchCountdownState.value}  -  ${myPref.sharedSoundCountdown}")
            }
        )
    }
}



@Composable
fun VerticalSliderSound(titleArabic:String, title:String, sliderValue:Int, myPref:MySharedPreferences){

    var sliderProgressValue by rememberSaveable { mutableIntStateOf(sliderValue) }
    Box {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(titleArabic, textAlign = TextAlign.Center, fontSize = 23.sp)

            Text(title, textAlign = TextAlign.Center, fontSize = 11.sp)

            Spacer(modifier = Modifier.padding(5.dp))

            Box(contentAlignment = Alignment.BottomCenter){
                Card(
                    modifier = Modifier.wrapContentSize(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation()
                ) {
                    VerticalSliderSound(
                        progressValue = sliderProgressValue
                    ) {
                        sliderProgressValue = it
                        //MAJ des sharedPreferences
                        when (titleArabic) {
                            strElFatiha -> myPref.saveSoundElFatiha(myPref.sharedSoundElFatiha, it)
                            strAyat -> myPref.saveSoundAyat(myPref.sharedSoundAyat, it)
                            else -> myPref.saveSoundOther(myPref.sharedSoundOther, it)
                        }

                        val sound = when (titleArabic) {
                            strElFatiha -> myPref.getSoundElFatiha(myPref.sharedSoundElFatiha, 60)
                            strAyat -> myPref.getSoundAyat(myPref.sharedSoundElFatiha, 90)
                            else -> myPref.getSoundOther(myPref.sharedSoundElFatiha, 10)
                        }
                        player.volume = (sound / divided)
                    }
                }

                val icoSpeaker = when (sliderProgressValue) {
                    in 80..100 -> R.drawable.ico_speaker3
                    in 30..79 -> R.drawable.ico_speaker2
                    in 1..29 -> R.drawable.ico_speaker1
                    else -> R.drawable.ico_speaker0
                }
                Image (
                    painter = painterResource(id = icoSpeaker),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .size(40.dp)
                )
            }
        }
    }
}



@Composable
fun VerticalSliderSound(progressValue: Int? = null, value: (Int) -> Unit) {

    val state = rememberComposeVerticalSliderState()

    ComposeVerticalSlider(
        state = state,
        enabled = state.isEnabled.value,
        progressValue = progressValue,
        width = 70.dp,
        height = 200.dp,
        radius = CornerRadius(20f, 20f),
        trackColor = MaterialTheme.colorScheme.tertiaryContainer,
        progressTrackColor = MaterialTheme.colorScheme.primary,
        onProgressChanged = {
            value(it)
        },
        onStopTrackingTouch = {
            value(it)
        }
    )
}


@Composable
fun SliderSpeed(valSpeed: Float, myPref: MySharedPreferences) {
    var sliderSpeedPosition by remember { mutableFloatStateOf(valSpeed) }
    Row(
        Modifier
            .fillMaxWidth()
        /*.padding(start = 20.dp, end = 20.dp)*/,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "Speed")
        Spacer(modifier = Modifier.padding(40.dp))
        Column (horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "x$sliderSpeedPosition")
            Slider(
                value = sliderSpeedPosition,
                onValueChange = {
                    sliderSpeedPosition = arroundValue(it)
                    myPref.saveSpeed(myPref.sharedSoundSpeed, sliderSpeedPosition)
                    val playbackParameters = PlaybackParameters(sliderSpeedPosition) // 1.5x speed
                    player.playbackParameters = playbackParameters
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                //steps = ,
                valueRange = 0.5f..2f
            )
        }

    }
}

private fun arroundValue(v:Float):Float{
    return (v * 10.0).roundToInt() / 10.0F
}