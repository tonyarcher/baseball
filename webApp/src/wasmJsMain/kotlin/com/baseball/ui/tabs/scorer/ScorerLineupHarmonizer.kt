package com.baseball.ui.tabs.scorer

import com.baseball.BaseballConstants
import com.baseball.game.localAwayActivePitcherId
import com.baseball.game.localAwayActivePitcherName
import com.baseball.game.localAwayBatterIndex
import com.baseball.game.localAwayBench
import com.baseball.game.localAwayLineup
import com.baseball.game.localHomeActivePitcherId
import com.baseball.game.localHomeActivePitcherName
import com.baseball.game.localHomeBatterIndex
import com.baseball.game.localHomeBench
import com.baseball.game.localHomeLineup
import com.baseball.models.Game
import com.baseball.models.Player

internal fun harmonizeAwayLineup(game: Game, awayRoster: List<Player>) {
    if (localAwayLineup.isEmpty()) {
        localAwayLineup.addAll(awayRoster.filter { it.position != BaseballConstants.Positions.P }.take(9))
        localAwayBench.addAll(
            awayRoster.filter {
                it.position == BaseballConstants.Positions.P &&
                        it.id != game.gameState.currentPitcherId
            } + awayRoster.drop(10)
        )
        localAwayActivePitcherId = game.gameState.currentPitcherId
            ?: awayRoster.find { it.position == BaseballConstants.Positions.P }?.id ?: 210L
        localAwayActivePitcherName = game.gameState.currentPitcherName
            ?: awayRoster.find { it.position == BaseballConstants.Positions.P }?.name ?: "Sonny Gray"
        localAwayBatterIndex = localAwayLineup.indexOfFirst { it.id == game.gameState.currentBatterId }
            .coerceAtLeast(0)
    } else {
        localAwayLineup.removeAll { p -> awayRoster.none { it.id == p.id } }
        localAwayBench.removeAll { p -> awayRoster.none { it.id == p.id } }
        val newAway = awayRoster.filter { r ->
            localAwayLineup.none { it.id == r.id } && localAwayBench.none { it.id == r.id }
        }
        localAwayBench.addAll(newAway)
        while (localAwayLineup.size < 9 && localAwayBench.isNotEmpty()) {
            localAwayLineup.add(localAwayBench.removeFirst())
        }
    }
}

internal fun harmonizeHomeLineup(game: Game, homeRoster: List<Player>) {
    if (localHomeLineup.isEmpty()) {
        localHomeLineup.addAll(homeRoster.filter { it.position != BaseballConstants.Positions.P }.take(9))
        localHomeBench.addAll(
            homeRoster.filter {
                it.position == BaseballConstants.Positions.P && it.id != game.gameState.currentPitcherId
            } + homeRoster.drop(10)
        )
        localHomeActivePitcherId = game.gameState.currentPitcherId
            ?: homeRoster.find { it.position == BaseballConstants.Positions.P }?.id ?: 110L
        localHomeActivePitcherName = game.gameState.currentPitcherName
            ?: homeRoster.find { it.position == BaseballConstants.Positions.P }?.name ?: "Justin Steele"
        localHomeBatterIndex = localHomeLineup.indexOfFirst { it.id == game.gameState.currentBatterId }
            .coerceAtLeast(0)
    } else {
        localHomeLineup.removeAll { p -> homeRoster.none { it.id == p.id } }
        localHomeBench.removeAll { p -> homeRoster.none { it.id == p.id } }
        val newHome = homeRoster.filter { r ->
            localHomeLineup.none { it.id == r.id } && localHomeBench.none { it.id == r.id }
        }
        localHomeBench.addAll(newHome)
        while (localHomeLineup.size < 9 && localHomeBench.isNotEmpty()) {
            localHomeLineup.add(localHomeBench.removeFirst())
        }
    }
}
