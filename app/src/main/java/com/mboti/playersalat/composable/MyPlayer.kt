package com.mboti.playersalat.composable

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import com.mboti.playersalat.commun.convertToText
import com.mboti.playersalat.R
import com.mboti.playersalat.model.Music
import com.mboti.playersalat.player
import kotlinx.coroutines.delay




/*--------------------------------------------------------
TODO Ajouter dans le manifeste.xml les deux lignes afin de
 contrer les problèmes d'allocation mémoire pour API24

 <application
        android:hardwareAccelerated="false"
        android:largeHeap="true"

        +

    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.34.0")
    implementation ("androidx.media3:media3-exoplayer:1.3.1")
 --------------------------------------------------------*/

class MyPlayer{

    init {
    }
    @Composable
    fun InitPlayer(myList: List<Music>) {
        val playingSongIndex = remember { mutableIntStateOf(0) }

        // currentMediaItemIndex : Returns the index of the current MediaItem in the timeline,
        // or the prospective index if the current timeline is empty.
        LaunchedEffect(player.currentMediaItemIndex) {
            playingSongIndex.intValue = player.currentMediaItemIndex
        }

        val context = LocalContext.current
        LaunchedEffect(Unit) {
           myList.forEach {
                val path = "android.resource://" + context.packageName + "/" + it.audioSelected
                val mediaItem = MediaItem.fromUri(Uri.parse(path))
                player.addMediaItem(mediaItem)
            }
        }


        player.prepare()

        val currentPosition = remember { mutableLongStateOf(0) }

        LaunchedEffect(key1 = player.currentPosition, key2 = player.isPlaying) {
            delay(1000)
            currentPosition.longValue = player.currentPosition
        }

        val sliderPosition = remember { mutableLongStateOf(0) }
        LaunchedEffect(currentPosition.longValue) {
            sliderPosition.longValue = currentPosition.longValue
        }

        val totalDuration = remember { mutableLongStateOf(0) }
        LaunchedEffect(player.duration) {
            if (player.duration > 0) {
                totalDuration.longValue = player.duration
            }
        }


        /**
         *   UI
         */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row (Modifier.fillMaxWidth().padding(top=16.dp), horizontalArrangement = Arrangement.End){
                SettingsMixer()
            }

            Spacer(modifier = Modifier.height(24.dp))


            val isPlaying = remember { mutableStateOf(false) }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = {
                        isPlaying.value = false
                        player.setPlayWhenReady(false);
                        player.stop();
                        player.seekTo(0);
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        //contentColor = Color.White
                    ),
                    enabled = player.isPlaying
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_stop),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                ButtonControl(icon = R.drawable.ic_previous, size = 20.dp, onClick = {
                    player.seekToPreviousMediaItem()
                })

                Spacer(modifier = Modifier.width(10.dp))


                FilledTonalIconToggleButton(
                    checked = isPlaying.value,
                    onCheckedChange = {
                        isPlaying.value = it
                        if (isPlaying.value) {
                            player.play()
                        } else {
                            player.pause()
                        }
                    },
                    colors = IconButtonDefaults.filledTonalIconToggleButtonColors(if (isPlaying.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.size(80.dp),
                )
                {
                    if (isPlaying.value) {
                        //Icon(Icons.Filled.Lock, contentDescription = "Localized description", modifier = Modifier.size(50.dp))
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_pause),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_play),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }


                Spacer(modifier = Modifier.width(10.dp))

                ButtonControl(icon = R.drawable.ic_next, size = 20.dp, onClick = {
                    player.seekToNextMediaItem()
                })
            }


            TrackSliderPlayer(
                value = sliderPosition.longValue.toFloat(),
                onValueChange = {
                    sliderPosition.longValue = it.toLong()
                },
                onValueChangeFinished = {
                    currentPosition.longValue = sliderPosition.longValue
                    player.seekTo(sliderPosition.longValue)
                },
                songDuration = totalDuration.longValue.toFloat(),
                currentPosition,
                totalDuration,
            )
        }
    }


    @Composable
    fun ButtonControl(icon: Int, size: Dp, onClick: () -> Unit) {
        Button(
            onClick = {
                onClick()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                //contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(size)
            )
        }
    }


    /**
     * Tracks and visualizes the song playing actions.
     */
    @Composable
    fun TrackSliderPlayer(
        value: Float,
        onValueChange: (newValue: Float) -> Unit,
        onValueChangeFinished: () -> Unit,
        songDuration: Float,
        currentPosition: MutableLongState,
        totalDuration: MutableLongState
    ) {
        Slider(
            value = value,
            onValueChange = { onValueChange(it) },
            onValueChangeFinished = { onValueChangeFinished() },
            valueRange = 0f..songDuration,
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = Color.DarkGray,
                inactiveTrackColor = Color.Gray,
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {

            Text(
                text = (currentPosition.longValue).convertToText(),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                color = Color.Black,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )

            val remainTime = totalDuration.longValue - currentPosition.longValue
            Text(
                text = if (remainTime >= 0) remainTime.convertToText() else "",
                modifier = Modifier
                    .padding(8.dp),
                color = Color.Black,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
    }
}

