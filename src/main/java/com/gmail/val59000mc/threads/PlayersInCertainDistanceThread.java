package com.gmail.val59000mc.threads;

import java.util.HashMap;
import java.util.Map;

import com.gmail.val59000mc.UhcCore;
import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.game.GameState;
import com.gmail.val59000mc.players.UhcPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayersInCertainDistanceThread implements Runnable {

    private final double MAX_DISTANCE = 100.;

    private final GameManager gameManager;
    private final PlayersInCertainDistanceThread task;

    public PlayersInCertainDistanceThread(GameManager gameManager) {
        this.gameManager = gameManager;
        this.task = this;
    }

    @Override
    public void run() {

        Map<UhcPlayer, Double> playersAndDistances = new HashMap<>();

        for(UhcPlayer player : gameManager.getPlayerManager().getOnlinePlayingPlayers()) {
            if(!player.isPlaying()) continue;
            if(player.getTeam().isFull())   continue;
            Player bPlayer;
            try {
                bPlayer = player.getPlayer();
            } catch (UhcPlayerNotOnlineException e) {
                // e.printStackTrace();
                continue;
            }
            for(UhcPlayer player2: gameManager.getPlayerManager().getOnlinePlayingPlayers()) {
                Player bPlayer2;
                try {
                    bPlayer2 = player2.getPlayer();
                } catch (UhcPlayerNotOnlineException e) {
                    continue;
                }
                // Check no son el mismo jugador y no son del mismo equipo
                if(player.equals(player2) || !player2.isPlaying() || player.isInTeamWith(player2))  continue;
                if(player2.getTeam().isFull())   continue;

                double distance = bPlayer.getLocation().distance(bPlayer2.getLocation());
                if(distance > MAX_DISTANCE) continue;

                // Corregir situacion de tres o mas jugadores en un rango de 100 bloques

                if (!playersAndDistances.containsKey(player)) {
                    playersAndDistances.put(player, distance);
                } else {
                    if (distance < playersAndDistances.get(player)) {
                        playersAndDistances.put(player, distance);
                    } // Si miro esto mucho me va a dar algo en los ojos
                }

            }
        }

        for(UhcPlayer player: playersAndDistances.keySet()) {
            int distance = playersAndDistances.get(player).intValue();
            try {
                // Logic for message
                player.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "Hay un jugador a " + ((int) distance) + " bloques."));

                player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 30, 3));

            } catch (UhcPlayerNotOnlineException e) {
                e.printStackTrace();
            }
        }


        if(!gameManager.getGameState().equals(GameState.ENDED))
            Bukkit.getScheduler().runTaskLater(UhcCore.getPlugin(), task, 20);

    }
    
}
