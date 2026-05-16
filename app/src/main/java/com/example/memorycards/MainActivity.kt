package com.example.memorycards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoryGame()
        }
    }
}

@Composable
fun MemoryGame() {
    val symbols = listOf("🍎", "🍌", "🍇", "🍒", "🍋", "🥝")

    var cards by remember { mutableStateOf((symbols + symbols).shuffled()) }
    var opened by remember { mutableStateOf(listOf<Int>()) }
    var matched by remember { mutableStateOf(listOf<Int>()) }
    var moves by remember { mutableStateOf(0) }
    var locked by remember { mutableStateOf(false) }

    LaunchedEffect(opened) {
        if (opened.size == 2) {
            locked = true
            moves++

            val first = opened[0]
            val second = opened[1]

            delay(800)

            if (cards[first] == cards[second]) {
                matched = matched + first + second
            }

            opened = listOf()
            locked = false
        }
    }

    fun resetGame() {
        cards = (symbols + symbols).shuffled()
        opened = listOf()
        matched = listOf()
        moves = 0
        locked = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF20232A))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Memory Cards",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Ruchy: $moves",
            color = Color(0xFF61DAFB),
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.height(420.dp)
        ) {
            itemsIndexed(cards) { index, symbol ->
                val visible = opened.contains(index) || matched.contains(index)

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(90.dp)
                        .background(
                            if (visible) Color(0xFF61DAFB) else Color.DarkGray
                        )
                        .clickable {
                            if (
                                !locked &&
                                !opened.contains(index) &&
                                !matched.contains(index) &&
                                opened.size < 2
                            ) {
                                opened = opened + index
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (visible) symbol else "?",
                        fontSize = 36.sp
                    )
                }
            }
        }

        if (matched.size == cards.size) {
            Text(
                text = "Wygrałeś!",
                color = Color.Green,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = { resetGame() },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("Nowa gra")
        }
    }
}