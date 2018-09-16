package com.productions.delford.temprandomhelper

class Match(val p1Character: Character, val p2Character: Character ) {
    var p1Score: Int = 0
    var p2Score: Int = 0

    var winState = WinState.Pending


    enum class WinState {
        P1Win, P2Win, Draw, Pending
    }

    fun checkWinState() {
        when  {
            p1Score > p2Score -> winState = WinState.P1Win
            p2Score > p1Score -> winState = WinState.P2Win
            p1Score == p2Score -> winState= WinState.Draw
        }
    }
}