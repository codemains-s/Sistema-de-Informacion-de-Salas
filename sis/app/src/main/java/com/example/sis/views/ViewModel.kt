package com.example.sis.views
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sis.logic.logicRoom.RoomResult
import com.example.sis.logic.logicRoom.roomList
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {
    private val _roomResult = mutableStateOf<RoomResult>(RoomResult.Success(emptyList()))
    val roomResult: State<RoomResult> = _roomResult

    fun fetchRooms() {
        viewModelScope.launch {
           // _roomResult.value = roomList("Bearer")
        }
    }
}
